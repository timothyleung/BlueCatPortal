package tw.com.fivehundred.tool;

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