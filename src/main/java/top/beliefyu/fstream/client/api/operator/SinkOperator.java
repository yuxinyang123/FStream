package top.beliefyu.fstream.client.api.operator;

import top.beliefyu.fstream.client.api.function.SinkFunction;

/**
 * SinkOperator
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-14 22:01
 */
public class SinkOperator implements DataOperator {
    public <T> SinkOperator(SinkFunction<T> function) {

    }
}
