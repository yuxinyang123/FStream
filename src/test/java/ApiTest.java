import org.junit.jupiter.api.Test;
import top.beliefyu.fstream.client.api.DataStream;

/**
 * ApiTest
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-13 01:30
 */
class ApiTest {

    @Test
    void buildTest(){
        DataStream<Integer> dataStream = new DataStream<>(() -> 110);
        DataStream<Long> map = dataStream.map(Integer::longValue);
        System.out.println(map);
    }

}
