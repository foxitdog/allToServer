package io.github.foxitdog.alltoserver.web.websocket.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

//import java.util.Map.Entry;
public class WebsocketInterceptor extends HttpSessionHandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest req, ServerHttpResponse res, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
		return super.beforeHandshake(req, res, wsHandler, attributes);
	}
	
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
		super.afterHandshake(request, response, wsHandler, ex);
	}
}
