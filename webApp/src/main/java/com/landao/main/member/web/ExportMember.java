package com.landao.main.member.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.landao.main.member.service.MemberService;

@RequestMapping("/member")
@Controller
public class ExportMember {
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value = "/exportMembers", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> exportMembers(HttpServletRequest request,HttpServletResponse response) throws SecurityException, NoSuchMethodException, Exception {
		return memberService.exportMembers(request,response);
	}
	
	@RequestMapping(value = "/importMembers", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> importMembers(String fileData){
		return memberService.importMembers(fileData);
	}
}
