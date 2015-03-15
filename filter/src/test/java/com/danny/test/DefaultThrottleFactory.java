package com.danny.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.danny.CacheKeyGenerator;
import com.danny.GenericThrottleCompFactory;
import com.danny.ResponseFaker;


@Component
public class DefaultThrottleFactory extends GenericThrottleCompFactory {

    public DefaultThrottleFactory() {
        // List->List
        registerComp("1", new CacheKeyGenerator<Req>() {

            @Override
            public List<String> get(Req t) {
                List<String> keys = new ArrayList<String>();
                keys.add(t.a);
                return keys;
            }
        }, new ResponseFaker<List<Req>, Resp>() {

            @Override
            public List<Resp> fakeResponse(List<Req> request) {
                List<Resp> resps = new ArrayList<Resp>();
                for (Req req : request) {
                    Resp resp = new Resp();
                    resp.b = req.a + "Faker";
                    resps.add(resp);
                }
                return resps;
            }
        });
        // Object->Object
        registerComp("2", new CacheKeyGenerator<Req>() {

            @Override
            public List<String> get(Req t) {
                List<String> keys = new ArrayList<String>();
                keys.add(t.a);
                return keys;
            }
        }, new ResponseFaker<Req, Resp>() {

            @Override
            public List<Resp> fakeResponse(Req request) {
                Resp resp = new Resp();
                resp.b = request.a + "Faker";
                return Arrays.asList(resp);
            }
        });

        // List->Object
        registerComp("3", new CacheKeyGenerator<Req>() {

            @Override
            public List<String> get(Req t) {
                List<String> keys = new ArrayList<String>();
                keys.add(t.a);
                return keys;
            }
        }, new ResponseFaker<List<Req>, Resp>() {

            @Override
            public List<Resp> fakeResponse(List<Req> request) {
                Resp resp = new Resp();
                resp.b = request.get(0).a + "Faker";
                return Arrays.asList(resp);
            }
        });

        // Object->List
        registerComp("4", new CacheKeyGenerator<Req>() {

            @Override
            public List<String> get(Req t) {
                List<String> keys = new ArrayList<String>();
                keys.add(t.a);
                return keys;
            }
        }, new ResponseFaker<Req, Resp>() {

            @Override
            public List<Resp> fakeResponse(Req request) {
                Resp resp = new Resp();
                resp.b = request.a + "Faker";
                return Arrays.asList(resp);
            }
        });

        // []->List
        registerComp("5", new CacheKeyGenerator<Req>() {

            @Override
            public List<String> get(Req t) {
                List<String> keys = new ArrayList<String>();
                keys.add(t.a);
                return keys;
            }
        }, new ResponseFaker<Req[], Resp>() {

            @Override
            public List<Resp> fakeResponse(Req[] request) {
                List<Resp> resps = new ArrayList<Resp>();
                for (Req req : request) {
                    Resp resp = new Resp();
                    resp.b = req.a + "Faker";
                    resps.add(resp);
                }
                return resps;
            }
        });
    }
}
