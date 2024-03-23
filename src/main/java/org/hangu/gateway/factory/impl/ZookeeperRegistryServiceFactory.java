package org.hangu.gateway.factory.impl;

import com.hangu.rpc.common.properties.ZookeeperConfigProperties;
import com.hangu.rpc.common.registry.RegistryService;
import com.hangu.rpc.common.registry.ZookeeperRegistryService;
import java.util.Objects;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.hangu.center.common.enums.ErrorCodeEnum;
import org.hangu.center.common.exception.RpcStarterException;
import org.hangu.gateway.constant.CommonCons;
import org.hangu.gateway.factory.RegistryServiceFactory;
import org.hangu.gateway.utils.ConfigUtils;

/**
 * @author wuzhenhong
 * @date 2024/3/23 10:10
 */
public class ZookeeperRegistryServiceFactory implements RegistryServiceFactory {

    @Override
    public RegistryService buildRegistryService(Properties properties) {
        String address = this.getProperty(properties, CommonCons.ZK_CENTER_CLUSTER_ADDRESS);
        if(StringUtils.isBlank(address)) {
            throw new RpcStarterException(ErrorCodeEnum.FAILURE.getCode(), "zk地址屬性" + CommonCons.ZK_CENTER_CLUSTER_ADDRESS
                + "沒有配置");
        }
        ZookeeperConfigProperties configProperties = new ZookeeperConfigProperties();
        configProperties.setHosts(address);
        return new ZookeeperRegistryService(configProperties);
    }
}
