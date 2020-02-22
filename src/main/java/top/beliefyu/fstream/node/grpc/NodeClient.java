package top.beliefyu.fstream.node.grpc;

import io.grpc.Channel;
import top.beliefyu.fstream.common.grpc.GrpcClient;
import top.beliefyu.fstream.rpc.MessageRequest;
import top.beliefyu.fstream.rpc.MessageResponse;

import java.util.Iterator;

/**
 * NodeClient
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-23 03:06
 */
public class NodeClient extends GrpcClient {
    public NodeClient(Channel channel) {
        super(channel);
    }

    public NodeClient(String host, int port) {
        super(host, port);
    }

    public NodeClient(String hostPort) {
        super(hostPort);
    }

    public Iterator<MessageResponse> pullMessage() {
        return blockingStub.pullMessage(MessageRequest.newBuilder().build());
    }
}
