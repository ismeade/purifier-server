package com.ade.purifier.main;

import com.ade.purifier.server.acp.socket.AcpSocketServer;
import com.ade.purifier.server.acp.socket.codec.ByteSecret;
import com.ade.purifier.server.app.socket.AppSocketServer;
import com.ade.purifier.service.web.TomcatServer;
import org.apache.catalina.LifecycleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Created by ismeade on 2014/11/11.
 */
public class Main {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        if (!ByteSecret.initialize()) {
            logger.error("加密模块加载错误.");
            return;
        }

//        try {
//            new TomcatServer().startServer();
//            logger.info("tomcat startup...");
//        } catch (LifecycleException e) {
//            e.printStackTrace();
//        }

        AcpSocketServer acpSocketServer = AcpSocketServer.getInstance();
        acpSocketServer.startup();

        AppSocketServer appSocketServer = AppSocketServer.getInstance();
        appSocketServer.startup();
    }

}
