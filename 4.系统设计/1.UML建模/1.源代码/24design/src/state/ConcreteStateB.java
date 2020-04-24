package state;

public class ConcreteStateB extends State
{
    public void Handle(Context context)
    {
        System.out.println("µ±Ç°×´Ì¬ÊÇ B.");
        context.setState(new ConcreteStateA());
    }
}

