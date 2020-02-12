package ProxyPattern;

/**
 * @author RHHR1314
 * @version 1.0
 * @created 30-11��-2019 20:17:57
 */
public class PermissionProxy implements AbstractPermission {

	private int level = 0;
	private RealPermission permission = new RealPermission();


	public void modifyNote() {

		if(0 == level) {
			System.out.println("对不起，你没有该权限！");
		}else if (1 == level) {
			permission.modifyNote();

		}
	}

	public void publishNote() {

		if(0 == level) {
			System.out.println("对不起，你没有该权限！");
		}else if (1 == level) {
			permission.publishNote();

		}
	}

	/**
	 * 
	 * @param level
	 */
	public void setLevel(int level) {

		this.level = level;
	}

	public void viewNote() {

		System.out.println("查看帖子");
	}

	public void modifyUserInfo() {

		if (0 == level) {
			System.out.println("对不起，你没有该权限！");
		} else if (1 == level) {
			permission.modifyUserInfo();

		}
	}
}// end PermissionProxy