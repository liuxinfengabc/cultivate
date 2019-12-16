package Simple_Factory_Pattern;


/**
 * HisenseTV�ǳ����ƷTV�ӿڵ���һ�����࣬����һ�־����Ʒ����ͬ�ľ����Ʒ��ʵ��ҵ�񷽷�ʱ������ͬ��
 * @author RHHR1314
 * @version 1.0
 * @created 13-12��-2019 15:04:30
 */
public class HisenseTV implements TV {

	public HisenseTV(){

	}

	public void finalize() throws Throwable {

	}
	public void play(){
		System.out.println("海信电视机播放中。。。。。。");
	}
}//end HisenseTV