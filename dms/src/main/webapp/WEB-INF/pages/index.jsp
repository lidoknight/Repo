<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" type="text/css" href="${resRoot }/css/frame.css" />
</head>
<body>
	<div id="header">
		<jsp:include page="header.jsp"></jsp:include>
	</div>
	<div id="mainPage">
		<div id="navi">
			<jsp:include page="navigation.jsp"></jsp:include>
		</div>
		<div id="content">
			<jsp:include page="content.jsp"></jsp:include>
		</div>
	</div>
	<div id="footer">
		<jsp:include page="footer.jsp"></jsp:include>
	</div>
</body>
</html>