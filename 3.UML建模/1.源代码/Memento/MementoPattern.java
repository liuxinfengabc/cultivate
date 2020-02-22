package Memento;
/**
 * @author Lenovo
 * @version 1.0
 * @created 09-2月-2020 17:51:26
 */
public class MementoPattern {
	public MementoPattern(){
	}
	public static void main(String[] args) {
		Originator or = new Originator();
		Caretaker cr= new Caretaker();
		//设置发起人or这个对象的初态为S0
		or.setState("S0");
		System.out.println("初始状态："+or.getState());
		//保存状态
		//or.createMemento() 记录有s0状态的备忘录对象
		cr.setMemento(or.createMemento());
		//保存初始态
		or.setState("S1");
		System.out.println("新的状态："+or.getState());
		//恢复状态
		or.restoreMemento(cr.getMemento());
		//or的状态又变为S0
		System.out.print("恢复状态："+or.getState());
	}
}//end MementoPattern