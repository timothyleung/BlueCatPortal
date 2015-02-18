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

public class OneIPAddressDelete extends TagSupport {
	private String serverid1="BDDS1";
	private String serverid2="BDDS2";
	
	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		JspWriter out = this.pageContext.getOut();
		Map session = ActionContext.getContext().getSession();
		String select_config = (String) session.get("select_config");
		String IP_Address = (String) session.get("IP_Address");
		String select_servers = (String) session.get("select_servers");
		System.out.println("In one IP addres Delete java");
		System.out.println(select_config + " " + IP_Address + " " + select_servers);
		System.out.println(session);
		session.remove("select_servers");
		session.remove("IP_Address");
		session.remove("select_config");
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server"); // com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPIBindingStub@63af701e
		request.setAttribute("select_config", select_config); //hkbu

		try {
			APIEntity config = service.getEntityByName(0, select_config,
					ObjectTypes.Configuration); // getting a single entity to modify it.. (object from database have hkbu as their name field and return type Configuration)
			long id = config.getId(); 
			System.out.println("Printing config :" + config);
			 System.out.println("Cofig ID is : " + id);
			// �R��IP , �����X����mac
			
			APIEntity config_ip = service.getIP4Address(id, IP_Address.trim()); // using this id we can get IP4 address?
			
			System.out.println("Printing config_ip & ID:" + config_ip + " & " + config_ip.getId());
			String mac_addr= "";
			if (config_ip!=null){
				mac_addr=Tools.getMACbyMACADDRESSstring(config_ip.getProperties());  // getting the mac address
				service.delete(config_ip.getId());
			}
			
			// Assume we have mac address here, otherwise it will crash and throw exception. 
			APIEntity config_MAC = service.getMACAddress(id, mac_addr.trim()); // retrieve the APIEntity for the MAC address using mac address string
			if (config_ip!=null){
				service.delete(config_MAC.getId()); // api call to remove the MAC service!
			}
			
			session.put("erroMessage", IP_Address+" Has Been Deleted");

			APIEntity service_data = service.getEntityByName(id,
					serverid1, ObjectTypes.Server); //BDDS1
			//service.deployServerServices(service_data.getId(),"services=DHCP"); // redeploy? 
			APIEntity service_data2 = service.getEntityByName(id,
					serverid2, ObjectTypes.Server); //BDDS2
			//service.deployServerServices(service_data2.getId(),"services=DHCP");
			
			response.sendRedirect("/FiveHundredNet/BlueCat/DeletePage?choose=OneIPAddress");
			String userName=(String) session.get("userName");
			String log_Content=userName+","+"IP Address"+","+IP_Address+","+"Delete"+","+"Success"+","+Tools.getTime()+","+"IP Address Delete";
			LogMian.writeLog(log_Content, userName);
		} catch (Exception e1) {
			String userName=(String) session.get("userName");
			String log_Content=userName+","+""+","+""+","+""+","+"Single IP address deletion system error"+","+Tools.getTime()+","+"";
			LogMian.writeLog(log_Content, userName);
			try {
				out.write("<FONT COLOR='#FF0000'>" + "Single IP address deletion system error" + "</FONT>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return super.doStartTag();
	}
}
