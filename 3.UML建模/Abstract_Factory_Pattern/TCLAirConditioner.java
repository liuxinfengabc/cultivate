package Abstract_Factory_Pattern;


/**
 * ��AirConditioner�����࣬ʵ���˸��෽��changeTemperature(),
 * AirConditioner��TCLAirConditioner��HaierAirConditioner����һ����Ʒ�ȼ��ṹ��
 * @author RHHR1314
 * @version 1.0
 * @created 16-12��-2019 19:34:49
 */
public class TCLAirConditioner implements AirConditioner {

	public TCLAirConditioner(){

	}

	public void finalize() throws Throwable {

	}
	public void changeTemperature(){

		System.out.println("TCL空调温度改变中。。。。");
	}
}//end TCLAirConditioner