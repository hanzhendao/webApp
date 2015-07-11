package com.landao.main.common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class AbstractExportExcel<T> {
	public final String CELL_STYLE_STRING = "CELL_STYLE_STRING";
	public final String CELL_STYLE_LONG = "CELL_STYLE_LONG";
	public final String CELL_STYLE_DOUBLE = "CELL_STYLE_DOUBLE";
	public final String CELL_STYLE_DATE = "CELL_STYLE_DATE";
	public final String CELL_STYLE_DATETIME = "CELL_STYLE_DATETIME";
	public final String CELL_STYLE_TITLE = "CELL_STYLE_TITLE";
	
	//excel2007的最大行是1048576   下面的结果1048448   相差 128行   这128行可以设置标题行，和公司的特殊标志
	int pageSize = 16382;
	int sheetHasPages=64;
	
	/**
	 * 导出
	 * @param request 请求
	 * @param fileName	下载时的文件名
	 * @param title		表格标题名
	 * @param headers	表格属性列名数组
	 * @param fields	输出的字段名
	 * @param response	响应
	 */
	public void generateExcel(
			HttpServletRequest request,
			String fileName,
			String title,
			String[] headers,
			String[] fields,
			HttpServletResponse response){
		try {
			ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();		//获得泛型T的实际类型
	        Class<T> entityClass= (Class<T>)type.getActualTypeArguments()[0];
	        Method[] getMethods=new Method[fields.length];		//获得所有的字段method方法
	        for (int i = 0; i < fields.length; i++){
	            String getMethodName = "get"
	                    + fields[i].substring(0, 1).toUpperCase()
	                    + fields[i].substring(1);
                Method getMethod = entityClass.getMethod(getMethodName,new Class[]{});
                getMethods[i]=getMethod;
	        }
	    	
			XSSFWorkbook wb = new XSSFWorkbook();
			Map<String, XSSFCellStyle> styles = createStyles(wb);
			List<String> sheetRefList = new ArrayList<String>();
			List<File> sheetFileList = new ArrayList<File>();
			int totalSize = getTotalSize();
			int totalPage = totalSize / pageSize
					+ (totalSize % pageSize == 0 ? 0 : 1);
			int totalSheet = totalPage / sheetHasPages
					+ (totalPage % sheetHasPages == 0 ? 0 : 1);
			
			String filePath=request.getSession().getServletContext().getRealPath("/tempFiles");		//服务器中的临时文件夹
			//产生随机数文件夹
			filePath += "/"+UUID.randomUUID().toString().replaceAll("-", "");
			
			File mkFile=new File(filePath);
			if(!mkFile.exists()){
				mkFile.mkdir();
			}else{
				//已经含有目录，表示已经出错
				throw new RuntimeException("导出出错");
			}
			
			for (int i = 1; i <= totalSheet; i++) {
				XSSFSheet sheet = wb.createSheet(title + i);
				String sheetRef = sheet.getPackagePart().getPartName().getName();
				sheetRefList.add(sheetRef.substring(1));
				File tmp = new File(filePath + "/" + (title + i) + ".xml");
				sheetFileList.add(tmp);
			}
			File tmpFile = new File(filePath + "/template2.xlsx");
			String resultFile = filePath + "/" + fileName + ".xlsx";
			FileOutputStream os = new FileOutputStream(tmpFile);
			wb.write(os);
			os.close();
			
			for(int i=0,currentPage=1; i<totalSheet;i++){
				Writer fw = new OutputStreamWriter(new FileOutputStream(sheetFileList.get(i)), "UTF-8");
				int leftPage=sheetHasPages;
				if (i == totalSheet - 1) {
					leftPage = totalPage - i * sheetHasPages;
				}
				int rowIndex=0;
				for (int j = 0; j < leftPage; j++) {
					boolean endFlag=false;
					if(j==leftPage-1){
						endFlag=true;
					}
					List<T> dataList = generateData(pageSize, currentPage);
					rowIndex=generateExcelSheet(headers, fields, dataList,rowIndex,endFlag, getMethods, fw, styles);
					currentPage++;
				}
				fw.close();
			}
			
			SimpleDateFormat sdf22 = new SimpleDateFormat("yyyyMMdd");
            String fileDate = sdf22.format(new Date());
        	fileName=fileName+"_"+fileDate;
        	String zipName=fileName+".zip";
        	zipName=new String(zipName.getBytes("GBK"), "iso8859-1");

			response.reset();
            OutputStream out=response.getOutputStream();
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Content-Disposition", "attachment;filename="+zipName);	// 指定下载的文件名 
            response.setContentType("application/x-zip-compressed;charset=gb2312");
			
			FileOutputStream resultOutput = new FileOutputStream(resultFile);
			substituteAll(tmpFile, sheetFileList, sheetRefList, resultOutput);
			resultOutput.close();
			ZipOutputStream zip=new ZipOutputStream(out);
            ZipEntry entry=new ZipEntry(fileName+".xlsx");
            
            InputStream isInputStream=new FileInputStream(resultFile);
            zip.putNextEntry(entry);
            copyStream(isInputStream, zip);
            
            zip.closeEntry();
            zip.close();
            isInputStream.close();
			
			// 删除临时文件
			tmpFile.delete();
			for (File file : sheetFileList) {
				file.delete();
			}
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb) {
		Map<String, XSSFCellStyle> stylesMap = new HashMap<String, XSSFCellStyle>();
		XSSFDataFormat fmt = wb.createDataFormat();
		XSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		stylesMap.put(CELL_STYLE_STRING, style);

		XSSFCellStyle style2 = wb.createCellStyle();
		style2.setDataFormat(fmt.getFormat("0"));
		style2.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		stylesMap.put(CELL_STYLE_LONG, style2);

		XSSFCellStyle style3 = wb.createCellStyle();
		style3.setDataFormat(fmt.getFormat("0.00"));
		style3.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		style3.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		stylesMap.put(CELL_STYLE_DOUBLE, style3);

		XSSFCellStyle style4 = wb.createCellStyle();
		style4.setDataFormat(fmt.getFormat("yyyy-MM-dd"));
		style4.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style4.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		stylesMap.put(CELL_STYLE_DATE, style4);

		XSSFCellStyle style5 = wb.createCellStyle();
		style5.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		style5.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style5.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style5.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		stylesMap.put(CELL_STYLE_TITLE, style5);

		XSSFCellStyle style6 = wb.createCellStyle();
		style4.setDataFormat(fmt.getFormat("yyyy-MM-dd HH:mm:ss"));
		style4.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style4.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		stylesMap.put(CELL_STYLE_DATETIME, style6);
		return stylesMap;
	}

	public int generateExcelSheet(String[] headers, String[] fields,List<T> dataList,
			int rowIndex, boolean endFlag, Method[] getMethods, Writer out, Map<String, XSSFCellStyle> styles)
					throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		XSSFCellStyle stringStyle = styles.get(this.CELL_STYLE_STRING);
		XSSFCellStyle longStyle = styles.get(this.CELL_STYLE_LONG);
		XSSFCellStyle doubleStyle = styles.get(this.CELL_STYLE_DOUBLE);
		XSSFCellStyle dateStyle = styles.get(this.CELL_STYLE_DATE);

		Calendar calendar = Calendar.getInstance();
		SpreadsheetWriter sw = new SpreadsheetWriter(out);
		if(rowIndex==0){
			sw.beginWorkSheet();
			// 设置列宽
			sw.beginSetColWidth();
			for (int i = 0; i < headers.length; i++) {
				sw.setColWidthBeforeSheet(i, 13);
			}
			sw.endSetColWidth();
	
			sw.beginSheet();
			// 设置表头高度
			sw.insertRowWithheight(0, headers.length, 25);
			// 设置表头样式
			int styleIndex = ((XSSFCellStyle) styles.get(this.CELL_STYLE_TITLE))
					.getIndex();
			for (int i = 0, len = headers.length; i < len; i++) {
				sw.createCell(i, headers[i], styleIndex);
			}
			sw.endWithheight();
			rowIndex=1;
		}
		int cellIndex = 0;
		int len=dataList.size();
		int dataIndex=0;
		for (int rownum = rowIndex; dataIndex < len;dataIndex++, rownum++ , rowIndex++) {
			cellIndex = 0;
			sw.insertRow(rownum);
			for (int m = 0; m < fields.length; m++){
				Object value = getMethods[m].invoke(dataList.get(dataIndex), new Object[]{});
				//判断值的类型后进行强制类型转换 
                if(value instanceof Date){
                	//日期类型
                    calendar.setTime((Date) value);
					sw.createCell(cellIndex, calendar, dateStyle.getIndex());
                }
                else if(value instanceof Integer){
                    // int类型
                	sw.createCell(cellIndex, (Integer)value, longStyle.getIndex());
                }else if(value instanceof Long){
                    // long类型
                	sw.createCell(cellIndex, (Long)value, longStyle.getIndex());
                }
                else if(value instanceof Double){
                    // double类型
                	sw.createCell(cellIndex, (Double)value,doubleStyle.getIndex());
                }
                else {
                	// 字符串类型
                	sw.createCell(cellIndex, value.toString(),stringStyle.getIndex());
				}
				cellIndex++;
			}
			sw.endRow();
			if (rownum % 2000 == 0) {
				out.flush();
			}
		}
		if(endFlag){
			sw.endSheet();
			sw.endWorkSheet();
		}
		return rowIndex;
	}
	
	public void substituteAll(File zipfile, List<File> tmpfileList,
			List<String> entryList, OutputStream out) throws ZipException, IOException{
		ZipFile zip = new ZipFile(zipfile);
		ZipOutputStream zos = new ZipOutputStream(out);
		Enumeration en = zip.entries();
		while (en.hasMoreElements()) {
			ZipEntry ze = (ZipEntry) en.nextElement();
			if (!entryList.contains(ze.getName())) {
				zos.putNextEntry(new ZipEntry(ze.getName()));
				InputStream is = zip.getInputStream(ze);
				copyStream(is, zos);
				is.close();
			}
		}
		InputStream is = null;
		for (int i = 0, len = entryList.size(); i < len; i++) {
			zos.putNextEntry(new ZipEntry(entryList.get(i)));
			is = new FileInputStream(tmpfileList.get(i));
			copyStream(is, zos);
			is.close();
		}
		zos.close();
		zip.close();
	}
	
	// 复制数据流
	public void copyStream(InputStream in, OutputStream out) throws IOException {
		byte[] chunk = new byte[1024 * 10];
		int count;
		while ((count = in.read(chunk)) >= 0) {
			out.write(chunk, 0, count);
		}
	}
	
	public abstract int getTotalSize();
	
	public abstract List<T> generateData(int pageSize, int pageNo);
}
