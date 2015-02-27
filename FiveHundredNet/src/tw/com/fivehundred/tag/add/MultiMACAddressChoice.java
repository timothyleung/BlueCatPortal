package tw.com.fivehundred.tag.add;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;

import tw.com.fivehundred.add.been.OneMacAddress;
import tw.com.fivehundred.log.LogMian;
import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;

public class MultiMACAddressChoice extends TagSupport {
	private String file_path;
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");
	private String title[] = { "MAC Address", "IP Address", "Machine Type",
			"Location", "Owner", "Department", "Phone Number", "Reference",
			"Input Date","Overwrite" };
	private String vauleName[] = { "MAC_Address", "IP_Address", "Machine_Type",
			"Location", "Owner", "Department", "Phone_Number", "Reference",
			"Input_Date", "choose_data" };
	private String read_error_message = "";
	final String page_title = "Import Multi IP/MAC Addresses";

	@Override
	public int doStartTag() throws JspException {
		
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		Map session = ActionContext.getContext().getSession();
		file_path = (String) session.get("file_path");
		String choose_data = (String) session.get("choose_data");
		String select_config = "hkbu"; 
		JspWriter out = this.pageContext.getOut();
		String[] data = null;
		APIEntity[] fields = null;
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);
		APIEntity config;
		long id = 0;

		try {
			try {
				config = service.getEntityByName(0, select_config,
						ObjectTypes.Configuration);
				id = config.getId();
			} catch (RemoteException e1) {
				out.write("Please check error message");
			}
			APIEntity[] IPNETWORKfields = Tools.getAllIPAddressNetwork(service, id);
			String ip4Network = "";
			out.write("<h1>" + page_title +"</h1><br>");

			out.write("<table class=\"table table-striped table-hover table-bordered\">");
			out.write("<tr><th><input type='radio' name='ip_choice' value='1' checked onclick='showip3()'>IP Address<input type='radio' name='ip_choice' value='2' onclick='showip4()'>IP Address NetWork</th><td>");
			out.write("<select name='MultiMacAddress.IP_Address_NetWork' id='MultiMacAddress.IP_Address_NetWork' style='display:none'>");
			out.write("<option vaule=''>Please select </option>");
			for (int j = 0; j < IPNETWORKfields.length; j++) {
				ip4Network = Tools.getIPNETWORKbyCIDRstring(IPNETWORKfields[j].getProperties());
				
				//if (ip4Network.split("/")[0].split("\\.")[3].equals("0")) {
					if (IPNETWORKfields[j].getName()!=null){
						out.write("<option vaule='" + ip4Network + "'>"
								+ ip4Network + "  ["+IPNETWORKfields[j].getName() + "]</option>");
					}else{
						out.write("<option vaule='" + ip4Network + "'>"
								+ ip4Network + " </option>");
					}
				//}
			}
			out.write("</select>");
			out.write("</td></tr>");
			out.write("</table>");

		} catch (IOException e) {
			String userName = (String) session.get("userName");
			String log_Content = userName + "," + "" + "," + "" + ","
					+ "Bulk MAC Address" + "," + "Bulk read system error" + ","
					+ Tools.getTime() + "," + "";
			LogMian.writeLog(log_Content, userName);
			try {
				out.write("Bulk read system error");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return super.doStartTag();
	}

	private String[] readFile() {
		BufferedReader dllRead = null;
		ArrayList<String> fileData = new ArrayList<String>();
		String aa = "";
		try {
			dllRead = new BufferedReader(new FileReader(new File(file_path)));
			try {
				while ((aa = dllRead.readLine()) != null) {
					fileData.add(aa);
				}
			} catch (IOException e) {
				read_error_message = read_error_message
						+ "File read failure<br>";
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			read_error_message = read_error_message + "File not find<br>";
			e.printStackTrace();
		} finally {
			try {
				dllRead.close();
			} catch (IOException e) {
				read_error_message = read_error_message
						+ "File close failure<br>";
				e.printStackTrace();
			}
		}
		try {
			String[] data = new String[fileData.size()];
			data = fileData.toArray(data);
			return data;
		} catch (Exception e) {
			return null;
		}
	}
}
