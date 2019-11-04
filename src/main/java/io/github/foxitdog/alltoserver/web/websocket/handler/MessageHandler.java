package io.github.foxitdog.alltoserver.web.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import io.github.foxitdog.alltoserver.util.Common;
import io.github.foxitdog.alltoserver.web.websocket.entity.WSMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Log4j2
public class MessageHandler extends TextWebSocketHandler {

    @Autowired
    ThreadPoolTaskScheduler threadPoolTaskScheduler;
    int sendTimeout = 60*1000;


    /**
     * 处理接收信息 处理广播回应： 信息格式： {lx:(0/1/2/....),}
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.debug("sessionid:"+session.getId()+",message:"+ payload);
        WSMessage msg;
        try {
            msg = JSON.parseObject(payload,WSMessage.class);
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
            sendMessage(session,WSMessage.getRequestBuilder()
                    .type("error").data(Common.getNormalFalseJson(e.getMessage()))
                    .build().buildMessage());
            return;
        }
        wsHandle(msg,session);
    }

    private void wsHandle(WSMessage msg, WebSocketSession session) {
        if(!StringUtils.isEmpty(msg.getType())){
            Runnable r = new NomalMessageHandler(msg,session,sendTimeout);
            threadPoolTaskScheduler.submit(r);
        }
    }

    /**
     * 处理连接信息
     */
    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        log.warn("webSocket连接接入sessionid:"+session.getId()+","+session.getRemoteAddress().toString());
    }

    /**
     * 处理连接关闭信息
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.warn("sessionid:"+session.getId()+","+session.getRemoteAddress().toString()+"连接关闭status:"+ status);
        String target = (String) session.getAttributes().get("session");
    }

    /**
     * 处理websocket出错信息
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Ip地址:" + session.getRemoteAddress().toString() + ",异常:" + exception.getMessage(), exception);
        sendMessage(session,WSMessage.buildErrorTextMessage(exception.getMessage()));
    }
    
    public void sendMessage(WebSocketSession session, WebSocketMessage message) {
        synchronized (session) {
            try {
                session.sendMessage(message);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
