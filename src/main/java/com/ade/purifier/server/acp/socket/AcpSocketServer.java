package com.ade.purifier.server.acp.socket;

import com.ade.purifier.server.acp.socket.codec.ByteCodecFactory;
import com.ade.purifier.server.acp.config.AcpSocketProperties;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * Created by ismeade on 2014/8/29.
 */
public class AcpSocketServer {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private static AcpSocketServer instance = new AcpSocketServer();

    private SocketAcceptor acceptor;

    public static AcpSocketServer getInstance() {
        return instance;
    }

    private void initialize() {
        this.acceptor = new NioSocketAcceptor(AcpSocketProperties.VALUE_SERVER_PROCESSORCOUNT);
        Executor threadPool = Executors.newFixedThreadPool(AcpSocketProperties.VALUE_SERVER_NTHREADS);
        this.acceptor.getFilterChain().addLast("exector", new ExecutorFilter(threadPool));
        this.acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ByteCodecFactory()));
        this.acceptor.setReuseAddress(true);  //设置的是主服务监听的端口可以重用
        this.acceptor.getSessionConfig().setReuseAddress(true); //设置每一个非主监听连接的端口可以重用
        this.acceptor.getSessionConfig().setReceiveBufferSize(AcpSocketProperties.VALUE_SERVER_RECEIVEBUFFERSIZE);
        this.acceptor.getSessionConfig().setSendBufferSize(AcpSocketProperties.VALUE_SERVER_SENDBUFFERSIZE);
        this.acceptor.getSessionConfig().setTcpNoDelay(true); //设置为非延迟发送

        this.acceptor.getSessionConfig().setWriteTimeout(10000);
        this.acceptor.getSessionConfig().setWriterIdleTime(100000);

        this.acceptor.setBacklog(AcpSocketProperties.VALUE_SERVER_BACKLOG);
        this.acceptor.setDefaultLocalAddress(new InetSocketAddress(AcpSocketProperties.VALUE_SERVER_PORT));
        this.acceptor.setHandler(new AcpSocketServerHandler());
    }

    public void startup() {
        initialize();
        try {
            this.acceptor.bind();
            logger.info("socket server startup. port: " + AcpSocketProperties.VALUE_SERVER_PORT);
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

}
