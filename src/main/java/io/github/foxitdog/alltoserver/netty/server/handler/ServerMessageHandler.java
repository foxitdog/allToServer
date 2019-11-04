package io.github.foxitdog.alltoserver.netty.server.handler;

import com.alibaba.fastjson.JSONObject;
import io.github.foxitdog.alltoserver.datachannel.DataChannel;
import io.github.foxitdog.alltoserver.util.Common;
import io.github.foxitdog.alltoserver.util.Constants;
import io.github.foxitdog.alltoserver.util.ErrorCode;
import io.github.foxitdog.alltoserver.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息处理器
 */
@Log4j2
public class ServerMessageHandler extends SimpleChannelInboundHandler<JSONObject> implements DataChannel {
    @Autowired
    ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public long timestamp = 0;

    ScheduledFuture sf;

    String servername;

    ChannelHandlerContext ctx;

    static ConcurrentHashMap<Integer, LinkObject> linkMap = new ConcurrentHashMap<>();

    /**
     * 新的TCP建立回调函数
     */
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        log.info("新的TCP连接建立:" + channelHandlerContext + ",remoteAddress:" + channelHandlerContext.channel().remoteAddress().toString());
        final ChannelHandlerContext c = channelHandlerContext;
        timestamp = System.currentTimeMillis();
        ctx = channelHandlerContext;
        sf = threadPoolTaskScheduler.scheduleWithFixedDelay(() -> {
            long ts = System.currentTimeMillis();
            if (ts - timestamp > 10 * 1000) {
                c.close();
                if (sf != null) {
                    sf.cancel(true);
                }
            }
        }, 5 * 1000);
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) {
        log.info("TCP连接已断开:" + channelHandlerContext + ",remoteAddress:" + channelHandlerContext.channel().remoteAddress().toString());
        sf.cancel(true);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, JSONObject msg) {
        if (log.isDebugEnabled()){
            log.debug(msg);
        }
        timestamp = System.currentTimeMillis();
        String type = msg.getString("type");
        if (type == null) {
            return;
        }
        if (type.equals("request")) {
            // 暂时不需要实现
        }
        if (type.equals("response")) {
            int linksum = msg.getIntValue("linksum");
            LinkObject linkObject = linkMap.get(linksum);
            if (linkObject == null) {
                writeInternalException("响应消息无linksum");
                return;
            }
            synchronized (linkObject) {
                linkObject.data = msg;
                linkObject.state = LinkObject.SendType.RECEIVED;
                linkObject.notify();
            }
            return;
        }
        if (type.equals("inner")) {
            JSONObject jo = msg.getJSONObject("data");
            type = jo.getString("type");
            if ("init".equals(type)) {
                if (servername != null) {
                    return;
                }
                servername = jo.getString("server");
                if (servername == null) {
                    writeInternalException("初始化失败，未添加server字段");
                    ctx.close();
                    return;
                }
                if (Constants.channelTable.putIfAbsent(servername, this) != null) {
                    writeInternalException("初始化失败，已经有相关服务注册");
                    ctx.close();
                }else{
                    log.info("服务："+servername+" 初始化成功");
                }
                return;
            }
            if ("heartbeat".equals(type)) {
                return;
            }
            return;
        }

    }


    public void writeInternalException(String msg) {
        writeException(ErrorCode.INTERNAL_EXCEPTION, msg);
    }

    public void writeException(int code, String msg) {
        JSONObject exception = Common.getJSONObject("type", "inner", "data", Common.getJSONObject("type", "exception", "code", code, "msg", msg));
        ctx.writeAndFlush(exception);
    }

    static AtomicInteger vint = new AtomicInteger();

    /**
     * 获取非0数
     *
     * @return
     */
    public int getlinksum() {
        int linksum = vint.addAndGet(1);
        if (linksum == 0) {
            return getlinksum();
        }
        return linksum;
    }

    /**
     * 同步发送数据的方法
     *
     * @param message 发送的数据
     * @return 返回的数据
     */
    @Override
    public JSONObject sendForResult(JSONObject message) {
        int linksum = getlinksum();
        JSONObject meta = message.getJSONObject("meta");
        int outTime = 60 * 1000;;
        if (meta!=null){
            outTime = meta.getIntValue("outTime");
            if (outTime <= 0) {
                outTime = 60 * 1000;
            }
        }
        message.put("linksum", linksum);
        LinkObject link = new LinkObject();
        synchronized (link) {
            linkMap.put(linksum, link);
            log.debug("sendForResult--message:{}", message);
            ctx.writeAndFlush(message);
            link.state = LinkObject.SendType.SENDED;
            try {
                link.wait(outTime);
            } catch (InterruptedException e) {
                return Common.getNormalFalseJson(e.getMessage());
            }
            linkMap.remove(linksum);
            if (link.state == LinkObject.SendType.SENDED) {
                return Common.getNormalFalseJson("数据返回超时");
            }
            return link.data;
        }
    }


    /**
     * 异常
     *
     * @param ctx   ChannelHandlerContext对象
     * @param cause Throwable对象
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof Exception) {
            writeException(ErrorCode.INTERNAL_EXCEPTION, "服务发送错误，exception：" + StringUtils.getExceptionStack((Exception) cause));
        }
        log.error(cause.getMessage(), cause);
    }

    private static class LinkObject {

        /**
         * 数据
         */
        JSONObject data;

        /**
         * 0:未发 1:已发 2:接受
         */
        SendType state = SendType.NOSEND;

        private enum SendType {
            NOSEND, SENDED, RECEIVED
        }
    }
}