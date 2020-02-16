package Memento;
/**
 * @author Lenovo
 * @version 1.0
 * @created 09-2ÔÂ-2020 17:51:26
 */
public class Memento {
	private String state;
	public Memento(){
	}
	public void finalize() throws Throwable {
	}
	public String getState(){
		return state;
	}
	/**
	 * 
	 * @param state
	 */
	public Memento(String state){
		this.state=state;
	}
	/**
	 * 
	 * @param state
	 */
	public void setState(String state){
		this.state=state;
	}
}//end Memento