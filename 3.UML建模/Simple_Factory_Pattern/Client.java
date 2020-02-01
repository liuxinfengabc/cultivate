package Simple_Factory_Pattern;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			TV tv;
			String brandName = XMLUtilTV.getBrandName();
			tv = TVFactory.produceTV(brandName);
			tv.play();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
