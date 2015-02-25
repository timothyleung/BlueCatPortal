package tw.com.fivehundred.tag.delete;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import tw.com.fivehundred.add.been.OneMacAddress;
import tw.com.fivehundred.tool.AddressType;
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
		String page_title = "Single IP Check";
		boolean check = false;
		try {
			APIEntity config;
			long id = 0;
			APIEntity config_ip = null;
			out.write("<h1>" + page_title +"</h1><br>");
			out.write("<table class=\"table table-striped table-hover table-bordered\">");
			out.write("<tr><td>IP Address</td><td>MAC Address</td></tr>");
			if (Tools.checkIPAddress(IP_Address)) {
				try {
					config = service.getEntityByName(0, select_config,
							ObjectTypes.Configuration);
					id = config.getId();
					config_ip = service.getIP4Address(id, IP_Address);
					
					int single_delete_indicator = 0;
					check = Tools.single_deletion_check(out, config_ip, AddressType.IP, IP_Address, single_delete_indicator);

				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else {
				out.write("<tr><td colspan=\"100%\">" + IP_Address + " have an invalid format :( </td></tr>");
			}
			out.write("</table>");
			if (!check){
				out.write("<br><input class=\"btn btn-default\"  type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=DeletePage&log=ipCheckErro'\">");
			} else {
				out.write("<br><input class=\"btn btn-default\"  type='submit' value='Delete' onclick=\"form1.action='/FiveHundredNet/BlueCat/DeletePage?choose=check&jump=oneIPDelete'\">");
				out.write("<input class=\"btn btn-default\" type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=DeletePage&log=ipUserCancels'\">");	
			}			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}
