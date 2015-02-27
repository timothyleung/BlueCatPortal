package tw.com.fivehundred.tag.add;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import javax.tools.Tool;

import tw.com.fivehundred.add.been.OneMacAddress;
import tw.com.fivehundred.tool.AddressType;
import tw.com.fivehundred.tool.Tools;

import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;
import com.opensymphony.xwork2.ActionContext;

public class OneMACAddressCheck extends TagSupport {

	private String title[] = { "MAC Address", "IP Address", "Machine Type",
			"Location", "Owner", "Department", "Phone Number", "Input Date",
			"Reference" };
	private String vauleName[] = { "MAC_Address", "IP_Address", "Machine_Type",
			"Location", "Owner", "Department", "Phone_Number", "Input_Date",
			"Reference" };

	@SuppressWarnings("unused")
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		JspWriter out = this.pageContext.getOut();
		Map session = ActionContext.getContext().getSession();
		OneMacAddress oneMacAddress = (OneMacAddress) session.get("one_data");
		String ip_choice = (String) session.get("ip_choice");
		System.out.println("Will get I user ip choice? 1 for ip 2 for network " + session.get("ip_choice"));
		System.out.println("In add:oneMACADDRESSCheck printing oneMacAddress : " + oneMacAddress.toString());
		String select_config = "hkbu";// (String) session.get("select_config");
		// session.remove("one_data");
		session.put("old_data", oneMacAddress);
		session.remove("select_config");
		ProteusAPI_PortType service = (ProteusAPI_PortType) session
				.get("ready_server");
		request.setAttribute("select_config", select_config);
		APIEntity config;
		final String page_title = "Checking Single IP/MAC Address";

		/**
		 * Tim Implementation
		 * 
		 */
		try {
			boolean check = false;
			boolean ip_format_check=true;
			boolean mac_format_check;
			if (ip_choice.equals("1")){
				ip_format_check = Tools.checkIPAddress(oneMacAddress.getIP_Address());
			}
			mac_format_check = Tools.checkMacAddress(oneMacAddress.getMAC_Address());
			out.write("<h1>" + page_title +"</h1><br>");
			out.write("<table class=\"table table-striped table-hover table-bordered\">");			
			// check format of ip and mac before passing in
			if(mac_format_check && ip_format_check){
				config = service.getEntityByName(0, select_config,
						ObjectTypes.Configuration);
				if(ip_choice.equals("1")){
					check = Tools.single_addition_check(out, service, config, AddressType.IP, oneMacAddress, 0);
				} else if( ip_choice.equals("2")){
					check = Tools.single_addition_check(out, service, config, AddressType.IPV4_NETWORK, oneMacAddress, 0);
				}
			} else {
				if(!ip_format_check){
					out.write("<tr class='danger'><th>IP Address</th><td span='100%'>" + oneMacAddress.getIP_Address() + " is in invalid format.</td></tr>");
				}
				
				if(!mac_format_check){
					out.write("<tr class='danger'><th>MAC Address</th> <td span='100%'>" + oneMacAddress.getMAC_Address() + " is in invalid format.</td></tr>");
				}
			}
			out.write("</table>");
			
			if(mac_format_check && ip_format_check){
				if(check){
					out.write("<input class=\"btn btn-warning\" type='submit' name='Overwrite' value='Overwrite' onclick=\"form1.action='/FiveHundredNet/BlueCat/AddPage?choose=check&jump=oneAdd'\">");
				} else {
					out.write("<input class=\"btn btn-default\" type='submit' value='Add' onclick=\"form1.action='/FiveHundredNet/BlueCat/AddPage?choose=check&jump=oneAdd'\">");					// add... 
				}
				out.write("<input class=\"btn btn-default\" type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=AddPage&log=userCancel'\">");
			} else {
				// back button only!
				out.write("<input class=\"btn btn-default\" type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=AddPage&log=checkErro'\">");
			}
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		return super.doStartTag();
	}
}
//
//		// assert(ip_choice.equals("1") || ip_choice.equals("2"));
//		String ip_address = "";
//		String message_mac = "";
//		String message_ip = "";
//		String container_Status = "";
//		boolean mac_repeat = false;
//		// ���d
//		String DuplicatedIP="";//��MAC������,���X������IP
//		String DuplicatedMAC="";//��IP������,���X������MAC
//
//		long id = 0;
//		// check mac address stored in onemacaddress
//			if (oneMacAddress.getMAC_Address().trim().length() > 0) {
//				// ���dMac Address
//				String[] mac_array = oneMacAddress.getMAC_Address().trim()
//						.split("-");
//				if (mac_array.length == 1) {
//					mac_array = oneMacAddress.getMAC_Address().split(":");
//				}
//			if (mac_array.length != 6) {
//				message_mac = "Incorrect MAC Address: Please check MAC address format";
//			} else {
//				if (Tools.checkMacAddress(mac_array)) {
//					message_mac = "Incorrect MAC Address: Please check MAC address format";
//				} else {
//		try {
//			config = service.getEntityByName(0, select_config,
//					ObjectTypes.Configuration);
//			id = config.getId();
//			try {
//				
//				// get config_mac if config mac == null  ( it is not in database) else something is linked (CORRECT!)
//				APIEntity config_mac = service.getMACAddress(id,
//						oneMacAddress.getMAC_Address());
//				if (config_mac != null) {
//					System.out.println("Config mac in one mac address check is not null");
//					APIEntity[] mac_ip_array=service.getLinkedEntities(config_mac.getId(),ObjectTypes.IP4Address,0,100);
//					DuplicatedIP="No Ip Data";
//					if (mac_ip_array.length>0){
//						// hopefully this will return a ip address 
//						DuplicatedIP =Tools.getIPbyADDRESSstring(mac_ip_array[0].getProperties()) ;
//					}
//					message_mac = "Duplicated MAC Address";
//					mac_repeat = true;
//				}
//				System.out.println("Config mac in one mac address check is  null");
//
//			} catch (RemoteException e) {
//				message_mac = "MAC Address Check Failure: Please check input MAC address";
//			}
//		} catch (RemoteException e1) {
//			e1.printStackTrace();
//		}
//				}
//			}
//		} else {
//			message_mac = "Incorrect MAC Address: Please check MAC address format";
//		}
//		// ���dip
//		boolean check_ip = false;
//		// check format of ip address
//		if (oneMacAddress.getIP_Address().trim().length() > 0) {
//			
//			String[] Network = oneMacAddress.getIP_Address().trim().split("/"); // useless split..... why split with / ???
//			for(String s : Network) {
//				System.out.println("onemacaddress  What is in network s : " + s ) ;
//			}
//			
//			// check ip address format
//			String[] ip_marray = Network[0].split("\\.");
//			if (ip_marray.length != 4) {
//				message_ip = "Incorrect IP Address: Please check IP address format";
//			} else {
//				if (Tools.checkIPAddress(ip_marray)) {
//					message_ip = "Incorrect IP Address: Please check IP address format";
//				} else {
//	try {
//		config = service.getEntityByName(0, select_config,
//				ObjectTypes.Configuration);
//		id = config.getId();
//		try {
//			if (Network.length == 1) {
//				String ip_adress = ip_marray[0] + "."
//						+ ip_marray[1] + "." + ip_marray[2];
//				if (oneMacAddress.getIP_Address().equals(
//						ip_adress + ".0")
//						|| oneMacAddress.getIP_Address()
//								.equals(ip_adress + ".0")) {
//					message_ip = "IP reserved by system" ; // reserved by system  all .0 ip
// 				} else {
// 					// think it is valid 
//					APIEntity config_ip = service
//							.getIP4Address(id, oneMacAddress
//									.getIP_Address());
//					if (config_ip != null) {
//						// ip exists in system, get its mac!						
//						DuplicatedMAC=Tools.getMACbyMACADDRESSstring(config_ip.getProperties()); 
//						message_ip = "Duplicated IP Address";
//					}
//				}
//			} else {
//				// check_ip = true means it is using system assign ip
//				check_ip = true;
//			}
//		} catch (RemoteException e2) {
//			e2.printStackTrace();
//			message_ip = "IP Address Check Failure: Please check input IP address ";
//		}
//	} catch (RemoteException e1) {
//		e1.printStackTrace();
//	}
//				}
//			}
//		}
//		
//		// no ip given? , so.............. using auto assign ip system, so empty string........ why you have to 
//		// do this......
//		if (oneMacAddress.getIP_Address().trim().length() == 0 || check_ip) { 
//			System.out.println("one mac addresscheck no ip given! getIP_Address : " + oneMacAddress.getIP_Address());
//			//������ assignNextAvailableIP4Address() 
//			String[] Network = oneMacAddress.getIP_Address_NetWork().trim().split("/");
//			if (Network[1].indexOf("[")>0)
//			{
//				//Network[0] is a.b.c.d
//				// if format is something like 24 [ 1st subnet ] 
//				Network[1]=Network[1].substring(0, Network[1].indexOf("[")).trim(); 
//				// return 24
//			}
//			// check ip address 
//			String[] ip_marray = Network[0].split("\\.");
//			if (ip_marray.length != 4) {
//				message_ip = "Incorrect IP Address: Please check IP address format";
//			} else {
//				if (Tools.checkIPAddress(ip_marray)) {
//					message_ip = "Incorrect IP Address: Please check IP address format";
//				} else {
//					try {
//						config = service.getEntityByName(0, select_config,
//								ObjectTypes.Configuration);
//						id = config.getId();
//						try {
//							
//							
//							String ip_adress0 = ip_marray[0] + "."
//									+ ip_marray[1] + "." + ip_marray[2];
//							if (Network[0].equals(ip_adress0 + ".0")) {
//								// ������������ipNetwork
//								APIEntity[] fields = service
//										.searchByObjectTypes(
//												Tools.getIPNETWORKwithoutName(oneMacAddress.getIP_Address_NetWork()),
//												ObjectTypes.IP4Network, 0, 10);
//								for(APIEntity a : fields){
//									System.out.println("See what did I search using 101.78.134.0/24 : " + a.getName() + " " + a.getProperties() );
//								}
//								if (fields.length != 0) { // maybe I got something from the search 
//									
//									String ip4Network = Tools.getIPNETWORKbyCIDRstring(fields[0].getProperties());
//											 
//									String[] ip_number = ip4Network.split("/"); // guess this will return 101.78.134.0
//									ip_address = ip_number[0];
//									
//									String network_submast = ""; //ip_number[1] is 24 (subnet mask)
//
//									network_submast=ip_number[1].trim();
//									
//									ip4Network = ip_address;
//									if (Integer.parseInt(Network[1]) == Integer
//											.parseInt(network_submast)) {
//										// fields[0] is an entity of a subnet... 
//										// didnt mention about what will happen if no available IP4 address , 
//										String can_use_ip=service.getNextAvailableIP4Address(fields[0].getId());
//																				
//										oneMacAddress.setIP_Address(can_use_ip);
//										message_ip = "System Automatically Assigns IP";
//										
//									} else {
//										message_ip = "IP Address Check Failure:Subnet Mask Error";
//									}
//								} else {
//									// Maybe no avaliable ip4 network, 
//									message_ip = "IP Address Check Failure:System Automatically Assigns IP Error";
//								}
//							} else {
//								message_ip = "IP Address Check Failure:IP Address Error";
//							}
//						} catch (RemoteException e2) {
//							e2.printStackTrace();
//							message_ip = "IP Address Check Failure: Please check input IP address ";
//						}
//					} catch (RemoteException e1) {
//						e1.printStackTrace();
//					}
//				}
//			}
//		}
//
//		
//		// WE CAN OUT PUT EVERYTHING NOW!
//		try {
//
//			
//			out.write("<table cellspacing='0' width='200%' border='1' borderColor='#DEE6EE'>");
//			for (int i = 0; i < title.length; i++) {
//				if (i == 2) {
//					out.write("</table>");
//					out.write("<div>Below data used user defined field (UDF)</div>");
//					out.write("<table cellspacing='0' width='200%' border='1' borderColor='#DEE6EE'>");
//				}
//				out.write("<tr><th align=left>" + title[i] + "</th>");
//				out.write("<td>");
//				if (i == 0) {
//					out.write("<input type='text' name='oneMacAddress."
//							+ vauleName[i]
//							+ "' readonly='readonly' value='"
//							+ oneMacAddress.getMAC_Address()
//							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'"
//							+ "/>");
//
//					if (message_mac.length() != 0) {
//						out.write("<br><FONT COLOR='#FF0000'>(" + message_mac
//								+ ")</FONT>");
//					}
//				}
//				if (i == 1) {
//					out.write("<input type='text' name='oneMacAddress."
//							+ vauleName[i]
//							+ "' readonly='readonly' value='"
//							+ oneMacAddress.getIP_Address()
//							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'"
//							+ "/>");
//
//					if (message_ip.length() != 0) {
//						out.write("<br><FONT COLOR='#FF0000'>(" + message_ip
//								+ ")</FONT>");
//					}
//					if (DuplicatedIP.length() != 0) {
//					out.write("</td></tr>");
//					out.write("<tr><th align=left>" + title[i] + " (In Used)</th>");
//					out.write("<td>");
//					out.write("<FONT COLOR='#FF0000'>" + DuplicatedIP + "</FONT><input type='hidden' id='DuplicatedIP' name='DuplicatedIP' value='" + DuplicatedIP + "' />");
//					}
//					if (DuplicatedMAC.length() != 0) {
//						out.write("</td></tr>");
//						out.write("<tr><th align=left>Mac Address (In Used)</th>");
//						out.write("<td>");
//						out.write("<FONT COLOR='#FF0000'>" + DuplicatedMAC + "</FONT><input type='hidden' id='DuplicatedMAC' name='DuplicatedMAC' value='" + DuplicatedMAC + "' />");
//					}
//					
//					
//				}
//				if (i == 2) {
//					out.write("<input type='text' name='oneMacAddress."
//							+ vauleName[i]
//							+ "' readonly='readonly' value='"
//							+ oneMacAddress.getMachine_Type()
//							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
//				}
//				if (i == 3) {
//					out.write("<input type='text' name='oneMacAddress."
//							+ vauleName[i]
//							+ "' readonly='readonly' value='"
//							+ oneMacAddress.getLocation()
//							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
//				}
//				if (i == 4) {
//					out.write("<input type='text' name='oneMacAddress."
//							+ vauleName[i]
//							+ "' readonly='readonly' value='"
//							+ oneMacAddress.getOwner()
//							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
//				}
//				if (i == 5) {
//					out.write("<input type='text' name='oneMacAddress."
//							+ vauleName[i]
//							+ "' readonly='readonly' value='"
//							+ oneMacAddress.getDepartment()
//							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
//				}
//				if (i == 6) {
//					out.write("<input type='text' name='oneMacAddress."
//							+ vauleName[i]
//							+ "' readonly='readonly' value='"
//							+ oneMacAddress.getPhone_Number()
//							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
//				}
//				if (i == 7) {
//					out.write("<input type='text' name='oneMacAddress."
//							+ vauleName[i]
//							+ "' readonly='readonly' value='"
//							+ oneMacAddress.getInput_Date()
//							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
//				}
//				if (i == 8) {
//					out.write("<input type='text' name='oneMacAddress."
//							+ vauleName[i]
//							+ "' readonly='readonly' value='"
//							+ oneMacAddress.getReference()
//							+ "'style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;'/>");
//				}
//				out.write("</td></tr>");
//			}
//			out.write("</table>");
//			// mac �i�H���� ���O ip����
//			if (message_ip.length() == 0 && message_mac.length() == 0) {
//				out.write("<input type='submit' value='Add' onclick=\"form1.action='/FiveHundredNet/BlueCat/AddPage?choose=check&jump=oneAdd'\">");
//				out.write("<input type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=AddPage&log=userCancel'\">");
//			} else if (message_ip.equals("System Automatically Assigns IP") && message_mac.length() == 0) {
//				out.write("<input type='submit' value='Add' onclick=\"form1.action='/FiveHundredNet/BlueCat/AddPage?choose=check&jump=oneAdd'\">");
//				out.write("<input type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=AddPage&log=userCancel'\">");
//			} else if (mac_repeat && message_ip.length() == 0) {
//				out.write("<input type='submit' value='Overwrite' onclick=\"form1.action='/FiveHundredNet/BlueCat/AddPage?choose=check&jump=oneAdd'\">");
//				out.write("<input type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=AddPage&log=userCancel'\">");
//			} else {
//				out.write("<input type='submit' value='Overwrite' onclick=\"form1.action='/FiveHundredNet/BlueCat/AddPage?choose=check&jump=oneAdd&overwrite=1'\">");
//				out.write("<input type='submit' value='Back' onclick=\"form1.action='/FiveHundredNet/BlueCat/BackPage?choose=AddPage&log=checkErro'\">");
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

