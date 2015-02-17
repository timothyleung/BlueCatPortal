<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.fivehundred.com.tw.add" prefix="hans"%>
<%@ taglib uri="http://www.fivehundred.com.tw.base" prefix="hans1"%>
<form name="form1" method="post"
	action="">
	<div>
		<table>
			<tr>
			<td>Bluecat DHCP Server: BDDS1 & BDDS2</td>
				<td style="display:none">Bluecat configuration:<input type="text" name="select_config"
					readonly="readonly" value="${select_config}"
					style="border: 1px; border-bottom-style: none; border-top-style: none; border-left-style: none; border-right-style: none;" />
				</td>
			</tr>
		</table>
	</div>
		<hans:OneMACAddressCheck />
</form>

