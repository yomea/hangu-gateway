package org.hangu.gateway.factory.impl;

import com.hangu.rpc.common.properties.JedisConfigPropertis;
import com.hangu.rpc.common.registry.RedisRegistryService;
import com.hangu.rpc.common.registry.RegistryService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.hangu.center.common.enums.ErrorCodeEnum;
import org.hangu.center.common.exception.RpcStarterException;
import org.hangu.gateway.constant.CommonCons;
import org.hangu.gateway.factory.RegistryServiceFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * @author wuzhenhong
 * @date 2024/3/23 10:10
 */
public class RedisRegistryServiceFactory implements RegistryServiceFactory {

    @Override
    public RegistryService buildRegistryService(Properties properties) {
        JedisConfigPropertis jedisConfigPropertis = this.jedisConfigPropertis(properties);
        return new RedisRegistryService(this.jedisSentinelPool(jedisConfigPropertis));
    }

    private JedisConfigPropertis jedisConfigPropertis(Properties properties) {
        String nodes = this.getProperty(properties, CommonCons.REDIS_CENTER_NODE_ADDRESS);
        if(StringUtils.isBlank(nodes)) {
            throw new RpcStarterException(ErrorCodeEnum.SUCCESS.getCode(), "redis地址未配置！");
        }
        String master = this.getProperty(properties, CommonCons.REDIS_CENTER_NODE_MASTER);
        String password = this.getProperty(properties, CommonCons.REDIS_CENTER_NODE_PASSWORD);
        JedisConfigPropertis configPropertis = new JedisConfigPropertis();
        configPropertis.setNodes(nodes);
        configPropertis.setMaster(master);
        configPropertis.setPassword(password);
        return configPropertis;
    }


    private JedisSentinelPool jedisSentinelPool(JedisConfigPropertis configPropertis) {

        HashSet<String> infos = new HashSet<>();
        String[] split = configPropertis.getNodes().split(",");
        Collections.addAll(infos, split);
        return new JedisSentinelPool(configPropertis.getMaster(), infos, this.jedisPoolConfig(), 5000,
            configPropertis.getPassword());
    }

    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestWhileIdle(true);
        config.setMinEvictableIdleTimeMillis(60000);
        config.setTimeBetweenEvictionRunsMillis(30000);
        config.setNumTestsPerEvictionRun(-1);
        config.setMaxTotal(500);
        config.setMaxIdle(20);
        config.setMinIdle(8);
        return config;
    }
}
