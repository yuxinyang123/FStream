package top.beliefyu.fstream.node.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.beliefyu.fstream.common.zk.ZkClient;

/**
 * NodeService
 *
 * @author yuxinyang
 * @version 1.0
 * @date 2020-02-23 03:02
 */
public class NodeService {
    private static final Logger logger = LoggerFactory.getLogger(NodeService.class);

    /**
     *
     * @date 2020/4/2 2:57
     * @author yuxinyang
     */
    public void register(ZkClient client,String uuid){
        client.creatNode("/nodes/node-"+uuid,uuid.getBytes());
    }
}
