package io.github.foxitdog.alltoserver.web.websocket.entity;

import com.alibaba.fastjson.JSON;
import io.github.foxitdog.alltoserver.util.Common;
import io.github.foxitdog.alltoserver.util.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.TextMessage;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class WSMessage {
    public static final String T_FASTCANDATA = "fastCanData";
    public static final String T_UNFASTCANDATA = "unfastCanData";
    public static final String T_SUBSCRIBE = "subscribe";
    public static final String T_UNSUBSCRIBE = "unsubscribe";

    private static int LINKSUM = 0;

    /**
     * 类型
     */
    String type;
    /**
     * 数据
     */
    Object data;

    /**
     * 连接计数器
     */
    Integer linksum;
    /**
     * 请求或响应
     */
    String state;

    public boolean isRequest() {
        return "request".equals(state);
    }

    public boolean isResponse() {
        return "response".equals(state);
    }

    public static WSMessage.WSMessageBuilder getRequestBuilder() {
        return WSMessage.builder().linksum(LINKSUM++).state("request");
    }

    public static WSMessage.WSMessageBuilder getResponseBuilder() {
        return WSMessage.builder().linksum(LINKSUM++).state("response");
    }

    public TextMessage buildMessage() {
        return new TextMessage(JSON.toJSONString(this));
    }

    public static TextMessage buildErrorTextMessage(int code, String message) {
        return WSMessage.builder().type("error").data(Common.getNormalFalseJson(code,message)).build().buildMessage();
    }

    public static TextMessage buildErrorTextMessage(String message) {
        return buildErrorTextMessage(ErrorCode.INTERNAL_EXCEPTION, message);
    }

    public static WSMessage buildErrorMessage(int code, String message, String target) {
        return WSMessage.builder().type("error").data(Common.getNormalFalseJson(code,message)).build();
    }

    public static WSMessage buildErrorMessage(String message, String target) {
        return buildErrorMessage(ErrorCode.INTERNAL_EXCEPTION, message, target);
    }

}
