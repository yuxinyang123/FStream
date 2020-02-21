package top.beliefyu.fstream.common.grpc;

import io.grpc.Channel;
import top.beliefyu.fstream.rpc.DataStreamRequest;
import top.beliefyu.fstream.rpc.DataStreamResponse;

/**
 * ServerGrpcClient
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-19 03:57
 */
public class ServerGrpcClient extends GrpcClient {
    public ServerGrpcClient(Channel channel) {
        super(channel);
    }

    public ServerGrpcClient(String host, int port) {
        super(host, port);
    }

    public DataStreamResponse submitDataStream(DataStreamRequest request) {
        return blockingStub.submitDataStream(request);
    }
}
