package bridge;

public class RefinedAbstraction extends Abstraction
{
	  //构造方法
	   protected RefinedAbstraction(Implementor imple)
	   {
	       super(imple);
	   }
	   public void Operation()
	   {
	       System.out.println("扩展抽象化(Refined Abstraction)角色被访问" );
	       //访问实现类中的属性或方法
	       imple.OperationImpl();
	   }
	}
