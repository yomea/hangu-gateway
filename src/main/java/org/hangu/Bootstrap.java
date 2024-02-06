package org.hangu;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.hangu.server.NettyServer;

/**
 * @author wuzhenhong
 */

public class Bootstrap {

    public static void main(String[] args) {
        NettyServer.start(8089, Bootstrap.buildWorkExecutor());
    }

    private static Executor buildWorkExecutor() {
        // 处理请求业务线程
        return new ThreadPoolExecutor(128, 128,
            0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1024));
    }
}