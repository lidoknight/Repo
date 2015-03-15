package com.danny;

import java.util.List;

/**
 * 用于构建缓存限流器键值
 * 
 * @author 12070642
 * 
 * @param <T>
 */
public interface CacheKeyGenerator<T> {

    List<String> get(T t);
}
