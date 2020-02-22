package Memento;


/**
 * @author Lenovo
 * @version 1.0
 * @created 09-2月-2020 17:51:26
 */
public class Caretaker {
	private Memento memento;
//	public Memento m_Memento;
	public Caretaker(){

	}
	public void finalize() throws Throwable {

	}
	public Memento getMemento(){
		return memento;
	}
	/**
	 * 
	 * @param m
	 */
	public void setMemento(Memento m){
		memento=m;
	}
}//end Caretaker(管理者)