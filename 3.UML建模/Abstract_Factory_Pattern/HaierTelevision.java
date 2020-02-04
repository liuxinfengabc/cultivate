package Abstract_Factory_Pattern;


/**
 * HaierTelevision��Television�����࣬ʵ������Television�ж����ҵ�񷽷�play()��
 * @author RHHR1314
 * @version 1.0
 * @created 16-12��-2019 19:34:49
 */
public class HaierTelevision implements Television {

	public HaierTelevision(){

	}

	public void finalize() throws Throwable {

	}
	public void play(){

		System.out.println("Haier电视机播放中");
	}
}//end HaierTelevision