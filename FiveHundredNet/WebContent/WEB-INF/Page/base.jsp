<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<script type="text/javascript" src="/FiveHundredNet/js/jquery-2.1.3.min.js"></script>

<script type="text/javascript" src="/FiveHundredNet/js/bootstrap.min.js"></script>
<link href="/FiveHundredNet/Css/bootstrap.min.css" rel="stylesheet">
<link href="/FiveHundredNet/Css/common.css" rel="stylesheet">

<script>
function showip1(){
	document.getElementById("oneMacAddress.IP_Address").style.display="";
	document.getElementById("oneMacAddress.IP_Address_NetWork").style.display="none";
}
function showip2(){
	document.getElementById("oneMacAddress.IP_Address").style.display="none";
	document.getElementById("oneMacAddress.IP_Address_NetWork").style.display="";
}
function showip3(){ 
	document.getElementById("MultiMacAddress.IP_Address_NetWork").style.display="none";
}
function showip4(){ 
	document.getElementById("MultiMacAddress.IP_Address_NetWork").style.display="";
}
function checkall(){
	form1.action="/FiveHundredNet/BlueCat/AddPage?choose=check&jump=multi"; 
	var m = $('input[type=checkbox]').length;
	var n = $( "input[type=checkbox]:checked" ).length;
	if (m==n){
		form1.submit();
	}else{ 
		alert("Please Choice All Checkbox!");
	}
	return false;
}
function checkalldel(){
	form1.action="/FiveHundredNet/BlueCat/DeletePage?choose=check&jump=multi"; 
	var m = $('input[type=checkbox]').length;
	var n = $( "input[type=checkbox]:checked" ).length;
	if (m==n){
		form1.submit();
	}else{ 
		alert("Please Check all the boxes!");
	}
	return false;
} 
</script>
<script type='text/javascript'>
        $(document).ready(function () {

            console.log("HELLO")
            function exportTableToCSV(filename) {
            	var table = $('#resulttable');
            	
                var headers = table.find('tr:has(th)');
                var rows = table.find('tr:has(td)'); 
                var tmpColDelim = String.fromCharCode(11);  
                var tmpRowDelim = String.fromCharCode(0);  
 
                var colDelim = '","';
                var rowDelim = '"\r\n"';

                    // Grab text from table into CSV formatted string
                    var csv = '"';
                    csv += formatRows(headers.map(grabRow));
                    csv += rowDelim;
                    csv += formatRows(rows.map(grabRow)) + '"';

                    csv = encodeURIComponent(csv);
                    console.log(csv);
                    
                    // Data URI //application/csv
                    var csvData = 'data:application/csv;charset=utf-8,' + csv;
                    //var csvData = 'data:text/plain;base64,' + csv;
                    
                    $("#export").attr("href", csvData);
                     
                    //$(".export").attr("href", csvData ).attr("download", "export.csv"); 
                    //window.open(csvData).attr("download", "export.csv");
                   // window.location.href ='export.csv;data:application/csv;charset=utf-8,helloWorld';
                   
                   /*
                $(this)
                    .attr({
                    'download': filename
                        ,'href': csvData
                        ,'target' : '_blank' //if you want it to open in a new window
                });*/

                //------------------------------------------------------------
                // Helper Functions 
                //------------------------------------------------------------
                // Format the output so it has the appropriate delimiters
                function formatRows(rows){
                    return rows.get().join(tmpRowDelim)
                        .split(tmpRowDelim).join(rowDelim)
                        .split(tmpColDelim).join(colDelim);
                }
                // Grab and format a row from the table
                function grabRow(i,row){
                     
                    var $row = $(row);
                    //for some reason $cols = $row.find('td') || $row.find('th') won't work...
                    var $cols = $row.find('td'); 
                    if(!$cols.length) $cols = $row.find('th');  

                    return $cols.map(grabCol)
                                .get().join(tmpColDelim);
                }
                // Grab and format a column from the table 
                function grabCol(j,col){
                    var $col = $(col),
                        $text = $col.text();

                    return $text.replace('"', '""'); // escape double quotes

                }
            }


            // This must be a hyperlink
            $("#export").click(function (event) {
                // var outputFile = 'export'
                //var outputFile = window.prompt("What do you want to name your output file (Note: This won't have any effect on Safari)") || 'export';
               // outputFile = outputFile.replace('.csv','') + '.csv'
                  
                // CSV
                exportTableToCSV.apply(""); //outputFile);
                
                // IF CSV, don't do event.preventDefault() or return false
                // We actually need this to be a typical hyperlink
            });
        });
</script>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">HKBU DHCP Portal</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
          <li class="dropdown ${not empty add? 'active' : '' }">
          <a href="/FiveHundredNet/BlueCat/AddPage?choose=OneMACAddressShow" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Import <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="/FiveHundredNet/BlueCat/AddPage?choose=OneMACAddressShow">Single IP/MAC Address</a></li>
            <li class="divider"></li>
            <li><a href="/FiveHundredNet/BlueCat/AddPage?choose=MultiMACAddress">Bulk IP/MAC Address</a></li>
          </ul>
        </li>
        <li class="dropdown ${not empty delete? 'active' : '' }">
          <a href="/FiveHundredNet/BlueCat/DeletePage?choose=DeleteData" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Deletion <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="/FiveHundredNet/BlueCat/DeletePage?choose=DeleteData">Single IP Address</a></li>
            <li><a href="/FiveHundredNet/BlueCat/DeletePage?choose=OneMACAddress">Single MAC Address</a></li>
            <li class="divider"></li>
            <li><a href="/FiveHundredNet/BlueCat/DeletePage?choose=MultiMACAddress">Bulk IP/MAC Address</a></li>
          </ul>
        </li>
        <li class="${not empty history? 'active' : '' }"><a href="/FiveHundredNet/BlueCat/HistoryPage?choose=HistoryData">Change Log</a></li>
        <li class="${not empty seach? 'active' : '' }"><a href="/FiveHundredNet/BlueCat/SeachPage?choose=SeachData">Search </a></li>
 
      </ul>

      <ul class="nav navbar-nav navbar-right">        
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">${userName}<span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="/FiveHundredNet/BlueCat/LogOutPage">Log Out</a></li>

          </ul>
        </li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>


<div id="wrap">

	<div id="main">
		<div id="conter">
			<jsp:include page="${ContentPage}"></jsp:include>
		</div>
	</div>
</div>
<nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">

    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <p class="navbar-brand" style='font-size:small'>edvance technology &copy; 2015 &middot; English (US)</p>
    </div>
    </div><!-- /.navbar-collapse -->
 
</nav>
