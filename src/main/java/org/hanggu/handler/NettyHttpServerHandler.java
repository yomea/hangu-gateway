package org.hanggu.handler;

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
import java.text.MessageFormat;
import java.util.concurrent.Executor;

/**
 * @author wuzhenhong
 * @date 2023/8/9 9:21
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private Executor executor;

    public NettyHttpServerHandler(Executor executor) {
        this.executor = executor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        executor.execute(() -> {
            this.service(request, ctx);
        });
    }

    private void service(FullHttpRequest request, ChannelHandlerContext ctx) {
        HttpMethod httpMethod = request.method();
        if (HttpMethod.GET.name().equals(httpMethod.name())) {
            this.doGet(request, ctx);
        } else if (HttpMethod.HEAD.name().equals(httpMethod.name())) {
            this.doHead(request, ctx);
        } else if (HttpMethod.POST.name().equals(httpMethod.name())) {
            this.doPost(request, ctx);
        } else if (HttpMethod.PUT.name().equals(httpMethod.name())) {
            this.doPut(request, ctx);
        } else if (HttpMethod.DELETE.name().equals(httpMethod.name())) {
            this.doDelete(request, ctx);
        } else if (HttpMethod.OPTIONS.name().equals(httpMethod.name())) {
            this.doOptions(request, ctx);
        } else if (HttpMethod.TRACE.name().equals(httpMethod.name())) {
            this.doTrace(request, ctx);
        } else {
            String errMsg = String.format("http.method_not_implemented => %s", httpMethod.name());
            ByteBuf byteBuf = ctx.alloc().buffer();
            byteBuf.writeBytes(errMsg.getBytes(StandardCharsets.UTF_8));
            FullHttpResponse response = this.createResponse(HttpResponseStatus.METHOD_NOT_ALLOWED, ctx.alloc().buffer());
            response.headers().add("Content-Type","text/plain;charset=UTF-8");
            ctx.writeAndFlush(response);
        }
    }

    private void doPost(FullHttpRequest request, ChannelHandlerContext ctx) {



    }

    private void doGet(FullHttpRequest request, ChannelHandlerContext ctx) {

    }

    private void doTrace(FullHttpRequest request, ChannelHandlerContext ctx) {
    }

    private void doOptions(FullHttpRequest request, ChannelHandlerContext ctx) {
        
    }

    private void doDelete(FullHttpRequest request, ChannelHandlerContext ctx) {
        
    }

    private void doPut(FullHttpRequest request, ChannelHandlerContext ctx) {
        
    }

    private void doHead(FullHttpRequest request, ChannelHandlerContext ctx) {

    }

    private FullHttpResponse createResponse(HttpResponseStatus status, ByteBuf byteBuf) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, byteBuf);
        response.headers().set("Content-Length",response.content().readableBytes());
        return response;
    }
}
