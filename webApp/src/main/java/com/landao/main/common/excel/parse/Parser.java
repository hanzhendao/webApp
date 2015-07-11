package com.landao.main.common.excel.parse;

import org.apache.poi.ss.usermodel.Row;
import com.landao.main.common.excel.model.Excel;

public interface Parser {
	public Excel parse(Row row, Excel excelRead);

	public int getStartRow();
}
