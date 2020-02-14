package top.beliefyu.fstream.client.api.operator;

import top.beliefyu.fstream.client.api.DataStream;

import java.util.List;

/**
 * UnionOperator
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-14 22:26
 */
public class UnionOperator implements DataOperator {
    public <T> UnionOperator(List<DataStream<? super T>> dataStreams) {

    }
}
