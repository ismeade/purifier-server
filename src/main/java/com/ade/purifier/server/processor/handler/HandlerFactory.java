package com.ade.purifier.server.processor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ismeade on 2014/8/31.
 */
public class HandlerFactory {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(HandlerFactory.class);

    private static ConcurrentMap<String, Class<? extends BasicHandler>> map = new ConcurrentHashMap<>();

    static {
        init();
    }

    private static void init() {
        setHandler("registrationReq", RegistrationHandler.class);
        setHandler("bindMacReq", BindMacHandler.class);
        setHandler("setCityReq", SetCityHandler.class);
        setHandler("loginReq", LoginHandler.class);
        setHandler("forgotReq", ForgotHandler.class);
        setHandler("onOffReq", OnOffHandler.class);
        setHandler("getAllReq", GetAllHandler.class);
        setHandler("useTimeReq", UseTimeHandler.class);
        setHandler("speedReq", SpeedHandler.class);
        setHandler("patternReq", PatternHandler.class);
        setHandler("sterilizeReq", SterilizeHandler.class);
        setHandler("timingReq", TimingHandler.class);
        setHandler("anionReq", AnionHandler.class);
        setHandler("lockReq", LockHandler.class);
        setHandler("cycleReq", CycleHandler.class);
        setHandler("standardReq", StandardHandler.class);
        setHandler("upgradeReq", UpgradeHandler.class);
        setHandler("resetTimeReq", ResetTimeHandler.class);
        setHandler("upPasswordReq", UpPasswordHandler.class);
        setHandler("findPasswordReq", FindPasswordHandler.class);
        // TODO
    }

    public static void setHandler(String type, Class<? extends BasicHandler> clazz) {
        map.put(type, clazz);
    }

    public static BasicHandler newHandler(String msgType) {
        if (msgType == null || "".equals(msgType.trim())) {
            logger.error("msgType is null or \"\"");
            return null;
        }
        Class<? extends BasicHandler> clazz = map.get(msgType);
        if (clazz == null) {
            logger.error("not find Class for key=" + msgType);
            return null;
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }


}
