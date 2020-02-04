package FacadePattern;


/**
 * @author RHHR1314
 * @version 1.0
 * @created 30-11��-2019 21:46:51
 */
public class Client {

	public Client(){

	}

	public void finalize() throws Throwable {

	}
	public static void main(String args[]) {
		GeneralSwitchFacade gsf = new GeneralSwitchFacade();
		gsf.on();
		System.out.println("-----------------");
		gsf.off();
	}
}//end Client