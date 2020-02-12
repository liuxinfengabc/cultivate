package BuilderPattern;


/**
 * KFCWaiter����ָ�����࣬��KFC�ײ����������У�������KFC�ķ���Ա�������ж�����һ�������������͵ı���mb�����彨���������ɿͻ���ָ��������cons
 * truct���������е���mb����Ĳ�����װ�����͹���������������ͻ��˷���һ�ݰ�����ʳ�����ϵ������ײ͡�
 * @author RHHR1314
 * @version 1.0
 * @created 16-12��-2019 20:31:07
 */
public class KFCWaiter {

	private MealBuilder mb;
	public MealBuilder m_MealBuilder;

	public KFCWaiter(){

	}

	public void finalize() throws Throwable {

	}
	public Meal construct(){
		mb.buildDrink();
		mb.buildFood();
		return mb.getMeal();
	}

	/**
	 * 
	 * @param mb
	 */
	public void setMealBuilder(MealBuilder mb){

		this.mb = mb;
	}
}//end KFCWaiter