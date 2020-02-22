package top.beliefyu.fstream.common.grpc;

import io.grpc.Channel;
import top.beliefyu.fstream.rpc.*;
import top.beliefyu.fstream.server.ServerService.PhysicsExecution;

import java.util.Iterator;

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

    public NodeGrpcClient(String host) {
        super(host.split(":")[0], Integer.parseInt(host.split(":")[1]));
    }

    public Iterator<MessageResponse> pullMessage() {
        return blockingStub.pullMessage(MessageRequest.newBuilder().build());
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
