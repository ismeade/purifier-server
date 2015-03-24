package com.ade.purifier.server.acp.entity;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 * Created by ismeade on 2014/8/29.
 */
public class VirtualAcpManager {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(VirtualAcpManager.class);

    private static ConcurrentMap<String, VirtualAcp> map = new ConcurrentHashMap<>();
    private static ConcurrentMap<Long, String> ids = new ConcurrentHashMap<>();

    public static void addVirtualAcp(String mac, IoSession session) {
        if (mac == null || "".equals(mac.trim())) {
            logger.error("mac is null or \"\".");
            return;
        }
        if (session == null) {
            logger.error("session is null.");
            return;
        }
        VirtualAcp virtualAcp = new VirtualAcp(mac, session);
        map.put(mac.toUpperCase(), virtualAcp);
        ids.put(session.getId(), mac.toUpperCase());
        logger.info("add new Virtual:" + virtualAcp);
    }

    public static VirtualAcp getVirtualAcp(String mac) {
        if (mac == null || "".equals(mac.trim())) {
            logger.error("mac is null or \"\"");
            return null;
        } else {
            return map.get(mac.toUpperCase());
        }
    }

    public static VirtualAcp getVirtualAcp(long id) {
        String mac = ids.get(id);
        if (mac == null || "".equals(mac.trim())) {
            logger.error("not find session's id=" + id);
            return null;
        } else {
            return map.get(mac.toUpperCase());
        }
    }

    public static VirtualAcp removeVirtualAcp(long id) {
        String mac = ids.remove(id);
        if (mac != null && !"".equals(mac.trim())) {
            logger.error("remove session's id=" + id);
            return map.remove(mac.toUpperCase());
        } else {
            return null;
        }

    }

    public static String[] getMacs() {
        return map.keySet().toArray(new String[0]);
    }

    public static VirtualAcp[] getVirtualAcps() {
        return map.values().toArray(new VirtualAcp[0]);
    }

}
