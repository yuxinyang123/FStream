package top.beliefyu.fstream.client.api;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import top.beliefyu.fstream.client.api.function.SourceFunction;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * DataStream
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-11 21:57
 */
public class DataStream<T> {
    protected DataStream<T> parentDataStream;
    protected Collection<DataStream<T>> childDataStreams;
    protected String name;
    protected String uid;

    protected DataStream(DataStream<T> parentDataStream, Collection<DataStream<T>> childDataStreams) {
        this.parentDataStream = parentDataStream;
        if (this.childDataStreams == null) {
            this.childDataStreams = childDataStreams;
        } else {
            this.childDataStreams.addAll(childDataStreams);
        }
        String uid = UUID.randomUUID().toString();
        this.name = uid;
        this.uid = uid;
    }

    protected DataStream(DataStream<T> parentDataStream, DataStream<T> childDataStream) {
        this(parentDataStream, Lists.newArrayList(childDataStream));
    }

    public  SingleStream<T> addSource(SourceFunction<T> function) {
        return new SingleStream<T>(null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
