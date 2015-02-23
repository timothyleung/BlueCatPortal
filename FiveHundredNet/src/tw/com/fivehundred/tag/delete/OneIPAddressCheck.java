package tw.com.fivehundred.tag.delete;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import tw.com.fivehundred.add.been.OneMacAddress;
import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;

public class OneIPAddressCheck extends TagSupport {

	private String title[] ={ "IP Address"};
	private String vauleName[] = {"IP_Address"};

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		Map session = ActionContext.getContext().getSession();
		String select_config = (String) session.get("select_config");
		String IP_Address = (String) session.get("IP_Address");
		session.remove("IP_Address");
		session.remove("select_config");
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);
		JspWriter out = this.pageContext.getOut();
		try {
			String message_ip = "";
			String message_mac="";
			APIEntity config;
			long id = 0;
			boolean check=false;
			// ���dip
			if (IP_Address.trim().length() > 0) {
				String[] ip_marray = IP_Address.trim()
						.split("\\.");
				if (ip_marray.length != 4) {
					message_ip = "Incorrect IP Address: Please check IP address format";
				} else {
					if (Tools.checkIPAddress(ip_marray)) {
						message_ip = "Incorrect IP Address: Please check IP address format";
					} else {
						try {
							config = service.getEntityByName(0, select_config,
									ObjectTypes.Configuration);
							id = config.getId();
							try {
								APIEntity config_ip = service.getIP4Address(id,
										IP_Address);
								if (config_ip != null) {
									message_mac= Tools.getMACbyMACADDRESSstring(config_ip.getProperties());
									check=true;
									message_ip = "Record found";// "Duplicated MAC Address";
								}else{
									message_ip = "IP address record not found"; //"Free IP Address";
								}
							} catch (RemoteException e2) {
								message_ip = "IP Address Check Failure: Please check input IP address ";
							}
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
				}
			} else {
				message_ip = "Incorrect IP Address: Please check IP address format";
			}
			out.write("<table class=\"table table-striped table-hover table-bordered\">");
			for (int i = 0; i < title.length; i++) {
				out.write("<tr><td align=left>" + title[i] + "</td><td>");
				out.write("<input type='text' name='IP_Address' readonly='readonly' value=" + IP_Address + ">");
				out.write("</td><td>Mac Address : "+ message_mac + "</td></tr>");
			}
			out.write("</table>");
			if(!check){				
				out.write("<FONT COLOR='#FF0000'>(" +message_ip+ ")</FONT>");
				out.write("<br><input class=\"btn btn-default\"  type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=DeletePage&log=ipCheckErro'\">");
			}else{
				out.write("<br><input class=\"btn btn-default\"  type='submit' value='Delete' onclick=\"form1.action='/FiveHundredNet/BlueCat/DeletePage?choose=check&jump=oneIPDelete'\">");
				out.write("<input class=\"btn btn-default\" type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=DeletePage&log=ipUserCancels'\">");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}
