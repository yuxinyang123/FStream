package top.beliefyu.fstream.client.api.function;

/**
 * SinkFunction
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-14 21:59
 */
@FunctionalInterface
public interface SinkFunction<T> extends UserDefineFunction {
    public T toSink();
}
