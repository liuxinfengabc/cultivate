package FacadePattern;


/**
 * @author RHHR1314
 * @version 1.0
 * @created 30-11��-2019 21:46:51
 */
public class AirConditioner {

	public AirConditioner(){

	}

	public void finalize() throws Throwable {

	}
	public void off(){
		System.out.println( "空调关闭");

	}

	public void on(){
		System.out.println( "空调打开");

	}
}//end AirConditioner