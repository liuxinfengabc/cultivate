package Simple_Factory_Pattern;


/**
 * TVFactory�ǹ����࣬��������ϵͳ�ĺ��ģ����ṩ�˾�̬��������produceTV()�����������а���һ���ַ������͵Ĳ��������ڲ�ҵ���߼��и��ݲ���ֵ�Ĳ�ͬ
 * ʵ������ͬ�ľ����Ʒ�࣬������Ӧ�Ķ���
 * @author RHHR1314
 * @version 1.0
 * @created 13-12��-2019 15:04:30
 */
public class TVFactory {

	public TVFactory(){

	}

	public void finalize() throws Throwable {

	}
	public void play(){

	}

	/**
	 * 
	 * @param brand
	 */
	public static TV produceTV(String brand)throws Exception{
		if(brand.equalsIgnoreCase("Haier")) {
			System.out.println("电视机工厂生产海尔电视机");
			return new HaierTV();
		}
		else if(brand.equalsIgnoreCase("Hisense")) {
			System.out.println("电视机工厂生产海信电视机");
			return new HisenseTV();
		}else {
			throw new Exception("对不起，咱不能生产该品牌电视机！");
		}

	}
}//end TVFactory