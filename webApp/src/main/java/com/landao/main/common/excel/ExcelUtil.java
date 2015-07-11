package com.landao.main.common.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.landao.main.common.excel.model.Excel;
import com.landao.main.common.excel.model.ExcelVersionType;
import com.landao.main.common.excel.parse.Parser;

public class ExcelUtil {
	
	public final static String SUCCESS = "0";
	public final static String FILE_NOT_CHOOSED = "1";
	public final static String FILE_FORMAT_ERROR = "2";
	public final static String FILE_PARSE_ERROR = "3";
	
	public final static String TRUE_VALUE = "TRUE";
	public final static String FALSE_VALUE = "FALSE";

	public final static String DATA_TYPE_LONG = "long";
	public final static String DATA_TYPE_DOUBLE = "double";
	public final static String DATA_TYPE_DATE = "date";

	/**
	 * get Workbook by input stream
	 * @param is
	 * @return Workbook
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static Workbook getWorkbook(InputStream is)
			throws IOException, InvalidFormatException {
		// check mark/reset not supported
		if (!is.markSupported()) {
			is = new PushbackInputStream(is, 8);
		}
		// support excel 2003
		if (POIFSFileSystem.hasPOIFSHeader(is)) {
			return new HSSFWorkbook(is);
		}
		// support excel 2007 and 2010
		if (POIXMLDocument.hasOOXMLHeader(is)) {
			return new XSSFWorkbook(OPCPackage.open(is));
		}
		
		throw new IOException("can not parse excel version.");
	}

	/**
	 * Get excel version 
	 * @param wb
	 * @return ExcelVersionType
	 * @throws IOException
	 */
	public static ExcelVersionType getExcelVersion(Workbook wb)
			throws IOException {
		if (wb instanceof HSSFWorkbook) {
			return ExcelVersionType.Excel2003;
		} else if (wb instanceof XSSFWorkbook) {
			return ExcelVersionType.Excel2010;
		}
		throw new IOException("can not parse excel version.");
	}
	
	/**
	 * the 5 types: 
	 * 1.String cell 
	 * 2.Number cell 
	 * 3.boolean cell:TRUE FALSE
	 * 4.error cell 
	 * 5.formula cell
	 */
	public static String getStringValue(Cell cell, String dataType) {
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getRichStringCellValue().getString().trim();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				if (DATA_TYPE_DATE.equals(dataType)) {
					return SimpleDateFormat.getInstance().format(cell.getDateCellValue());
				} else if (DATA_TYPE_LONG.equals(dataType)) {
					return String.valueOf(new DecimalFormat("0").format(cell.getNumericCellValue()));
				} else {
					return String.valueOf(cell.getNumericCellValue());
				}
			} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				if (cell.getBooleanCellValue()) {
					return TRUE_VALUE;
				} else {
					return FALSE_VALUE;
				}
			} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
				return String.valueOf(cell.getErrorCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				return cell.getCellFormula();
			}
		}
		return "";
	}
	
	/**
	 * parse excel
	 * @param excel
	 * @return Excel
	 */
	public static Excel parseExcel(Excel excel) {
		try {
			Workbook wb = getWorkbook(excel.getExlFileStream());
			Sheet sheet = null;
			int sheetNum = wb.getNumberOfSheets();
			String sheetName = excel.getSheetName();
			if(sheetNum == 0){
				return excel;
			}else if(sheetNum == 1){
				sheet = wb.getSheetAt(0);
			}else{
				for (int index = 0; index < wb.getNumberOfSheets(); index++) {
					String sheetNameTemp = wb.getSheetName(index);
					if(sheetNameTemp.equals(sheetName)){
						sheet = wb.getSheetAt(index);
						break;
					}
				}
			}
			
			if(sheet.getPhysicalNumberOfRows()<1){
				String message = "页签为空，请检查导入文件。";
				excel.addExcelError(null, 0, message, sheetName, null);
				return excel;
			}
			
			Parser parser = excel.getSheetParsers().get(sheetName);
			if (parser == null) {
				return excel;
			}
			for (int m = parser.getStartRow(); m < sheet.getPhysicalNumberOfRows(); m++) {
				Row row = sheet.getRow(m);
				if (row == null)
					continue;
				else {
					try {
						excel = parser.parse(row, excel);
					} catch (NullPointerException e) {
						e.printStackTrace();
						continue;
					}
					if (excel != null && excel.getObj() != null) {
						excel.getResultList().add(excel.getObj());
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			excel.setResultCode(FILE_NOT_CHOOSED);
		} catch (IOException e) {
			e.printStackTrace();
			excel.setResultCode(FILE_FORMAT_ERROR);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			excel.setResultCode(FILE_FORMAT_ERROR);
		}
		if (excel.getResultList().size() == 0 && SUCCESS.equals(excel.getResultCode())) {
			excel.setResultCode(FILE_FORMAT_ERROR);
		}
		return excel;
	}
	
}
