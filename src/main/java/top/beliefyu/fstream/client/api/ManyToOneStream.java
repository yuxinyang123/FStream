package top.beliefyu.fstream.client.api;

import java.util.Collection;

/**
 * ManyToOneStream
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-12 00:50
 */
public class ManyToOneStream<T> extends DataStream<T> {
    protected ManyToOneStream(DataStream<T> parentDataStream, Collection<DataStream<T>> childDataStreams) {
        super(parentDataStream, childDataStreams);
    }

    protected ManyToOneStream(DataStream<T> parentDataStream, DataStream<T> childDataStream) {
        super(parentDataStream, childDataStream);
    }
}
