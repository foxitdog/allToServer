package io.github.foxitdog.alltoserver.web.websocket.handler;

import com.alibaba.fastjson.JSONObject;
import io.github.foxitdog.alltoserver.netty.server.handler.ServerMessageHandler;
import io.github.foxitdog.alltoserver.web.websocket.entity.WSMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 */
@Log4j2
public class NomalMessageHandler extends WSMessageHandler {

    public NomalMessageHandler(WSMessage message, WebSocketSession session, long sendTimeout) {
        super(message, session,sendTimeout);
    }

    @Override
    void handleNormal() {
        switch (message.getType()) {
            default:
                break;
        }
    }

    @Override
    void handleRequest() {
        Object obj = null;
        switch (message.getType()) {
            case WSMessage.T_FASTCANDATA:
                obj = handleFastCanData();
                break;
            case WSMessage.T_UNFASTCANDATA:
            	obj = handleUnFastCanData();
                break;
            case WSMessage.T_SUBSCRIBE:
            	obj = handleSubscribe();
            	break;
            case WSMessage.T_UNSUBSCRIBE:
            	obj = handleUnSubscribe();
            	break;
            default:
                break;
        }
        sendResponse(obj);
    }

    private Object handleUnSubscribe() {
    	log.info("handleUnSubscribe:"+message.getData());
    	return null;
	}

	private Object handleSubscribe() {
		log.info("handleSubscribe:"+message.getData());
        return null;
	}

	private Object handleFastCanData() {
    	log.info("handleFastCanData:"+message.getData());
        return null;
    }
    
    private Object handleUnFastCanData() {
    	log.info("handleUnFastCanData:"+message.getData());
    	return null;
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
