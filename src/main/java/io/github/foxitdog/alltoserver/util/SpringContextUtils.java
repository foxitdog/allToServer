package io.github.foxitdog.alltoserver.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils {

	@Autowired
	public static ApplicationContext applicationContext;

	/**
	 * @return the applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @param ac the applicationContext to set
	 */
	@Autowired
	public void setApplicationContext(ApplicationContext ac) {
		applicationContext = ac;
	}
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}
	public static <T> T getBean(String name,Class<T> clazz) {
		return applicationContext.getBean(name,clazz);
	}
}
