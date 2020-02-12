package BuilderPattern;


/**
 * ���彨���࣬��������A�ײͣ��ǳ�������������࣬ʵ�����ڳ��������������Ĳ�����װ������
 * @author RHHR1314
 * @version 1.0
 * @created 16-12��-2019 20:31:07
 */
public class SubMealBuilderA extends MealBuilder {

	public SubMealBuilderA(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	public void buildDrink(){

		meal.setDrink("一杯可乐");
	}

	public void buildFood(){

		meal.setFood("一个鸡腿堡");
	}
}//end SubMealBuilderA