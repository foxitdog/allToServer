package io.github.foxitdog.alltoserver.netty.server.handler;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class ServerMessageHandlerTest {

    @Test
    public void normalTest(){
        String s[] ="{\"type\":\"heartbeat\"}|||".split("\\|\\|\\|");
        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
        }
    }

}