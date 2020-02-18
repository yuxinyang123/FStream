package top.beliefyu.fstream.common.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZkClient
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-18 17:57
 */
public class ZkClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkClient.class);

    private CuratorFramework curator;

    private String connectString;

    public ZkClient(String host, int port) {
        connectString = host + ":" + port;
    }


    private void init() {
        curator = CuratorFrameworkFactory.newClient(connectString,
                5000, 3000, new RetryNTimes(5, 1000));
        curator.start();
    }

    public void creatEphemeralNode(String path, byte[] data) throws Exception {
        String pathPrefix = path + "-";
        curator.getConnectionStateListenable().addListener((CuratorFramework curatorFramework, ConnectionState connectionState) -> {
            while (true) {
                try {
                    if (curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                                .forPath(pathPrefix, data);
                        break;
                    }
                } catch (Exception e) {
                    LOGGER.error("retry fail!", e);
                }
            }
        });
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(pathPrefix, data);
    }

    public void close() {
        curator.close();
    }

}
