package FacadePattern;


/**
 * @author RHHR1314
 * @version 1.0
 * @created 30-11��-2019 21:46:51
 */
public class GeneralSwitchFacade {

	private AirConditioner ac;
	private Fan fan;
	private Light[] lights = new Light[4];
	private Television tv;
	public Light m_Light;
	public Fan m_Fan;
	public AirConditioner m_AirConditioner;
	public Television m_Television;



	public void finalize() throws Throwable {

	}
	public GeneralSwitchFacade(){

		lights[0] = new Light("左前");
		lights[1] = new Light("右前");
		lights[2] = new Light("左后");
		lights[3] = new Light("右后");
		fan = new Fan();
		ac = new AirConditioner();
		tv = new Television();
	}

	public void off(){
		lights[0].off();
		lights[1].off();
		lights[2].off();
		lights[3].off();
		fan.off();
		ac.off();
		tv.off();
	}

	public void on(){
		lights[0].on();
		lights[1].on();
		lights[2].on();
		lights[3].on();
		fan.on();
		ac.on();
		tv.on();
	}
}//end GeneralSwitchFacade