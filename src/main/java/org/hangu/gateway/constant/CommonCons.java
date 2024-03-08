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
}
