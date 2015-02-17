<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.fivehundred.com.tw.add" prefix="hans"%>
<%@ taglib uri="http://www.fivehundred.com.tw.base" prefix="hans1"%>
<form name="form1" method="post"
	action="/FiveHundredNet/BlueCat/AddPage?choose=OneMACAddressShow">
	Please select Bluecat configuration<br/>
	<div>
		<table>
			<tr>
				<td>Bluecat configuration :<hans1:SelectList /></td>
			</tr>
		</table>
	</div>
	<input type="submit" value="Check" >
</form>

