package top.beliefyu.fstream.client.api.operator;

import top.beliefyu.fstream.client.api.function.MapperFunction;

/**
 * MapOperator
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-13 00:21
 */
public class MapOperator implements DataOperator {
    public <IN, OUT> MapOperator(MapperFunction<IN, OUT> function) {
    }
}
