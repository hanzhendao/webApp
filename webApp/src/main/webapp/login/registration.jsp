<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="ThemeBucket">
    <link rel="shortcut icon" href="#" type="image/png">

    <title>注册</title>
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/iCheck/green.css" rel="stylesheet">
    <link href="../css/login.css" rel="stylesheet">
    <link href="../css/bootstrapValidator.min.css" rel="stylesheet">
    
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="../js/html5shiv.js"></script>
    <script src="../js/respond.min.js"></script>
    <![endif]-->
</head>
<body class="login-body">
<div class="container">
	<form id="defaultForm" class="form-signin" method="post" action="../user/regist">
        <div class="form-signin-heading text-center">
            <h1 class="sign-title">注册</h1>
            <img src="../images/login-logo.png" alt="">
        </div>

        <div class="login-wrap">
            <p>输入账户信息</p>
            <div class="form-group">
				<input type="text" class="form-control" name="userName" placeholder="用户名"/>
			</div>
			<div class="form-group">
            	<input type="password" class="form-control" name="password" placeholder="密码"/>
            </div>
            <div class="form-group">
            	<input type="password" class="form-control" name="confirmPassword" placeholder="确认密码"/>
            </div>
            <div class="form-group">
            	<div class="row">
            		<label class="col-lg-6 col-sm-6"><input type="radio" name="sex" class="form-control" checked>男</label>
	            	<label class="col-lg-6 col-sm-6"><input type="radio" name="sex" class="form-control">女</label>
            	</div>
			</div>
            <div class="form-group">
            	<input type="text" class="form-control" name="email" placeholder="邮箱"/>
            </div>
            <button type="submit" class="btn btn-lg btn-login btn-block">
                <i class="fa fa-check"></i>
            </button>
        </div>
    </form>
   
</div>

<!-- Placed js at the end of the document so the pages load faster -->
<script src="../js/jquery-1.10.2.min.js"></script>
<script src="../js/icheck.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/bootstrapValidator.min.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	$('input').iCheck({
	    checkboxClass: 'icheckbox_square-green',
	    radioClass: 'iradio_square-green',
	    increaseArea: '20%' // optional
	  });
	
	$('#defaultForm').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            userName: {
                message: 'The username is not valid',
                validators: {
                    notEmpty: {
                        message: '用户名不能为空'
                    },
                    stringLength: {
                        min: 6,
                        max: 20,
                        message: '用户名长度6-20'
                    },
                    remote: {
                        url: '../user/checkRegistUserNameForRepeat',
                        message: '用户名已被使用'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_\.]+$/,
                        message: '用户名只能包含字母，数字和下划线，点'
                    }
                }
            },
            password: {
                message: 'The username is not valid',
                validators: {
                    notEmpty: {
                        message: '密码不能为空'
                    },
                    stringLength: {
                        min: 6,
                        max: 20,
                        message: '密码长度6-20'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_\.]+$/,
                        message: '密码只能包含字母，数字和下划线，点'
                    }
                }
            },
            confirmPassword: {
                validators: {
                    notEmpty: {
                        message: '确认密码不能为空'
                    },
                    identical: {
                        field: 'password',
                        message: '两次密码不一致'
                    }
                }
            },
            email: {
                validators: {
                    notEmpty: {
                        message: '邮箱不能为空'
                    },
                    emailAddress: {
                        message: '邮箱不合法'
                    }
                }
            }
        }
    });
});
</script>
</body>
</html>