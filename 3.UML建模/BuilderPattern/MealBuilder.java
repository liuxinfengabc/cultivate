package BuilderPattern;


/**
 * �ײͽ����ߣ���һ�������࣬�����˳���Ĳ�����װ����buildFood������builderDrink��������MealBuilder�ж�����Meal���͵Ķ���mea
 * l���ṩ�˹�������getMeal�������ڷ��ض���
 * @author RHHR1314
 * @version 1.0
 * @created 16-12��-2019 20:31:07
 */
public abstract class MealBuilder {

	protected Meal meal = new Meal();
	public Meal m_Meal;

	public MealBuilder(){

	}

	public void finalize() throws Throwable {

	}
	public abstract void buildDrink();

	public abstract void buildFood();

	public Meal getMeal(){
		return meal;
	}
}//end MealBuilder