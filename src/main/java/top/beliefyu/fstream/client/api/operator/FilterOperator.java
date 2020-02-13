package top.beliefyu.fstream.client.api.operator;

import top.beliefyu.fstream.client.api.function.FilterFunction;

/**
 * FilterOperator
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-13 21:21
 */
public class FilterOperator implements DataOperator {
    public <T> FilterOperator(FilterFunction<T> function) {

    }
}
