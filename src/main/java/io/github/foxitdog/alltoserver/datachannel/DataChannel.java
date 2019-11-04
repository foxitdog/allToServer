package io.github.foxitdog.alltoserver.datachannel;

import com.alibaba.fastjson.JSONObject;

public interface DataChannel {

    JSONObject sendForResult(JSONObject data);

}
