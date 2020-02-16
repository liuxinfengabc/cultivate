package Abstract_Factory_Pattern;


/**
 * HairAirConditioner��AirConditioner�����࣬ʵ������AirConditioner�ж����ҵ�񷽷�changeTemperature
 * ()��������
 * @author RHHR1314
 * @version 1.0
 * @created 16-12��-2019 19:34:49
 */
public class HaierAirConditioner implements AirConditioner {

	public HaierAirConditioner(){

	}

	public void finalize() throws Throwable {

	}
	public void changeTemperature(){

		System.out.println("Haier空调温度改变中。。。");
	}
}//end HairAirConditioner

