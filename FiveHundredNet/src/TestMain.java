import com.bluecatnetworks.proteus.api.client.java.EntityProperties;
import com.bluecatnetworks.proteus.api.client.java.ProteusAPIUtils;
import com.bluecatnetworks.proteus.api.client.java.constants.EntityCategories;
import com.bluecatnetworks.proteus.api.client.java.constants.ObjectProperties;
import com.bluecatnetworks.proteus.api.client.java.constants.ObjectTypes;
import com.bluecatnetworks.proteus.api.client.java.constants.UserHistoryPrivileges;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIAccessRight;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIEntity;
import com.bluecatnetworks.proteus.api.client.java.proxy.APIUserDefinedField;
import com.bluecatnetworks.proteus.api.client.java.proxy.ProteusAPI_PortType;

public class TestMain {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try {
					
			ProteusAPI_PortType service = ProteusAPIUtils
					.connect("192.168.1.151"); //150
			service.login("hans", "0300");
			String address="00-16-C5-0A-51-55";
			String ip="10.10.10.19";
			APIEntity config = service.getEntityByName(0, "hkbu",
					ObjectTypes.Configuration);
			long id=config.getId();
			String MAC_Address = "0015c50a5147", IP_Address = "158.182.138.227",
					   Machine_Type = "PC NIL", Location = "CVA212", Owner = "Lam Wai Hung",
					   Department = "ITO", Phone_Number = "7240", Input_Date = "24/08/11", 
					   Reference = "tem138";
		
			//抓取mac address 在指定記憶池	
			//APIEntity config4 = service.getMACAddress(id, "00-00-00-00-00-01");
			//System.out.println(config4.getProperties());
			
//			//抓取mac address 在指定記憶池	
//			APIEntity config4 = service.getEntityByName(id, "macpool1",
//					ObjectTypes.MACPool);
//			service.associateMACAddressWithPool(config.getId(),address,config4.getId());
//			System.out.println(config4.getId());
			//將mac address加入			
//			EntityProperties props0 = new EntityProperties();
//			props0.addProperty(ObjectProperties.name, "hans");
//			String user_defined="";
//			user_defined=user_defined+"ip_machine_type="+Machine_Type+"|";
//			user_defined=user_defined+"ip_location="+Location+"|";
//			user_defined=user_defined+"ip_device_ower="+Owner+"|";
//			user_defined=user_defined+"ip_department="+Department+"|";
//			user_defined=user_defined+"ip_input_date="+Input_Date+"|";
//		//	user_defined=user_defined+ObjectProperties.phoneNumber+"="+Phone_Number+"|";
//		//	user_defined=user_defined+"Reference="+Reference+"|";
//			long mac_long=service.addMACAddress(id, address, props0.getPropertiesString());
//			System.out.println(props0.getPropertiesString());
//			System.out.println(user_defined);
//			//加入ip
//			long ip_ans=service.assignIP4Address(id, ip, address,"|","MAKE_DHCP_RESERVED",user_defined);
//			System.out.println(ip_ans);
//			//更換IP 的狀態
//			APIEntity config6 = service.getIP4Address(id,ip);
//			System.out.println(config6);
//			service.changeStateIP4Address(config6.getId() ,"MAKE_STATIC" ,MAC_Address);
//			//抓資料庫裡的mac address
//			APIEntity config2=service.getMACAddress(id,address);
//			System.out.println(config2.getId());
//			//讀取新加入的mac address的id
//			APIEntity config3=service.getEntityById(config2.getId());
//			System.out.println(config3.getProperties());
//			//抓取mac pool裡所有的名稱
//			APIEntity[] fields = service.getEntities(id,ObjectTypes.MACPool,0,10 );
//			System.out.println(fields.length);
//			for(int i=0;i<fields.length;i++){				
//				System.out.println(fields[i].getName());
//			}
//			//刪除IP
//			APIEntity config7=service.getIP4Address(id,ip);
//			System.out.println(config7.getId());
//			service.delete(config7.getId());
//			//刪除mac
//			APIEntity config5=service.getMACAddress(id,address);
//			System.out.println(config5.getId());
//			service.delete(config5.getId());
//			//抓取大量的mac address
//			APIEntity[] fields = service.getEntities(id,ObjectTypes.MACPool,0,10 );
//			System.out.println(fields.length);
//			for(int i=0;i<fields.length;i++){	
//				APIEntity[] mac_address_array=service.getLinkedEntities(fields[i].getId(),ObjectTypes.MACAddress,0,999);
//				for(int j=0;j<mac_address_array.length;j++){
//					System.out.println(mac_address_array[j].getProperties());
//				}
//			}
//			//抓取Configuration裡所有的名稱
//			APIEntity[] fields = service.getEntities(id,ObjectTypes.Configuration,0,999);
//			System.out.println(fields.length);
//			for(int i=0;i<fields.length;i++){				
//				System.out.println(fields[i].getName());
//			}
//			//抓取Configuration裡所有的mac address
//			APIEntity[] fields = service.getEntities(id,ObjectTypes.MACAddress,0,999);
//			System.out.println(fields.length);
//			for(int i=0;i<fields.length;i++){				
//				System.out.println(fields[i].getProperties());
//			}
//			//所有有用到的ip
//			APIEntity[] fields=service.searchByObjectTypes ("10.10",ObjectTypes.IP4Address,0,9999);
//			System.out.println(fields.length);
//			for(int i=0;i<fields.length;i++){	
//				System.out.println(fields[i].getProperties());
//			}
			
			//所有有用到的CDIR
			APIEntity[] fields=service.searchByObjectTypes ("158",ObjectTypes.IP4Network,0,9999);
			System.out.println(fields.length);
			for(int i=0;i<fields.length;i++){	
				System.out.println(fields[i].getProperties());
			}
			
			//所有有用到的BLOCK
//			APIEntity[] fields=service.getEntities(id,ObjectTypes.IP4Block,0,9999);
//			System.out.println(fields.length);
//			for(int i=0;i<fields.length;i++){	
//				System.out.println(fields[i].getProperties());
//			}
			
						
//			//所有有用到的ip
//			APIEntity[] fields=service.searchByObjectTypes ("158.182.1",ObjectTypes.IP4Address,0,9999);
//			System.out.println(fields.length);
//			for(int i=0;i<fields.length;i++){	
//				System.out.println(fields[i].getProperties());
//			}
			
//			//所有的mac
//			APIEntity[] fields=service.searchByObjectTypes ("00-15",ObjectTypes.MACAddress,0,9999);
//			System.out.println(fields.length);
//			for(int i=0;i<fields.length;i++){	
//				System.out.println(fields[i].getProperties());
//			}
//			//該有的ip			
//			APIEntity[] fields=service.getIP4NetworksByHint (id,0,10,ObjectProperties.hint + "=0.0|");
//			System.out.println(fields.length);
//			for(int i=0;i<fields.length;i++){	
//				System.out.println(fields[i].getProperties());
//			}
//			//抓network
//			APIEntity[] fields = service.searchByCategory("158.182", EntityCategories.IP4Objects, 0, 9999);
//			System.out.println(fields.length);
//			for(int i=0;i<fields.length;i++){	
//				System.out.println(fields[i].getProperties());
//			}
			
//          //加入新的new network
//			if (config == null) {
//				APIEntity newConfig = new APIEntity();
//				newConfig.setName("Configuration");
//				newConfig.setType(ObjectTypes.Configuration);
//				service.addEntity(0, newConfig);
//			}	
//			config = service.getEntityByName(0, "Configuration",
//					Configuration);
//			Long configID = config.getId();
//			EntityProperties props1 = new EntityProperties();
//			int gatewayOffset1 = 1;// 1 would be x.x.x.1, -1 would be x.x.x.254
//			String dhcp = ObjectProperties.reservedDHCPRange;
//			String from1 = "FROM_START";
//			String name1 = "reservedDHCP";
//			int offset1 = 2;// Starts from x.x.x.2 going upward or x.x.x.253
//							// going down
//			int size1 = 25;
//			String reservedDHCP = String.format("{%s,%d,%d,%s,%s,0}", dhcp,
//					offset1, size1, from1, name1);
//			props1.addProperty(ObjectProperties.gateway, gatewayOffset1 + "");
//			props1.addProperty(ObjectProperties.reservedAddresses, reservedDHCP);
//			String propString1 = props1.getPropertiesString();
//			System.out.println(propString1);
//			service.addIP4NetworkTemplate(configID, "dhcp reserved",
//					propString1);
//			EntityProperties props2 = new EntityProperties();
//			int gatewayOffset2=(-1);//Gateway will be at x.x.x.254
//			String block= ObjectProperties.reservedBlock;
//			String from2 = "FROM_END";
//			String name2 = "reservedBlock";
//			int offset2 = 4;//Starts from x.x.x.251 going downward
//			int size2 = 10;
//			String
//			reservedBLOCK=String.format("{%s,%d,%d,%s,%s}",block,offset2,size2,from2,name2);
//			props2.addProperty(ObjectProperties.gateway, gatewayOffset2 + "");
//			props2.addProperty(ObjectProperties.reservedAddresses, reservedBLOCK);
//			String propString2 = props2.getPropertiesString();
//			service.addIP4NetworkTemplate(configID, "block reserved", propString2);
//			//you can add multiple reserved ranges to a template.
//			EntityProperties props = new EntityProperties();
//			props.addProperty(ObjectProperties.gateway, gatewayOffset1 + "");
//			props.addProperty(ObjectProperties.reservedAddresses,
//			reservedDHCP+","+reservedBLOCK);
//			String propString = props.getPropertiesString();
//			service.addIP4NetworkTemplate(configID, "NetworkTemplate", propString);
//			System.out.println(propString+"");
//			//You can now add a new network to any block using this template.
//			//加入新的mac pool
//			APIEntity newConfig = new APIEntity();
//			newConfig.setName("macpool1");
//			newConfig.setType(ObjectTypes.MACPool);
//			service.addEntity(id, newConfig);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
