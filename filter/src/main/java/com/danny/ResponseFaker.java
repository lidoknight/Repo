package com.danny;

import java.util.List;

/**
 * 用于构建返回信息
 * 
 * @author 12070642
 *
 * @param <Q>
 * @param <R>
 */
public interface ResponseFaker<Q, R> {
    
    List<R> fakeResponse(Q request);

}
