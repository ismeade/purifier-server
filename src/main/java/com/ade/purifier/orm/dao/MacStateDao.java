package com.ade.purifier.orm.dao;

import com.ade.purifier.orm.model.MacState;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by ismeade on 2014/8/23.
 */
public class MacStateDao {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private ObjectContext context;

    public MacStateDao(ObjectContext context) {
        this.context = context;
    }

    public MacState getMacState(int macId) {
        Expression expression = MacState.MAC_ID.eq(macId);
        SelectQuery<MacState> select = SelectQuery.query(MacState.class, expression);
        List<MacState> macStates = context.select(select);
        return macStates.size() > 0 ? macStates.get(0) : null;
    }

    public int insertMacState(int macId) {
        MacState macState = context.newObject(MacState.class);
        macState.setMacId(macId);
        try {
            context.commitChanges();
            return macState.getId();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return -1;
        }
    }

    public int insertMacState(int macId, int filter_age, int mac_age, int mac_run, int mac_gear, int mac_run_model, int mac_timing, int uv_state, int nions_state, int filter_replace_warning, int tipping_warning, int panel_open_warning, int indoor_temperature, int indoor_humidity, int smell_c, int indoor_proper, Date update_time) {
        MacState macState = context.newObject(MacState.class);
        macState.setMacId(macId);
        macState.setFilterAge(filter_age);
        macState.setMacAge(mac_age);
        macState.setMacGear(mac_gear);
        macState.setMacRun(mac_run);
        macState.setMacRunModel(mac_run_model);
        macState.setMacTiming(mac_timing);
        macState.setUvState(uv_state);
        macState.setNionsState(nions_state);
        macState.setFilterReplaceWarn(filter_replace_warning);
        macState.setTippingWarn(tipping_warning);
        macState.setPanelOpenWarn(panel_open_warning);
        macState.setIndoorTemperature(indoor_temperature);
        macState.setIndoorHumidity(indoor_humidity);
        macState.setSmellC(smell_c);
        macState.setIndoorProper(indoor_proper);
        macState.setUpdateTime(update_time);
        try {
            context.commitChanges();
            return macState.getId();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return -1;
        }
    }

    public MacState updateMacStateTime(int macId, int filter_age, int mac_age) {
        MacState macState = getMacState(macId);
        if (macState != null) {
            macState.setFilterAge(filter_age);
            macState.setMacAge(mac_age);
            try {
                context.commitChanges();
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        return macState;
    }

}
