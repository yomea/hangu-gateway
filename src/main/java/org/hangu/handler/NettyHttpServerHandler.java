package org.hangu.handler;

import com.hangu.common.entity.HttpServletRequest;
import com.hangu.common.properties.HanguProperties;
import com.hangu.common.registry.RegistryService;
import com.hangu.consumer.http.HttpGenericProxyFactory;
import com.hangu.consumer.http.HttpGenericService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wuzhenhong
 * @date 2023/8/9 9:21
 */
@Slf4j
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private Executor executor;

    private RegistryService registryService;

    private HanguProperties hanguProperties;

    public NettyHttpServerHandler(Executor executor, RegistryService registryService, HanguProperties hanguProperties) {
        this.executor = executor;
        this.registryService = registryService;
        this.hanguProperties = hanguProperties;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        HttpServletRequest apiRequest = HttpGenericProxyFactory.buildRequest(request);
        executor.execute(() -> {
            this.service(apiRequest, ctx);
        });

    }



    private void service(HttpServletRequest request, ChannelHandlerContext ctx) {
        String httpMethod = request.getMethod();
        if (HttpMethod.GET.name().equals(httpMethod)) {
            this.doGet(request, ctx);
        } else if (HttpMethod.HEAD.name().equals(httpMethod)) {
            this.doHead(request, ctx);
        } else if (HttpMethod.POST.name().equals(httpMethod)) {
            this.doPost(request, ctx);
        } else if (HttpMethod.PUT.name().equals(httpMethod)) {
            this.doPut(request, ctx);
        } else if (HttpMethod.DELETE.name().equals(httpMethod)) {
            this.doDelete(request, ctx);
        } else if (HttpMethod.OPTIONS.name().equals(httpMethod)) {
            this.doOptions(request, ctx);
        } else if (HttpMethod.TRACE.name().equals(httpMethod)) {
            this.doTrace(request, ctx);
        } else {
            String errMsg = String.format("http.method_not_implemented => %s", httpMethod);
            ByteBuf byteBuf = ctx.alloc().buffer();
            byteBuf.writeBytes(errMsg.getBytes(StandardCharsets.UTF_8));
            FullHttpResponse response = this.createResponse(HttpResponseStatus.METHOD_NOT_ALLOWED, byteBuf);
            response.headers().add("Content-Type","text/plain;charset=UTF-8");
            ctx.writeAndFlush(response);
        }
    }

    private void writeAndFlush(HttpServletRequest request, ChannelHandlerContext ctx) {

        try {
            HttpGenericService httpProxy = HttpGenericProxyFactory.httpProxy(request.getURI(), this.registryService, this.hanguProperties);
            String resp = httpProxy.http(request);
            ByteBuf byteBuf = ctx.alloc().buffer();
            byteBuf.writeBytes(resp.getBytes(StandardCharsets.UTF_8));
            FullHttpResponse response = this.createResponse(HttpResponseStatus.OK, byteBuf);
            response.headers().add("Content-Type","application/json;charset=UTF-8");
            ctx.writeAndFlush(response);
        } catch (Exception e) {
            log.error("调用失败！", e);
            ByteBuf byteBuf = ctx.alloc().buffer();
            byteBuf.writeBytes("调用失败！".getBytes(StandardCharsets.UTF_8));
            FullHttpResponse response = this.createResponse(HttpResponseStatus.OK, byteBuf);
            response.headers().add("Content-Type","text/plain;charset=UTF-8");
            ctx.writeAndFlush(response);
        }
    }


    private void doPost(HttpServletRequest request, ChannelHandlerContext ctx) {
        this.writeAndFlush(request, ctx);
    }

    private void doGet(HttpServletRequest request, ChannelHandlerContext ctx) {
        this.writeAndFlush(request, ctx);
    }

    private void doTrace(HttpServletRequest request, ChannelHandlerContext ctx) {
        this.writeAndFlush(request, ctx);
    }

    private void doOptions(HttpServletRequest request, ChannelHandlerContext ctx) {
        this.writeAndFlush(request, ctx);
    }

    private void doDelete(HttpServletRequest request, ChannelHandlerContext ctx) {
        this.writeAndFlush(request, ctx);
    }

    private void doPut(HttpServletRequest request, ChannelHandlerContext ctx) {
        this.writeAndFlush(request, ctx);
    }

    private void doHead(HttpServletRequest request, ChannelHandlerContext ctx) {
        this.writeAndFlush(request, ctx);
    }

    private FullHttpResponse createResponse(HttpResponseStatus status, ByteBuf byteBuf) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, byteBuf);
        response.headers().set("Content-Length",response.content().readableBytes());
        return response;
    }
}
