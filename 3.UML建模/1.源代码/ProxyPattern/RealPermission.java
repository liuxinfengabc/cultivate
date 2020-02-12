package ProxyPattern;


/**
 * @author RHHR1314
 * @version 1.0
 * @created 30-11��-2019 20:17:57
 */
public class RealPermission implements AbstractPermission {


	public void modifyNote(){
		System.out.println("修改发帖内容");

	}

	public void modifyUserInfo(){

		System.out.println("修改用户信息");
	}

	public void publishNote(){
		System.out.println("发布新帖");

	}

	/**
	 * 
	 * @param level
	 */
	public void setLevel(int level){

	}

	public void viewNote(){

	}
}//end RealPermission