package BuilderPattern;

import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		String type = sc.nextLine();
		
		// 动态确认套餐种类
		MealBuilder mb = (MealBuilder)XMLUtil.getBean(type);
		// 服务员是指挥者
		KFCWaiter waiter = new KFCWaiter();
		// 服务员准备套餐
		waiter.setMealBuilder(mb);
		// 客户获得套餐
		Meal meal = waiter.construct();
		
		System.out.println("套餐组成：");
		System.out.println(meal.getFood());
		System.out.println(meal.getDrink());


	}

}
