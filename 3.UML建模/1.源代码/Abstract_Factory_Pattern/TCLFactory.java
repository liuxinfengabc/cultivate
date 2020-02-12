package Abstract_Factory_Pattern;

/**
 * @author RHHR1314
 * @version 1.0
 * @created 16-12��-2019 19:34:49
 */
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