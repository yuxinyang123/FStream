package top.beliefyu.fstream.client.api.function;

import java.util.Collection;

/**
 * FlatMapFunctioon
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-13 16:01
 */
@FunctionalInterface
public interface FlatMapFunction<IN, OUT> extends UserDefineFunction {
    public void transfer(IN msg, Collection<OUT> collects);
}
