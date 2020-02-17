package top.beliefyu.fstream.common.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * GrpcServer
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-17 03:07
 */
public class GrpcServer {
    private static final Logger logger = LoggerFactory.getLogger(GrpcServer.class);

    private final int port;
    private final Server server;

    public GrpcServer(int port) throws IOException {
        this.port = port;
        server = ServerBuilder.forPort(port).addService(new GrpcService())
                .build();
    }


    /**
     * Start serving requests.
     */
    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                logger.info("*** shutting down gRPC server since JVM is shutting down");
                try {
                    GrpcServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                logger.info("*** server shut down");
            }
        });
    }

    /**
     * Stop serving requests and shutdown resources.
     */
    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main method.  This comment makes the linter happy.
     */
    public static void main(String[] args) throws Exception {
        GrpcServer server = new GrpcServer(8980);
        server.start();
        server.blockUntilShutdown();
    }
}
