package Factory;

import java.util.Scanner;

/**
 * ������
 * @author RHHR1314
 * @version 1.0
 * @created 13-12��-2019 16:44:23
 */
public class Client {

	public Client(){

	}

	public void finalize() throws Throwable {

	}
	public static void main(String args[]) {
		try {
			Scanner sc = new Scanner(System.in);
			String type = sc.nextLine();
			TV tv;
			TVFactory factory;
			factory = (TVFactory)XMLUtil.getBean(type);
			//HaierTVFactory factory = new HaierTVFactory();
			//HisenseTVFactory factory = new HisenseTVFactory();
			tv = factory.produceTV();
			tv.play();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}//end Client