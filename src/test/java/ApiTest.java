import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.beliefyu.fstream.client.DataStream;
import top.beliefyu.fstream.client.api.window.SlidingWindows;
import top.beliefyu.fstream.util.SerializableUtil;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ApiTest
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-13 01:30
 */
class ApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTest.class);

    public static DataStream<?> buildDataStream() {
        DataStream<Integer> dataStream = new DataStream<>(() -> 110);
        DataStream<Long> map = dataStream.map(Integer::longValue);
        DataStream<Long> filter = map.filter(i -> i.compareTo(0L) > 0);
        DataStream<Character> flatMap = filter.flatMap((i, c) ->
                c.addAll(i.toString().chars().mapToObj(ii -> (char) ii).collect(Collectors.toList()))
        );

        DataStream<Integer> dataStream2 = new DataStream<>(() -> 111);
        DataStream<Integer> union = dataStream.union(dataStream2, flatMap.map(Integer::valueOf));
        DataStream<String> window = union.window(new SlidingWindows());
        window.setSink(() -> "to sink");
        return dataStream;
    }

    @Test
    void buildTest() {

        DataStream dataStream = buildDataStream();


        System.out.println(dataStream.getTreeHead());
        System.out.println(dataStream.getTreeNode());

        assertAll("testNumbers",
                () -> assertEquals(2, dataStream.getTreeHead().size()),
                () -> assertEquals(9, dataStream.getTreeNode().size())
        );
    }

    @Test
    void serializableTest() {
        DataStream dataStream1 = buildDataStream();
        byte[] bytes = SerializableUtil.toBytes(dataStream1);
        DataStream dataStream2 = SerializableUtil.toObject(bytes);

        assertEquals(dataStream1.getTreeHead().size(), dataStream2.getTreeHead().size());
        assertEquals(dataStream1.getTreeNode().size(), dataStream2.getTreeNode().size());
    }

}
