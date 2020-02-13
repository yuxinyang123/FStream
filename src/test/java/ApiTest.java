import org.junit.jupiter.api.Test;
import top.beliefyu.fstream.client.api.DataStream;

import java.util.stream.Collectors;

/**
 * ApiTest
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-13 01:30
 */
class ApiTest {

    @Test
    void buildTest() {
        DataStream<Integer> dataStream = new DataStream<>(() -> 110);
        DataStream<Long> map = dataStream.map(Integer::longValue);
        DataStream<Long> filter = map.filter(i -> i.compareTo(0L) > 0);
        DataStream<Character> flatMap = filter.flatMap((i, c) ->
                c.addAll(i.toString().chars().mapToObj(ii -> (char) ii).collect(Collectors.toList()))
        );
        System.out.println(flatMap);
    }

}
