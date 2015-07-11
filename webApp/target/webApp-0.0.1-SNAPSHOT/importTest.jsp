<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
</head>
<body>
<div>

<p></p>
<form action="member/exportMembers.json" method="post">
<input type="submit" value="html格式的excel导出" id="html">
</form>

<form id="form1" enctype="multipart/form-data" method="post" action="upload.php">
	<div class="row">
		<label for="fileToUpload">选择文件上传</label>
		<input type="file" name="fileToUpload" id="fileToUpload" onchange="fileSelected();"/>
    </div>
	<div id="fileName"></div>
	<div id="fileSize"></div>
	<div id="fileType"></div>
	<div class="row">
		<input type="button" onclick="uploadFile()" value="Upload" />
    </div>
	<div id="progressNumber"></div>
</form>

</div>
</body>
<script src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript">
	function fileSelected() {
		var file = document.getElementById('fileToUpload').files[0];
		if (file) {
			var fileSize = 0;
			if (file.size > 1024 * 1024)
				fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
			else
				fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
			
			document.getElementById('fileName').innerHTML = 'Name: ' + file.name;
			document.getElementById('fileSize').innerHTML = 'Size: ' + fileSize;
			document.getElementById('fileType').innerHTML = 'Type: ' + file.type;
		}
	}

	function uploadFile() {
		var fd = new FormData();
		fd.append("fileToUpload", document.getElementById('fileToUpload').files[0]);
		var xhr = new XMLHttpRequest();
		xhr.upload.addEventListener("progress", uploadProgress, false);
		xhr.addEventListener("load", uploadComplete, false);
		xhr.addEventListener("error", uploadFailed, false);
		xhr.addEventListener("abort", uploadCanceled, false);
		xhr.open("POST", "member/importMembers");
		xhr.send(fd);
	}
	
	function uploadProgress(evt) {
		if (evt.lengthComputable) {
			var percentComplete = Math.round(evt.loaded * 100 / evt.total);
			document.getElementById('progressNumber').innerHTML = percentComplete.toString() + '%';
		}
		else {
			document.getElementById('progressNumber').innerHTML = 'unable to compute';
		}
	}
	
	function uploadComplete(evt) {
		/* This event is raised when the server send back a response */
		alert(evt.target.responseText);
	}
	
	function uploadFailed(evt) {
		alert("There was an error attempting to upload the file.");
	}
	
	function uploadCanceled(evt) {
		alert("The upload has been canceled by the user or the browser dropped the connection.");
	}
</script>
</html>