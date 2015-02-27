package tw.com.fivehundred.tool;

import java.util.HashMap;
import java.util.Map;

public final class Const  {

	  /** Opposite of {@link #FAILS}.  */
	  public static final boolean PASSES = true;
	  /** Opposite of {@link #PASSES}.  */
	  public static final boolean FAILS = false;
	  
	  /** Opposite of {@link #FAILURE}.  */
	  public static final boolean SUCCESS = true;
	  /** Opposite of {@link #SUCCESS}.  */
	  public static final boolean FAILURE = false;

	  /** 
	   Useful for {@link String} operations, which return an index of <tt>-1</tt> when 
	   an item is not found. 
	  */
	  public static final int NOT_FOUND = -1;
	  
	  /* The configuration name of blue cat */
	  public static final String CONFIG_NAME = "hkbu";
	  /** System property - <tt>file.separator</tt>*/
	  public static final String SERVER_NAME1 = "BDDS1";
	  
	  public static final String SERVER_NAME2 = "BDDS2";
	  
		public static final String IMPORT_TITLE[] = { "MAC Address", "IP Address", "Machine Type",
				"Location", "Owner", "Department", "Phone Number", "Input Date",
				"Reference" };
		public static final String IMPORT_VALUE_NAME[] = { "MAC_Address", "IP_Address", "Machine_Type",
				"Location", "Owner", "Department", "Phone_Number", "Input_Date",
				"Reference" };
	  public static final Map<String, String> IMPORT_ATTR_MAP;
	  static
	  {
	    	IMPORT_ATTR_MAP = new HashMap<String, String>();
	    	IMPORT_ATTR_MAP.put("MAC Address", "MAC_Address");
	    	IMPORT_ATTR_MAP.put("IP Address", "IP_Address");
	    	IMPORT_ATTR_MAP.put("Machine Type", "Machine_Type");
	    	IMPORT_ATTR_MAP.put("Location", "Location");
	    	IMPORT_ATTR_MAP.put("Owner", "Owner");
	    	IMPORT_ATTR_MAP.put("Department", "Department");
	    	IMPORT_ATTR_MAP.put("Phone Number", "Phone_Number");
	    	IMPORT_ATTR_MAP.put("Input Date", "Input_Date");
	    	IMPORT_ATTR_MAP.put("Reference", "Reference");
	  }

	  /**
	   The caller references the constants using <tt>Consts.EMPTY_STRING</tt>, 
	   and so on. Thus, the caller should be prevented from constructing objects of 
	   this class, by declaring this private constructor. 
	  */
	  private Const(){
	    //this prevents even the native class from 
	    //calling this ctor as well :
	    throw new AssertionError();
	  }
	}