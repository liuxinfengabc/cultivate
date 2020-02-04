package Abstract_Factory_Pattern;


/**
 * @author RHHR1314
 * @version 1.0
 * @created 16-12��-2019 19:34:49
 */
public class TCLTelevision implements Television {

	public TCLTelevision(){

	}

	public void finalize() throws Throwable {

	}
	public void play(){

		System.out.println("TCL电视机播放中");
	}
}//end TCLTelevision