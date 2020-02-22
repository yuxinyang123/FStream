package top.beliefyu.fstream.common.grpc;

import io.grpc.Channel;
import top.beliefyu.fstream.client.api.DataStream;
import top.beliefyu.fstream.rpc.DataStreamRequest;
import top.beliefyu.fstream.rpc.DataStreamResponse;

import static com.google.protobuf.ByteString.copyFrom;
import static top.beliefyu.fstream.util.SerializableUtil.toBytes;

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

    public ServerGrpcClient(String hostPort) {
        super(hostPort);
    }

    public DataStreamResponse submitDataStream(DataStream dataStream) {
        return blockingStub.submitDataStream(DataStreamRequest.newBuilder()
                .setTimestamp(System.nanoTime())
                .setDataStreamBytes(copyFrom(toBytes(dataStream)))
                .build()
        );
    }
}
