<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/FiveHundredNet/Css/Base.css">
<link href="/FiveHundredNet/Css/bootstrap.min.css" rel="stylesheet">

<title>Bluecat IPAM Protal - Deletion Page</title>
<script type="text/javascript">
	function doIt() {
		var erroMessage = "${erroMessage}";
		if(erroMessage.length!=0){
		alert(erroMessage);
		}
	}
	window.onload = doIt;
</script>
</head>
<body>
	<jsp:include page="/WEB-INF/Page/base.jsp" />
</body>
</html>
