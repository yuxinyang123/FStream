package top.beliefyu.fstream.common;

import java.util.List;

/**
 * Buffer
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-17 16:12
 */
public interface Buffer<T> {

    public T get();

    public List<T> getAll();

    public void add(T item);

    public void clear();

}
