package top.beliefyu.fstream.node.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.beliefyu.fstream.common.BlockingQueueBuffer;
import top.beliefyu.fstream.common.Buffer;
import top.beliefyu.fstream.common.zk.ZkClient;
import top.beliefyu.fstream.server.service.ServerService.PhysicsExecution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NodeService
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-23 03:02
 */
public class NodeService {
    private static final Logger logger = LoggerFactory.getLogger(NodeService.class);

    private Map<String, Buffer<?>> inputBufferMap = new ConcurrentHashMap<>();
    private Map<String, Buffer<?>> outputBufferMap = new ConcurrentHashMap<>();
    private Map<String, PhysicsExecution> physicsExecutionMap = new ConcurrentHashMap<>();

    /**
     * 注册Node元信息至Zookeeper
     *
     * @date 2020/4/2 2:57
     * @author yuxinyang
     */
    public void register(ZkClient client, String uuid) {
        client.creatNode("/nodes/node-" + uuid, uuid.getBytes());
    }

    /**
     * 初始化输入输出缓冲区
     *
     * @date 2020/4/3 21:29
     * @author yuxinyang
     */
    public void initBuffer(String operatorId, Boolean hasUpstream) {
        if (hasUpstream) {
            inputBufferMap.put(operatorId, new BlockingQueueBuffer<>(100));
        }
        outputBufferMap.put(operatorId, new BlockingQueueBuffer<>(100));
    }

    /**
     * 保存所有正在运行的任务
     *
     * @date 2020/4/3 21:31
     * @author yuxinyang
     */
    public void taskRecordInMap(PhysicsExecution physicsExecution) {
        physicsExecutionMap.put(physicsExecution.getUid(), physicsExecution);
        logger.debug("operatorUid:[{}]",physicsExecution.getUid());
    }

    //todo 源算子的单独处理

    public Map<String, Buffer<?>> getInputBufferMap() {
        return inputBufferMap;
    }

    public Map<String, Buffer<?>> getOutputBufferMap() {
        return outputBufferMap;
    }

    public Map<String, PhysicsExecution> getPhysicsExecutionMap() {
        return physicsExecutionMap;
    }
}
