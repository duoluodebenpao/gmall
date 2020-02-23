package org.frank.common.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @Author: frank
 * @Date: 2019-12-14
 * @Description:
 *
 */
public class NacosClient {

    private static Logger logger = LoggerFactory.getLogger(NacosClient.class);

    private String serverAddr;
    ConfigService configService;


    public NacosClient(String serverAddr){

        this.serverAddr = serverAddr;
    }

    private void connect() throws NacosException{
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        try {
            configService = NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            logger.error("connect to nacos failed, {}",e);
        }
    }

    public String getConfig(String group,String dataId) throws NacosException {
            if (configService == null){
                connect();
            }
            return configService.getConfig(dataId,group,5000);
    }

    public void publishConfig(String group,String dataId,String data) throws NacosException {
        if (configService == null){
            connect();
        }
        configService.publishConfig(dataId,group,data);
    }


}
