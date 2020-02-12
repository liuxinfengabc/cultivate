package Factory;


/**
 * HaierTV�ǳ����ƷTV�ӿڵ����࣬����һ�־����Ʒ��ʵ������TV�ӿ��ж����ҵ�񷽷�play()��
 * @author RHHR1314
 * @version 1.0
 * @created 13-12��-2019 16:44:23
 */
public class HaierTV implements TV {

	public HaierTV(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	public void play(){

		System.out.println("海尔电视机播放中。。。。。");
	}
}//end HaierTV