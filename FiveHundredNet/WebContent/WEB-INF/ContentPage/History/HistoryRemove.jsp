<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.fivehundred.com.tw.History" prefix="hans" %>
<form name="form1" method="post" action="/FiveHundredNet/BlueCat/AddPage?choose=check&jump=one">
	<!--[if IE]><p>Right click and save as.</p><![endif]--><a id="export" class="myButton" download="logdata.csv" href="#">export</a> &nbsp;&nbsp; <a id="clear" class="myButton" href="/FiveHundredNet/BlueCat/HistoryPage?choose=HistoryRemove">clear log</a><br><br>
	<table cellspacing="0" width="110%" border="1" borderColor='#DEE6EE'  id='resulttable' name='resulttable' class='resulttable'>
		<hans:HistoryRemove/>
	</table>
</form>

