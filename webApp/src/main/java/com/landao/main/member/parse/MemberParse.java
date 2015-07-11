package com.landao.main.member.parse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.landao.main.common.excel.ExcelUtil;
import com.landao.main.common.excel.model.Excel;
import com.landao.main.common.excel.parse.Parser;
import com.landao.main.member.viewmodel.MemberBean;

public class MemberParse implements Parser{
	@Override
	public Excel parse(Row row, Excel excel) {
		MemberBean memberBean=new MemberBean();
		memberBean.setRow(row.getRowNum());
		
		Cell cell = null;
		int columnNum = row.getPhysicalNumberOfCells();
		for(int i = 0; i<columnNum; i++){
			cell = row.getCell(i);
			if(cell!=null && cell.getCellType() == Cell.CELL_TYPE_FORMULA){
				excel.addExcelError(row, i, "含有公式，请去掉公式", excel.getSheetName(), null);
			}
		}
		
		String cardCode = ExcelUtil.getStringValue(row.getCell(0), null);
		if(StringUtils.isEmpty(cardCode)) {
			excel.addExcelError(row, 0, "会员卡号", excel.getSheetName(), null);
		}else{
			memberBean.setCardCode(cardCode.replaceAll(" ", ""));
		}
		
		String name = ExcelUtil.getStringValue(row.getCell(1), null);
		if(StringUtils.isEmpty(name)) {
			excel.addExcelError(row, 1, "姓名", excel.getSheetName(), null);
		}else{
			memberBean.setName(name.replaceAll(" ", ""));
		}
		
		excel.setObj(memberBean);
		return excel;
	}

	@Override
	public int getStartRow() {
		return 1;
	}
}
