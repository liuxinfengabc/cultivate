package FacadePattern;


/**
 * @author RHHR1314
 * @version 1.0
 * @created 30-11��-2019 21:46:51
 */
public class Fan {

	public Fan(){

	}

	public void finalize() throws Throwable {

	}
	public void off(){
		System.out.println( "风扇关闭");

	}

	public void on(){
		System.out.println( "风扇打开");
	}
}//end Fan