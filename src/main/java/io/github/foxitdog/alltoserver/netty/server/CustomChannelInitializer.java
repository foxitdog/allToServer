package io.github.foxitdog.alltoserver.netty.server;

import io.github.foxitdog.alltoserver.netty.server.handler.MessageDecodeAndEncode;
import io.github.foxitdog.alltoserver.netty.server.handler.ServerMessageHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;

public class CustomChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Resource
    ApplicationContext applicationContext;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline
                .addLast(new MessageDecodeAndEncode())
                // 添加消息handler
                .addLast(applicationContext.getBean(ServerMessageHandler.class));
    }
}
