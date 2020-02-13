package top.beliefyu.fstream.client.api.operator;

import top.beliefyu.fstream.client.api.function.FlatMapFunction;

/**
 * FlatMapOperator
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-13 16:27
 */
public class FlatMapOperator implements DataOperator {
    public <IN, OUTS> FlatMapOperator(FlatMapFunction<IN, OUTS> flatMapFunction) {

    }
}
