package com.danny;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suning.framework.scm.client.SCMClient;
import com.suning.framework.scm.client.SCMClientImpl;
import com.suning.framework.scm.client.SCMListener;
import com.suning.framework.scm.client.SCMNode;

/**
 * 
 * @author 12070642
 * 
 */
public class FilterConfig1 {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected static final String CONF_PATH = "/cacheConfig.properties";
    /**
     * SCM节点
     */
    private volatile SCMNode appConfigNode = null;
    protected volatile boolean powerOn = true;

    protected volatile int threshold = 0;

    protected volatile int capacity = 100;

    protected final String name;

    protected final String path;

    private SCMListener listener = new SCMListener() {
        @Override
        public void execute(String oldValue, String newValue) {
            config();
        }
    };

    protected FilterConfig1(String name) {
        this.name = name;
        this.path = CONF_PATH;
    }

    protected FilterConfig1(String name, String path) {
        this.name = name;
        this.path = path;
        //config();
    }

    protected void config() {
        SCMClient client = SCMClientImpl.getInstance();
        if (appConfigNode == null) {
            appConfigNode = client.getConfig(path);
            appConfigNode.sync();
            appConfigNode.monitor(listener);
        }
        String appConfig = appConfigNode.getValue();
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
