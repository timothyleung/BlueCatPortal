package tw.com.fivehundred.add.been;

public class OneMacAddress {
	String MAC_Address;
	String IP_Address;
	String Machine_Type;
	String Location;
	String Owner;
	String Department;
	String Phone_Number;
	String Input_Date;
	String Reference;
	String IP_Address_NetWork;
	String Servers;

	public String getMAC_Address() {
		return MAC_Address;
	}

	public void setMAC_Address(String mAC_Address) {
		if (mAC_Address.equals("xx-xx-xx-xx-xx-xx")) {
			MAC_Address = "";
		} else {
			MAC_Address = mAC_Address.trim();
		}
	}

	public String getIP_Address() {
		return IP_Address;
	}

	public void setIP_Address(String iP_Address) {
		if (iP_Address.equals("xxx.xxx.xxx.xxx")) {
			IP_Address = "";
		} else {
			IP_Address = iP_Address.trim();
		}
	}

	public String getMachine_Type() {
		return Machine_Type;
	}

	public void setMachine_Type(String machine_Type) {
		Machine_Type = machine_Type.trim();
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location.trim();
	}

	public String getOwner() {
		return Owner;
	}

	public void setOwner(String owner) {
		Owner = owner.trim();
	}

	public String getDepartment() {
		return Department;
	}

	public void setDepartment(String department) {
		Department = department.trim();
	}

	public String getPhone_Number() {
		return Phone_Number;
	}

	public void setPhone_Number(String phone_Number) {
		Phone_Number = phone_Number.trim();
	}

	public String getInput_Date() {
		return Input_Date;
	}

	public void setInput_Date(String input_Date) {
		Input_Date = input_Date.trim();
	}

	public String getReference() {
		return Reference;
	}

	public void setReference(String reference) {
		Reference = reference.trim();
	}

	public String getIP_Address_NetWork() {
		return IP_Address_NetWork;
	}

	public void setIP_Address_NetWork(String iP_Address_NetWork) {
		IP_Address_NetWork = iP_Address_NetWork;
	}

	public String getServers() {
		return Servers;
	}

	public void setServers(String servers) {
		Servers = servers;
	}

}
