package io.github.foxitdog.alltoserver.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class OkHttpUtils {
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static final MediaType XML = MediaType.parse("text/xml; charset=UTF-8");
	public static final OkHttpClient client = new OkHttpClient.Builder().cookieJar(new MyCookies()).build();

	/**
	 * post请求 (xml)
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String postXML(String url, String content) throws IOException {
		return post(url,content,XML);
	}

	/**
	 * post 请求
	 * @param url
	 * @param content
	 * @param mediaType
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, String content, MediaType mediaType) throws IOException {
		if (mediaType == null) {
			mediaType = XML;
		}
		log.debug("post url:{}, content:{}",url,content);
		RequestBody body = RequestBody.create(mediaType, content);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		String result = response.body().string();
		log.debug("post url:{}, response:{}",url,result);
		return result;
	}

	/**
	 * post 请求
	 * @param url
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static JSONObject postJson(String url, String content) throws IOException {
		JSONObject json= (JSONObject) JSONObject.parse(post(url,content,JSON));
		return json;
	}

	public static byte[] postB(String url, String content, MediaType mediaType) throws IOException {
		if (mediaType == null) {
			mediaType = XML;
		}
		RequestBody body = RequestBody.create(mediaType, content);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().bytes();
	}

	/**
	 * get 请求
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String get(String url) throws IOException {
		log.debug("get url:{}",url);
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		String result = response.body().string();
		log.debug("post url:{}, response:{}",url,result);
		return result;
	}
}
