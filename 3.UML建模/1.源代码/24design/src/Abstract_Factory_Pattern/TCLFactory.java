package Abstract_Factory_Pattern;


public class TCLFactory implements EFactory {

	public TCLFactory() {

	}

	public void finalize() throws Throwable {

	}

	public AirConditioner produceAirConditioner() {

		return new TCLAirConditioner();
	}

	public Television produceTelevision() {

		return new TCLTelevision();
	}
}// end TCLFactory