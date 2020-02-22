package top.beliefyu.fstream.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.beliefyu.fstream.client.DataStream;
import top.beliefyu.fstream.client.api.operator.DataOperator;
import top.beliefyu.fstream.common.zk.ZkClient;
import top.beliefyu.fstream.rpc.HeartBeatResponse;
import top.beliefyu.fstream.rpc.PhysicsExecutionResponse;
import top.beliefyu.fstream.server.grpc.NodeGrpcClient;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ServerService
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-20 20:02
 */
public class ServerService {

    private static final Logger logger = LoggerFactory.getLogger(ServerService.class);

    /**
     * host与权重map，zk更新map时有可能会与生成物理执行计划产生冲突，故加锁
     */
    private Map<String, Integer> hostWeightMap = Collections.synchronizedSortedMap(new TreeMap<>());

    public void listenAllRegisteredServicesFromZk(ZkClient client, String path) {
        List<String> childrenList = client.getChildrenList(path);
        if (childrenList != null) {
            for (String host : childrenList) {
                hostWeightMap.put(host, 0);
            }
        }
        client.refreshNodesInBackground(path, hostWeightMap);
    }

    @SuppressWarnings("unchecked")
    public List<PhysicsExecution> generatePhysicsExecution(DataStream dataStream) {
        Collection<DataStream> treeHead = dataStream.getTreeHead();
        Collection<DataStream> treeNode = dataStream.getTreeNode();

        //todo getOperatorHostMap
        Map<DataOperator, String> operatorHostMap = new HashMap<>(treeNode.size());
        List<String> hostList = getHostList(treeNode.size());
        Iterator<String> hostIterator = hostList.iterator();
        for (DataStream stream : treeNode) {
            if (hostIterator.hasNext()) {
                String host = hostIterator.next();
                DataOperator operator = stream.getOperator();
                operatorHostMap.put(operator, host);
            }
        }
        logger.debug("getOperatorHostMap:[{}]", operatorHostMap);

        //todo transfer2PhysicsExecutions
        List<PhysicsExecution> physicsExecutions = new ArrayList<>();
        for (DataStream stream : treeNode) {
            List<String> upstreamHosts = stream.getParentOperator() == null ? null :
                    (List<String>) stream.getParentOperator().stream().map(operatorHostMap::get).collect(Collectors.toList());
            List<String> downstreamHosts = stream.getChildOperator() == null ? null :
                    (List<String>) stream.getChildOperator().stream().map(operatorHostMap::get).collect(Collectors.toList());
            PhysicsExecution physicsExecution = new PhysicsExecution()
                    .setUpstreamHost(upstreamHosts)
                    .setDownstreamHost(downstreamHosts)
                    .setHost(operatorHostMap.get(stream.getOperator()))
                    .setOperator(stream.getOperator())
                    .setName(stream.getName())
                    .setUid(stream.getUid());
            physicsExecutions.add(physicsExecution);
        }
        logger.info("transfer 2 PhysicsExecutions:[{}]", physicsExecutions);

        return physicsExecutions;

    }

    private List<String> getHostList(int num) {
        List<String> result = new ArrayList<>(num);
        hostWeightMap.forEach((k, v) -> {
            if (v != null) {
                result.add(k);
            }
        });
        logger.debug("getHostList " + result.toString());
        // 所有取出的host权重加1，权重排序按自然排序
        result.forEach(i -> hostWeightMap.compute(i, (k, v) -> v != null ? v + 1 : null));
        return result;
    }

    public void distributePhysicsExecution(List<PhysicsExecution> physicsExecutions) {
        physicsExecutions.forEach(i -> {
            NodeGrpcClient client = new NodeGrpcClient(i.getHost());
            HeartBeatResponse heartBeatResponse = client.doHeartBeatTest();
            logger.debug("[{}],heartBeat:[{}]", i.getName(), heartBeatResponse.getMsg());
            PhysicsExecutionResponse physicsExecutionResponse = client.submitPhysicsExecution(i);
            logger.info("[{}],submitPhysicsExecution:[{}]", i.getName(), physicsExecutionResponse.getMsg());
        });
        logger.info("all PhysicsExecution submitted!");
    }

    public class Task implements Serializable {
        private List<PhysicsExecution> physicsExecutions;
        private String id = UUID.randomUUID().toString();
    }

    public class PhysicsExecution implements Serializable {
        private List<String> upstreamHost;
        private String host;
        private List<String> downstreamHost;

        private DataOperator operator;

        private String name;
        private String uid;

        public PhysicsExecution() {
        }

        public List<String> getUpstreamHost() {
            return upstreamHost;
        }

        public PhysicsExecution setUpstreamHost(List<String> upstreamHost) {
            this.upstreamHost = upstreamHost;
            return this;
        }


        public String getHost() {
            return host;
        }

        public PhysicsExecution setHost(String host) {
            this.host = host;
            return this;
        }

        public List<String> getDownstreamHost() {
            return downstreamHost;
        }

        public PhysicsExecution setDownstreamHost(List<String> downstreamHost) {
            this.downstreamHost = downstreamHost;
            return this;
        }


        public DataOperator getOperator() {
            return operator;
        }

        public PhysicsExecution setOperator(DataOperator operator) {
            this.operator = operator;
            return this;
        }

        public String getName() {
            return name;
        }

        public PhysicsExecution setName(String name) {
            this.name = name;
            return this;
        }

        public String getUid() {
            return uid;
        }

        public PhysicsExecution setUid(String uid) {
            this.uid = uid;
            return this;
        }

        @Override
        public String toString() {
            return "PhysicsExecution{" +
                    "upstreamHost=" + upstreamHost +
                    ", host='" + host + '\'' +
                    ", downstreamHost=" + downstreamHost +
                    ", operator=" + operator +
                    ", name='" + name + '\'' +
                    ", uid='" + uid + '\'' +
                    '}';
        }
    }
}
