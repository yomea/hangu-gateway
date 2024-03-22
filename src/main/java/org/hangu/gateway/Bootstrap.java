package org.hangu.gateway;

import com.hangu.rpc.common.properties.HanguProperties;
import com.hangu.rpc.common.properties.ZookeeperConfigProperties;
import com.hangu.rpc.common.registry.HanguRegistryService;
import com.hangu.rpc.common.registry.RegistryService;
import com.hangu.rpc.common.registry.ZookeeperRegistryService;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.hangu.center.discover.client.DiscoverClient;
import org.hangu.center.discover.config.impl.ClientResponseHandlerConfigDefaultImpl;
import org.hangu.center.discover.properties.ClientProperties;
import org.hangu.center.discover.starter.CenterClientStarter;
import org.hangu.gateway.server.NettyServer;

/**
 * @author wuzhenhong
 */

public class Bootstrap {

    public static void main(String[] args) {
        NettyServer.start(8099,
            Bootstrap.buildWorkExecutor(),
            Bootstrap.buildHanguRegistryService(),
            Bootstrap.buildHanguProperties());
    }

    private static Executor buildWorkExecutor() {
        // 处理请求业务线程
        return new ThreadPoolExecutor(128, 128,
            0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1024));
    }

    private static RegistryService buildZkRegistryService() {
        ZookeeperConfigProperties properties = new ZookeeperConfigProperties();
        properties.setHosts("192.168.203.233:2181");
        return new ZookeeperRegistryService(properties);
    }

    private static RegistryService buildHanguRegistryService() {
        ClientProperties clientProperties = new ClientProperties();
        clientProperties.setPeerNodeHosts("localhost:9991,localhost:9992");
        DiscoverClient discoverClient = CenterClientStarter.start(clientProperties, Collections.singletonList(new ClientResponseHandlerConfigDefaultImpl()));
        return new HanguRegistryService(discoverClient);
    }

    private static HanguProperties buildHanguProperties() {
        HanguProperties properties = new HanguProperties();
        properties.setMaxNum(8);
        properties.setCoreNum(4);
        return properties;
    }
}