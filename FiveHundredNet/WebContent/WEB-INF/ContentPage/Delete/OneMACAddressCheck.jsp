<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.fivehundred.com.tw.delete" prefix="hans"%>
<form name="form1" method="post"
	action="">
	<div>
		<table>
			<tr>
				<td>Bluecat configuration : <input type="text" name="select_config"
					readonly="readonly" value="${select_config}"
					style="border: 1px; border-bottom-style: none; border-top-style: none; border-left-style: none; border-right-style: none;" />
				</td>
			</tr>
		</table>
	</div>
		<hans:OneMACAddressCheck />
</form>

