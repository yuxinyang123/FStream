package top.beliefyu.fstream.client.api;

import top.beliefyu.fstream.client.api.function.SourceFunction;
import top.beliefyu.fstream.client.api.operator.DataOperator;
import top.beliefyu.fstream.client.api.operator.SourceOperator;

import java.util.Collection;
import java.util.Collections;

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
     * @date 2020-02-12 22:25
     * @author yuxinyang
     */
    private static Collection<? super DataStream> TREE_HEAD = Collections.emptySet();


    private Collection<? super DataOperator> parentOperator = Collections.emptySet();
    private DataOperator operator;
    private Collection<? super DataOperator> childOperator = Collections.emptySet();

    private DataStream() {
    }

    public <OUT> DataStream(SourceFunction<OUT> function) {
        setSource(function);
    }

    private <OUT> void setSource(SourceFunction<OUT> function) {
        parentOperator = null;
        operator = new SourceOperator<>();
        TREE_HEAD.add(this);
    }

    public class Test extends DataStream{

    }

    public void start() {

    }

}
