package top.beliefyu.fstream.client.api;

import java.util.Collection;

/**
 * SingleStream
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-12 00:46
 */
public class SingleStream<T> extends DataStream<T> {

    protected SingleStream(DataStream<T> parentDataStream, Collection<DataStream<T>> childDataStreams) {
        super(parentDataStream, childDataStreams);
    }

    protected SingleStream(DataStream<T> parentDataStream, DataStream<T> childDataStream) {
        super(parentDataStream, childDataStream);
    }

}
