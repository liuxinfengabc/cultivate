package state;

public class ConcreteStateA extends State
{
    public void Handle(Context context)
    {
        System.out.println("µ±Ç°×´Ì¬ÊÇ A.");
        context.setState(new ConcreteStateB());
    }
}
