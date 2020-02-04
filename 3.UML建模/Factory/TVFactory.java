package Factory;


/**
 * TVFactory是一个抽象类，它可以是一个接口，也可以是一个抽象类，它包含了抽象的工厂方法produce()，返回一个抽象产品TV类型的对象。
 * @author RHHR1314
 * @version 1.0
 * @created 13-12月-2019 16:44:23
 */
public interface TVFactory {

	public TV produceTV();

}