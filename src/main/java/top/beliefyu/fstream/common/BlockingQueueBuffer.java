package top.beliefyu.fstream.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * BlockingQueueBuffer
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-17 23:15
 */
public class BlockingQueueBuffer<T> implements Buffer<T> {

    private static final Logger logger = LoggerFactory.getLogger(BlockingQueueBuffer.class);

    private final BlockingQueue<T> queue;

    public BlockingQueueBuffer(int capacity) {
        queue = new ArrayBlockingQueue<>(capacity);
    }

    @Override
    public T get() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            logger.error("something wrong!", e);
            return null;
        }
    }

    @Override
    public List<T> getAll() {
        List<T> result = new ArrayList<>();
        queue.drainTo(result);
        return result;
    }

    @Override
    public void add(T item) {
        try {
            queue.put(item);
        } catch (InterruptedException e) {
            logger.error("something wrong!", e);
        }
    }

    @Override
    public void clear() {
        queue.clear();
    }
}
