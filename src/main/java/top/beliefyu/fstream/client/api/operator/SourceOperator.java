package top.beliefyu.fstream.client.api.operator;

import top.beliefyu.fstream.client.api.function.SourceFunction;

/**
 * SourceOperator
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-12 21:29
 */
public class SourceOperator implements DataOperator {
    public <OUT> SourceOperator(SourceFunction<OUT> function) {

    }
}
