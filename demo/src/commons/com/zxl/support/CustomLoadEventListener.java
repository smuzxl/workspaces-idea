package com.zxl.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.event.LoadEvent;
import org.hibernate.event.def.DefaultLoadEventListener;
import org.hibernate.persister.entity.EntityPersister;

import java.util.HashSet;
import java.util.Set;

public class CustomLoadEventListener extends DefaultLoadEventListener {
    private static final long serialVersionUID = 7397843707566999735L;
    private static boolean enableAll2ndCache = true;
    private static Set<String> disabledList = new HashSet<String>();
    protected final Log logger = LogFactory.getLog(getClass());

    public CustomLoadEventListener() {
        enableAll2ndCache = true;
        logger.warn("Hibernate Using Customer LoadEventListener:" + this.getClass());
    }

    public static void addDisabledEntity(String entity) {
        disabledList.add(entity);
        enableAll2ndCache = false;
    }

    public static void enableAll2ndCache() {
        enableAll2ndCache = true;
        disabledList.clear();
    }

    @Override
    protected Object loadFromSecondLevelCache(LoadEvent event, EntityPersister persister, LoadType options) {
        if (enableAll2ndCache || !disabledList.contains(persister.getEntityName())) {
            return super.loadFromSecondLevelCache(event, persister, options);
        }
        return null;
    }
}
