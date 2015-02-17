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
		session.remove("select_servers");
		session.remove("IP_Address");
		session.remove("select_config");
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);

		try {
			APIEntity config = service.getEntityByName(0, select_config,
					ObjectTypes.Configuration);
			long id = config.getId();
			
			 
			// 刪除IP , 先找出相關mac
			APIEntity config_ip = service.getIP4Address(id, IP_Address.trim());
			String mac_addr= "";
			if (config_ip!=null){
			mac_addr=Tools.getMACbyMACADDRESSstring(config_ip.getProperties());
			service.delete(config_ip.getId());
			}
			
			//刪除mac
			APIEntity config_MAC = service.getMACAddress(id, mac_addr.trim());
			if (config_ip!=null){
			service.delete(config_MAC.getId());
			}
			
			session.put("erroMessage", IP_Address+" Has Been Deleted");
			//harry
//			APIEntity service_data = service.getEntityByName(id,
//					select_servers, ObjectTypes.Server);
//			service.deployServer(service_data.getId());
			APIEntity service_data = service.getEntityByName(id,
					serverid1, ObjectTypes.Server); 
			service.deployServerServices(service_data.getId(),"services=DHCP");
			APIEntity service_data2 = service.getEntityByName(id,
					serverid2, ObjectTypes.Server); 
			service.deployServerServices(service_data2.getId(),"services=DHCP");
			
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
