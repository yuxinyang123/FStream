package top.beliefyu.fstream.common.grpc;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.beliefyu.fstream.client.api.DataStream;
import top.beliefyu.fstream.rpc.DataStreamRequest;
import top.beliefyu.fstream.rpc.DataStreamResponse;
import top.beliefyu.fstream.rpc.RpcServerGrpc;
import top.beliefyu.fstream.util.SerializableUtil;

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
    public void submitDataStreamBytes(DataStreamRequest request, StreamObserver<DataStreamResponse> responseObserver) {
        pushDataStream(request);
        responseObserver.onNext(buildDataStreamResponse("success"));
        responseObserver.onCompleted();
        LOGGER.debug("submit success,[{}]",
                SerializableUtil.<DataStream>toObject(request.getDataStreamBytes().toByteArray()).getName());
    }

    private DataStreamResponse buildDataStreamResponse(String msg) {
        return DataStreamResponse.newBuilder().setMsg(msg).setTimestamp(System.currentTimeMillis()).build();
    }

    private void pushDataStream(DataStreamRequest request) {
        //todo 传递或处理DataStream对象
    }
}
