<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.fivehundred.com.tw.add" prefix="hans"%>
<%@ taglib uri="http://www.fivehundred.com.tw.base" prefix="hans1"%>
<form name="form1" enctype="multipart/form-data" method="post"
	action="/FiveHundredNet/BlueCat/AddPage?choose=check&jump=read">
	<div>
		<table>
			<tr>
				<td width="80%">Bluecat configuration : hkbu<span style="display:none"><hans1:SelectList /></span></td>
			</tr>
			 
		</table> 
	</div> 
	<hans:MultiMACAddressChoice /> 
	<a href="/FiveHundredNet/BlueCat/DownPage">Demonstration Files</a><br />
	<input type="file" name="fileData" size="20" accept=".csv" value=".csv" /><br>
	<input class='btn btn-default' type="submit" value="Read">
</form>

