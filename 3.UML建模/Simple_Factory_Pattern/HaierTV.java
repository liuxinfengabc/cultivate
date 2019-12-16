package Simple_Factory_Pattern;


/**
 * HaierTV�ǳ����ƷTV�ӿڵ����࣬����һ�־����Ʒ��ʵ������TV�ӿ��ж����ҵ�񷽷�play()��
 * @author RHHR1314
 * @version 1.0
 * @created 13-12��-2019 15:04:30
 */
public class HaierTV implements TV {

	public HaierTV(){

	}

	public void finalize() throws Throwable {

	}
	public void play(){
		System.out.println("海尔电视机播放中。。。。。。");
		
	}
}//end HaierTV