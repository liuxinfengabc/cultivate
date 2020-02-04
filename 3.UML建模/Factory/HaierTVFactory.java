package Factory;


/**
 * HaierTVFactory�Ǿ��幤���࣬���ǳ��󹤳���TVFactory�����࣬ʵ���˳��󹤳�����produceTV(),
 * �ڹ��������д���������һ������ľ����Ʒ��
 * @author RHHR1314
 * @version 1.0
 * @created 13-12��-2019 16:44:23
 */
public class HaierTVFactory implements TVFactory {

	public HaierTVFactory(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	public TV produceTV(){
		System.out.println("海尔电视机工厂生产海尔电视机。");
		return new HaierTV();
	}
}//end HaierTVFactory
