package top.beliefyu.fstream.client.api.function;

/**
 * MapperFunction
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-13 00:46
 */
public interface MapperFunction<IN, OUT> {
    public OUT transfer(IN msg);
}
