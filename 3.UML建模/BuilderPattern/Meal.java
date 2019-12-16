package BuilderPattern;


/**
 * �ײ���Meal�Ǹ��Ӳ�Ʒ����������������Ա����food��drink������food��ʾ��ʳ��drink��ʾ���ϣ���Meal�л�������Ա���Ե�Getter������S
 * etter������
 * @author RHHR1314
 * @version 1.0
 * @created 16-12��-2019 20:24:03
 */
public class Meal {

	private String drink;
	private String food;

	public Meal(){

	}

	public void finalize() throws Throwable {

	}
	public String getDrink(){
		return this.drink;
	}

	public String getFood(){
		return this.food;
	}

	/**
	 * 
	 * @param drink
	 */
	public void setDrink(String drink){

		this.drink = drink;
	}

	/**
	 * 
	 * @param food
	 */
	public void setFood(String food){

		this.food = food;
	}
}//end Meal