package interpreter;

public class TerminalExpression implements AbstractExpression
{
    public Object interpret(String info)
    {
		return info;
        //对终结符表达式的处理
    }
}
