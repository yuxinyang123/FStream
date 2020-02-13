package top.beliefyu.fstream.client.api.function;

/**
 * SourceFunction
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-12 02:01
 */
@FunctionalInterface
public interface SourceFunction<OUT> extends UserFunction {
    public OUT get();
}
