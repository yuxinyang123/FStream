package top.beliefyu.fstream.common.grpc;

import io.grpc.stub.StreamObserver;
import top.beliefyu.fstream.rpc.*;

/**
 * GrpcServer
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-17 01:17
 */
public class GrpcServer extends RpcServerGrpc.RpcServerImplBase {
    @Override
    public void pullMessage(MessageRequest request, StreamObserver<MessageResponse> responseObserver) {
        responseObserver.onNext(streamToMessage());
        responseObserver.onCompleted();
    }

    private MessageResponse streamToMessage() {
        //todo 流数据获取与转换
        return null;
    }


    @Override
    public void doHeartBeatTest(HeartBeatRequest request, StreamObserver<HeartBeatResponse> responseObserver) {
        responseObserver.onNext(buildHeartBeatResponse());
        responseObserver.onCompleted();
    }

    private HeartBeatResponse buildHeartBeatResponse() {
        return HeartBeatResponse.newBuilder()
                .setTimestamp(System.currentTimeMillis())
                .setMsg("pong")
                .build();
    }
}
