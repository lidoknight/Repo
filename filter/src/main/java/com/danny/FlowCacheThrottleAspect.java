package com.danny;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * 缓存限流器切面
 * 
 * @author 12070642
 * 
 */
@Aspect
@Component
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FlowCacheThrottleAspect {

    @Autowired
    GenericThrottleCompFactory factory;

    @Pointcut("@annotation(com.suning.ebuy.cis.component.impl.filter.Filter)")
    public void filterPointCut() {

    }

    @Around("filterPointCut()")
    public Object invoke(ProceedingJoinPoint invocation) throws Throwable {
        Method method = ((MethodSignature) invocation.getSignature()).getMethod();
        Filter filter = AnnotationUtils.getAnnotation(method, Filter.class);
        CacheThrottleFacade facade = new CacheThrottleFacade(filter.name(), filter.path(), factory);
        if (facade.open()) {
            Class<?> returnType = method.getReturnType();
            int index = filter.requestIndex();
            Object[] args = invocation.getArgs();
            if (index < 0 || index >= args.length) {
                throw new ArrayIndexOutOfBoundsException("Filter Argumens Index is Out of Array");
            }
            Object request = args[index];
            if (request.getClass().isArray()) {
                // 数组处理
                Object[] elems = (Object[]) request;
                List arguments = new ArrayList();
                for (Object elem : elems) {
                    arguments.add(elem);
                }
                return handle(arguments, facade, invocation, returnType, false);
            } else if (request instanceof List) {
                // Collection处理
                List coll = (List) request;
                return handle(coll, facade, invocation, returnType, false);
            } else {
                // pojo处理
                return handle(Arrays.asList(request), facade, invocation, returnType, true);
            }
        } else {
            return invocation.proceed();
        }

    }

    private Object handle(List<?> requests, CacheThrottleFacade facade, ProceedingJoinPoint invocation,
            Class<?> returnType, boolean isPojo) throws Throwable {
        List responses = new ArrayList();
        List<String> keys = facade.filterUp(requests, responses, isPojo);
        try {
            if (CollectionUtils.isEmpty(responses)) {
                return invocation.proceed();
            } else {
                if (returnType.isArray()) {
                    return responses.toArray();
                } else if (Collection.class.isAssignableFrom(returnType)) {
                    return responses;
                } else {
                    return responses.get(0);
                }
            }
        } finally {
            facade.filterDown(keys);
        }
    }
}
