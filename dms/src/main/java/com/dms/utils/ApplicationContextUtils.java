package com.dms.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextUtils implements ApplicationContextAware {

	public static ApplicationContext context;
	
	//private ThreadLocal<BaseDao> dao=new ThreadLocal<BaseDao>();
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}

}
