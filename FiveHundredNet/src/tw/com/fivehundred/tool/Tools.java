package tw.com.fivehundred.tool;

import java.io.File;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;

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
	
//	public static Cookie getCookieByName(HttpServletRequest request,String name){
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
		
		//這邊要改..
		
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
	//慢
	public static APIEntity[] getAllIPAddress(ProteusAPI_PortType service,
			long id) {
		APIEntity[] ans = null;
		ArrayList<APIEntity> aPIEntity_list = new ArrayList<APIEntity>();
		// 該有的ip
		// 所有有用到的BLOCK
		APIEntity[] fields;
		try {
			fields = service.getEntities(id, ObjectTypes.IP4Block, 0, 9999);
			APIEntity[] fields1 = null;
			String container_cidr = "";
			for (int j = 0; j < fields.length; j++) {
				if (fields[j].getProperties().indexOf("CIDR=") != -1) {
					
					container_cidr = getIPNETWORKbyCIDRstring( fields[j].getProperties()) ;
				}
				// 所有的mac
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
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public static APIEntity[] getAllIPAddressNetwork(ProteusAPI_PortType service,
			long id) {
		APIEntity[] ans = null;
		ArrayList<APIEntity> aPIEntity_list = new ArrayList<APIEntity>();
		// 該有的ip
		// 所有有用到的BLOCK
		APIEntity[] Blocks;
		try {
			Blocks = service.getEntities(id, ObjectTypes.IP4Block, 0, 9999); //IP4Block IP4Network
			
			//APIEntity[] fields2 = service.getEntities(id, ObjectTypes.IP4Network, 0, 9999);
			
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
				
				//APIEntity networks=service.getEntityByCIDR(fields[j].getId(),container_cidr,ObjectTypes.IP4Network);
				//APIEntity[] networks=service.getIP4NetworksByHint(fields[j].getId(),1000,999,ObjectTypes.IP4Network);//container_cidr.split("\\.")[0]);
				
				
				
				/*
				ans = ArrayUtils.addAll(ans, ip4networks);
				// 所有的mac
				int count_number = 0;
				do {
					fields1 = service.searchByObjectTypes( container_cidr.split("\\.")[0],
							ObjectTypes.IP4Network, 1000 * count_number, 999);
					
					//String bb=fields1.;
					
					count_number++;
					for (int i = 0; i < fields1.length; i++) {
						aPIEntity_list.add(fields1[i]);
					}
				} while (fields1.length == 999);
				*/
				
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
	public static boolean checkMacAddress(String[] data) {
		boolean ans = false;
		for (int i = 0; i < data.length; i++) {
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
		String[] data = new String[9];
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
	
	public static String getIPNETWORKbyCIDRstring(String CIDRstring) {
		String rtnvalue= CIDRstring.substring(
				CIDRstring.indexOf("CIDR=") + 5,
				CIDRstring.indexOf( "|", CIDRstring.indexOf("CIDR=")));
		return rtnvalue;
	}
	public static String getIPNETWORKwithoutSub(String withnamestring){
		String[] Network = withnamestring.trim().split("/");
		return Network[0];
	}
	public static String getIPNETWORKwithoutName(String withnamestring){
		String[] Network = withnamestring.trim().split(" ");
		return Network[0];
	}
	public static String getIPbyADDRESSstring(String ADDRESSstring) {
		String ipstr = ADDRESSstring;
		if (ipstr.indexOf("address=")>=0){
		ipstr=ipstr.substring(ipstr.indexOf("address=")+8,ipstr.length());
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
	public static String getMACbyMACADDRESSstring(String Mstring) {
		boolean haveresult = false;
		String ipstr = Mstring;
		if (ipstr.indexOf("macAddress=")>=0){
		ipstr=ipstr.substring(ipstr.indexOf("macAddress=")+11,ipstr.length());
		haveresult = true;
		}
		ipstr=ipstr.substring(0,ipstr.indexOf("|"));
		String rtnvalue= "";
		if (haveresult){
			rtnvalue= ipstr;
		}
		return rtnvalue;
	}
	
}
