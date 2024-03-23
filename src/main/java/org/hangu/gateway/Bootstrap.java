package org.hangu.gateway;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import com.hangu.rpc.common.properties.HanguProperties;
import com.hangu.rpc.common.properties.ZookeeperConfigProperties;
import com.hangu.rpc.common.registry.HanguRegistryService;
import com.hangu.rpc.common.registry.RegistryService;
import com.hangu.rpc.common.registry.ZookeeperRegistryService;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hangu.center.common.util.CommonUtils;
import org.hangu.center.discover.client.DiscoverClient;
import org.hangu.center.discover.config.impl.ClientResponseHandlerConfigDefaultImpl;
import org.hangu.center.discover.properties.ClientProperties;
import org.hangu.center.discover.starter.CenterClientStarter;
import org.hangu.gateway.constant.CommonCons;
import org.hangu.gateway.factory.RegistryServiceFactory;
import org.hangu.gateway.server.NettyServer;
import org.hangu.gateway.utils.ConfigUtils;

/**
 * @author wuzhenhong
 */

public class Bootstrap {

    public static void main(String[] args) {
        Integer port = NumberUtils.toInt(ConfigUtils.getProperty(CommonCons.LISTENER_PORT, "8099"));
        NettyServer.start(port,
            Bootstrap.buildWorkExecutor(),
            Bootstrap.buildRegistryService(),
            Bootstrap.buildHanguProperties());
    }

    private static Executor buildWorkExecutor() {
        Integer coreNum = NumberUtils.toInt(ConfigUtils.getProperty(CommonCons.WORK_THREAD_NUM, "128"));
        // 处理请求业务线程
        return new ThreadPoolExecutor(coreNum, coreNum,
            0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1024));
    }

    private static RegistryService buildRegistryService() {
        String factoryClassName = ConfigUtils.getProperty(CommonCons.REGISTRY_SERVICE_CLASS_NAME);
        Class<RegistryServiceFactory> registryServiceFactoryClass = ClassUtil.loadClass(factoryClassName);
        try {
            RegistryServiceFactory factory = registryServiceFactoryClass.newInstance();
            return factory.buildRegistryService(null);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static HanguProperties buildHanguProperties() {
        HanguProperties properties = new HanguProperties();
        properties.setMaxNum(8);
        properties.setCoreNum(4);
        return properties;
    }
}