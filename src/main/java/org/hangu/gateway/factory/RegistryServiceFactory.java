package org.hangu.gateway.factory;

import com.hangu.rpc.common.registry.RegistryService;
import java.util.Objects;
import java.util.Properties;
import org.hangu.gateway.constant.CommonCons;
import org.hangu.gateway.utils.ConfigUtils;

/**
 * @author wuzhenhong
 * @date 2024/3/23 10:09
 */
@FunctionalInterface
public interface RegistryServiceFactory {

    default String getProperty(Properties properties, String key) {
        return this.getProperty(properties, key, null);
    }

    default String getProperty(Properties properties, String key, String defVal) {
        String val = null;
        if(Objects.nonNull(properties)) {
            val = properties.getProperty(key);
        }
        if(Objects.isNull(val)) {
            val = ConfigUtils.getProperty(key, defVal);
        }
        return val;
    }

    RegistryService buildRegistryService(Properties properties);
}
