package Abstract_Factory_Pattern;



public class HaierAirConditioner implements AirConditioner {

	public HaierAirConditioner(){

	}

	public void finalize() throws Throwable {

	}
	public void changeTemperature(){

		System.out.println("Haier空调创建成功");
	}
}//end HairAirConditioner

