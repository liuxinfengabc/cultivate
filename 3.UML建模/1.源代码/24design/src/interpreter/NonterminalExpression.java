package interpreter;

public class NonterminalExpression implements AbstractExpression
{
    private AbstractExpression exp1;
    private AbstractExpression exp2;
    public Object interpret(String info)
    {
		return info;
        //非对终结符表达式的处理
    }
}

