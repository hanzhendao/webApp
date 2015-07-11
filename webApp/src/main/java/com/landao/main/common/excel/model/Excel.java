package com.landao.main.common.excel.model;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.landao.main.common.excel.ExcelUtil;
import com.landao.main.common.excel.parse.Parser;

public class Excel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String filePath = null;
	private String sheetName = null;
	private String resultCode = ExcelUtil.SUCCESS;
	private Object obj = null;
	private InputStream exlFileStream = null;
	private Map<String, Parser> sheetParsers = null;
	private List<Object> resultList = new ArrayList<Object>();
	private List<ExcelError> errorList = new ArrayList<ExcelError>();
	
	/**
	 * add error information to list
	 * @param cell
	 * @param message
	 * @param sheetNum
	 */
	public void addExcelError(Row row,int col,String message,String sheetName,String numtype){
		ExcelError excelError = new ExcelError();
		if(row != null){
			Cell cell = row.getCell(col);
			if (cell != null) {
				excelError.setColNum(cell.getColumnIndex());
				excelError.setRowNum(cell.getRowIndex());
				excelError.setSheetName(sheetName);
				excelError.setMessage(message);
				excelError.setValue(ExcelUtil.getStringValue(cell, numtype));
				errorList.add(excelError);
				this.setResultCode(ExcelUtil.FILE_PARSE_ERROR);
			} else {
				excelError.setColNum(col);
				excelError.setRowNum(row.getRowNum());
				excelError.setSheetName(sheetName);
				excelError.setMessage(message);
				excelError.setValue("");
				errorList.add(excelError);
				this.setResultCode(ExcelUtil.FILE_PARSE_ERROR);
			}
		}else{
			excelError.setColNum(col);
			excelError.setRowNum(0);
			excelError.setSheetName(sheetName);
			excelError.setMessage(message);
			excelError.setValue("");
			errorList.add(excelError);
			this.setResultCode(ExcelUtil.FILE_PARSE_ERROR);
		}
		
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public InputStream getExlFileStream() {
		return exlFileStream;
	}

	public void setExlFileStream(InputStream exlFileStream) {
		this.exlFileStream = exlFileStream;
	}

	public List<Object> getResultList() {
		return resultList;
	}

	public void setResultList(List<Object> resultList) {
		this.resultList = resultList;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public List<ExcelError> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<ExcelError> errorList) {
		this.errorList = errorList;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public Map<String, Parser> getSheetParsers() {
		return sheetParsers;
	}

	public void setSheetParsers(Map<String, Parser> sheetParsers) {
		this.sheetParsers = sheetParsers;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	
}
