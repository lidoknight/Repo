package com.danny.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.danny.Filter;

@Component
public class Service {

    @Filter(name = "1", path = "")
    public List<Resp> serve(List<Req> reqs) {
        return convert(reqs, "OOOO");
    }

    @Filter(name = "1", path = "", requestIndex = 1)
    public List<Resp> serve(String flag, List<Req> reqs) {
        return convert(reqs, "OOOO");
    }

    @Filter(name = "2", path = "")
    public Resp serve(Req req) {
        return convert(req, "))))");
    }

    @Filter(name = "3", path = "")
    public Resp serve2(List<Req> req) {
        return convert(req.get(0), "))))");
    }

    @Filter(name = "4", path = "")
    public List<Resp> serve3(Req req) {
        return convert(Arrays.asList(req), "))))");
    }

    @Filter(name = "1", path = "")
    public List<Resp> serve(Req[] req) {
        return convert(Arrays.asList(req), "))))");
    }

    public void print(List<Resp> resps) {
        for (Resp resp : resps) {
            System.out.println(resp.b);
        }
    }

    private List<Resp> convert(List<Req> reqs, String info) {
        List<Resp> resps = new ArrayList<Resp>();
        for (Req req : reqs) {
            resps.add(convert(req, info));
        }
        return resps;
    }

    private Resp convert(Req req, String info) {
        Resp resp = new Resp();
        resp.b = req.a + info;
        return resp;
    }

}
