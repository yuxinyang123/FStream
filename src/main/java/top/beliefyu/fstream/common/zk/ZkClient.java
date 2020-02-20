package top.beliefyu.fstream.common.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    private PathChildrenCache pathChildrenCache;

    private String connectString;

    public ZkClient(String host, int port) {
        connectString = host + ":" + port;
        init();
    }


    private void init() {
        curator = CuratorFrameworkFactory.builder()
                .sessionTimeoutMs(10000)
                .connectionTimeoutMs(5000)
                .retryPolicy(new RetryNTimes(5, 1000))
                .connectString(connectString)
                .namespace("fstream")
                .build();
    }

    public void start() {
        curator.start();
        LOGGER.info("zkClient started, listening on " + connectString);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("*** shutting down zkClient server since JVM is shutting down");
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            LOGGER.info("*** zkClient shut down");
        }));
    }

    public void creatNode(String path, byte[] data) {
        //无限重连
        curator.getConnectionStateListenable().addListener((CuratorFramework curatorFramework, ConnectionState connectionState) -> {
            while (connectionState == ConnectionState.LOST) {
                try {
                    if (curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                                .forPath(path, data);
                        break;
                    }
                } catch (Exception e) {
                    LOGGER.error("retry fail!", e);
                }
            }
        });

        try {
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(path, data);
        } catch (Exception e) {
            LOGGER.error("creatNode fail!", e);
        }
    }

    @Nullable
    public List<String> getChildrenList(String path) {
        try {
            return curator.getChildren().forPath(path);
        } catch (Exception e) {
            LOGGER.error("getChildrenList fail!", e);
            return null;
        }
    }

    public void refreshNodesInBackgroud(String path, Map<String, Object> map) {
        pathChildrenCache = new PathChildrenCache(curator, path, true);
        pathChildrenCache.getListenable().addListener((CuratorFramework client, PathChildrenCacheEvent e) -> {
            switch (e.getType()) {
                case CHILD_ADDED:
                    LOGGER.debug("CHILD_ADDED");
                    map.put(e.toString(), e.getData());
                    break;
                case CHILD_UPDATED:
                    LOGGER.debug("CHILD_UPDATED");
                    map.put(e.toString(), e.getData());
                    break;
                case CHILD_REMOVED:
                    LOGGER.debug("CHILD_REMOVED");
                    map.put(e.toString(), null);
                    break;
                default:
                    break;
            }
        });
    }

    public void close() throws IOException {
        if (pathChildrenCache != null) {
            pathChildrenCache.close();
        }

        if (curator != null) {
            curator.close();
        }

    }

}
