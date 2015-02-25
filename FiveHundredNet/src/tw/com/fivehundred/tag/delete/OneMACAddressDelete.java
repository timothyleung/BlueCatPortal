package tw.com.fivehundred.tag.delete;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;

import tw.com.fivehundred.add.been.OneMacAddress;
import tw.com.fivehundred.log.LogMian;
import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.EntityProperties;
import com.bluecatnetworks.proteus.api.client.java.constants.ObjectProperties;
import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;

public class OneMACAddressDelete extends TagSupport {
	private String title[] ={ "MAC Address"};
	private String vauleName[] = {"MAC_Address"};
	private String serverid1="BDDS1";
	private String serverid2="BDDS2";
	
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		JspWriter out = this.pageContext.getOut();
		Map session = ActionContext.getContext().getSession();
		System.out.println(session.toString());
		String select_config = (String) session.get("select_config");
		String MAC_Address = (String) session.get("MAC_Address");
		System.out.println("MAC_ADDRESS in one mac addr deletion is " + MAC_Address);
		String select_servers = (String) session.get("select_servers");
		session.remove("select_servers");
		session.remove("MAC_Address");
		session.remove("select_config");
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);
		
		try {
			APIEntity config = service.getEntityByName(0, select_config,
					ObjectTypes.Configuration);
			long id = config.getId();
			
			// MAC ADdress is null : ( !
			APIEntity config7 = service.getMACAddress(id, MAC_Address);
			
			//���X����ip
			APIEntity[] mac_ip_array=service.getLinkedEntities(config7.getId(),ObjectTypes.IP4Address,0,100);
			String IP_addr=""; 
			if (mac_ip_array.length>0){
				for (int x = 0; x < mac_ip_array.length; x++) {
					IP_addr = Tools
							.getIPbyADDRESSstring(mac_ip_array[x]
									.getProperties());
					APIEntity configtmp = service.getIP4Address(id,
							IP_addr);
					if (configtmp != null) {
						service.delete(configtmp.getId());
					}
				} 
			} 
			
			service.delete(config7.getId());

			config7 = service.getMACAddress(id, MAC_Address);
			if (config7 != null) {
				//�q���G				
				out.write("<form name='form1' method='post'	action=''>");
				out.write("<table cellspacing='0' width='200%' border='1' borderColor='#DEE6EE'>");
				//harry
				//out.write("<tr><td>Bluecat configuration:"+select_config+"</td><td></td></tr>");
				out.write("<tr><td>Bluecat DHCP Server: BDDS1 & BDDS2</td><td></td></tr>");
				for (int i = 0; i < title.length; i++) {
					out.write("<tr><td align=left>" + title[i] + "</td><td>");
					out.write("<input type='text' name='MAC_Address' readonly='readonly' value='"
							+ MAC_Address
							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");

					out.write("</td></tr>");
				}
				out.write("</table>");			
					out.write("<FONT COLOR='#FF0000'>(" +"This IP Address is being used can not delete the MAC Address"+ ")</FONT>");
					out.write("<br><input type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/DeletePage?choose=OneMACAddress'\">");
					out.write("</form>");					
					String userName=(String) session.get("userName");
					String log_Content=userName+","+"MAC Address"+","+MAC_Address+","+"Delete"+","+"Fail"+","+Tools.getTime()+","+"MAC Address Delete";
					LogMian.writeLog(log_Content, userName);
			} else {			
				//harry
//				APIEntity service_data = service.getEntityByName(id,
//						select_servers, ObjectTypes.Server);
//				service.deployServer(service_data.getId());
				APIEntity service_data = service.getEntityByName(id,
						serverid1, ObjectTypes.Server); 
				service.deployServerServices(service_data.getId(),"services=DHCP");
				APIEntity service_data2 = service.getEntityByName(id,
						serverid2, ObjectTypes.Server); 
				service.deployServerServices(service_data2.getId(),"services=DHCP");
				
				session.put("erroMessage", MAC_Address+" Has Been Deleted");
				response.sendRedirect("/FiveHundredNet/BlueCat/DeletePage?choose=OneMACAddress");
				String userName=(String) session.get("userName");
				String log_Content=userName+","+"MAC Address"+","+MAC_Address+","+"Delete"+","+"Success"+","+Tools.getTime()+","+"MAC Address Delete";
				LogMian.writeLog(log_Content, userName);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			String userName=(String) session.get("userName");
			String log_Content=userName+","+""+","+""+","+""+","+"Bulk MAC address deletion system error"+","+Tools.getTime()+","+"";
			LogMian.writeLog(log_Content, userName);
			try {
				out.write("<FONT COLOR='#FF0000'>" + "Bulk MAC address deletion system error"
						+ "</FONT>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return super.doStartTag();
	}
}
