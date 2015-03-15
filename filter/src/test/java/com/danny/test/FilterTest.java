package com.danny.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:conf/spring/cis-filter.xml" })
public class FilterTest extends AbstractTestNGSpringContextTests {

    @Autowired
    Service service;

    @DataProvider(name = "params")
    public Object[][] readyParams() {
        List<Req> reqs = new ArrayList<Req>();
        String[] strs = new String[] { "a", "b", "c", "d", "a" };
        for (int i = 0; i < 5; i++) {
            Req req = new Req();
            req.a = strs[i];
            reqs.add(req);
        }
        ;
        return new Object[][] { new Object[] { reqs } };
    }

    /**
     * List->List
     */
    @Test
    public void scene1() {
        List<Req> reqs = new ArrayList<Req>();
        String[] strs = new String[] { "a", "b", "c", "d", "a" };
        for (int i = 0; i < 5; i++) {
            Req req = new Req();
            req.a = strs[i];
            reqs.add(req);
        }
        List<Resp> resps = service.serve(reqs);
        service.print(resps);
    }

    /**
     * List->List index=1
     */
    @Test
    public void scene2() {
        List<Req> reqs = new ArrayList<Req>();
        String[] strs = new String[] { "a", "b", "c", "d", "a" };
        for (int i = 0; i < 5; i++) {
            Req req = new Req();
            req.a = strs[i];
            reqs.add(req);
        }
        List<Resp> resps = service.serve("", reqs);
        service.print(resps);
    }

    /**
     * Object->Object
     * 
     * @param reqs
     */
    @Test(dataProvider = "params")
    public void scene3(List<Req> reqs) {
        Resp resp = service.serve(reqs.get(0));
        System.out.println(resp.b);
    }
    
    /**
     * List->Object
     * 
     * @param reqs
     */
    @Test(dataProvider = "params")
    public void scene4(List<Req> reqs) {
        Resp resp = service.serve2(reqs);
        System.out.println(resp.b);
    }
    
    /**
     * Object->List
     * 
     * @param reqs
     */
    @Test(dataProvider = "params")
    public void scene5(List<Req> reqs) {
        List<Resp> resp = service.serve3(reqs.get(0));
        service.print(resp);
    }
    
    /**
     * []->List
     * 
     * @param reqs
     */
    @Test(dataProvider = "params")
    public void scene6(List<Req> reqs) {
        List<Resp> resp = service.serve(reqs.toArray(new Req[reqs.size()]));
        service.print(resp);
    }

}
