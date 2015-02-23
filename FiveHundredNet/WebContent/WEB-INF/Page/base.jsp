<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<script type='text/javascript' src='https://code.jquery.com/jquery-1.11.0.min.js'></script>
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
<div id="wrap">
	<div id="header">
		<div>
			<h1>HKBU DHCP Portal</h1>
		</div>
	</div>
	<div id="menu">
		<ul>
			<li ${add}><a href="/FiveHundredNet/BlueCat/AddPage?choose=OneMACAddressShow">Import</a></li>
			<li ${delete}><a 
				href="/FiveHundredNet/BlueCat/DeletePage?choose=DeleteData">Deletion</a></li>
			<li ${history}><a 
				href="/FiveHundredNet/BlueCat/HistoryPage?choose=HistoryData">Change log</a></li>
			<li ${seach}><a  href="/FiveHundredNet/BlueCat/SeachPage?choose=SeachData">Search</a></li>
			<li><a href="/FiveHundredNet/BlueCat/LogOutPage">Log out</a></li>
		</ul>
		<div class="clear"></div>
	</div>
	<div>
		<table>
			<tr>
				<td width="80%">User : ${userName}</td>
			</tr>
		</table>
	</div>
	<div id="main">
		<div id="conter">
			<jsp:include page="${whichPage}"></jsp:include>
		</div>
	</div>
</div>
<nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">
  <div class="container-fluid ">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <p class="navbar-brand" >edvance technology &copy; 2015 &middot; English (US)</p>
    </div>

    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
 
</nav>