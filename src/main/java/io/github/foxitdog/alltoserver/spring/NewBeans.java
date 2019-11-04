package io.github.foxitdog.alltoserver.spring;

import io.github.foxitdog.alltoserver.netty.server.CustomChannelInitializer;
import io.github.foxitdog.alltoserver.netty.server.handler.ServerMessageHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Configuration
public class NewBeans {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(@Value("${setting.threadpool.num}")int poolsize) {
        ThreadPoolTaskScheduler tpte = new ThreadPoolTaskScheduler();
        tpte.setPoolSize(poolsize);
        return tpte;
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public CustomChannelInitializer faceClientChannelInitializer(){
        CustomChannelInitializer fcci = new CustomChannelInitializer();
        return fcci;
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public ServerMessageHandler socketMessageOfFaceManagerHandler(){
        ServerMessageHandler smodmh = new ServerMessageHandler();
        return smodmh;
    }
}
