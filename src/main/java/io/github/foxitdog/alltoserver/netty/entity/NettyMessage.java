package io.github.foxitdog.alltoserver.netty.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * netty数据包
 */
@Setter
@Getter
@ToString
public final class NettyMessage {
    private static int LINKSUM=0;

    public NettyMessage() {
    }

    /**
     * 整体消息
     */
    private byte[] message;

    /**
     * 消息体
     */
    private byte[] body;

    /**
     * 命令字
     */
    private byte order;

    /**
     * 命令字
     */
    private int linkSum;

    /**
     * 消息长
     */
    private int len;

    public int getLen(){
      if (body!=null){
          return body.length;
      }
      return 0;
    };
    public static NettyMessage getNettyMessage(){
        NettyMessage nm=new NettyMessage();
        nm.setLinkSum(LINKSUM++);
        return nm;
    }
}