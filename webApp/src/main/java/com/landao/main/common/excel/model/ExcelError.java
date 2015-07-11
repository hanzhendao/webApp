package com.landao.main.common.excel.model;

import java.io.Serializable;

public class ExcelError implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String message = null;
	private String sheetName = null;
	private int rowNum = 0;
	private int colNum = 0;
	private String value = null;

	public ExcelError(){}
	
	public ExcelError(String message, String sheetName, int rowNum,
			int colNum, String value) {
		this.message = message;
		this.sheetName = sheetName;
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
