<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.fivehundred.com.tw.History" prefix="hans" %>
<form name="form1" method="post" action="/FiveHundredNet/BlueCat/AddPage?choose=check&jump=one">
	<table class="table table-striped table-hover table-bordered" id='resulttable' name='resulttable' class='resulttable'>
		<hans:HistoryList/>
	</table>
</form>

