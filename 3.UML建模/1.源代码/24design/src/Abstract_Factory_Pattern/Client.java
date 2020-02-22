package Abstract_Factory_Pattern;

import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			EFactory factory;
			Television tv;
			AirConditioner ac;
			System.out.println("请输入工厂名称：");
			Scanner sc = new Scanner(System.in);
			String type = sc.nextLine();
			factory = (EFactory)XMLUtil.getBean(type);
			tv = factory.produceTelevision();
			tv.play();
			ac = factory.produceAirConditioner();
			ac.changeTemperature();
		}catch(Exception e) {
			System.out.println("创建失败！");
		}

	}

}
