package io.github.foxitdog;

import com.alibaba.fastjson.JSONObject;
import io.github.foxitdog.alltoserver.util.Common;
import io.github.foxitdog.alltoserver.util.OkHttpUtils;
import org.junit.Test;

import java.io.IOException;

public class ApplicationTest {

    @Test
    public void getServerData() {
        try {
            System.out.println(getData("test1", "我是你巴巴变"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getData(String servername, Object msg) throws IOException {
        JSONObject response = OkHttpUtils.postJson("http://localhost:8080/", Common.getJSONObject("server", servername, "data", msg).toJSONString());
        return response.get("data");
    }
}