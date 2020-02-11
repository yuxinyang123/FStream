package top.beliefyu.fstream.client.api;

import java.util.Collection;

/**
 * OneToManyStream
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-12 00:49
 */
public abstract class OneToManyStream<T> extends DataStream<T> {

    protected OneToManyStream(DataStream<T> parentDataStream, Collection<DataStream<T>> childDataStreams) {
        super(parentDataStream, childDataStreams);
    }

    protected OneToManyStream(DataStream<T> parentDataStream, DataStream<T> childDataStream) {
        super(parentDataStream, childDataStream);
    }
}
