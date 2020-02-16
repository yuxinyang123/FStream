package top.beliefyu.fstream.common.grpc;

import io.grpc.Channel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import org.apache.log4j.Logger;
import top.beliefyu.fstream.rpc.*;

import java.util.Iterator;


/**
 * GrpcClient
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-17 01:18
 */
public class GrpcClient {
    private static final Logger logger = Logger.getLogger(GrpcClient.class);

    private final RpcServerGrpc.RpcServerBlockingStub blockingStub;
    private final RpcServerGrpc.RpcServerStub asyncStub;


    public GrpcClient(Channel channel) {
        blockingStub = RpcServerGrpc.newBlockingStub(channel);
        asyncStub = RpcServerGrpc.newStub(channel);
    }

    public GrpcClient(String host, int port) {
        this(NettyChannelBuilder.forAddress(host, port)
                .negotiationType(NegotiationType.PLAINTEXT)
                .build());
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
