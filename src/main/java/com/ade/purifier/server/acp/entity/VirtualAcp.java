package com.ade.purifier.server.acp.entity;

import com.ade.purifier.utils.ByteUtils;
import com.ade.purifier.utils.MsgUtils;
import com.ade.purifier.utils.WeatherUtils;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 虚拟acp类，每台已连接的净化器对应一个对象，由VirtualAcpManager来管理
 * Created by ismeade on 2014/8/29.
 */
public class VirtualAcp {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private String    mac;
    private IoSession session;
    private Object    results;

    private boolean   busy = false;

    public VirtualAcp(String mac, IoSession session) {
        this.mac = mac;
        this.session = session;
        Thread t = new Thread(new AutoTask());
        t.start();
    }

    public boolean isConnected() {
        if (this.session == null) {
            return false;
        }
        return this.session.isConnected();
    }

    public Object send(Object message) {
        return this.send(message, 5000);
    }

    public Object send(Object message, long timeOut) {
        long startTime = System.currentTimeMillis();
        if (busy) {
            logger.info("mac:" + mac + " busy.");
        }
        while(busy) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // no happen
            }
            if (System.currentTimeMillis() - startTime > timeOut) {
                logger.error("busy time out.");
                return null;
            }
        }
        return this.send_(message, startTime - timeOut);
    }

    /**
     * 发指令给净化器
     * @param message 消息体
     * @return BusinessContext
     */
    private synchronized Object send_(Object message, long timeOut) {
        try {
            if (this.session == null) {
                logger.error("IoSession is null.");
                return null;
            }
            if (!this.session.isConnected()) {
                logger.error("The connection has been disconnected.");
                return null;
            }
            timeOut = timeOut <= 0 ? 5000 : timeOut;
            // 清空结果缓存
            this.results = null;
            logger.info("---> acp " + ByteUtils.toStr((byte[]) message));
            this.busy = true;
            this.session.write(message);
            long startTime = System.currentTimeMillis();
            while(this.results == null) {
                Thread.sleep(100);
                if (System.currentTimeMillis() - startTime > timeOut) {
                    logger.error("time out.");
                    return null;
                }
            }
        } catch(Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            this.busy = false;
        }
        return this.results;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public IoSession getSession() {
        return session;
    }

    public void setSession(IoSession session) {
        this.session = session;
    }

    public Object getResults() {
        return results;
    }

    public void setResults(Object results) {
        this.results = results;
    }

    public String toString() {
        return "[mac=" + mac + ", session=" + session + "]";
    }

    class AutoTask implements Runnable {

        @Override
        public void run() {

            while(true) {
                try {
                    Thread.sleep(1000);
                    if (session.isConnected()) {
                        Calendar c = new GregorianCalendar();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH) + 1;
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);
                        int second = c.get(Calendar.SECOND);
                        logger.info("发送物理时间-->" + session);
                        session.write(MsgUtils.createAcpInstruction((byte) 0x40, (byte) year, (byte) month, (byte) day, (byte) hour, (byte) minute, (byte) second));
                        Thread.sleep(500);
                        byte[] weat = WeatherUtils.getWeather(mac);
                        if (weat != null) {
                            byte[] in2 = MsgUtils.createAcpInstruction((byte) 0x8f, (byte)(weat[0] + 30), (byte)(weat[1] + 30), weat[2], weat[3], weat[4], weat[5]);
                            session.write(in2);
                        }
                    } else {
                        VirtualAcpManager.removeVirtualAcp(session.getId());
                    }
                    Thread.sleep(1800 * 1000);
                } catch (InterruptedException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }

        }

    }

}
