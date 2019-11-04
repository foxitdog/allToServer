package io.github.foxitdog.alltoserver.web.websocket.handler;

import io.github.foxitdog.alltoserver.web.websocket.entity.WSMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * json格式的数据
 */
@Log4j2
public abstract class WSMessageHandler implements Runnable {

	WSMessage message;

	WebSocketSession session;

	String target;
	private ConcurrentHashMap<Integer, LinkObject> linkMap = new ConcurrentHashMap<>();

	private long sendTimeout;

	public WSMessageHandler(WSMessage message, WebSocketSession session,long sendTimeout) {
		this.message = message;
		this.session = session;
		this.target = (String) session.getAttributes().get("session");
		this.sendTimeout = sendTimeout;
	}

	@Override
	public void run() {
		try {
			if (message.isResponse()) {
				handleResponse();
				return;
			}

			if (message.isRequest()) {
				handleRequest();
				return;
			}

			handleNormal();
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			sendMessage(WSMessage.buildErrorTextMessage(e.getMessage()));
		}
	}

	/**
	 * 处理正常的ws数据
	 */
	abstract void handleNormal();

	/**
	 * 处理请求的ws数据
	 */
	abstract void handleRequest();

	private void handleResponse() {
		LinkObject lo = linkMap.get(message.getLinksum());
		if (lo == null) {
			return;
		}
		linkMap.remove(message.getLinksum());
		synchronized (lo) {
			lo.state = LinkObject.SendType.RECEIVED;
			lo.data = message;
			lo.notify();
		}
	}

	private void send(WSMessage ms, CallBack cb) {
		int linksum = ms.getLinksum();
		LinkObject lo = new LinkObject();
		synchronized (lo) {
			try {
				lo.linkSum = linksum;
				linkMap.put(linksum, lo);
				sendMessage(ms.buildMessage());
				lo.state = LinkObject.SendType.SENDED;
				lo.wait(sendTimeout);
				linkMap.remove(linksum);
				if (lo.state == LinkObject.SendType.SENDED) {
					throw new TimeoutException("type:" + ms.getType() + ",linksum:" + ms.getLinksum() + "的命令超时未返回");
				} else {
					if (lo.data != null && cb != null) {
						cb.callback(lo.data);
					}
				}
			} catch (Exception e) {

			}
		}
	}

	void sendResponse(Object obj) {
		sendMessage(WSMessage.getResponseBuilder().linksum(message.getLinksum()).type(message.getType())
				.state("response").data(obj).build().buildMessage());
	}

	private static class LinkObject {
		/**
		 * 连接计数器
		 */
		int linkSum;

		/**
		 * 数据
		 */
		WSMessage data;

		/**
		 * 0:未发 1:已发 2:接受
		 */
		SendType state = SendType.NOSEND;

		private enum SendType {
			NOSEND, SENDED, RECEIVED
		}
	}

	public void sendMessage(WebSocketMessage message) {
		synchronized (session) {
			try {
				session.sendMessage(message);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	interface CallBack {
		void callback(WSMessage data);
	}
}
