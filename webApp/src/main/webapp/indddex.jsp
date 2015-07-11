<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
  <!--common-->
  <link href="css/bootstrap.min.css" rel="stylesheet">
  <link href="css/font-awesome.min.css" rel="stylesheet">

  <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
  <script src="js/html5shiv.js"></script>
  <script src="js/respond.min.js"></script>
  <![endif]-->
  <style type="text/css">
  i{margin-left: 12px;}
  </style>
</head>
<body>
<div>  
        <button class="btn btn-default" id="mail" addtabs="mail" title="我的消息">  
        添加tab页  
        </button>  
        <button class="btn btn-default" id="mail1" addtabs="mail1" title="我的消息1">  
        添加tab页  
        </button>  
        <div class="span12">  
            <ul class="nav-tabs nav" id="tabs1">  
                <li class="active"><a href="#tabs-1" id="tab_tabs-1">主页</a></li>  
                <li id="tab_tabs-2"><a href="#tabs-2">tabs2<i class="fa fa-check" tabclose="tabs-2"></i></a></li>  
                <li id="tab_tabs-3"><a href="#tabs-3">tabs3<i class="fa fa-close" tabclose="tabs-3"></i></a></li>  
            </ul>  
            <div class="tab-content">  
                <div class="tab-pane active" id="tabs-1">  
                    <p>内容一</p>  
                </div>  
                <div class="tab-pane" id="tabs-2">  
                    <p>内容二</p>  
                </div>  
                <div class="tab-pane" id="tabs-3">  
                    <p>内容三</p>  
                </div>  
            </div>  
        </div>  
    </div>
<script src="js/jquery-1.10.2.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
var addTabs = function (obj) {  
    id = "tab_" + obj.id;  
    console.log(obj);  
    $(".active").removeClass("active");  
       
    //如果TAB不存在，创建一个新的TAB  
    if (!$("#" + id)[0]) {  
        //固定TAB中IFRAME高度  
        mainHeight = $(document.body).height() - 95;  
        //创建新TAB的title  
        title = '<li role="presentation" id="tab_' + id + '"><a href="#' + id + '" aria-controls="' + id + '" role="tab" data-toggle="tab">' + obj.title;  
        //是否允许关闭  
        if (obj.close) {  
            title += ' <i class="fa fa-close" tabclose="' + id + '"></i>';  
        }  
        title += '</a></li>';  
        //是否指定TAB内容  
        if (obj.content) {  
            content = '<div role="tabpanel" class="tab-pane" id="' + id + '">' + obj.content + '</div>';  
        } else {//没有内容，使用IFRAME打开链接  
            content = '<div role="tabpanel" class="tab-pane" id="' + id + '"><iframe src="' + obj.url + '" width="100%" height="' + mainHeight +  
                    '" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe></div>';  
        }  
        //加入TABS  
        $(".nav-tabs").append(title);  
        $(".tab-content").append(content);  
    }  
       
    //激活TAB  
    $("#tab_" + id).addClass('active');  
    $("#" + id).addClass("active");  
};  
   
var closeTab = function (id) {  
    //如果关闭的是当前激活的TAB，激活他的前一个TAB  
    if ($("li.active").attr('id') == "tab_" + id) {  
        $("#tab_" + id).prev().addClass('active');  
        $("#" + id).prev().addClass('active');  
    }  
    //关闭TAB  
    $("#tab_" + id).remove();  
    $("#" + id).remove();  
};  
   
$(function () {  
    $("#tabs1 a").click(function (e) {  
        $(this).tab('show');  
    });  
    mainHeight = $(document.body).height() - 45;  
    $('.main-left,.main-right').height(mainHeight);  
    $("[addtabs]").click(function () {  
        addTabs({id: $(this).attr("id"), title: $(this).attr('title'), close: true});  
    });  
   
    $(".nav-tabs").on("click", "[tabclose]", function (e) {  
        id = $(this).attr("tabclose");  
        closeTab(id);  
    });  
}); 
</script>
</body>
</html>