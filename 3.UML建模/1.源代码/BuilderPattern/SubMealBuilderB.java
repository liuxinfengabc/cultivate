package BuilderPattern;


/**
 * @author RHHR1314
 * @version 1.0
 * @created 16-12��-2019 20:31:07
 */
public class SubMealBuilderB extends MealBuilder {

	public SubMealBuilderB(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	public void buildDrink(){
		meal.setDrink("一杯果汁");
	}

	public void buildFood(){
		meal.setFood("一杯鸡肉卷");
	}
}//end SubMealBuilderB