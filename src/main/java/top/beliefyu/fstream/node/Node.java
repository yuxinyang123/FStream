package top.beliefyu.fstream.node;

import top.beliefyu.fstream.common.grpc.GrpcServer;
import top.beliefyu.fstream.common.zk.ZkClient;
import top.beliefyu.fstream.node.grpc.NodeGrpcService;
import top.beliefyu.fstream.node.service.NodeService;

import java.util.UUID;

/**
 * Node
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-23 03:02
 */
public class Node {

    private static final String uid = UUID.randomUUID().toString();
    private static ZkClient zkClient;
    private static GrpcServer grpcServer;
    private static NodeService nodeService = new NodeService();

    public static void main(String[] args) {
        init();
        registerInZk();
        grpcServerStartAndBlockUntilShutdown();
    }

    private static void init() {
        zkClient = new ZkClient("192.168.2.129", 2181);
        grpcServer = new GrpcServer(6666, new NodeGrpcService());
        zkClient.start();
    }

    private static void registerInZk() {
        nodeService.register(zkClient, uid);
    }

    private static void grpcServerStartAndBlockUntilShutdown() {
        try {
            grpcServer.start();
            grpcServer.blockUntilShutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static NodeService getNodeService() {
        return nodeService;
    }
}
