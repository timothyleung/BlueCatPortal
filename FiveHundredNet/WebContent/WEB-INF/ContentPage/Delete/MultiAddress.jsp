<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.fivehundred.com.tw.delete" prefix="hans" %>
<%@ taglib uri="http://www.fivehundred.com.tw.base" prefix="hans1" %>
<form name="form1" enctype="multipart/form-data" method="post" action="/FiveHundredNet/BlueCat/DeletePage?choose=check&jump=read">
	<div>
		<table>
			<tr>
				<td width="50%">Bluecat configuration : <hans1:SelectList /></td>
			</tr>
		</table>
	</div>
	<hans:MultiMACAddressChoiceDel /> 
	Demonstration Files : <a href="/FiveHundredNet/BlueCat/DownDelPagemac">Delete MAC </a> &nbsp; &nbsp;  <a href="/FiveHundredNet/BlueCat/DownDelPageip">Delete IP </a>  <br/>	
	<input type="file" name="fileData" size="20" accept=".csv" value=".csv" /><br>
	<input type="submit" value="Read" >
</form>

