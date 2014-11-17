package com.cmbc.configserver.core.server;

import com.cmbc.configserver.common.ThreadFactoryImpl;
import com.cmbc.configserver.core.heartbeat.HeartbeatService;
import com.cmbc.configserver.core.processor.DefaultRequestProcessor;
import com.cmbc.configserver.core.service.ConfigServerService;
import com.cmbc.configserver.remoting.common.RequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * the controller of config server.It is the core class of the configserver-core project
 * Created by tongchuan.lin<linckham@gmail.com><br/>
 *
 * @Date 2014/10/31
 * @Time 11:12
 */
@Service("configServerController")
public class ConfigServerController {
    @Autowired
    private ConfigNettyServer configNettyServer;
    @Autowired
    private ConfigServerService configServerService;
    @Autowired
    private RequestProcessor defaultRequestProcessor;
    @Autowired
    private HeartbeatService heartbeatService;
    private ExecutorService remoteExecutor;

    public void setHeartbeatService(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }
    
    public void setDefaultRequestProcessor(RequestProcessor defaultRequestProcessor) {
        this.defaultRequestProcessor = defaultRequestProcessor;
    }

    public void setConfigNettyServer(ConfigNettyServer configNettyServer) {
        this.configNettyServer = configNettyServer;
    }

    public void setConfigServerService(ConfigServerService configServerService) {
        this.configServerService = configServerService;
    }


    public ConfigServerService getConfigServerService(){
        return this.configServerService;
    }
    /**
     * initialize the ConfigServer Controller
     * @return true if the controller initialize successfully,else false.
     */
    public boolean initialize() {
        this.remoteExecutor = Executors.newFixedThreadPool(this.configNettyServer.getNettyServerConfig().getServerWorkerThreads(), new ThreadFactoryImpl("ConfigServerExecutorThread_"));
        this.configNettyServer.initialize(this);
        this.registerProcessor();
        return true;
    }

    private void registerProcessor() {
        this.configNettyServer.getRemotingServer().registerDefaultProcessor(this.defaultRequestProcessor, this.remoteExecutor);
    }

    public void start() throws Exception{
        this.configNettyServer.start();
        this.configServerService.start();
        this.heartbeatService.start();
    }

    public void shutdown(){
        this.configNettyServer.getRemotingServer().shutdown();
        this.remoteExecutor.shutdown();
        this.configServerService.shutdown();
        this.heartbeatService.shutdown();
    }
}