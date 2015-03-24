package com.ade.purifier.server.app.socket;

import com.ade.purifier.server.app.config.AppSocketProperties;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * Created by ismeade on 2014/8/29.
 */
public class AppSocketServer {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private static AppSocketServer instance = new AppSocketServer();

    private SocketAcceptor acceptor;

    public static AppSocketServer getInstance() {
        return instance;
    }

    private void initialize() {
        this.acceptor = new NioSocketAcceptor(AppSocketProperties.VALUE_SERVER_PROCESSORCOUNT);
        Executor threadPool = Executors.newFixedThreadPool(AppSocketProperties.VALUE_SERVER_NTHREADS);
        this.acceptor.getFilterChain().addLast("exector", new ExecutorFilter(threadPool));
        this.acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        this.acceptor.setReuseAddress(true);  //设置的是主服务监听的端口可以重用
        this.acceptor.getSessionConfig().setReuseAddress(true); //设置每一个非主监听连接的端口可以重用
        this.acceptor.getSessionConfig().setReceiveBufferSize(AppSocketProperties.VALUE_SERVER_RECEIVEBUFFERSIZE);
        this.acceptor.getSessionConfig().setSendBufferSize(AppSocketProperties.VALUE_SERVER_SENDBUFFERSIZE);
        this.acceptor.getSessionConfig().setTcpNoDelay(true); //设置为非延迟发送
        this.acceptor.setBacklog(AppSocketProperties.VALUE_SERVER_BACKLOG);
        this.acceptor.setDefaultLocalAddress(new InetSocketAddress(AppSocketProperties.VALUE_SERVER_PORT));
        this.acceptor.setHandler(new AppSocketServerHandler());
    }

    public void startup() {
        initialize();
        try {
            this.acceptor.bind();
            logger.info("socket server startup. port: " + AppSocketProperties.VALUE_SERVER_PORT);
        } catch (IOException e) {
            logger.error("IOException: " + e.getLocalizedMessage(), e);
        }
    }

    public void shutdown() {
        if (this.acceptor != null) {
            this.acceptor.dispose(true);
        }
        this.acceptor = null;
        logger.info("socket server close.");
    }

    public void restart() {
        logger.info("socket server will restart.");
        shutdown();
        startup();
    }

    public static void main(String[] args) {
        try {
            AppSocketServer.getInstance().startup();
            Thread.sleep(10000);
            AppSocketServer.getInstance().shutdown();
            Thread.sleep(10000);
            AppSocketServer.getInstance().startup();
            Thread.sleep(10000);
            AppSocketServer.getInstance().restart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
