<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.fivehundred.com.tw.delete" prefix="hans"%>
<%@ taglib uri="http://www.fivehundred.com.tw.base" prefix="hans1"%>
<form name="form1" method="post"
	action="/FiveHundredNet/BlueCat/DeletePage?choose=check&jump=oneMACCheck">
	<div>
		<table>
			<tr>
				<td width="50%">Bluecat configuration : <hans1:SelectList /></td>
			</tr>
		</table>
	</div>
	<table cellspacing="0" width="200%" border="1" borderColor='#DEE6EE'>
		<hans:OneMACAddress />
	</table>
	<input type="submit" value="Check">
</form>

