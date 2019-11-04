package io.github.foxitdog.alltoserver.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.ScheduledExecutorService;

@Component
@Slf4j
public class NettyServer implements Runnable {
    @Value("${setting.netty.server.host}")
    String host;
    @Value("${setting.netty.server.port}")
    int port;
    @Autowired
    ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Resource
    ApplicationContext applicationContext;

    // 配置服务端的NIO线程组
    EventLoopGroup bossGroup = new NioEventLoopGroup();

    EventLoopGroup workerGroup = new NioEventLoopGroup();

    EventExecutorGroup group = new DefaultEventExecutorGroup(16);

    @PostConstruct
    public void init(){
        threadPoolTaskScheduler.execute(()->{
            new Thread(this,"NettyServer").start();
        });
    }

    @PreDestroy
    public void destory(){
        // 优雅退出,释放线程池资源
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        group.shutdownGracefully();
    }

    public void bind() throws Exception {
        try {
            CustomChannelInitializer fcci = applicationContext.getBean(CustomChannelInitializer.class);
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .childHandler(fcci);
            // 绑定端口, 同步等待成功
            ChannelFuture future = bootstrap.bind(host, port).sync();
            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();

            log.warn("设备管理服务关闭");
        } finally {
            // 优雅退出,释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void run() {
        try {
            log.info("启动设备管理服务 host:{} port:{}",host,port);
            bind();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }
}
