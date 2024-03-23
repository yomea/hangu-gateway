package org.hangu.gateway.constant;

import io.netty.handler.codec.http.HttpHeaderValues;

/**
 * @author wuzhenhong
 * @date 2023/8/9 9:16
 */
public class CommonCons {

    public static final int DEF_IO_THREADS = Runtime.getRuntime().availableProcessors() << 3;
    public static final String DEF_GATEWAY_KEY = "hangu.gateway.path";
    public static final String DEF_GATEWAY_CONFIG = "gateway.properties";
    public static final String CONTENT_TYPE_FORMAT = "%s;" + HttpHeaderValues.CHARSET + "=UTF-8";
    public static final String WORK_THREAD_NUM = "gateway.work.thread.num";
    public static final String LISTENER_PORT = "gateway.listener.port";
    public static final String ZK_CENTER_CLUSTER_ADDRESS = "zk.center.cluster.address";
    public static final String HANGU_CENTER_NODE_ADDRESS = "hangu.center.peer.node.hosts";
    public static final String REDIS_CENTER_NODE_ADDRESS = "redis.center.node.hosts";
    public static final String REDIS_CENTER_NODE_MASTER = "redis.center.node.master";
    public static final String REDIS_CENTER_NODE_PASSWORD = "redis.center.node.password";
    public static final String REGISTRY_SERVICE_CLASS_NAME = "registry.service.class.name";
    public static final String ENABLE_REGISTRY_CENTER = "gateway.enable.registry.center";
}
