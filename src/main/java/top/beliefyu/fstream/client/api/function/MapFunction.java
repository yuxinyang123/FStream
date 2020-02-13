package top.beliefyu.fstream.client.api.function;

/**
 * MapFunction
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-13 00:46
 */
@FunctionalInterface
public interface MapFunction<IN, OUT> extends UserFunction {
    public OUT transfer(IN msg);
}
