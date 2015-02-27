package tw.com.fivehundred.tool;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.ArrayUtils;

import tw.com.fivehundred.add.been.OneMacAddress;
import tw.com.fivehundred.log.LogMian;

import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;

public class Tools {
	
	public static void addCookie(HttpServletResponse response,String name,String value){
	    int maxAge=65536;//3600;
		Cookie cookie = new Cookie(name,value);
	    cookie.setPath("/");
	    if(maxAge>0)  cookie.setMaxAge(maxAge);
	    response.addCookie(cookie);
	}
	
	public static String getCookieByName(HttpServletRequest request,String name){
	    Map<String,Cookie> cookieMap = ReadCookieMap(request);
	    if(cookieMap.containsKey(name)){
	        Cookie cookie = (Cookie)cookieMap.get(name);
	        String rtnvalue = cookie.getValue();//"12";//cookieMap.get(name).toString();
	        return rtnvalue;
	    }else{
	        return "";
	    }   
	}
	
	private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){  
	    Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	    Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    return cookieMap;
	}
	
	public static APIEntity[] getAllMacAddress(ProteusAPI_PortType service,
			long id) {
		
		//�o���n��..
		
		APIEntity[] ans = null;
		ArrayList<APIEntity> aPIEntity_list = new ArrayList<APIEntity>();
		try {
			
			APIEntity[] fields = null;
			int count_number = 0;
			do {
				fields = service.getEntities(id, ObjectTypes.MACAddress,
						1000 * count_number, 999);
				count_number++;
				for (int i = 0; i < fields.length; i++) {
					System.out.println("Adding MACaddress into entity Tools.java, mac : " + fields[i].toString() + " " + fields[i].getProperties() + " " + fields[i].getName());
					aPIEntity_list.add(fields[i]);
				}
			} while (fields.length == 999);
			ans = new APIEntity[aPIEntity_list.size()];
			ans = aPIEntity_list.toArray(ans);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ans;
	}
	public static APIEntity[] getMacAddress(ProteusAPI_PortType service,
			long id,String macAddress) {
		APIEntity[] ans = null;
		ArrayList<APIEntity> aPIEntity_list = new ArrayList<APIEntity>();
		try {
			APIEntity[] fields = null;
			int count_number = 0;
			do {
				fields =  service.searchByObjectTypes(macAddress,
						ObjectTypes.MACAddress, 1000 * count_number,
						999);
				count_number++;
				for (int i = 0; i < fields.length; i++) {
					aPIEntity_list.add(fields[i]);
				}
			} while (fields.length == 999);
			ans = new APIEntity[aPIEntity_list.size()];
			ans = aPIEntity_list.toArray(ans);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ans;
	}
	//�C
	public static APIEntity[] getAllIPAddress(ProteusAPI_PortType service,
			long id) {
		APIEntity[] ans = null;
		ArrayList<APIEntity> aPIEntity_list = new ArrayList<APIEntity>();
		// ������ip
		// ������������BLOCK
		APIEntity[] fields;
		try {
			fields = service.getEntities(id, ObjectTypes.IP4Block, 0, 9999);
			APIEntity[] fields1 = null;
			String container_cidr = "";
			for (int j = 0; j < fields.length; j++) {
				if (fields[j].getProperties().indexOf("CIDR=") != -1) {
					
					container_cidr = getIPNETWORKbyCIDRstring( fields[j].getProperties()) ;
				}
				// ������mac
				int count_number = 0;
				do {
					fields1 = service.searchByObjectTypes(
							container_cidr.split("\\.")[0],
							ObjectTypes.IP4Address, 1000 * count_number, 999);
					count_number++;
					for (int i = 0; i < fields1.length; i++) {
						aPIEntity_list.add(fields1[i]);
					}
				} while (fields1.length == 999);
				ans = new APIEntity[aPIEntity_list.size()];
				ans = aPIEntity_list.toArray(ans);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ans;
	}
	public static APIEntity[] getAllServers(ProteusAPI_PortType service,long id){
		APIEntity[] ans = null;
		try {
			ans =  service.getEntities(id,ObjectTypes.Server,0,999);
			for (APIEntity s : ans){
				System.out.println("Printing server in getALLServers : " + s.getName() + " "  + s.getProperties() + " " + s.getType() + " " );
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public static APIEntity[] getAllIPAddressNetwork(ProteusAPI_PortType service,
			long id) {
		APIEntity[] ans = null;
		ArrayList<APIEntity> aPIEntity_list = new ArrayList<APIEntity>();
		// ������ip
		// ������������BLOCK
		APIEntity[] Blocks;
		try {
			Blocks = service.getEntities(id, ObjectTypes.IP4Block, 0, 9999); //IP4Block IP4Network
			
			APIEntity[] fields1 = null;
			String block_cidr = "";
			for (int j = 0; j < Blocks.length; j++) {
				if (Blocks[j].getProperties().indexOf("CIDR=") != -1) { 
					//String aa=fields[j].getName();
					block_cidr = getIPNETWORKbyCIDRstring(Blocks[j].getProperties()); 
					APIEntity[] ip4networks = service.searchByObjectTypes(block_cidr.split("\\.")[0],ObjectTypes.IP4Network, 0, 9999);
					if (ans==null){
						ans=ip4networks;
					}else{
					aPIEntity_list = appendValue(ans,ip4networks); 
					ans = aPIEntity_list.toArray(ans);
					}
				}
				
			}
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ans;
	}
	private static ArrayList<APIEntity> appendValue(APIEntity[] obj, APIEntity[] newObj) {
		 
		ArrayList<APIEntity> temp = new ArrayList<APIEntity>(Arrays.asList(obj));
		ArrayList<APIEntity> temp2 = new ArrayList<APIEntity>(Arrays.asList(newObj));
		temp.addAll(temp2); 
		
		return temp;
	 
	  }
	

	/* Return true if it is not a valid mac address */
	public static boolean checkMacAddress(String[] data) {
		System.out.println("CHeckking mac address : " + data);
		boolean ans = false;
		for (int i = 0; i < data.length; i++) {
			System.out.println("CHecking.. : " + data[i] + " " + data[i].length());
			
			if(data[i].length() > 2) {
				return true;
			}
			for (int j = 0; j < data[i].length(); j++) {
				switch (Character.toUpperCase(data[i].charAt(j))) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case 'A':
				case 'B':
				case 'C':
				case 'D':
				case 'E':
				case 'F':
					break;
				default:
					ans = true;
					break;
				}
				if (ans) {
					break;
				}
			}
			if (ans) {
				break;
			}
		}
		System.out.println("Result is : " + ans );
		return ans;
	}

	public static boolean checkIPAddress(String[] data) {
		boolean ans = false;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length(); j++) {
				if (!Character.isDigit(data[i].charAt(j))) {
					ans = true;
					break;
				}
			}
			if (!ans) {
				if (Integer.valueOf(data[i]) > 255) {
					ans = false;
				}
			}
			if (ans) {
				break;
			}
		}

		return ans;
	}

	public static void clean_all_file(String filePath) {
		File tmpFile = new File(filePath);
		try {
			if (tmpFile.exists()) {
				File[] all_file = tmpFile.listFiles();
				for (int i = 0; i < all_file.length; i++) {
					all_file[i].delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return format.format(date);
	}

	public static String[] readData(String original_data) {
		String[] data = new String[9]; // maybe reserve for machine type, etc.. ??
		int a = 0;
		String cont = "";
		for (int i = 0; i < data.length; i++) {
			data[i] = "";
		}
		for (int i = 0; i < original_data.length(); i++) {
			if (original_data.charAt(i) == ',') {
				a++;
			} else {
				data[a] = data[a] + original_data.charAt(i);
			}
		}
		return data;
	}
	
	/**
	 * get ipnetwork by cidr string? : CIDR=158.182.251.0/24|allowDuplicateHost=disable|inheritAllowDuplicateHost=true|pingBeforeAssign=disable|inheritPingBeforeAssign=true|gateway=158.182.251.2|inheritDefaultDomains=true|inheritDefaultView=true|inheritDNSRestrictions=true|
		return this cidr string : 158.182.251.0/24
		get ipnetwork by cidr string? : CIDR=158.182.252.0/24|allowDuplicateHost=disable|inheritAllowDuplicateHost=true|pingBeforeAssign=disable|inheritPingBeforeAssign=true|gateway=158.182.252.2|inheritDefaultDomains=true|inheritDefaultView=true|inheritDNSRestrictions=true|
		return this cidr string : 158.182.252.0/24
		get ipnetwork by cidr string? : CIDR=158.182.253.0/25|allowDuplicateHost=disable|inheritAllowDuplicateHost=true|pingBeforeAssign=disable|inheritPingBeforeAssign=true|gateway=158.182.253.2|inheritDefaultDomains=true|inheritDefaultView=true|inheritDNSRestrictions=true|
		return this cidr string : 158.182.253.0/25
		get ipnetwork by cidr string? : CIDR=158.182.253.128/26|allowDuplicateHost=disable|inheritAllowDuplicateHost=true|pingBeforeAssign=disable|inheritPingBeforeAssign=true|gateway=158.182.253.130|inheritDefaultDomains=true|inheritDefaultView=true|inheritDNSRestrictions=true|
		return this cidr string : 158.182.253.128/26
		get ipnetwork by cidr string? : CIDR=158.182.253.192/26|allowDuplicateHost=disable|inheritAllowDuplicateHost=true|pingBeforeAssign=disable|inheritPingBeforeAssign=true|gateway=158.182.253.194|inheritDefaultDomains=true|inheritDefaultView=true|inheritDNSRestrictions=true|
		r
	 */
	public static String getIPNETWORKbyCIDRstring(String CIDRstring) {
		System.out.println("get ipnetwork by cidr string? : " + CIDRstring);
		String rtnvalue= CIDRstring.substring(
				CIDRstring.indexOf("CIDR=") + 5,
				CIDRstring.indexOf( "|", CIDRstring.indexOf("CIDR=")));
		System.out.println("return this cidr string : " + rtnvalue);

		return rtnvalue;
	}
	public static String getIPNETWORKwithoutSub(String withnamestring){
		String[] Network = withnamestring.trim().split("/");
		return Network[0];
	}
	public static String getIPNETWORKwithoutName(String withnamestring){
		// sometime ip address nework : 158.182.1.0/24 [net1]
		// sometime ip address nework : 158.182.1.0/24 
		System.out.println("ip address nework : " + withnamestring);
		String[] Network = withnamestring.trim().split(" ");
		System.out.println("getIPNETWORKwithoutName newrok[0] : "  + Network[0]);
		// getIPNETWORKwithoutName newrok[0] : 101.78.134.0/24
		return Network[0];
	}
	public static String getIPbyADDRESSstring(String ADDRESSstring) {
		System.out.println("GETipbyaddressstring = " + ADDRESSstring);
		String ipstr = ADDRESSstring;
		if (ipstr.indexOf("address=")>=0){
			ipstr=ipstr.substring(ipstr.indexOf("address=")+8,ipstr.length());
		} else {
			return "";
		}
		ipstr=ipstr.substring(0,ipstr.indexOf("|"));
		String rtnvalue= ipstr;
		return rtnvalue;
	}
	public static String getIPbySTATEstring(String STATEstring) {
		String ipstr = STATEstring;
		if (ipstr.indexOf("state=")>=0){
		ipstr=ipstr.substring(ipstr.indexOf("state=")+6,ipstr.length());
		}
		ipstr=ipstr.substring(0,ipstr.indexOf("|"));
		String rtnvalue= ipstr;
		return rtnvalue;
	}
	
	/**
	 * extract macAddress from entity properties 
	 * @param Mstring string that stored all the properties of the identity
	 * @return mac address if found else return empty string
	 */
	public static String getMACbyMACADDRESSstring(String Mstring) {
		boolean haveresult = false;
		String ipstr = Mstring;
		String macKey = "macAddress=";
		if (ipstr.indexOf(macKey)>=0){
		ipstr=ipstr.substring(ipstr.indexOf(macKey)+macKey.length(),ipstr.length());
		System.out.println("IP string in getmacbymacaddress : " + ipstr);
		haveresult = true;
		}
		ipstr=ipstr.substring(0,ipstr.indexOf("|"));
		String rtnvalue= "";
		if (haveresult){
			rtnvalue= ipstr;
		}
		return rtnvalue;
	}
	
	/**
	 * TIM tools
	 */

	/**
	 * Remove mac and associated ip from the system 
	 * @param service
	 * @param entity
	 * @param mac_address
	 * @return
	 * @throws RemoteException
	 */
	public static boolean remove_mac_from_system(ProteusAPI_PortType service, APIEntity entity, String mac_address) throws RemoteException {
		long id = entity.getId();
		APIEntity config_mac = service.getMACAddress(id, mac_address);
		assert(config_mac != null) : "Config mac shouldnt be null";
		APIEntity[] mac_ip_array = service.getLinkedEntities( config_mac.getId(), ObjectTypes.IP4Address, 0, 100);
		if (mac_ip_array.length > 0) {
			for (int x = 0; x < mac_ip_array.length; x++) {
				
				String DuplicatedIP = Tools.getIPbyADDRESSstring(mac_ip_array[x].getProperties());
				System.out.println("Printing ip stick to mac : " + DuplicatedIP + " " + mac_address);
				APIEntity configtmp = service.getIP4Address(id,DuplicatedIP);
				if (configtmp != null) {
					service.delete(configtmp.getId());
				}
			}
		}
		service.delete(config_mac.getId()); 
		return true;
	}
	
	public static boolean remove_ip_from_system(ProteusAPI_PortType service, APIEntity entity, String ip_address) throws RemoteException{
		long id = entity.getId();
		String mac_address = find_mac_by_ip(service, entity, ip_address);
		APIEntity config_mac = service.getMACAddress(id, mac_address);
		System.out.println("Remove ip from system, removing mac sticking to this ip : " + ip_address + " " + mac_address);
		service.delete(config_mac.getId()); 
		APIEntity config_ip = service.getIP4Address(id,ip_address);
		service.delete(config_ip.getId());
		return true;
	}
	/**
	 * Assume mac in system already!
	 * @param service
	 * @param entity
	 * @param mac_address
	 * @return
	 * @throws RemoteException
	 */
	public static boolean mac_in_system(ProteusAPI_PortType service, APIEntity entity, String mac_address) throws RemoteException{
		long id = entity.getId();
		APIEntity config_mac = service.getMACAddress(id, mac_address);
		if (config_mac == null){
			return false;
		}
		APIEntity[] mac_ip_array=service.getLinkedEntities(config_mac.getId(),ObjectTypes.IP4Address,0,10);

		return mac_ip_array.length != 0;
	}
	
	/**
	 * Assume mac in system already! 
	 * @param service
	 * @param entity
	 * @param mac_address
	 * @return
	 * @throws RemoteException
	 */
	public static String find_ip_by_mac(ProteusAPI_PortType service, APIEntity entity, String mac_address) throws RemoteException{
		long id = entity.getId();
		APIEntity config_mac = service.getMACAddress(id, mac_address);
		APIEntity[] mac_ip_array=service.getLinkedEntities(config_mac.getId(),ObjectTypes.IP4Address,0,10);
		return Tools.getIPbyADDRESSstring(mac_ip_array[0].getProperties()) ;
	}
	/**
	 * Assume ip_address is in correct format!
	 * @param entity config entity
	 * @param ip_address
	 * @return
	 * @throws RemoteException 
	 */
	public static boolean ip_in_system(ProteusAPI_PortType service, APIEntity entity, String ip_address) throws RemoteException{
		long id = entity.getId();
		APIEntity config_ip = service.getIP4Address(id, ip_address);
		return config_ip != null;
	}
	
	public static String find_mac_by_ip(ProteusAPI_PortType service, APIEntity entity, String ip_address) throws RemoteException{
		long id = entity.getId();
		APIEntity config_ip = service.getIP4Address(id, ip_address);
		return Tools.getMACbyMACADDRESSstring(config_ip.getProperties()); 
	}
	/**
	 * 
	 * @param out write out html through here
	 * @param service sessions.get ready_server
	 * @param entity config entity 
	 * @param type either IP or NETWORK_IP
	 * @param oneMacAddress the structure that stored all the info, assume ip/mac is valid, but have to take care of network_ip
	 * @param index indicate it is bulk or single
	 * @return for multiple
	 * @throws IOException 
	 */
	public static boolean single_addition_check(JspWriter out, ProteusAPI_PortType service, APIEntity entity, AddressType type, OneMacAddress oneMacAddress, int index) throws IOException {
		assert(type == AddressType.IPV4_NETWORK || type == AddressType.IP) : "INVALID ADDRESSTYPE";
		String ip_address, mac_address, ip_msg="", mac_msg="";
		String associate_mac_address="";
		String associate_ip_address="";
		boolean overwrite_check = false;
		if (type == AddressType.IPV4_NETWORK) {
			APIEntity[] available_ip_entities = service.searchByObjectTypes(
							Tools.getIPNETWORKwithoutName(oneMacAddress.getIP_Address_NetWork()),
							ObjectTypes.IP4Network, 0, 10); // should only return 1 elem, coz we using exact search 10.1.1.0/24
			assert (available_ip_entities.length != 0) : "No available ip returned";
			APIEntity ipv4_entity = available_ip_entities[0];
			String ip4Network = Tools.getIPNETWORKbyCIDRstring(ipv4_entity.getProperties()); // return 10.0.1.0/24
			String available_ip =service.getNextAvailableIP4Address(ipv4_entity.getId());
			oneMacAddress.setIP_Address(available_ip);
			ip_msg = "IP Assigned By System";

		} else if ( type == AddressType.IP) {
			ip_address = oneMacAddress.getIP_Address();
			if(ip_in_system(service, entity, ip_address)){
				// find associated mac, must be true
				associate_mac_address = find_mac_by_ip(service, entity, ip_address);
				System.out.println("Associate mac address exists! : " + associate_mac_address);
				ip_msg = "Associate mac address exists! : " + associate_mac_address;
				overwrite_check = true;
				// need to rmb this
			} else {
				// not in system : )
			}
		}
		
		mac_address = oneMacAddress.getMAC_Address(); // assume mac_address is in correct format
		if(mac_in_system(service, entity, mac_address)){
			associate_ip_address = find_ip_by_mac(service, entity, mac_address);
			System.out.println("Associate ip address exists! : " + associate_ip_address);
			mac_msg = "Associate ip address exists! : " + associate_ip_address;
			overwrite_check = true;
			// need to rmb this
		} else {
			// happy
		}
		
		// print out everything
		if (index == 0) {
			// single printing
			print_import_single_ip_mac_row(out, oneMacAddress, ip_msg, mac_msg);
			print_import_single_udf_row(out, oneMacAddress);
			
		} else {
			// multiple printing
		}
		return overwrite_check;
	}
	/**
	 * 
	 * @param out
	 * @param entity config entity
	 * @param type
	 * @param data
	 * @return
	 * @throws IOException 
	 */
	public static boolean multiple_deletion_check(JspWriter out, ProteusAPI_PortType service, APIEntity entity, AddressType type, List<String> data) throws IOException{
		long id = entity.getId();
		APIEntity new_entity;
		for (int i=0; i < data.size(); i++ ){
			String mac_or_ip = data.get(i);
			System.out.println("Multiple deletin check printing ip/mac : " + mac_or_ip);

			if(type == AddressType.IP ){
				String ip_address = mac_or_ip;
				if (Tools.checkIPAddress(ip_address)){
					new_entity = service.getIP4Address(id, mac_or_ip);
					single_deletion_check(out, new_entity, type, mac_or_ip, i+1); 
				} else {
					// print invaid ip format
					out.write("<tr><td colspan=\"100%\">" + ip_address + " have an invalid format :( </td></tr>");
				}
			} else if (type == AddressType.MAC){
				String mac_address = mac_or_ip;
				if (Tools.checkMacAddress(mac_address)){
					new_entity = service.getMACAddress(id, mac_or_ip);
					if(new_entity == null){
						single_deletion_check(out,new_entity, AddressType.MAC, mac_or_ip, i+1);
					} else {
						APIEntity[] config_mac_array=service.getLinkedEntities(new_entity.getId(),ObjectTypes.IP4Address,0,100);
						single_deletion_check(out,config_mac_array[0], AddressType.MAC, mac_or_ip, i+1);
					}
				} else {
					// print invalid mac format 
					out.write("<tr><td colspan=\"100%\">" + mac_address + " have an invalid format :( </td></tr>");

				}
			}
		}
		return true;
	}
	/**
	 * 
	 * @param out
	 * @param entity
	 * @param type 
	 * @param mac_or_ip 
	 * @param index if index == 0 , it is single deletion  else multi deletion
	 * @return
	 * @throws IOException
	 */
	public static boolean single_deletion_check(JspWriter out, APIEntity entity, AddressType type, String mac_or_ip, int index) throws IOException{
		if(entity == null){
			out.write("<tr><td colspan=\"100%\">No record found for " + mac_or_ip + "</td></tr>");
			return false;
		}
		
		String mac_address, ip_address;
		if (type == AddressType.IP){
			System.out.println(entity);
			System.out.println(entity.getProperties());

			mac_address = Tools.getMACbyMACADDRESSstring(entity.getProperties());
			ip_address = mac_or_ip;
			if(index == 0){
				// single case
				if(mac_address.isEmpty()){
					print_single_ip_mac_error(out, ip_address);
					return false;
				} else {
					print_single_ip_mac_success(out, ip_address, mac_address);
					return true;
				}
			} else {
				// multiple case
				if(mac_address.isEmpty()){
					print_multiple_ip_mac_error(out, ip_address, index);
					return false;
				} else {
					print_multiple_ip_mac_success(out, ip_address, mac_address, index);
					return true;
				}
			}
		} 
		
		// MAC type
		
		ip_address= Tools.getIPbyADDRESSstring(entity.getProperties());
		mac_address = mac_or_ip;
		if ( index == 0){
			// single mac delete case 
			if(ip_address.isEmpty()){
				System.out.println("1");
				print_single_mac_ip_error(out, mac_address);
				return false;
			} else {
				System.out.println("2 mac , ip = " + mac_address + " " + ip_address);
				print_single_mac_ip_success(out, mac_address, ip_address);
				return true;
			}			
		} else {
			// multi mac delete case 
			if(ip_address.isEmpty()){
				System.out.println("3");

				print_multiple_mac_ip_error(out, mac_address, index);
				return false;
			} else {
				System.out.println("4");

				print_multiple_mac_ip_success(out, mac_address, ip_address, index);
				return true;
			}
		}
	}

	/* For import */
	private static void print_import_single_ip_mac_row(JspWriter out, OneMacAddress oneMacAddress, String ip_msg, String mac_msg) throws IOException{
		print_import_single_ip_mac_row_wrapper(out, ImportAttr.MAC_ADDR.getIndex(), oneMacAddress.getMAC_Address(), mac_msg);
		print_import_single_ip_mac_row_wrapper(out, ImportAttr.IP_ADDR.getIndex(), oneMacAddress.getIP_Address(), ip_msg);
	}
	
	private static void print_import_single_ip_mac_row_wrapper(JspWriter out, int type, String ip_or_mac, String msg) throws IOException{
		if (!msg.isEmpty()){
			// print row with special treatment
			out.write("<tr class=\"warning\"><th>" + Const.IMPORT_TITLE[type] + "</th>" 
				+"<td><input type='text' name='oneMacAddress."
				+ Const.IMPORT_VALUE_NAME[type]
				+ "' readonly='readonly' value='"
				+ ip_or_mac + "'/>" + "<br>(" + msg +")</td></tr>");
			
		} else {
			print_row_wrapper(out, type, ip_or_mac);
		}
		
	}
	private static void print_import_single_udf_row(JspWriter out, OneMacAddress oneMacAddress) throws IOException{
		print_row_wrapper(out, ImportAttr.MACHINE_TYPE.getIndex(), oneMacAddress.getMachine_Type());
		print_row_wrapper(out, ImportAttr.LOCATION.getIndex(), oneMacAddress.getLocation());
		print_row_wrapper(out, ImportAttr.OWNER.getIndex(), oneMacAddress.getOwner());
		print_row_wrapper(out, ImportAttr.DEPT.getIndex(), oneMacAddress.getDepartment());
		print_row_wrapper(out, ImportAttr.PHONE_NUM.getIndex(), oneMacAddress.getPhone_Number());
		print_row_wrapper(out, ImportAttr.INPUT_DATE.getIndex(), oneMacAddress.getInput_Date());
		print_row_wrapper(out, ImportAttr.REF.getIndex(), oneMacAddress.getReference());
	}
	
	private static void print_row_wrapper(JspWriter out, int index, String value) throws IOException{

		print_row(out, new String[]{Const.IMPORT_TITLE[index], Const.IMPORT_VALUE_NAME[index], value});
	}
	
	private static void print_row(JspWriter out, String[] list) throws IOException{
		out.write("<tr>");
		out.write("<th>" + list[0] + "</th>");
		out.write("<th><input type='text' name='oneMacAddress." + list[1]
							+ "' readonly='readonly' value='" + list[2] + "'/></th>");
		out.write("</tr>");
	}
	/* For deletion */
	private static void print_single_mac_ip_error(JspWriter out, String mac_address) throws IOException {
		String error_msg = "No associated IP address found";
		out.write("<tr><td>" + mac_address + "</td><td>" + error_msg + "</td></tr>");
	}
	
	private static void print_single_mac_ip_success(JspWriter out, String mac_address, String ip_address) throws IOException {
		out.write("<tr><td>");
		print_single_input_attributes_mac(out, mac_address);
		out.write("</td><td>");
		print_single_input_attributes_ip(out, ip_address);
		out.write("</td></tr>");
	}
	
	private static void print_multiple_mac_ip_error(JspWriter out, String mac_address, int index) throws IOException{
		String error_msg = "No associated MAC address found";
		out.write("<tr><td>" + mac_address + "</td><td>" + error_msg + "</td><td>" 
				+ "<input type='checkbox' name='choose_delete' value='" 
				+ index +  "'></td></tr>");
	}
	
	private static void print_multiple_mac_ip_success(JspWriter out, String ip_address, String mac_address, int index) throws IOException{
		out.write("<tr><td>" + mac_address + "</td><td>" + ip_address + "</td><td>" 
				+ "<input type='checkbox' name='choose_delete' value='" 
				+ index +  "' checked></td></tr>");
	}
	
	private static void print_single_ip_mac_error(JspWriter out, String ip_address) throws IOException{
		String error_msg = "No associated MAC address found";
		out.write("<tr><td>" + ip_address + "</td><td>" + error_msg + "</td></tr>");
	}
	
	private static void print_single_ip_mac_success(JspWriter out, String ip_address, String mac_address) throws IOException{
		out.write("<tr><td>");
		print_single_input_attributes_ip(out, ip_address);
		out.write("</td><td>");
		print_single_input_attributes_mac(out, mac_address);
		out.write("</td></tr>");
	}
	
	private static void print_multiple_ip_mac_error(JspWriter out, String ip_address, int index) throws IOException{
		String error_msg = "No associated MAC address found";
		out.write("<tr><td>" + ip_address + "</td><td>" + error_msg + "</td><td>" 
				+ "<input type='checkbox' name='choose_delete' value='" 
				+ index +  "'></td></tr>");
	}
	
	private static void print_multiple_ip_mac_success(JspWriter out, String ip_address, String mac_address, int index) throws IOException{
		out.write("<tr><td>" + ip_address + "</td><td>" + mac_address + "</td><td>" 
				+ "<input type='checkbox' name='choose_delete' value='" 
				+ index +  "' checked></td></tr>");
	}
	
	/** Helper Functions **/
	private static void print_single_input_attributes_ip(JspWriter out, String ip_address) throws IOException{
		out.write("<input type='text' name='IP_Address' readonly='readonly' value='" + ip_address+ "'/>");
	}
	
	private static void print_single_input_attributes_mac(JspWriter out, String mac_address) throws IOException{
		out.write("<input type='text' name='MAC_Address' readonly='readonly' value='" + mac_address+ "'/>");
	}

	public static boolean checkIPAddress(String ip_address){
		final String IPADDRESS_PATTERN = 
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		return Pattern.matches(IPADDRESS_PATTERN, ip_address);		
	}
	
	public static boolean checkMacAddress(String mac_address){
		return Pattern.matches("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$", mac_address);
	}
	
	/** END **/

}
