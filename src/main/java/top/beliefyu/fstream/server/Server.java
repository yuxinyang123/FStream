package top.beliefyu.fstream.server;

import top.beliefyu.fstream.common.grpc.GrpcServer;
import top.beliefyu.fstream.common.zk.ZkClient;
import top.beliefyu.fstream.server.grpc.ServerGrpcService;
import top.beliefyu.fstream.server.service.ServerService;

/**
 * Server
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-18 21:05
 */
public class Server {

    private static ZkClient zkClient;

    private static GrpcServer grpcServer;

    private static ServerService serverService = new ServerService();

    public static void main(String[] args) {
        init();
        listeningRegisteredServices();
        grpcServerStartAndBlockUntilShutdown();
    }

    private static void init() {
        zkClient = new ZkClient("192.168.2.129", 2181);
        grpcServer = new GrpcServer(6666, new ServerGrpcService());
        zkClient.start();
    }


    private static void listeningRegisteredServices() {
        serverService.listenAllRegisteredServicesFromZk(zkClient, "/nodes");
    }

    private static void grpcServerStartAndBlockUntilShutdown() {
        try {
            grpcServer.start();
            grpcServer.blockUntilShutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ServerService getServerService() {
        return serverService;
    }
}
