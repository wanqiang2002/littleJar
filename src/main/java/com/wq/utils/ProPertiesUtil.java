package com.wq.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ProPertiesUtil {

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("spideXpath.properties");
    private static Map<String,String> map = new HashMap<String,String>();

    /**
     * 获取配置
     * @see ${fns:getConfig('adminPath')}
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null){
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

}
