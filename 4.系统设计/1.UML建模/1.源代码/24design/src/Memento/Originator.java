package Memento;


/**
 * @author Lenovo
 * @version 1.0
 * @created 09-2月-2020 17:51:26
 */
public class Originator {

	private String state;
	public Memento m_Memento;

	public Originator(){

	}

	public void finalize() throws Throwable {

	}
	public Memento createMemento(){
		return null;
	}

	public String getState(){
		return "";
	}

	/**
	 * 
	 * @param m
	 */
	public void restoreMemento(Memento m){

	}

	/**
	 * 
	 * @param state
	 */
	public void setState(String state){

	}
}//end Originator（发起人）