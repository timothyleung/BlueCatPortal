<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.fivehundred.com.tw.Seach" prefix="hans"%>
<%@ taglib uri="http://www.fivehundred.com.tw.base" prefix="hans1"%>
<form name="form1" method="post"
	action="/FiveHundredNet/BlueCat/SeachPage?choose=check&jump=seach">
	<div>
		<table>
			<tr>
				<td width="50%">Bluecat configuration : <hans1:SelectList /></td>
			</tr>
		</table>
	</div>
	<table class="table table-striped table-hover table-bordered">
		<hans:Seach />
	</table>
	<input class='btn btn-default' type="submit" value="Search">
</form>

