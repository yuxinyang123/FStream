package top.beliefyu.fstream.client.api.function;

/**
 * FilterFunction
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-13 19:16
 */
public interface FilterFunction<T> extends UserDefineFunction {
    public boolean transfer(T msg);
}
