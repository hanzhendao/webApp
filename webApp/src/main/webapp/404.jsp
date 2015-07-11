<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>404错误</title>
<link rel="stylesheet" type="text/css" href="css/error.css" media="all">
</head>
<% response.setStatus(HttpServletResponse.SC_OK); %>
<body class="body-bg">
<div class="main">
    <p class="title">非常抱歉，您要查看的页面没有找到</p>
    <a href="index.jsp" class="btn">返回网站首页</a>
</div>
</body>
</html>