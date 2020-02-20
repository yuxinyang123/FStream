package top.beliefyu.fstream.server;

import top.beliefyu.fstream.client.api.DataStream;
import top.beliefyu.fstream.common.zk.ZkClient;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * ServerService
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-20 20:02
 */
public class ServerService {

    private Map<String, Integer> hostWeightMap = new TreeMap<>();

    public void listenAllRegisteredServicesFromZk(ZkClient client, String path) {
        List<String> childrenList = client.getChildrenList(path);
        if (childrenList != null) {
            for (String host : childrenList) {
                hostWeightMap.put(host, 1);
            }
        }
        client.refreshNodesInBackground(path, hostWeightMap);
    }

    public void generatePhysicsExecution(DataStream dataStream) {
    }

    public void distributePhysicsExecution() {
    }
}
