package top.beliefyu.fstream.server.grpc;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.beliefyu.fstream.client.DataStream;
import top.beliefyu.fstream.rpc.DataStreamRequest;
import top.beliefyu.fstream.rpc.DataStreamResponse;
import top.beliefyu.fstream.rpc.RpcServerGrpc;
import top.beliefyu.fstream.server.Server;
import top.beliefyu.fstream.server.service.ServerService;
import top.beliefyu.fstream.util.SerializableUtil;

import java.util.List;

import static top.beliefyu.fstream.util.SerializableUtil.toObject;

/**
 * ClientGrpcService
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-19 02:43
 */
public class ServerGrpcService extends RpcServerGrpc.RpcServerImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerGrpcService.class);

    @Override
    public void submitDataStream(DataStreamRequest request, StreamObserver<DataStreamResponse> responseObserver) {
        responseObserver.onNext(buildDataStreamResponse("success"));
        responseObserver.onCompleted();
        pushDataStream(request);
        LOGGER.debug("submit success,[{}]",
                SerializableUtil.<DataStream>toObject(request.getDataStreamBytes().toByteArray()).getName());
    }

    private DataStreamResponse buildDataStreamResponse(String msg) {
        return DataStreamResponse.newBuilder().setMsg(msg).setTimestamp(System.currentTimeMillis()).build();
    }

    private void pushDataStream(DataStreamRequest request) {
        ServerService serverService = Server.getServerService();
        List<ServerService.PhysicsExecution> physicsExecutions = serverService.generatePhysicsExecution(toObject(request.getDataStreamBytes().toByteArray()));
        serverService.distributePhysicsExecution(physicsExecutions);
    }
}
