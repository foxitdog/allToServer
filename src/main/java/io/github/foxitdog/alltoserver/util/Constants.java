package io.github.foxitdog.alltoserver.util;

import io.github.foxitdog.alltoserver.datachannel.DataChannel;

import java.util.concurrent.ConcurrentHashMap;

public class Constants {
    public static ConcurrentHashMap<String, DataChannel> channelTable = new ConcurrentHashMap<>(16);
}
