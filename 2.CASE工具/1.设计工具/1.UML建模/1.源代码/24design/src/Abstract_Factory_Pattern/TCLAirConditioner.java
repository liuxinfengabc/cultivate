package Abstract_Factory_Pattern;



public class TCLAirConditioner implements AirConditioner {

	public TCLAirConditioner(){

	}

	public void finalize() throws Throwable {

	}
	public void changeTemperature(){

		System.out.println("TCL空调创建成功");
	}
}//end TCLAirConditioner