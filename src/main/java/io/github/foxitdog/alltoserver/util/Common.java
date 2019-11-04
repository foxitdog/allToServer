package io.github.foxitdog.alltoserver.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.Assert;

public class Common {
    public static final JSONObject EMPTY_JSONOBJECT = new JSONObject();
    public static final JSONArray EMPTY_JSONARRAY = new JSONArray();

    /**
     * {result:false,code:1100,message:${message}}
     *
     * @param message
     * @return
     */
    public static JSONObject getNormalFalseJson(String message) {
        return getNormalFalseJson(ErrorCode.NORMAL_RESULT, message);
    }
    /**
     * {result:false,code:1000,message:${message}}
     *
     * @param message
     * @return
     */
    public static JSONObject getExceptionFalseJson(String message) {
        return getNormalFalseJson(ErrorCode.INTERNAL_EXCEPTION, message);
    }

    /**
     * {result:false,code:${code},message:${message}}
     *
     * @param code
     * @param message
     * @return
     */
    public static JSONObject getNormalFalseJson(int code, String message) {
        JSONObject json = new JSONObject();
        return getNormalFalseJson(code, message, json);
    }

    /**
     * {result:false,code:${code},message:${message}}
     *
     * @param code
     * @param message
     * @param json
     * @return
     */
    public static JSONObject getNormalFalseJson(int code, String message, JSONObject json) {
        json.put("result", false);
        json.put("code", code);
        json.put("message", message);
        return json;
    }

    /**
     * {result:true,data:${data},message:${message}}
     *
     * @param data
     * @return
     */
    public static JSONObject getNormalJson(Object data) {
        return getNormalJson(data, "");
    }

    /**
     * {result:true,data:${data},message:${message}}
     *
     * @param data
     * @param message
     * @return
     */
    public static JSONObject getNormalJson(Object data, String message) {
        JSONObject json = new JSONObject();
        return getNormalJson(data, message, json);
    }

    /**
     * {result:true,data:${data},message:${message}}
     *
     * @param data
     * @param message
     * @param json
     * @return
     */
    public static JSONObject getNormalJson(Object data, String message, JSONObject json) {
        json.put("result", true);
        json.put("data", data == null ? "" : data);
        json.put("message", message);
        return json;
    }

    /**
     * {result:true,message:${message}}
     *
     * @return
     */
    public static JSONObject getOkJson() {
        JSONObject json = new JSONObject();
        return getOkJson("", json);
    }

    /**
     * {result:true,message:${message}}
     *
     * @param message
     * @return
     */
    public static JSONObject getOkJson(String message) {
        JSONObject json = new JSONObject();
        return getOkJson(message, json);
    }

    /**
     * {result:true,message:${message}}
     *
     * @param message
     * @param json
     * @return
     */
    public static JSONObject getOkJson(String message, JSONObject json) {
        json.put("result", true);
        json.put("message", message);
        return json;
    }

    /**
     * {type:"type",data:data}
     *
     * @param type
     * @param data
     * @return
     */
    public static JSONObject getRequestJSON(String type, JSONObject data) {
        JSONObject jo = new JSONObject();
        jo.put("type", type);
        jo.put("data", data);
        return jo;
    }

    /**
     * {type:"type"}
     *
     * @param type
     * @return
     */
    public static JSONObject getRequestJSON(String type) {
        return getRequestJSON(type, null);
    }

    public static JSONObject getJSONObject(Object... obj) {
        Assert.isTrue(obj.length % 2 == 0, "必须是k,v成对出现");
        JSONObject json = new JSONObject();
        for (int i = 0; i < obj.length / 2; i++) {
            json.put(obj[i * 2].toString(), obj[i * 2 + 1]);
        }
        return json;
    }
}
