package com.landao.main.member.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landao.main.common.excel.AbstractExportExcel;
import com.landao.main.common.excel.ExcelUtil;
import com.landao.main.common.excel.model.Excel;
import com.landao.main.common.excel.model.ExcelError;
import com.landao.main.common.excel.parse.Parser;
import com.landao.main.common.util.EndecryptUtils;
import com.landao.main.member.parse.MemberParse;
import com.landao.main.member.viewmodel.MemberBean;
import com.landao.main.repository.dao.ExcelDao;
import com.landao.main.repository.dao.MemberDao;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private ExcelDao excelDao;
	public Map<String, Object> exportMembers(HttpServletRequest request, HttpServletResponse response) throws SecurityException, NoSuchMethodException, Exception {
		Map<String, Object> operateMap=new HashMap<String, Object>();
		System.out.println(request.getServletPath());
		System.out.println(request.getSession().getServletContext().getRealPath("/"));
		long start=System.currentTimeMillis();
		String[] headers=new String[]{"会员卡号","姓名"};
		String[] fields=new String[]{"cardCode","name"};
		AbstractExportExcel<MemberBean> exportExcel=new AbstractExportExcel<MemberBean>(){

			@Override
			public int getTotalSize() {
				return memberDao.getTotalSize("member.getMembersCount");
			}

			@Override
			public List generateData(int pageSize, int pageNo) {
				return memberDao.getPageData(pageSize,pageNo);
			}
			
		};
		exportExcel.generateExcel(request, "会员", "会员", headers, fields, response);
		long end=System.currentTimeMillis();
	    System.out.println("用时"+(end-start)+"ms");
		return operateMap;
	}
	
	public Map<String, Object> importMembers(String fileData) {
		Map<String, Object> operateMsg=new HashMap<String, Object>();
		byte[] fileBytes=EndecryptUtils.decodeStringByBase64(fileData);
		ByteArrayInputStream inputStream=new ByteArrayInputStream(fileBytes);
		Excel excel = new Excel();
		excel.setExlFileStream(inputStream);
		Map<String, Parser> map = new HashMap<String, Parser>();
		String sheetName = "库存盘点资料";
		map.put(sheetName, new MemberParse());
		excel.setSheetName(sheetName);
		excel.setSheetParsers(map);
		excel = ExcelUtil.parseExcel(excel);
		
		StringBuilder message = new StringBuilder();
		List<MemberBean> excelBeans=new ArrayList<MemberBean>();
		boolean flag=true;
		// 保存信息
		List<ExcelError> errors = excel.getErrorList();
		if (errors.isEmpty()) {
			// 获得所有数据
			List<Object> memberObjects = excel.getResultList();
			//检查重复数据
			Map<String, Object> repeatMap=new HashMap<String, Object>();
			for (Object obj : memberObjects) {
				MemberBean bean = (MemberBean) obj;
				String key=bean.getCardCode();
				if(repeatMap.get(key)==null){
					repeatMap.put(key,key);
				}
				else {
					message.append("行:" + (bean.getRow() + 1) + "")
				       .append(" 会员卡号:"+bean.getCardCode()+"已经存在")
				       .append("<br>");
					flag=false;
					break;
				}
				excelBeans.add(bean);
			}
		}
		else {
			message.append("excel数据不合法");
			message.append("<br>");
			for (ExcelError error : errors) {
				message.append("行:" + (error.getRowNum() + 1));
				message.append(" 列:" + (error.getColNum() + 1));
				message.append("  " + error.getMessage() + "  "
						+ error.getValue());
				message.append("  数据不合法<br>");
			}
			flag=false;
		}
		if(flag){
			//数据正常
		}
		else {
			operateMsg.put("msg", message.toString());
		}
		return operateMsg;
	}

}
