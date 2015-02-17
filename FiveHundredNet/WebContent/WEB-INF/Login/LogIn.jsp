<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*,tw.com.fivehundred.tool.Tools" errorPage=""%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
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
	<div align="center" valign="center">
		<table height="100%" width="50%">
			<tr>
				<td align="center" valign="center">
					<form action="/FiveHundredNet/BlueCat/LogIn" method="post">
						<h1>HKBU DHCP Portal</h1>
						<p>
						<h5>Please enter the account password</h5>
						<table>
							<tr>
								<td>Username : </td>
								<td><input name="userName" type="text" size="20"
									maxlength="25"></td>
							</tr>
							<tr>
								<td>Password : </td>
								<td><input name="passWord" type="password" size="20"
									maxlength="25"></td>
							</tr>
							<tr>
								<td>Bluecat Address Manager IP : </td>
								<td><input name="serverIP" type="text" size="20"
									maxlength="25" value="<%=Tools.getCookieByName(request,"serverIP") %>" ></td>
							</tr>
						</table>
						<input name="login" type="submit" value="Login"> <input
							name="login" type="reset" value="Clean">
					</form>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>