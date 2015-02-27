<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.fivehundred.com.tw.add" prefix="hans"%>
<%@ taglib uri="http://www.fivehundred.com.tw.base" prefix="hans1"%>
<form name="form1" method="post"
	action="/FiveHundredNet/BlueCat/AddPage?choose=check&jump=one">
	<div>
		<table>
			<tr>
				<td>Bluecat DHCP Server: BDDS1 & BDDS2
				</td>
			</tr>
		</table>
	</div>
	<table class="table table-striped table-hover table-bordered">
		<hans:OneMACAddress />
	</table>
	<input class="btn btn-default"type="submit" value="Check" >
</form>

