package top.beliefyu.fstream.client.api;

import top.beliefyu.fstream.client.api.function.*;
import top.beliefyu.fstream.client.api.operator.*;
import top.beliefyu.fstream.client.api.window.WindowAssigner;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static java.util.Arrays.asList;

/**
 * DataStream
 * 交互用api
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-11 21:57
 */
public class DataStream<T> implements Serializable {
    /**
     * DAG的头引用集合
     */
    private static final Collection<? super DataStream> TREE_HEAD = new HashSet<>();

    /**
     * DAG所有流引用
     */
    private static final Collection<? super DataStream> TREE_NODE = new HashSet<>();

    /**
     * DAG算子节点
     */
    private Collection<? super DataOperator> parentOperator = new HashSet<>();
    private DataOperator operator;
    private Collection<? super DataOperator> childOperator = new HashSet<>();

    private final String uid = UUID.randomUUID().toString();
    private String name = uid;

    /**
     * 判断是否为多输出流，输出算子为keyBy等
     */
    private boolean isMultipleOutput;
    /**
     * 判断是否为多输入流，输出算子为union等
     */
    private boolean isMultipleInput;

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

    public DataStream<T> keyBy(String... names) {
        DataStream<T> outDataStream = buildNextDataStream(new KeyByOperator(asList(names)));
        outDataStream.isMultipleOutput = true;
        return outDataStream;
    }

    @SafeVarargs
    public final DataStream<T> union(DataStream<? super T>... dataStreams) {
        DataStream<T> outDataStream = buildNextDataStream(new UnionOperator(asList(dataStreams)));
        outDataStream.isMultipleInput = true;
        return outDataStream;
    }

    public <OUT> DataStream<OUT> window(WindowAssigner assigner) {
        return buildNextDataStream(new WindowOperator(assigner));
    }

    public void setSink(SinkFunction<T> sink) {
        DataStream<T> sinkStream = buildNextDataStream(new SinkOperator(sink));
        sinkStream.childOperator = null;
    }

    private <OUT> DataStream<OUT> buildNextDataStream(DataOperator operator) {
        DataStream<OUT> nextDataStream = new DataStream<>();
        nextDataStream.parentOperator.add(this.operator);
        nextDataStream.operator = operator;

        childOperator.add(operator);
        TREE_NODE.add(nextDataStream);
        return nextDataStream;
    }

    public DataStream<T> name(String name) {
        this.name = name;
        return this;
    }

    public void submit() {

    }

    public static Collection<? super DataStream> getTreeHead() {
        return TREE_HEAD;
    }

    public static Collection<? super DataStream> getTreeNode() {
        return TREE_NODE;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void clear() {
        TREE_HEAD.clear();
        TREE_NODE.clear();
    }


    @Override
    public String toString() {
        if (name.equals(uid)) {
            return String.format("%s@-%s", uid.substring(0, 8),
                    operator.getClass().getSimpleName().split("Operator")[0]);
        } else {
            return String.format("%s-%s@-%s", name, uid.substring(0, 8), operator.getClass().getSimpleName().split("Operator")[0]);
        }
    }


}

