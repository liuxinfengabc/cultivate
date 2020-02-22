package ProxyPattern;

import java.util.Scanner;

/**
 * @author RHHR1314
 * @version 1.0
 * @created 30-11��-2019 20:17:57
 */
public class Client {

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		String type = sc.nextLine();
		AbstractPermission permission;
		permission = (AbstractPermission)XMLUtil.getBean(type);
		
		//PermissionProxy permission = new PermissionProxy();
		permission.modifyUserInfo();
		permission.viewNote();
		permission.publishNote();
		permission.modifyNote();
		System.out.println("---------------------------");
		permission.setLevel(1);
		permission.modifyUserInfo();
		permission.viewNote();
		permission.publishNote();
		permission.modifyNote();
	}

}// end Client