package tw.com.fivehundred.tool;

public enum ImportAttr {
	MAC_ADDR(0),
	IP_ADDR(1),
	MACHINE_TYPE(2),
	LOCATION(3),
	OWNER(4),
	DEPT(5),
	PHONE_NUM(6),
	INPUT_DATE(7),
	REF(8);
	
	ImportAttr(int i){
		value = i;
	}
	
	public final int value;
	public int getIndex(){
		return value;
	}
}
