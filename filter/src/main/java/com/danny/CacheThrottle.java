package com.danny;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 内部缓存限流器
 * 
 * @author 12070642
 * 
 */
class CacheThrottle extends FilterConfig {

    private static ConcurrentHashMap<String, CacheThrottle> throttles = new ConcurrentHashMap<String, CacheThrottle>();
    private ConcurrentHashMap<String, AtomicInteger> cache = new ConcurrentHashMap<String, AtomicInteger>();

    /**
     * Constructor
     */
    private CacheThrottle(String name, String path) {
        super(name, path);
    }

    /**
     * 递增,你不能直接调用该方法
     * <p>
     * Usage:
     * </p>
     * <code>
     * CacheThrottle throttle = CacheThrottle.getInstance();
     * if(throttle.preCheck(key)){
     *     throttle.increase(key);
     * }
     * </code>
     * 
     * @param key
     */
    void increase(String key) {
        if (preCheck(key)) {
            while (null != cache.get(key)) {
                try {
                    cache.get(key).incrementAndGet();
                    break;
                } catch (NullPointerException e) {
                    // skip and handle in next segment
                }
            }
            // 存在一种极限情况,上块校验失败,到下块校验也失败的情况,此时会直接忽略缓存
            while (null == cache.get(key)) {
                if (null != cache.putIfAbsent(key, new AtomicInteger(1))) {
                    try {
                        cache.get(key).incrementAndGet();
                        break;
                    } catch (NullPointerException e) {
                    }
                }
            }
        }
    }

    /**
     * 递减
     * 
     * @param key
     */
    void decrease(String key) {
        try {
            if (null != cache.get(key) && cache.get(key).get() > 1) {
                cache.get(key).decrementAndGet();
            } else {
                if (null != cache.get(key) && cache.get(key).get() <= 1) {
                    // 存在一种极限情况,上块校验成功,但由于其他线程导致实际校验结果失败
                    cache.remove(key);
                }
            }
        } catch (NullPointerException e) {
            // has removed and skip
        }
    }

    /**
     * 预先校验:由于未做synchronize同步,因此当key相同且时间相同调用时会超出阀值些许.<br/>
     * 些许具体视同时进入该方法的线程数.
     * 
     * @param key
     * @return
     */
    boolean preCheck(String key) {
        if (powerOn) {
            AtomicInteger integer = cache.get(key);
            return integer == null ? threshold != 0 : integer.get() < threshold;
        }
        return false;
    }

    /**
     * 容量校验
     * 
     * @return
     */
    boolean rangeCheck() {
        return size() < capacity;
    }

    boolean needLimit() {
        return isOpen() && rangeCheck();
    }

    /**
     * 打开/关闭开关
     * 
     * @param on
     */
    void trigger(boolean on) {
        LOGGER.info("Cache Throttle Button is " + (on ? "ON" : "OFF"));
        this.powerOn = on;
        // 清空缓存
        this.cache.clear();
    }

    /**
     * 显示内容
     * 
     * @param key
     * @return
     */
    int showInfo(String key) {
        AtomicInteger integer = cache.get(key);
        return null != integer ? integer.get() : 0;
    }

    /**
     * 显示大小
     * 
     * @return
     */
    int size() {
        return cache.size();
    }

    /**
     * 显示Top信息
     * 
     * @return
     */
    String topInfo(int topSize) {
        // 复制一个局部Map结构
        Map<String, AtomicInteger> map = new HashMap<String, AtomicInteger>(cache);
        if (map.size() > 0) {
            StringBuilder sb = new StringBuilder();
            List<Entry<String, AtomicInteger>> list = new ArrayList<Entry<String, AtomicInteger>>(map.size());
            for (Entry<String, AtomicInteger> entry : map.entrySet()) {
                list.add(entry);
            }
            Collections.sort(list, new Comparator<Entry<String, AtomicInteger>>() {

                @Override
                public int compare(Entry<String, AtomicInteger> o1, Entry<String, AtomicInteger> o2) {
                    // 逆序
                    return o2.getValue().get() - o1.getValue().get();
                }
            });
            for (int i = 0; i < Math.min(list.size(), topSize); i++) {
                Entry<String, AtomicInteger> entry = list.get(i);
                String cmmdtyCode = entry.getKey().substring(0, 9);
                String supplier = entry.getKey().substring(9, 17);
                String plant = entry.getKey().substring(17, 21);
                String invLo = entry.getKey().substring(21, 25);
                String flag = entry.getKey().substring(25);
                final char split = '#';
                sb.append(cmmdtyCode).append(split).append(supplier).append(split).append(plant).append(split)
                        .append(invLo).append(split).append(flag);
                sb.append(":").append(entry.getValue().get()).append("\n");
            }
            return sb.toString();
        } else {
            return "EMPTY";
        }
    }

    static CacheThrottle newInstance(String name, String path) {
        CacheThrottle throttle = throttles.get(name);
        if (null == throttle) {
            throttle = new CacheThrottle(name, path);
            throttles.putIfAbsent(name, throttle);
        }
        return throttle;
    }

}