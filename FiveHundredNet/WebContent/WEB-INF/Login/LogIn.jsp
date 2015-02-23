<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*,tw.com.fivehundred.tool.Tools" errorPage=""%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<link href="/FiveHundredNet/Css/bootstrap.min.css" rel="stylesheet">
<link href="/FiveHundredNet/Css/signin.css" rel="stylesheet">

<title>Bluecat IPAM Login Page</title>
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
	<div class="container" >

		<form class="form-signin " action="/FiveHundredNet/BlueCat/LogIn" method="post">
						<h1 class="form-signin-heading">HKBU DHCP Portal</h1>
						<input class="form-control" placeholder="Username" name="userName" type="text" size="20"
									maxlength="25">
						<input class="form-control" placeholder="Password" name="passWord" type="password" size="20"
									maxlength="25">
						<input class="form-control" placeholder="Server IP" name="serverIP" type="text" size="20"
									maxlength="25" value="<%=Tools.getCookieByName(request,"serverIP") %>" >
									
					<button type="submit" class="btn btn-primary btn-block" name="login" value="Login">Log in</button>
					<button type="reset" class="btn btn-primary btn-block" name="login" value="Clean">Reset</button>	
		</form>
	</div>
<nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">

    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <p class="navbar-brand" style="color:#fff">edvance technology &copy; 2015 &middot; English (US)</p>
    </div>
    </div><!-- /.navbar-collapse -->
 
</nav>
</body>
</html>