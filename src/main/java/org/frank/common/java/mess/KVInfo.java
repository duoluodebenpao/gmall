package org.frank.common.java.mess;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author frank
 */
public class KVInfo extends HashMap<String, String> implements Serializable {

    public KVInfo(Map<? extends String, ? extends String> m) {
        super(m);
    }

    public KVInfo() {
        super();
    }

    /**
     * 获取一个整型的值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int getAsInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(this.get(key));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 获取一个long型字段
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long getAsLong(String key, long defaultValue) {
        try {
            return Long.parseLong(this.get(key));
        } catch (Exception ex) {
            return defaultValue;
        }
    }
}
