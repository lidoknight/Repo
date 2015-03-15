package com.danny;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author 12070642
 * 
 */
public class FilterConfig {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected static final String CONF_PATH = "/cacheConfig.properties";
    /**
     * SCM节点
     */
    protected volatile boolean powerOn = true;

    protected volatile int threshold = 1;

    protected volatile int capacity = 100;

    protected final String name;

    protected final String path;

    private CountDownLatch connectedSignal = new CountDownLatch(1);

    private String appConfigNode;

    protected FilterConfig(String name) {
        this.name = name;
        this.path = CONF_PATH;
        // config();
    }

    protected FilterConfig(String name, String path) {
        this.name = name;
        this.path = path;
        // config();
    }

    protected void config() throws IOException, InterruptedException, KeeperException {
        ZooKeeper zk = new ZooKeeper("", 2000, new Watcher() {

            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == KeeperState.SyncConnected) {
                    connectedSignal.countDown();
                }
            }

        });
        connectedSignal.await();
        if (appConfigNode == null) {
            appConfigNode = zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zk.sync(path, null, null);
            // 重新绑定zk
        }
        String appConfig = new String(zk.getData(path, new Watcher() {

            @Override
            public void process(WatchedEvent event) {
                if (event.getType() == EventType.NodeDataChanged) {
                    try {
                        config();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, null), Charset.defaultCharset());
        if (StringUtils.isNotEmpty(appConfig)) {
            Properties props = new Properties();
            try {
                props.load(new ByteArrayInputStream(appConfig.getBytes(Charset.defaultCharset())));
                powerOn = Boolean.valueOf(props.getProperty("powerOn"));
                threshold = Integer.valueOf(props.getProperty("threshold"));
                capacity = Integer.valueOf(props.getProperty("capacity"));
            } catch (IOException e) {
                LOGGER.error("SCM Config meet IO Exception:", e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 
     * 获取阀值
     * 
     * @return
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * 
     * 设置阀值
     * 
     * @return
     */
    public void setThreshold(int threshold) {
        LOGGER.info("Cache Throttle Threshold is " + threshold);
        this.threshold = threshold;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        LOGGER.info("Cache Throttle Capacity is " + capacity);
        this.capacity = capacity;
    }

    /**
     * 开关打开?
     * 
     * @return
     */
    public boolean isOpen() {
        return powerOn;
    }

}
