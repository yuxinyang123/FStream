package top.beliefyu.fstream.server.grpc;

import io.grpc.Channel;
import top.beliefyu.fstream.common.grpc.GrpcClient;
import top.beliefyu.fstream.rpc.*;
import top.beliefyu.fstream.server.service.ServerService.PhysicsExecution;

import static com.google.protobuf.ByteString.copyFrom;
import static top.beliefyu.fstream.util.SerializableUtil.toBytes;

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

    public NodeGrpcClient(String host) {
        super(host);
    }


    public HeartBeatResponse doHeartBeatTest() {
        return blockingStub.doHeartBeatTest(HeartBeatRequest.newBuilder()
                .setTimestamp(System.currentTimeMillis())
                .setMsg("ping")
                .build());
    }


    public PhysicsExecutionResponse submitPhysicsExecution(PhysicsExecution physicsExecution) {
        return blockingStub.submitPhysicsExecution(
                PhysicsExecutionRequest.newBuilder()
                        .setTimestamp(System.nanoTime())
                        .setPhysicsExecution(copyFrom(toBytes(physicsExecution)))
                        .build()
        );
    }
}
