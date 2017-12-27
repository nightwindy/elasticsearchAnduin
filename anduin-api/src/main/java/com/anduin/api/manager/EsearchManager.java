package com.anduin.api.manager;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by windy on 2017/12/27.
 */
@Slf4j
@Component
public class EsearchManager {

    private TransportClient client;

    @Autowired
    private String elasticsearchHost;

    @Autowired
    private Integer elasticsearchPort;

    public Client getClient() {
        if(client == null) {
            Settings settings = Settings.builder()
                    .put("cluster.name", "my-application").build();
            try {
                client = new PreBuiltTransportClient(settings)
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticsearchHost), elasticsearchPort));
            } catch (UnknownHostException e) {
                log.error("初始化esearch client失败", e);
            }
        }
        return client;
    }
}
