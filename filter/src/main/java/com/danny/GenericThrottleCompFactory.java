package com.danny;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

@SuppressWarnings("unchecked")
public abstract class GenericThrottleCompFactory {

    private Map<String, InnerCompPair> utils = new HashMap<String, InnerCompPair>();

    public <T> List<String> generateKey(String name, T source) {
        InnerCompPair util = utils.get(name);
        Assert.notNull(util, "Component has not be registered");
        return ((CacheKeyGenerator<T>) util.getCallback()).get(source);
    }

    public <T> List<?> responseFake(String name, T source) {
        InnerCompPair util = utils.get(name);
        Assert.notNull(util, "Component has not be registered");
        return ((ResponseFaker<T, ?>) util.getResponse()).fakeResponse(source);
    }

    public void registerComp(String name, CacheKeyGenerator<?> keyGen, ResponseFaker<?, ?> faker) {
        InnerCompPair util = new InnerCompPair(keyGen, faker);
        utils.put(name, util);
    }

    public static final class InnerCompPair {
        private final CacheKeyGenerator<?> callback;
        private final ResponseFaker<?, ?> response;

        public InnerCompPair(CacheKeyGenerator<?> callback, ResponseFaker<?, ?> response) {
            Assert.notNull(callback, "KeyGen Component must not be null");
            Assert.notNull(response, "FakeResponse Component must not be null");
            this.callback = callback;
            this.response = response;
        }

        public CacheKeyGenerator<?> getCallback() {
            return callback;
        }

        public ResponseFaker<?, ?> getResponse() {
            return response;
        }
    }

}
