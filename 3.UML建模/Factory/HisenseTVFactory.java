package Factory;


/**
 * return new HisenseTV();
 * @author RHHR1314
 * @version 1.0
 * @created 13-12��-2019 16:44:23
 */
public class HisenseTVFactory implements TVFactory {

	public HisenseTVFactory(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	public TV produceTV(){
		System.out.println("海信电视机工厂生产海信电视机。");
		return new HisenseTV();
	}
}//end HisenseTVFactory