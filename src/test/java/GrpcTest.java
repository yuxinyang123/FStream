import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;
import top.beliefyu.fstream.client.api.DataStream;
import top.beliefyu.fstream.common.grpc.*;
import top.beliefyu.fstream.rpc.DataStreamRequest;
import top.beliefyu.fstream.rpc.DataStreamResponse;

import java.io.IOException;

/**
 * GrpcTest
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-17 03:03
 */
public class GrpcTest {

    @Test
    void startServerTest() throws IOException {
        GrpcServer grpcServer = new GrpcServer(6666, new NodeGrpcService());
        grpcServer.start();
        startClient();
    }


    private void startClient() {
        NodeGrpcClient client = new NodeGrpcClient("localhost", 6666);
        System.out.println(client.doHeartBeatTest().getMsg());
    }

    @Test
    void startServerServiceTest() throws IOException {
        GrpcServer grpcServer = new GrpcServer(6667, new ServerGrpcService());
        grpcServer.start();
        testDataStreamRpc();
    }

    void testDataStreamRpc() {
        DataStream dataStream = ApiTest.buildDataStream();
        System.out.println(dataStream.getName());
        ServerGrpcClient client = new ServerGrpcClient("localhost", 6667);
        DataStreamResponse dataStreamResponse = client.submitDataStreamBytes(DataStreamRequest.newBuilder()
                .setTimestamp(System.nanoTime())
                .setDataStreamBytes(ByteString.copyFrom(dataStream.toByteArray()))
                .build()
        );
    }
}
