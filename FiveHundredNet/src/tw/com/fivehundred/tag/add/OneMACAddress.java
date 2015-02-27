package tw.com.fivehundred.tag.add;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.bluecatnetworks.proteus.api.client.java.EntityProperties;
import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;

import tw.com.fivehundred.add.been.OneMacAddress;
import tw.com.fivehundred.tool.Const;
import tw.com.fivehundred.tool.Tools;

public class OneMACAddress extends TagSupport {

	// tim
	//harry
	private String title[] = { "MAC Address", "IP Address NetWork",
			"IP Address", "Servers", "Machine Type", "Location", "Owner",
			"Department", "Phone Number", "Input Date", "Reference" };

	private String vauleName[] = { "MAC_Address", "IP_Address_NetWork",
			"IP_Address", "Servers", "Machine_Type", "Location", "Owner",
			"Department", "Phone_Number", "Input_Date", "Reference" };

	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");
	private String demonstration_text[] = { "xx-xx-xx-xx-xx-xx", "",
			"xxx.xxx.xxx.xxx", "", "", "", "", "", "",
			format.format(new Date()), "" };
	private long id;

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		APIEntity config;
		JspWriter out = this.pageContext.getOut();
		Map session = ActionContext.getContext().getSession();
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");

		String select_config = Const.CONFIG_NAME;
		session.remove("select_config");

		OneMacAddress oneMacAddress = (OneMacAddress) session.get("one_data"); // getting a null oneMacAddressTructure
		session.remove("one_data");
		try {
			config = service.getEntityByName(0, select_config,
					ObjectTypes.Configuration);
			id = config.getId();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		APIEntity[] fields = Tools.getAllIPAddressNetwork(service, id);
		APIEntity[] servers = Tools.getAllServers(service, id);
		String ip4Network = "", servers_name = "";
		
		final String page_title = "Import Single IP/MAC address";
		
		// boolean not_null = false;
		try {
			
			out.write("<h1>" + page_title +"</h1><br>");
			//harry
			out.write("<table class=\"table table-striped table-hover table-bordered\">");
			
			out.write("<tr><th align=left width=40%>MAC Address</th><td>");
			out.write("<input type='text' size='20' name='oneMacAddress.MAC_Address' onfocus=\"if (this.value=='xx-xx-xx-xx-xx-xx') this.value='';\" onblur=\"if (this.value=='') this.value='xx-xx-xx-xx-xx-xx';\" value=\"xx-xx-xx-xx-xx-xx\"" + ">");
			out.write("</td></tr>");
			
			out.write("<tr><th align=left><input type='radio' name='ip_choice' value='1' checked onclick='showip1()'>IP Address<input type='radio' name='ip_choice' value='2' onclick='showip2()'>IP Address NetWork</th><td>");
			out.write("<input type='text' size='20' name='oneMacAddress.IP_Address' id='oneMacAddress.IP_Address' onfocus=\"if (this.value=='xxx.xxx.xxx.xxx') this.value='';\" onblur=\"if (this.value=='') this.value='xxx.xxx.xxx.xxx';\" value=\"xxx.xxx.xxx.xxx\"" + ">");
			out.write("<select name='oneMacAddress.IP_Address_NetWork' id='oneMacAddress.IP_Address_NetWork' style='display:none'>");
			out.write("<option vaule=''>Please select </option>");
			for (int j = 0; j < fields.length; j++) {
				ip4Network = Tools.getIPNETWORKbyCIDRstring(fields[j].getProperties());
				
				//if (ip4Network.split("/")[0].split("\\.")[3].equals("0")) {
					if (fields[j].getName()!=null){
						out.write("<option vaule='" + ip4Network + "'>"
								+ ip4Network + "  ["+fields[j].getName() + "]</option>");
					}else{
						out.write("<option vaule='" + ip4Network + "'>"
								+ ip4Network + " </option>");
					}
				//}
			}
			out.write("</select>");
			out.write("</td></tr>");
			  
			
			//3
			out.write("<tr style='display:none'><th align=left>Servers</th><td >");
			out.write("<select name='oneMacAddress.Servers'>");
			for (int j = 0; j < servers.length; j++) {
				servers_name = servers[j].getName();
				out.write("<option vaule='" + servers_name + "'>"
						+ servers_name + "</option>");

			}
	
			out.write("</select>");
			out.write("</td></tr>");
			out.write("</table>");
			
			out.write("<div><h5>The fields below are user defined field (UDF)</h5></div>");
			out.write("<table class=\"table table-striped table-hover table-bordered\">");
			print_information_row(out, "Machine Type", oneMacAddress.getMachine_Type(), "" , "oneMacAddress.Machine_Type");

			print_information_row(out, "Location", oneMacAddress.getLocation(), "", "oneMacAddress.Location");
			
			print_information_row(out, "Owner", oneMacAddress.getOwner(), "", "oneMacAddress.Owner");
			
			print_information_row(out, "Department", oneMacAddress.getDepartment(), "", "oneMacAddress.Department");

			print_information_row(out, "Extension", oneMacAddress.getPhone_Number(), "", "oneMacAddress.Phone_Number");

			print_information_row(out, "Input Date", oneMacAddress.getInput_Date(), format.format(new Date()), "oneMacAddress.Input_Date");
		
			print_information_row(out, "Network Reference", oneMacAddress.getReference(), "", "oneMacAddress.Reference");

			out.write("</table>"); 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
	
	private static void print_information_row(JspWriter out, String header, String value1, String value2, String html_type) throws IOException{
		out.write("<tr><th>" + header + "</th><td>");
		if ( value1 != null) {
			out.write("<input type='text' size='20' name='" + html_type +"' value='"
					+ value1 + "'>");
		}else{
			out.write("<input type='text' size='20' name='" + html_type + "' value='" + value2 + "'>");
		} 
		out.write("</td></tr>");
	}
}
