package tw.com.fivehundred.tag.delete;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import tw.com.fivehundred.add.been.OneMacAddress;
import tw.com.fivehundred.log.LogMian;
import tw.com.fivehundred.tool.AddressType;
import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * @author leungtimothy
 */

public class OneMACAddressCheck extends TagSupport {

	private String title[] = { "MAC Address", "Machine Type", "Location",
			"Owner", "Department", "Phone Number", "Input Date", "Reference" };
	private String vauleName[] = { "MAC_Address", "Machine Type", "Location",
			"Owner", "Department", "Phone Number", "Input Date", "Reference" };
	private String vauleName1[] = { "ip_machine_type=", "ip_location=",
			"ip_device_ower=", "ip_department=", "ip_phone_number=",
			"ip_input_date=", "ip_reference=" };

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		Map session = ActionContext.getContext().getSession();
		String select_config = (String) session.get("select_config");
		String MAC_Address = (String) session.get("MAC_Address");
		session.remove("select_config");
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);
		request.setAttribute("MAC_Address", MAC_Address);
		JspWriter out = this.pageContext.getOut();
		String page_title = "Single MAC check";
		try {
			APIEntity config;
			long id = 0;
			boolean check = false;
			MAC_Address = MAC_Address.toUpperCase();
			boolean is_valid_mac_address = Tools.checkMacAddress(MAC_Address);
			System.out.println("Tim regex valid mac check " + MAC_Address + " is " + is_valid_mac_address);

			APIEntity config_mac = null;
			out.write("<h1>" + page_title +"</h1><br>");
			out.write("<table class=\"table table-striped table-hover table-bordered\">");
			out.write("<tr><td>MAC Address</td><td>IP Address</td></tr>");
			if (is_valid_mac_address){
				try {
					config = service.getEntityByName(0, select_config,
							ObjectTypes.Configuration);
					id = config.getId();
					config_mac = service.getMACAddress(id, MAC_Address);

					int single_delete_indicator = 0;
					if(config_mac == null){
						check = Tools.single_deletion_check(out, config_mac, AddressType.MAC, MAC_Address, single_delete_indicator);
					} else {
						APIEntity[] config_mac_array=service.getLinkedEntities(config_mac.getId(),ObjectTypes.IP4Address,0,100);
						check = Tools.single_deletion_check(out, config_mac_array[0], AddressType.MAC, MAC_Address, single_delete_indicator);
					}

				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
				else {
					out.write("<tr><td colspan=\"100%\">" + MAC_Address + " have an invalid format :( </td></tr>");
				}
				out.write("</table>");
				if (!check){
					out.write("<br><input class=\"btn btn-default\"  type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=DeletePage&log=macCheckErro'\">");
				} else {
					out.write("<br><input class=\"btn btn-default\"  type='submit' value='Delete' onclick=\"form1.action='/FiveHundredNet/BlueCat/DeletePage?choose=check&jump=oneMACDelete'\">");
					out.write("<input class=\"btn btn-default\"  type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=DeletePage&log=macUserCancels'\">");
				}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doStartTag();
	}
}
//				
//				
//		if (is_valid_mac_address){
//		
//				
//				/**
//				 * Seperation
//				 */
//				System.out.println("Correct MAC ADDDDD");
//				try {
//					config = service.getEntityByName(0, select_config, ObjectTypes.Configuration);
//					id = config.getId();
//					try {
//						APIEntity config_MAC = service.getMACAddress(id, MAC_Address);
//						if (config_MAC != null) {
//							APIEntity[] mac_ip_array=service.getLinkedEntities(config_MAC.getId(),ObjectTypes.IP4Address,0,100);
//							System.out.println("MAC ip array : " + mac_ip_array[0].getName());
//							message_IP="No Ip Data";
//							//�o���n������mac_ip_array�������L�X��
//							if (mac_ip_array.length>0){
//								message_IP =Tools.getIPbyADDRESSstring(mac_ip_array[0].getProperties()) ;
//							} 
//							 
//							check = true;
//							message_MAC = "Record found";
//						} else {
//							message_MAC = "MAC address record not found";
//						}
//					} catch (RemoteException e2) {
//						message_MAC = "MAC Address Check Failure: Please check input MAC address";
//					}
//				} catch (RemoteException e1) {
//					e1.printStackTrace();
//				}
//			} else {
//				message_MAC = "Incorrect MAC Address: Please check MAC address format";
//			}
//			
//			String[] data = { "", "", "", "", "", "", "", "" };
//			/**
//			 * Bug here 
//			 * Kind of  fixed, but need further improvement, some action inside
//			 * the block is for invalid mac address as well :)
//			 */
//			if(is_valid_mac_address){
//			// ------------------------------
//			// ����Configuration��������mac address
//			APIEntity config_MAC1 = null;
//			if (MAC_Address.length() != 0) {
//				System.out.println("ONE MAC ADDR DEL : " + MAC_Address);
//				config_MAC1 = service.getMACAddress(id, MAC_Address);
//			}
//			if (config_MAC1 != null) {
//				String container_mac = "", container_ip_mac = "", container_other;
//				boolean cant_find_mac = true;
//				container_mac = config_MAC1.getProperties().substring(
//						config_MAC1.getProperties().indexOf("address=") + 8,
//						config_MAC1.getProperties()
//								.indexOf(
//										"|",
//										config_MAC1.getProperties().indexOf(
//												"address=")));
//				APIEntity[] fields_ip = service.getLinkedEntities(
//						config_MAC1.getId(), ObjectTypes.IP4Address, 0, 10);
//				data[0] = container_mac;
//				if (fields_ip.length != 0) {
//					for (int k = 0; k < vauleName1.length; k++) {
//						if (fields_ip[0].getProperties().indexOf(vauleName1[k]) != -1) {
//							container_other = fields_ip[0]
//									.getProperties()
//									.substring(
//											fields_ip[0].getProperties()
//													.indexOf(vauleName1[k])
//													+ vauleName1[k].length(),
//											fields_ip[0]
//													.getProperties()
//													.indexOf(
//															"|",
//															fields_ip[0]
//																	.getProperties()
//																	.indexOf(
//																			vauleName1[k])));
//							data[k + 1] = container_other;
//						} else {
//							data[k + 1] = "";
//						}
//					}
//				}
//			}
//			}
//			out.write("<table class=\"table table-striped table-hover table-bordered\">");
//			for (int i = 0; i < title.length; i++) {
//				out.write("<tr><td align=left>" + title[i] + "</td><td>");
//				out.write("<input type='text' name='"
//						+ vauleName[i]
//						+ "' readonly='readonly' value='"
//						+ data[i]
//						+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
//				if (i==0){
//					out.write(" ( IP Address : "+message_IP + " )");
//				}
//				out.write("</td></tr>");
//			}
//			out.write("</table>");
//			if (!is_valid_mac_address) {
//				out.write("<FONT COLOR='#FF0000'>(" + message_MAC + ")</FONT>");
//				out.write("<br><input class=\"btn btn-default\"  type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=DeletePage&log=macCheckErro'\">");
//			} else {
//				//out.write("<FONT COLOR='#FF0000'>(" + message_MAC + ")</FONT>");
//				out.write("<br><input class=\"btn btn-default\"  type='submit' value='Delete' onclick=\"form1.action='/FiveHundredNet/BlueCat/DeletePage?choose=check&jump=oneMACDelete'\">");
//				out.write("<input class=\"btn btn-default\"  type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=DeletePage&log=macUserCancels'\">");
//			}

