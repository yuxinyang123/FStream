package top.beliefyu.fstream.common.grpc;

import io.grpc.Channel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import top.beliefyu.fstream.rpc.RpcServerGrpc;


/**
 * GrpcClient
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-17 01:18
 */
public abstract class GrpcClient {

    protected final RpcServerGrpc.RpcServerBlockingStub blockingStub;

    public GrpcClient(Channel channel) {
        blockingStub = RpcServerGrpc.newBlockingStub(channel);
    }

    public GrpcClient(String host, int port) {
        this(NettyChannelBuilder.forAddress(host, port)
                .negotiationType(NegotiationType.PLAINTEXT)
                .build());
    }


}
