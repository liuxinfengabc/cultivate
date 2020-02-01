package Abstract_Factory_Pattern;


/**
 * HaierFactory��EFactoy��һ�����࣬ʵ������EFactory�ж���Ĺ������������ڴ��������Ʒ����HaierFactory�������ľ����Ʒ����
 * ��һ����Ʒ�顣
 * @author RHHR1314
 * @version 1.0
 * @created 16-12��-2019 19:34:49
 */
public class HaierFactory implements EFactory {

	public HaierFactory(){

	}

	public void finalize() throws Throwable {

	}
	public AirConditioner produceAirConditioner(){
		
		return new HaierAirConditioner();
	}

	public Television produceTelevision(){
		
		return new HaierTelevision();
	}
}//end HaierFactory