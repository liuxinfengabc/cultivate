package FacadePattern;


/**
 * @author RHHR1314
 * @version 1.0
 * @created 30-11��-2019 21:46:51
 */
public class Light {

	private String position;

	public Light(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param position
	 */
	public Light(String position){

		this.position = position;
	}

	public void off(){

		System.out.println(this.position + "灯关闭");
	}

	public void on(){
		System.out.println(this.position + "灯打开");
	}
}//end Light