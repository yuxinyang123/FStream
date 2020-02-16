import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import top.beliefyu.fstream.common.grpc.GrpcClient;
import top.beliefyu.fstream.common.grpc.GrpcServer;

import java.io.IOException;

/**
 * GrpcTest
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-17 03:03
 */
public class GrpcTest {
    @Order(1)
    @Test
    void startServer() throws IOException {
        GrpcServer grpcServer = new GrpcServer(6666);
        grpcServer.start();
    }

    @Order(2)
    @Test
    void startClient() {
        GrpcClient client = new GrpcClient("localhost", 6666);
        System.out.println(client.doHeartBeatTest().getMsg());
    }
}
