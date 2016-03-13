package com.zxl.service.impl;

import com.zxl.model.BaseObject;
import com.zxl.repository.BaseDao;
import com.zxl.service.DaoService;
import com.zxl.util.BeanUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DaoServiceImpl implements DaoService {
    @Autowired
    @Qualifier("baseDao")
    private BaseDao baseDao;
    @Autowired
    @Qualifier("hibernateTemplate")
    private HibernateTemplate hibernateTemplate;

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public void setHibernateTemplate(HibernateTemplate hbt) {
        hibernateTemplate = hbt;
    }

    @Override
    public <T extends BaseObject> T saveObject(T entity) {
        return baseDao.saveObject(entity);
    }

    @Override
    public <T extends BaseObject> T getObject(Class<T> clazz, Serializable id) {
        return baseDao.getObject(clazz, id);
    }

    @Override
    public <T extends BaseObject> List<T> getAllObjects(Class<T> clazz) {
        return baseDao.getAllObjects(clazz);
    }

    @Override
    public <T extends BaseObject> T removeObject(T entity) {
        return baseDao.removeObject(entity);
    }

    @Override
    public <T extends BaseObject> T removeObjectById(Class<T> clazz, Serializable id) {
        T entity = baseDao.getObject(clazz, id);
        if (entity == null) return null;
        this.removeObject(entity);
        return entity;
    }

    @Override
    public <T extends BaseObject> int getObjectCount(Class<T> clazz) {
        return baseDao.getObjectCount(clazz);
    }

    @Override
    public <T extends BaseObject> void removeObjectList(Collection<T> entityList) {
        baseDao.removeObjectList(entityList);
    }

    @Override
    public <T extends BaseObject> void saveObjectList(Collection<T> entityList) {
        baseDao.saveObjectList(entityList);
    }

    @Override
    public <T extends BaseObject> void addObjectList(Collection<T> entityList) {
        baseDao.addObjectList(entityList);
    }

    @Override
    public <T extends BaseObject, S extends Serializable> List<T> getObjectList(Class<T> clazz, Collection<S> idList) {
        return baseDao.getObjectList(clazz, idList);
    }

    @Override
    public <T extends BaseObject> List<T> getObjectList(Class<T> clazz, String orderField, boolean asc, int from, int rows) {
        return baseDao.getObjectList(clazz, orderField, asc, from, rows);
    }

    @Override
    public <T extends BaseObject> void saveObjectList(T... entityList) {
        for (T entity : entityList) {
            if (entity != null) baseDao.saveObject(entity);
        }
    }

    @Override
    public <T extends BaseObject, S extends Serializable> Map<S, T> getObjectMap(Class<T> clazz, Collection<S> idList) {
        Map<S, T> result = new HashMap<S, T>();
        for (S id : idList) {
            T obj = baseDao.getObject(clazz, id);
            if (obj != null) result.put(id, obj);
        }
        return result;
    }

    @Override
    public <T extends BaseObject> Map getObjectPropertyMap(Class<T> clazz, String keyname, String valuename) {
        return baseDao.getObjectPropertyMap(clazz, keyname, valuename);
    }

    @Override
    public <T extends BaseObject> T updateObject(T entity) {
        return baseDao.updateObject(entity);
    }

    @Override
    public <T extends BaseObject> T addObject(T entity) {
        return baseDao.addObject(entity);
    }

    @Override
    public <T extends BaseObject> T getObjectByUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue) {
        return baseDao.getObjectByUkey(clazz, ukeyName, ukeyValue);
    }

    @Override
    public <T extends BaseObject, S extends Serializable> Map getObjectPropertyMap(Class<T> clazz, String keyname, String valuename,
                                                                                   Collection<S> idList) {
        return baseDao.getObjectPropertyMap(clazz, keyname, valuename, idList);

    }

    @Override
    public List queryByRowsRange(final String hql, final int from, final int maxnum, final Object... params) {
        return hibernateTemplate.execute(new HibernateCallback<List>() {
            @Override
            public List doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                query.setFirstResult(from).setMaxResults(maxnum);
                if (params != null)
                    for (int i = 0, length = params.length; i < length; i++) {
                        query.setParameter(i, params[i]);
                    }
                return query.list();
            }
        });
    }

    @Override
    public <T extends BaseObject> List<T> getObjectListByField(Class<T> clazz, String fieldname, Serializable fieldvalue) {
        return baseDao.getObjectListByField(clazz, fieldname, fieldvalue);
    }

    @Override
    public <T extends BaseObject> T addPropertyNum(Class<T> clazz, Serializable id, String property, Integer num) {
        T entity = baseDao.getObject(clazz, id);
        if (entity != null) {
            try {
                PropertyUtils.setProperty(entity, property, (Integer) PropertyUtils.getProperty(entity, property) + num);
                baseDao.saveObject(entity);
            } catch (Exception e) {
            }
        }
        return entity;
    }

    @Override
    public <T extends BaseObject> void addPropertyNum(T entity, String property, Integer num) {
        hibernateTemplate.refresh(entity);    //���ڶ�̬���£���Ը���һ��ҲҪ����update�е��ֶ�����
        if (entity != null) {
            try {
                PropertyUtils.setProperty(entity, property, (Integer) PropertyUtils.getProperty(entity, property) + num);
                baseDao.saveObject(entity);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public <T extends BaseObject> T addPropertiesNum(Class<T> clazz, Serializable id, String properties, Integer... nums) {
        T entity = baseDao.getObject(clazz, id);
        if (entity != null) {
            String[] propertyList = StringUtils.split(properties, ",");
            addPropertiesNum(entity, propertyList, nums);
        }
        return entity;
    }

    @Override
    public <T extends BaseObject> void addPropertiesNum(T entity, String properties, Integer... nums) {
        if (entity != null) {
            hibernateTemplate.refresh(entity);    //���ڶ�̬���£���Ը���һ��ҲҪ����update�е��ֶ�����
            String[] propertyList = StringUtils.split(properties, ",");
            addPropertiesNum(entity, propertyList, nums);
        }
    }

    private <T extends BaseObject> void addPropertiesNum(T entity, String[] propertyList, Integer[] values) {
        if (propertyList.length == 0 || values == null || propertyList.length != values.length) return;
        int i = 0;
        for (String property : propertyList) {
            try {
                PropertyUtils.setProperty(entity, property, (Integer) PropertyUtils.getProperty(entity, property) + values[i]);
            } catch (Exception e) {
            }
            i++;
        }
        baseDao.saveObject(entity);
    }

    @Override
    public <T extends BaseObject> T updateProperties(Class<T> clazz, Serializable id, String properties, Serializable... values) {
        T entity = baseDao.getObject(clazz, id);
        String[] propertyList = properties.split(",");
        updateProperties(entity, propertyList, values);
        return entity;
    }

    @Override
    public <T extends BaseObject> void updateProperties(T entity, String properties, Serializable... values) {
        if (entity != null) {
            hibernateTemplate.refresh(entity);
            String[] propertyList = properties.split(",");
            updateProperties(entity, propertyList, values);
        }
    }

    private <T extends BaseObject> void updateProperties(T entity, String[] propertyList, Serializable... values) {
        if (propertyList.length == 0 || values == null || propertyList.length != values.length) return;
        int i = 0;
        for (String property : propertyList) {
            try {
                PropertyUtils.setProperty(entity, property, values[i]);
            } catch (Exception e) {
            }
            i++;
        }
        baseDao.saveObject(entity);
    }

    @Override
    public <T extends BaseObject, S extends Serializable> List<S> getObjectPropertyList(Class<T> clazz, String propertyname, boolean isDistinct) {
        return baseDao.getObjectPropertyList(clazz, propertyname, isDistinct);
    }

    @Override
    public <T extends BaseObject, S extends Serializable> S getObjectPropertyByUkey(Class<T> clazz, String ukeyname, Serializable ukeyvalue, String propertyname) {
        DetachedCriteria query = DetachedCriteria.forClass(clazz);
        query.add(Restrictions.eq(ukeyname, ukeyvalue));
        query.setProjection(Projections.property(propertyname));
        List result = hibernateTemplate.findByCriteria(query);
        return result.isEmpty() ? null : (S) result.get(0);
    }

    @Override
    public List<Map<String, Object>> queryMapBySQL(final String sql, final int from,
                                                   final int maxnum, final Object... params) {
        List result = hibernateTemplate.execute(new HibernateCallback<List>() {
            @Override
            public List doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql);
                query.setFirstResult(from).setMaxResults(maxnum).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                if (params != null)
                    for (int i = 0, length = params.length; i < length; i++) {
                        query.setParameter(i, params[i]);
                    }
                return query.list();
            }
        });
        return result;
    }

    @Override
    public <T extends BaseObject> Object getObjectProperty(Class<T> clazz, Serializable id, String propertyname) {
        T entity = baseDao.getObject(clazz, id);
        if (entity == null) return null;
        return BeanUtil.get(entity, propertyname);
    }

    @Override
    public <T extends BaseObject, S extends Serializable> List<T> getObjectBatch(Class<T> clazz, String propertyName, Collection<S> valueList) {
        return baseDao.getObjectBatch(clazz, propertyName, valueList);
    }

    @Override
    public <T extends BaseObject> void refreshUpdateProperty(T entity, String property, Object value) {
        baseDao.refreshUpdateProperty(entity, property, value);
    }

    @Override
    public List findByCriteria(DetachedCriteria query) {
        return baseDao.findByCriteria(query);
    }

    @Override
    public List findByCriteria(DetachedCriteria query, int from, int maxnum) {
        return baseDao.findByCriteria(query, from, maxnum);
    }

    @Override
    public List findByHql(String hql, Object... values) {
        return baseDao.findByHql(hql, values);
    }
}
