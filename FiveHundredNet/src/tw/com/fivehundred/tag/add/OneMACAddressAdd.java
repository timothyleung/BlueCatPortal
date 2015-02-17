package tw.com.fivehundred.tag.add;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
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

public class OneMACAddressAdd extends TagSupport {
	private String serverid1 = "BDDS1";
	private String serverid2 = "BDDS2";

	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {

		// CHECK完, 按ADD 或 OVERWRITE會進來
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		JspWriter out = this.pageContext.getOut();
		Map session = ActionContext.getContext().getSession();
		OneMacAddress oneMacAddress = (OneMacAddress) session.get("one_data");
		String select_config = "hkbu";// (String) session.get("select_config");
		session.remove("one_data");
		session.remove("select_config");
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);

		try {
			APIEntity config = service.getEntityByName(0, select_config,
					ObjectTypes.Configuration);
			long id = config.getId();
			// 刪除IP
			/*APIEntity config7 = service.getIP4Address(id,
					oneMacAddress.getIP_Address());
			if (config7 != null) {
				service.delete(config7.getId());
			}*/

			// 若overwrite就先刪所有mac關聯的ip
			String DuplicatedIP = "";
			String overwrite = (String) session.get("overwrite");
			if (overwrite==null){
				overwrite="";
			}
			if (overwrite.equals("1")) {

				// 刪除重覆的 ip
				try {
					APIEntity config_mac = service.getMACAddress(id, oneMacAddress.getMAC_Address());
					// harry 讀取與mac相關的ip
					APIEntity[] mac_ip_array = service.getLinkedEntities( config_mac.getId(), ObjectTypes.IP4Address, 0, 100);

					// 這邊要把所有mac_ip_array的值都印出來
					if (mac_ip_array.length > 0) {
						for (int x = 0; x < mac_ip_array.length; x++) {
							DuplicatedIP = Tools.getIPbyADDRESSstring(mac_ip_array[x].getProperties());
							APIEntity configtmp = service.getIP4Address(id,DuplicatedIP);
							if (configtmp != null) {
								service.delete(configtmp.getId());
							}
						}
					}
					//刪mac
					service.delete(config_mac.getId()); 
					
				} catch (Exception ex) {

				}

				// 刪除重覆的 mac
				try { 
					// 若單一ip, 就撿查該ip資訊
					APIEntity config_ip = service.getIP4Address(id,oneMacAddress.getIP_Address());
					if (config_ip != null) { 
						service.delete(config_ip.getId()); 
					}
					APIEntity config_mac = service.getMACAddress(id, oneMacAddress.getMAC_Address());
					if (config_mac != null) {
						service.delete(config_mac.getId()); 
					}
 
				} catch (Exception ex) {

				}
				
			}

			// 將mac address加入
			// EntityProperties props0 = new EntityProperties();
			// props0.addProperty(ObjectProperties.name, "hans");
			String user_defined = "";
			if (oneMacAddress.getMachine_Type() != null) {
				user_defined = user_defined + "ip_machine_type="
						+ oneMacAddress.getMachine_Type().trim() + "|";
			} else {
				user_defined = user_defined + "ip_machine_type=" + "" + "|";
			}
			if (oneMacAddress.getLocation() != null) {
				user_defined = user_defined + "ip_location="
						+ oneMacAddress.getLocation().trim() + "|";
			} else {
				user_defined = user_defined + "ip_location=" + "" + "|";
			}
			if (oneMacAddress.getOwner() != null) {
				user_defined = user_defined + "ip_device_ower="
						+ oneMacAddress.getOwner().trim() + "|";
			} else {
				user_defined = user_defined + "ip_device_ower=" + "" + "|";
			}
			if (oneMacAddress.getDepartment() != null) {
				user_defined = user_defined + "ip_department="
						+ oneMacAddress.getDepartment().trim() + "|";
			} else {
				user_defined = user_defined + "ip_department=" + "" + "|";
			}
			if (oneMacAddress.getInput_Date() != null) {
				user_defined = user_defined + "ip_input_date="
						+ oneMacAddress.getInput_Date().trim() + "|";
			} else {
				user_defined = user_defined + "ip_input_date=" + "" + "|";
			}
			if (oneMacAddress.getPhone_Number() != null) {
				user_defined = user_defined + "ip_phone_number="
						+ oneMacAddress.getPhone_Number().trim() + "|";
			} else {
				user_defined = user_defined + "ip_phone_number=" + "" + "|";
			}
			if (oneMacAddress.getReference() != null) {
				user_defined = user_defined + "ip_reference="
						+ oneMacAddress.getReference().trim() + "|";
			} else {
				user_defined = user_defined + "ip_reference=" + "" + "|";
			}

			try {
				long mac_long = service.addMACAddress(id, oneMacAddress.getMAC_Address(), "|");
				// 加入ip
				long ip_ans = service.assignIP4Address(id, oneMacAddress.getIP_Address(), oneMacAddress.getMAC_Address(), "|",
						"MAKE_DHCP_RESERVED", user_defined);
				session.put("select_config", select_config);
				/*session.put("erroMessage", oneMacAddress.getMAC_Address()
						+ " has joined");*/
				// harry
				// APIEntity service_data = service.getEntityByName(id,
				// oneMacAddress.getServers(), ObjectTypes.Server);
				// service.deployServer(service_data.getId());
				APIEntity service_data = service.getEntityByName(id, serverid1, ObjectTypes.Server);
				service.deployServerServices(service_data.getId(), "services=DHCP");
				APIEntity service_data2 = service.getEntityByName(id, serverid2, ObjectTypes.Server);
				service.deployServerServices(service_data2.getId(), "services=DHCP");

				// 重覆所以出錯
				OneMacAddress oneMacAddress1 = new OneMacAddress();
				session.put("one_data", oneMacAddress1);
				response.sendRedirect("/FiveHundredNet/BlueCat/AddPage?choose=OneMACAddress");
				
				String userName = (String) session.get("userName");
				String log_Content = userName + "," + "MAC Address" + ","
						+ oneMacAddress.getMAC_Address() + "," + "Add" + ","
						+ "Success" + "," + Tools.getTime() + ","
						+ "MAC Address Created";
				LogMian.writeLog(log_Content, userName);
				log_Content = userName + "," + "IP Address" + ","
						+ oneMacAddress.getIP_Address() + "," + "Add" + ","
						+ "Success" + "," + Tools.getTime() + ","
						+ "Add MAC Address";
				LogMian.writeLog(log_Content, userName);
				
			} catch (Exception e) {
				
				e.printStackTrace();
				if (e.toString().equals("Duplicate of another item")) {
					// 加入ip
					long ip_ans = service.assignIP4Address(id,
							oneMacAddress.getIP_Address(),
							oneMacAddress.getMAC_Address(), "|",
							"MAKE_DHCP_RESERVED", user_defined);
					session.put("select_config", select_config);
					session.put("erroMessage", oneMacAddress.getMAC_Address()
							+ " has joined");
					// harry
					// APIEntity service_data=service.getEntityByName(id,
					// oneMacAddress.getServers(), ObjectTypes.Server);
					// service.deployServer(service_data.getId());
					APIEntity service_data = service.getEntityByName(id,
							serverid1, ObjectTypes.Server);
					service.deployServerServices(service_data.getId(),
							"services=DHCP");
					APIEntity service_data2 = service.getEntityByName(id,
							serverid2, ObjectTypes.Server);
					service.deployServerServices(service_data2.getId(),
							"services=DHCP");

					OneMacAddress oneMacAddress1 = new OneMacAddress(); 
					session.put("one_data", oneMacAddress1);
					response.sendRedirect("/FiveHundredNet/BlueCat/AddPage?choose=OneMACAddress");
					String userName = (String) session.get("userName");
					String log_Content = userName + "," + "MAC Address" + ","
							+ oneMacAddress.getMAC_Address() + "," + "Add"
							+ "," + "Success" + "," + Tools.getTime() + ","
							+ "MAC Address Created";
					LogMian.writeLog(log_Content, userName);
					log_Content = userName + "," + "IP Address" + ","
							+ oneMacAddress.getIP_Address() + "," + "Add" + ","
							+ "Success" + "," + Tools.getTime() + ","
							+ "Add MAC Address";
					LogMian.writeLog(log_Content, userName);
					
				} else {
					
					String userName = (String) session.get("userName");
					String log_Content = userName + "," + "MAC Address" + ","
							+ oneMacAddress.getMAC_Address() + "," + "Add"
							+ "," + "Erro:" + e + "," + Tools.getTime() + ","
							+ "MAC Address Add";
					LogMian.writeLog(log_Content, userName);
				}
			}
			
		} catch (Exception e1) {
			
			e1.printStackTrace();
			String userName = (String) session.get("userName");
			String log_Content = userName + "," + "" + "," + "" + "," + ""
					+ "," + "Single MAC address import system error" + ","
					+ Tools.getTime() + "," + "";
			LogMian.writeLog(log_Content, userName);
			
			/*
			try {
				out.write("<FONT COLOR='#FF0000'>"
						+ "Single MAC address import system error" + "</FONT>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}

		return super.doStartTag();
	}
}
