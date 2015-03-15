package com.danny;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class CacheThrottleFacade<T, K> {
    CacheThrottle throttle;
    GenericThrottleCompFactory factory;

    public CacheThrottleFacade(String name, String path, GenericThrottleCompFactory factory) {
        throttle = CacheThrottle.newInstance(name, path);
        this.factory = factory;
    }

    public boolean open() {
        return this.throttle.isOpen();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<String> filterUp(List<T> requests, Collection responses, boolean isPojo) {
        List<String> keys = new ArrayList<String>();
        if (throttle.needLimit()) {
            if (!CollectionUtils.isEmpty(requests)) {
                Go: {
                    for (T request : requests) {
                        List<String> re = factory.generateKey(throttle.name, request);
                        for (String r : re) {
                            if (throttle.preCheck(r)) {
                                throttle.increase(r);
                                keys.add(r);
                            } else {
                                responses.addAll(factory.responseFake(throttle.name, isPojo ? requests.get(0)
                                        : requests));
                                break Go;
                            }
                        }
                    }
                }
            }
        }
        return keys;
    }

    // public List<String> filterUp(Collection<T> requests, Collection<K> responses) {
    // List<String> keys = new ArrayList<String>();
    // if (throttle.needLimit()) {
    // if (!CollectionUtils.isEmpty(requests)) {
    // Iterator<T> itr = requests.iterator();
    // while (itr.hasNext()) {
    // T request = itr.next();
    // String key = factory.generateKey(sourceType, targetType, request);
    // if (throttle.preCheck(key)) {
    // throttle.increase(key);
    // keys.add(key);
    // } else {
    // if (null == responses) {
    // responses = new ArrayList<K>();
    // }
    // responses.add(factory.responseFake(sourceType, targetType, request));
    // itr.remove();
    // }
    // }
    // }
    // }
    // return keys;
    // }

    public void filterDown(List<String> keys) {
        if (throttle.isOpen()) {
            if (!CollectionUtils.isEmpty(keys)) {
                for (String key : keys) {
                    throttle.decrease(key);
                }
            }
        }
    }

}
