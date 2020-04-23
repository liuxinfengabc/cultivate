package bridge;

public abstract class Abstraction
{
	   protected Implementor imple;
	   protected Abstraction(Implementor imple)
	   {
	       this.imple=imple;
	   }
	   public abstract void Operation();   
	}

