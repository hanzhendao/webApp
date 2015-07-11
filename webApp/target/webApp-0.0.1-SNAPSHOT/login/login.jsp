<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="ThemeBucket">
    <link rel="shortcut icon" href="#" type="image/png">

    <title>登录</title>
    <link href="../css/style.css" rel="stylesheet">
    <link href="../css/login.css" rel="stylesheet">
    <link href="../css/style-responsive.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="../js/html5shiv.js"></script>
    <script src="../js/respond.min.js"></script>
    <![endif]-->
</head>
<body class="login-body">
<div class="container">
    <form class="form-signin" action="../user/login" method="post">
        <div class="form-signin-heading text-center">
            <h1 class="sign-title">登录</h1>
            <img src="../images/login-logo.png" alt="">
        </div>
        
        <c:if test="${state==false }">
	        <div class="alert alert-danger" style="width: 290px;margin: -82px 20px 0;position:absolute;">
			  <a class="close" data-dismiss="alert" style="color:black">×</a>
			  <h4 class="alert-heading">警告!</h4>
			  ${msg }
			</div>
        </c:if>
        
        <div class="login-wrap">
            <input type="text" name="userName" class="form-control" placeholder="用户名" autofocus value="${userName }">
            <input type="password" name="password" class="form-control" placeholder="密码" value="${password }">

            <button class="btn btn-lg btn-login btn-block" type="submit">
                <i class="fa fa-check"></i>
            </button>

            <div class="registration">
            	不是会员?<a class="" href="registration.jsp">注册</a>
            </div>
            <label class="checkbox">
                <input type="checkbox" name="isRemember" value="1">记住我
                <span class="pull-right">
                    <a data-toggle="modal" href="#myModal">忘记密码?</a>
                </span>
            </label>
        </div>

        <!-- Modal -->
        <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Forgot Password ?</h4>
                    </div>
                    <div class="modal-body">
                        <p>Enter your e-mail address below to reset your password.</p>
                        <input type="text" name="email" placeholder="Email" autocomplete="off" class="form-control placeholder-no-fix">

                    </div>
                    <div class="modal-footer">
                        <button data-dismiss="modal" class="btn btn-default" type="button">Cancel</button>
                        <button class="btn btn-primary" type="button">Submit</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- modal -->
    </form>
</div>

<!-- Placed js at the end of the document so the pages load faster -->
<script src="../js/jquery-1.10.2.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/modernizr.min.js"></script>
<script src="../businessJS/login.js"></script>
</body>
</html>