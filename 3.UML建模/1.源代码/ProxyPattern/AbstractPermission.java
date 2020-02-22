package ProxyPattern;


/**
 * @author RHHR1314
 * @version 1.0
 * @created 30-11ÔÂ-2019 20:17:57
 */
public interface AbstractPermission {

	public void modifyNote();

	public void modifyUserInfo();

	public void publishNote();

	/**
	 * 
	 * @param level
	 */
	public void setLevel(int level);

	public void viewNote();

}