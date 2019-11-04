package io.github.foxitdog.alltoserver.util;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MyCookies implements CookieJar {
	HashMap<String, List<Cookie>> cookieMap = new HashMap<>();

	@Override
	public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
		List<Cookie> pre = cookieMap.get(url.host());
		if (pre == null) {
			pre = new ArrayList<>();
			pre.addAll(cookies);
		} else {
			pre = pre.stream()
					.filter(cookie -> cookies.stream().noneMatch(ncookie -> cookie.name().equals(ncookie.name())))
					.collect(Collectors.toList());
			pre.addAll(cookies);
		}
		cookieMap.put(url.host(), pre);
	}

	@Override
	public List<Cookie> loadForRequest(HttpUrl url) {
		List<Cookie> pre = cookieMap.get(url.host());
		if (pre == null) {
			return new ArrayList<Cookie>();
		}
		return pre;
	}

}
