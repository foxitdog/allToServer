package io.github.foxitdog.alltoserver.controller;

import com.alibaba.fastjson.JSONObject;
import io.github.foxitdog.alltoserver.datachannel.DataChannel;
import io.github.foxitdog.alltoserver.util.Common;
import io.github.foxitdog.alltoserver.util.Constants;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class DataController {
    @RequestMapping(value = {"/"}, method = POST)
    public JSONObject index(@RequestBody JSONObject orginData) {
        String target = orginData.getString("server");
        DataChannel dc = Constants.channelTable.get(target);
        if (dc == null) {
            orginData.put("data",Common.getNormalFalseJson("未找到指定服务"));
            return orginData;
        }
        orginData.put("type","request");
        return dc.sendForResult(orginData);
    }
}
