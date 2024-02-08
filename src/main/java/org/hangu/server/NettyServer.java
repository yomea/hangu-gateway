package org.hangu.server;

import com.hangu.common.properties.HanguProperties;
import com.hangu.common.registry.RegistryService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.hangu.constant.CommonCons;
import org.hangu.handler.NettyHttpServerHandler;

/**
 * @author wuzhenhong
 * @date 2023/7/31 15:23
 */
@Slf4j
public class NettyServer {

    public static void start(int port, Executor executor, RegistryService registryService, HanguProperties hanguProperties) {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup(CommonCons.DEF_IO_THREADS);
        try {
            LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class)
                .group(boss, worker)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // 请求解码器
                        ch.pipeline()
                            .addLast("http-decoder", new HttpRequestDecoder())
                            // 将HTTP消息的多个部分合成一条完整的HTTP消息
                            .addLast("http-aggregator", new HttpObjectAggregator(65535))
                            // 响应转码器
                            .addLast("http-encoder", new HttpResponseEncoder())
                            .addLast("loggingHandler", loggingHandler)
                            // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
                            .addLast("http-chunked", new ChunkedWriteHandler())
                            // 自定义处理handler
                            .addLast("http-server", new NettyHttpServerHandler(executor, registryService, hanguProperties));
                    }
                });
            serverBootstrap.bind(port).sync().addListener(future -> {
                if (!future.isSuccess()) {
                    log.error("服务启动失败---》绑定失败！！！");
                }
            }).channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("启动服务失败！", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
