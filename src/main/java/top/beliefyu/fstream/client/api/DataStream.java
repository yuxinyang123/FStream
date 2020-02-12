package top.beliefyu.fstream.client.api;

import top.beliefyu.fstream.client.api.function.MapperFunction;
import top.beliefyu.fstream.client.api.function.SourceFunction;
import top.beliefyu.fstream.client.api.operator.DataOperator;
import top.beliefyu.fstream.client.api.operator.MapOperator;
import top.beliefyu.fstream.client.api.operator.SourceOperator;

import java.util.Collection;
import java.util.HashSet;

/**
 * DataStream
 * 交互用api
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-11 21:57
 */
public class DataStream<T> {
    /**
     * DAG的头结点集合
     */
    private static Collection<? super DataStream> TREE_HEAD = new HashSet<>();


    private Collection<? super DataOperator> parentOperator = new HashSet<>();
    private DataOperator operator;
    private Collection<? super DataOperator> childOperator = new HashSet<>();

    private DataStream() {
    }

    public DataStream(SourceFunction<T> function) {
        setSource(function);
    }


    public <OUT> DataStream<OUT> map(MapperFunction<T, OUT> function) {
        DataStream<OUT> nextDataStream = new DataStream<>();
        nextDataStream.parentOperator.add(this.operator);
        nextDataStream.operator = new MapOperator(function);
        buildNextOperator(nextDataStream.operator);
        return nextDataStream;
    }

    private <OUT> void setSource(SourceFunction<OUT> function) {
        parentOperator = null;
        operator = new SourceOperator(function);
        TREE_HEAD.add(this);
    }

    private void buildNextOperator(DataOperator operator) {
        childOperator.add(operator);
    }

    public void start() {

    }

}
