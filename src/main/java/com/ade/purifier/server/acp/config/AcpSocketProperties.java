package com.ade.purifier.server.acp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ismeade on 2014/8/18.
 */
public class AcpSocketProperties {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(AcpSocketProperties.class);

    private final static String FILE_NAME = "/acp_socket.properties";

    private final static String KEY_SERVER_PORT              = "server.port";
    private final static String KEY_SERVER_PROCESSORCOUNT    = "server.processorCount";
    private final static String KEY_SERVER_NTHREADS          = "server.nThreads";
    private final static String KEY_SERVER_RECEIVEBUFFERSIZE = "server.receiveBufferSize";
    private final static String KEY_SERVER_SENDBUFFERSIZE    = "server.sendBufferSize";
    private final static String KEY_SERVER_BACKLOG           = "server.backlog";

    public static int  VALUE_SERVER_PORT;
    public static int  VALUE_SERVER_PROCESSORCOUNT;
    public static int  VALUE_SERVER_NTHREADS;
    public static int  VALUE_SERVER_RECEIVEBUFFERSIZE;
    public static int  VALUE_SERVER_SENDBUFFERSIZE;
    public static int  VALUE_SERVER_BACKLOG;

    static {
        initialize();
    }

    private static void initialize() {
        logger.info("load file: " + FILE_NAME);
        InputStream in = null;
        try {
            in = AcpSocketProperties.class.getResourceAsStream(FILE_NAME);
            Properties properties = new Properties();
            properties.load(in);
            VALUE_SERVER_PORT              = Integer.parseInt(properties.getProperty(KEY_SERVER_PORT, "8095"));
            VALUE_SERVER_PROCESSORCOUNT    = Integer.parseInt(properties.getProperty(KEY_SERVER_PROCESSORCOUNT, "5"));
            VALUE_SERVER_NTHREADS          = Integer.parseInt(properties.getProperty(KEY_SERVER_NTHREADS, "10"));
            VALUE_SERVER_RECEIVEBUFFERSIZE = Integer.parseInt(properties.getProperty(KEY_SERVER_RECEIVEBUFFERSIZE, "1024"));
            VALUE_SERVER_SENDBUFFERSIZE    = Integer.parseInt(properties.getProperty(KEY_SERVER_SENDBUFFERSIZE, "10240"));
            VALUE_SERVER_BACKLOG           = Integer.parseInt(properties.getProperty(KEY_SERVER_BACKLOG, "100"));
        } catch (FileNotFoundException e) {
            logger.error(e.getLocalizedMessage() + " 没有找到配置文件:" + FILE_NAME);
        } catch (NumberFormatException e) {
            logger.error(e.getLocalizedMessage() + " 端口号和超时时间必须是数字!");
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

}
