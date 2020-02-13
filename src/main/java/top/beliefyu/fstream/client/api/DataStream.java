package top.beliefyu.fstream.client.api;

import top.beliefyu.fstream.client.api.function.FilterFunction;
import top.beliefyu.fstream.client.api.function.FlatMapFunction;
import top.beliefyu.fstream.client.api.function.MapFunction;
import top.beliefyu.fstream.client.api.function.SourceFunction;
import top.beliefyu.fstream.client.api.operator.*;

import java.util.Arrays;
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
     * DAG的头引用集合
     */
    private static Collection<? super DataStream> TREE_HEAD = new HashSet<>();

    /**
     * DAG所有流引用
     */
    private static Collection<? super DataStream> TREE_NODE = new HashSet<>();

    /**
     * DAG算子节点
     */
    private Collection<? super DataOperator> parentOperator = new HashSet<>();
    private DataOperator operator;
    private Collection<? super DataOperator> childOperator = new HashSet<>();

    /**
     * 判断是否为多输出流，输出算子为keyBy等
     */
    private boolean isMultipleOutput;

    private DataStream() {
    }

    public DataStream(SourceFunction<T> function) {
        setSource(function);
    }

    private <OUT> void setSource(SourceFunction<OUT> function) {
        parentOperator = null;
        operator = new SourceOperator(function);
        TREE_HEAD.add(this);
        TREE_NODE.add(this);
    }

    public <OUT> DataStream<OUT> map(MapFunction<T, OUT> function) {
        return buildNextDataStream(new MapOperator(function));
    }

    public <OUT> DataStream<OUT> flatMap(FlatMapFunction<T, OUT> function) {
        return buildNextDataStream(new FlatMapOperator(function));
    }

    public DataStream<T> filter(FilterFunction<T> function) {
        return buildNextDataStream(new FilterOperator(function));
    }

    public DataStream<T> keyBy(String... name) {
        DataStream<T> outDataStream = buildNextDataStream(new KeyByOperator(Arrays.asList(name)));
        outDataStream.isMultipleOutput = true;
        return outDataStream;
    }

    public DataStream<T> window

    private <OUT> DataStream<OUT> buildNextDataStream(DataOperator operator) {
        DataStream<OUT> nextDataStream = new DataStream<>();
        nextDataStream.parentOperator.add(this.operator);
        nextDataStream.operator = operator;

        childOperator.add(operator);
        TREE_NODE.add(this);
        return nextDataStream;
    }


    public void start() {

    }

}
