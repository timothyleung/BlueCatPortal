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

import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;

import tw.com.fivehundred.add.been.OneMacAddress;
import tw.com.fivehundred.tool.Const;
import tw.com.fivehundred.tool.Tools;

public class OneMACAddress extends TagSupport {

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

		String select_config = (String) session.get("select_config");
		//harry

		select_config=Const.CONFIG_NAME;
		session.remove("select_config");

		OneMacAddress oneMacAddress = (OneMacAddress) session.get("one_data");
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
		
		boolean not_null = false;
		try {
			
			out.write("<h1>" + page_title +"</h1><br>");
			//harry
			out.write("<table cellspacing='0' width='180%' border='1' borderColor='#DEE6EE'>");
			
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
			out.write("<div>Below data used user defined field (UDF)</div>");
			out.write("<table cellspacing='0' width='180%' border='1' borderColor='#DEE6EE'>");
			
			//4
			out.write("<tr><th align=left>Machine Type</th><td>");
			if (oneMacAddress.getMachine_Type() != null) {
				out.write("<input type='text' size='20' name='oneMacAddress.Machine_Type' value='"
						+ oneMacAddress.getMachine_Type() + "'>"); 
			}else{
				out.write("<input type='text' size='20' name='oneMacAddress.Machine_Type' value=''>");
			}
			out.write("</td></tr>");
			
			
			
			//5
			out.write("<tr><th align=left>Location</th><td>");
			if (oneMacAddress.getLocation() != null) {
				out.write("<input type='text' size='20' name='oneMacAddress.Location' value='"
						+ oneMacAddress.getLocation() + "'>");
				not_null = true;
			}else{
				out.write("<input type='text' size='20' name='oneMacAddress.Location' value=''>");
			} 
			out.write("</td></tr>");
			
		 
			//6
			out.write("<tr><th align=left>Owner</th><td>");
			if ( oneMacAddress.getOwner() != null) {
				out.write("<input type='text' size='20' name='oneMacAddress.Owner' value='"
						+ oneMacAddress.getOwner() + "'>");
				not_null = true;
			}else{
				out.write("<input type='text' size='20' name='oneMacAddress.Owner' value=''>");
			}
			out.write("</td></tr>");
			
			
			//7
			out.write("<tr><th align=left>Department</th><td>");
			if ( oneMacAddress.getDepartment() != null) {
				
				out.write("<input type='text' size='20' name='oneMacAddress.Department' value='"
						+ oneMacAddress.getDepartment() + "'>");
				not_null = true;
			}else{
				out.write("<input type='text' size='20' name='oneMacAddress.Department' value=''>");
			}
			out.write("</td></tr>");
			
			
			//8
			out.write("<tr><th align=left>Extension</th><td>");
			if ( oneMacAddress.getPhone_Number() != null) {
				out.write("<input type='text' size='20' name='oneMacAddress.Phone_Number' value='"
						+ oneMacAddress.getPhone_Number() + "'>");
				not_null = true;
			}else{
				out.write("<input type='text' size='20' name='oneMacAddress.Phone_Number' value=''>");
			}
			out.write("</td></tr>");
			
			
			//9
			out.write("<tr><th align=left>Input Date</th><td>");
			if ( oneMacAddress.getInput_Date() != null) {
				out.write("<input type='text' size='20' name='oneMacAddress.Input_Date' readonly value='"
						+ oneMacAddress.getInput_Date() + "'>");
				not_null = true;
			}else{
				out.write("<input type='text' size='20' name='oneMacAddress.Input_Date' readonly value=\""+format.format(new Date())+"\"" + ">");
			}
			
			out.write("<tr><th align=left>Network Reference</th><td>");
			if ( oneMacAddress.getReference() != null) {
				out.write("<input type='text' size='20' name='oneMacAddress.Reference' value='"
						+ oneMacAddress.getReference() + "'>");
				not_null = true;
			}else{
				out.write("<input type='text' size='20' name='oneMacAddress.Reference' value=''>");
			} 
			out.write("</td></tr>");
			out.write("</table>"); 

			/*
			
			for (int i = 0; i < title.length; i++) {

				if (i == 4) {
					out.write("</table>");
					out.write("<div>Below data used user defined field (UDF)</div>");
					out.write("<table cellspacing='0' width='200%' border='1' borderColor='#DEE6EE'>");
				}
				out.write("<tr><th align=left>" + title[i] + "</th><td>");
				if (i != 1 && i != 3) {
					not_null = false;
					if (i == 4 && oneMacAddress.getMachine_Type() != null) {
						out.write("<input type='text' size='20' name='oneMacAddress."
								+ vauleName[i]
								+ "' value='"
								+ oneMacAddress.getMachine_Type() + "'>");
						not_null = true;
					}
					if (i == 5 && oneMacAddress.getLocation() != null) {
						out.write("<input type='text' size='20' name='oneMacAddress."
								+ vauleName[i]
								+ "' value='"
								+ oneMacAddress.getLocation() + "'>");
						not_null = true;
					}
					if (i == 6 && oneMacAddress.getOwner() != null) {
						out.write("<input type='text' size='20' name='oneMacAddress."
								+ vauleName[i]
								+ "' value='"
								+ oneMacAddress.getOwner() + "'>");
						not_null = true;
					}
					if (i == 7 && oneMacAddress.getDepartment() != null) {
						out.write("<input type='text' size='20' name='oneMacAddress."
								+ vauleName[i]
								+ "' vaule='"
								+ oneMacAddress.getDepartment() + "'>");
						not_null = true;
					}
					if (i == 8 && oneMacAddress.getPhone_Number() != null) {
						out.write("<input type='text' size='20' name='oneMacAddress."
								+ vauleName[i]
								+ "' value='"
								+ oneMacAddress.getPhone_Number() + "'>");
						not_null = true;
					}
					if (i == 9 && oneMacAddress.getInput_Date() != null) {
						out.write("<input type='text' size='20' name='oneMacAddress."
								+ vauleName[i]
								+ "' value='"
								+ oneMacAddress.getInput_Date() + "'>");
						not_null = true;
					}
					if (i == 10 && oneMacAddress.getReference() != null) {
						out.write("<input type='text' size='20' name='oneMacAddress."
								+ vauleName[i]
								+ "' value='"
								+ oneMacAddress.getReference() + "'>");
						not_null = true;
					}
					if (!not_null) {
						out.write("<input type='text' size='20' name='oneMacAddress."
								+ vauleName[i]
								+ "'"
								+ " onfocus=\"if (this.value=='"
								+ demonstration_text[i]
								+ "') this.value='';\" onblur=\"if (this.value=='') this.value='"
								+ demonstration_text[i]
								+ "';\" value=\""
								+ demonstration_text[i] + "\"" + ">");
					}

				} else {
					if (i == 1) {
						out.write("<select name='oneMacAddress." + vauleName[i]
								+ "'>");
						for (int j = 0; j < fields.length; j++) {
							ip4Network = fields[j]
									.getProperties()
									.substring(
											fields[j].getProperties().indexOf(
													"CIDR=") + 5,
											fields[j].getProperties().indexOf(
													"|",
													(fields[j].getProperties()
															.indexOf("CIDR="))));
							if (ip4Network.split("/")[0].split("\\.")[3]
									.equals("0")) {
								out.write("<option vaule='" + ip4Network + "'>"
										+ ip4Network + "</option>");
							}
						}
						out.write("</select>");
					}
					if (i == 3) {
						out.write("<select name='oneMacAddress." + vauleName[i]
								+ "'>");
						for (int j = 0; j < servers.length; j++) {
							servers_name = servers[j].getName();
							out.write("<option vaule='" + servers_name + "'>"
									+ servers_name + "</option>");

						}
						out.write("</select>");
					}
				}
				out.write("</td></tr>");
			}*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}
