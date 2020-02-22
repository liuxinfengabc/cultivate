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
		//返回一个记录有发起人初态的备忘录对象
//		Memento m1= new Memento(state);
		return new Memento(state);
	}
	public String getState(){
		return state;
	}
	/**
	 * 
	 * @param m
	 */
	//还原状态
	public void restoreMemento(Memento m){
		this.setState(m.getState());//m.getState()=S0
	}

	/**
	 * 
	 * @param state
	 */
	public void setState(String state){
		this.state=state;
	}
}//end Originator（发起人）