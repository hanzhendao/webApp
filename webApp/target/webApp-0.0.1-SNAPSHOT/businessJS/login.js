$(function(){
	$("#login").click(function(){
		$.ajax({
			url:"../user/login",
			type : 'post',
			data:{userName:$("#username").val(),password:$("#password").val()},
			success : function(data) {
				if(data.state){
					alert('ok');
				}else {
					alert(data.msg);
				}
			}
		});
	});
});