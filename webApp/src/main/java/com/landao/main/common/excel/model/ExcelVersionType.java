package com.landao.main.common.excel.model;

public enum ExcelVersionType {
	
	Excel2003("2003",".xls"),Excel2010("2010",".xlsx");
	
	private String version;
	private String suffix;
	private ExcelVersionType(String version, String suffix) {
		this.version = version;
		this.suffix = suffix;
	}
	
	public String toString() {
		return super.toString() + "(" + version + "," + suffix + ")";
	}
}
