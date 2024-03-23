package org.hangu.gateway.factory.impl;

import com.hangu.rpc.common.registry.HanguRegistryService;
import com.hangu.rpc.common.registry.RegistryService;
import java.util.Collections;
import java.util.Properties;
import org.hangu.center.discover.client.DiscoverClient;
import org.hangu.center.discover.config.impl.ClientResponseHandlerConfigDefaultImpl;
import org.hangu.center.discover.properties.ClientProperties;
import org.hangu.center.discover.starter.CenterClientStarter;
import org.hangu.gateway.constant.CommonCons;
import org.hangu.gateway.factory.RegistryServiceFactory;
import org.hangu.gateway.utils.ConfigUtils;

/**
 * @author wuzhenhong
 * @date 2024/3/23 10:10
 */
public class HanguRegistryServiceFactory implements RegistryServiceFactory {

    @Override
    public RegistryService buildRegistryService(Properties properties) {
        String peerNodeHosts = this.getProperty(properties, CommonCons.HANGU_CENTER_NODE_ADDRESS);
        ClientProperties clientProperties = new ClientProperties();
        clientProperties.setPeerNodeHosts(peerNodeHosts);
        DiscoverClient discoverClient = CenterClientStarter.start(clientProperties, Collections.singletonList(new ClientResponseHandlerConfigDefaultImpl()));
        return new HanguRegistryService(discoverClient);
    }
}
