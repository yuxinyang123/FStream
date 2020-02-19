package top.beliefyu.fstream.common.grpc;

import io.grpc.Channel;
import top.beliefyu.fstream.rpc.HeartBeatRequest;
import top.beliefyu.fstream.rpc.HeartBeatResponse;
import top.beliefyu.fstream.rpc.MessageRequest;
import top.beliefyu.fstream.rpc.MessageResponse;

import java.util.Iterator;

/**
 * NodeGrpcClient
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-19 03:27
 */
public class NodeGrpcClient extends GrpcClient {

    public NodeGrpcClient(Channel channel) {
        super(channel);
    }

    public NodeGrpcClient(String host, int port) {
        super(host, port);
    }

    public Iterator<MessageResponse> pullMessage() {
        return blockingStub.pullMessage(MessageRequest.newBuilder().build());
    }

    public HeartBeatResponse doHeartBeatTest() {
        return blockingStub.doHeartBeatTest(buildHeartBeatRequest());
    }

    private HeartBeatRequest buildHeartBeatRequest() {
        return HeartBeatRequest.newBuilder()
                .setTimestamp(System.currentTimeMillis())
                .setMsg("ping")
                .build();
    }
}
