package com.zxl.repository.impl;

import com.zxl.model.BaseObject;
import com.zxl.repository.BaseDao;
import com.zxl.util.BeanUtil;
import org.apache.commons.collections.list.UnmodifiableList;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.cache.access.EntityRegionAccessStrategy;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.Serializable;
import java.util.*;

public class BaseDaoImpl implements BaseDao, InitializingBean {
    @Autowired
    @Qualifier("hibernateTemplate")
    protected HibernateTemplate hibernateTemplate;
    private Set<String> cachable = new HashSet<String>();
    private Map<String, String> idNameMap = new HashMap<String, String>();
    /*private Map<String, Gcache<String, Serializable>> cachedUkeyMap = new HashMap<String, Gcache<String, Serializable>>();*/
    private List<String> entityClassList = new ArrayList<String>();

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public <T extends BaseObject> T saveObject(T entity) {
        if (entity != null) {
            // if(entity instanceof LocalCachable &&
            // ((LocalCachable)entity).fromCache()){
            // throw new StaleLocalObjectException();
            // }
            hibernateTemplate.saveOrUpdate(entity);
        }
        return entity;
    }

    @Override
    public <T extends BaseObject> T getObject(Class<T> clazz, Serializable id) {
        if (id == null || clazz == null)
            return null;
        T o = hibernateTemplate.get(clazz, id);
        return o;
    }

    @Override
    public <T extends BaseObject> List<T> getAllObjects(Class<T> clazz) {
        if (clazz == null)
            return null;
        if (cachable.contains(clazz.getCanonicalName())) {
            // 锟斤拷锟斤拷锟斤拷锟�
            DetachedCriteria query = DetachedCriteria.forClass(clazz);
            query.setProjection(Projections.id());
            List idList = hibernateTemplate.findByCriteria(query);
            return getObjectList(clazz, idList);
        } else {
            List<T> result = hibernateTemplate.loadAll(clazz);
            return result;
        }
    }

    @Override
    public <T extends BaseObject> T removeObject(Class<T> clazz, Serializable id) {
        if (id == null || clazz == null)
            return null;
        T o = getObject(clazz, id);
        if (o != null)
            hibernateTemplate.delete(o);
        return o;
    }

    @Override
    public <T extends BaseObject> T removeObject(T object) {
        if (object != null)
            hibernateTemplate.delete(object);
        return object;
    }

    @Override
    public <T extends BaseObject, S extends Serializable> List<T> getObjectList(Class<T> clazz, Collection<S> idList) {
        if (idList == null || idList.isEmpty())
            return new ArrayList<T>(0);
        if (cachable.contains(clazz.getCanonicalName())) {
            return getObjectListUsingCache(clazz, idList);
        } else {
            return getObjectBatch(clazz, idNameMap.get(clazz.getCanonicalName()), idList);
        }
    }

    private <T extends BaseObject, S extends Serializable> List<T> getObjectListUsingCache(Class<T> clazz,
                                                                                           Collection<S> idList) {
        List<T> result = new ArrayList<T>();
        T obj = null;
        long t = System.currentTimeMillis();
        int count = 0;
        for (S id : idList) {
            obj = getObject(clazz, id);
            if (obj != null) {
                result.add(obj);
            }
            count++;
            if (System.currentTimeMillis() - t > DateUtils.MILLIS_PER_MINUTE * 2 && count < 100) {
                // 100锟斤拷锟斤拷锟节ｏ拷锟斤拷锟斤拷2min锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟解，直锟斤拷锟斤拷锟届常
                throw new IllegalArgumentException("cache too slow!!");
            }
        }
        return result;
    }

    @Override
    public <T extends BaseObject, S extends Serializable> List<T> getObjectBatch(Class<T> clazz, String propertyName,
                                                                                 Collection<S> valueList) {
        List<S> vlist = null;
        if (valueList instanceof List)
            vlist = (List) valueList;
        else
            vlist = new ArrayList<S>(valueList);
        List<List<S>> groupList = BeanUtil.partition(vlist, 500);
        List<T> result = new ArrayList<T>();
        for (List<S> group : groupList) {
            DetachedCriteria query = DetachedCriteria.forClass(clazz);
            query.add(Restrictions.in(propertyName, group));
            List rows = hibernateTemplate.findByCriteria(query);
            result.addAll(rows);
        }
        return result;
    }

    @Override
    public <T extends BaseObject> void removeObjectList(Collection<T> entityList) {
        for (T entity : entityList) {
            if (entity != null)
                removeObject(entity);
        }
    }

    @Override
    public <T extends BaseObject> void saveObjectList(Collection<T> entityList) {
        for (T entity : entityList) {
            if (entity != null)
                saveObject(entity);
        }
    }

    @Override
    public <T extends BaseObject> void updateObjectList(Collection<T> entityList) {
        for (T entity : entityList) {
            if (entity != null) {
                hibernateTemplate.update(entity);
            }
        }
    }

    @Override
    public <T extends BaseObject> int getObjectCount(Class<T> clazz) {
        DetachedCriteria query = DetachedCriteria.forClass(clazz);
        query.setProjection(Projections.rowCount());
        List result = hibernateTemplate.findByCriteria(query);
        if (result.isEmpty())
            return 0;
        return Integer.parseInt("" + result.get(0));
    }

    @Override
    public <T extends BaseObject> T removeObjectById(Class<T> clazz, Serializable id) {
        T entity = getObject(clazz, id);
        if (entity == null)
            return null;
        removeObject(entity);
        return entity;
    }

    @Override
    public <T extends BaseObject> T updateObject(T entity) {
        /*
		 * if (entity instanceof LocalCachable && ((LocalCachable)
		 * entity).fromCache()) { throw new StaleLocalObjectException(); }
		 */
        hibernateTemplate.update(entity);
        return entity;
    }

    @Override
    public <T extends BaseObject> T addObject(T entity) {
		/*
		 * if (entity instanceof LocalCachable && ((LocalCachable)
		 * entity).fromCache()) { throw new StaleLocalObjectException(); }
		 */
        hibernateTemplate.save(entity);
        return entity;
    }

    @Override
    public <T extends BaseObject> void addObjectList(Collection<T> entityList) {
        for (T entity : entityList) {
            if (entity != null)
                addObject(entity);
        }
    }

    @Override
    public <T extends BaseObject> List<T> getObjectList(Class<T> clazz, String orderField, boolean asc, int from,
                                                        int rows) {
        DetachedCriteria query = DetachedCriteria.forClass(clazz);
        if (asc)
            query.addOrder(Order.asc(orderField));
        else
            query.addOrder(Order.desc(orderField));
        List result = hibernateTemplate.findByCriteria(query, from, rows);
        return result;
    }

    @Override
    public <T extends BaseObject> void saveObjectList(T... entityList) {
        for (T entity : entityList) {
            if (entity != null)
                saveObject(entity);
        }
    }

    @Override
    public <T extends BaseObject> Map getObjectPropertyMap(Class<T> clazz, String keyname, String valuename) {
        Map result = new HashMap();
        DetachedCriteria query = DetachedCriteria.forClass(clazz);
        query.setProjection(Projections.projectionList().add(Projections.property(keyname), keyname)
                .add(Projections.property(valuename), valuename));
        query.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
        List entryList = hibernateTemplate.findByCriteria(query);
        for (Object entry : entryList)
            result.put(((Map) entry).get(keyname), ((Map) entry).get(valuename));
        return result;
    }

    @Override
    public <T extends BaseObject, S extends Serializable> Map getObjectPropertyMap(Class<T> clazz, String keyname,
                                                                                   String valuename, Collection<S> idList) {
        Map result = new HashMap();
        // TODO:
        for (S id : idList) {
            T entity = getObject(clazz, id);
            if (entity != null)
                result.put(BeanUtil.get(entity, keyname), BeanUtil.get(entity, valuename));
        }
        return result;
    }

    @Override
    public <T extends BaseObject> List<T> getObjectListByField(Class<T> clazz, String fieldname, Serializable fieldvalue) {
        DetachedCriteria query = DetachedCriteria.forClass(clazz);
        query.add(Restrictions.eq(fieldname, fieldvalue));
        if (cachable.contains(clazz.getCanonicalName())) {// 锟斤拷锟斤拷锟斤拷锟斤拷炔锟窖疘D
            query.setProjection(Projections.id());
            List idList = hibernateTemplate.findByCriteria(query);
            return getObjectListUsingCache(clazz, idList);
        } else {
            List result = hibernateTemplate.findByCriteria(query);
            return result;
        }
    }

    @Override
    public <T extends BaseObject, S extends Serializable> List<S> getObjectPropertyList(Class<T> clazz,
                                                                                        String propertyname) {
        return getObjectPropertyList(clazz, propertyname, false);
    }

    @Override
    public <T extends BaseObject, S extends Serializable> List<S> getObjectPropertyList(Class<T> clazz,
                                                                                        String propertyname, boolean isDistinct) {
        DetachedCriteria query = DetachedCriteria.forClass(clazz);
        if (isDistinct)
            query.setProjection(Projections.distinct(Projections.property(propertyname)));
        else
            query.setProjection(Projections.property(propertyname));
        List result = hibernateTemplate.findByCriteria(query);
        return result;
    }

    private <T extends BaseObject> T getByUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue) {
        DetachedCriteria query = DetachedCriteria.forClass(clazz);
        query.add(Restrictions.eq(ukeyName, ukeyValue));
        List result = hibernateTemplate.findByCriteria(query);
        if (result.isEmpty())
            return null;
        if (result.size() > 1)
            throw new IllegalStateException("锟斤拷询锟斤拷锟斤拷锟斤拷锟铰硷拷锟�" + clazz.getName() + ", ukey=" + ukeyName + ", value="
                    + ukeyValue);
        return (T) result.get(0);
    }

    @Override
    public <T extends BaseObject> T getObjectByUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue) {
        boolean cache = cachable.contains(clazz.getCanonicalName());
        if (!cache) {
            return getByUkey(clazz, ukeyName, ukeyValue);
        } else {
            Serializable id = getUkey(clazz, ukeyName, ukeyValue);
            T result = null;
            if (id == null) {
                id = getIdByUkey(clazz, ukeyName, ukeyValue);
                if (id != null) {
                    result = getObject(clazz, id);
                    setUkey(clazz, ukeyName, ukeyValue, id);
                }
            } else {
                result = getObject(clazz, id);
                if (result == null) {// 锟斤拷锟斤拷锟节ｏ拷锟斤拷锟斤拷锟斤拷ID锟戒化锟截诧拷一锟斤拷
                    id = getIdByUkey(clazz, ukeyName, ukeyValue);
                    if (id != null) {// 锟斤拷锟斤拷id锟斤拷锟斤拷耍锟斤拷锟斤拷锟斤拷锟斤拷没锟斤拷锟�
                        setUkey(clazz, ukeyName, ukeyValue, id);
                        result = getObject(clazz, id);
                    } else {// 锟斤拷锟襟不达拷锟节ｏ拷锟斤拷锟�
                        setUkey(clazz, ukeyName, ukeyValue, null);
                    }
                }
            }
            return result;
        }
    }

    private <T extends BaseObject> Serializable getIdByUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue) {
        DetachedCriteria query = DetachedCriteria.forClass(clazz);
        query.add(Restrictions.eq(ukeyName, ukeyValue));
        query.setProjection(Projections.id());
        List result = hibernateTemplate.findByCriteria(query);
        if (result.isEmpty())
            return null;
        if (result.size() > 1)
            throw new IllegalStateException("锟斤拷询锟斤拷锟斤拷锟斤拷锟铰硷拷锟�" + clazz.getName() + ", ukey=" + ukeyName + ", value="
                    + ukeyValue);
        return (Serializable) result.get(0);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, ClassMetadata> allData = hibernateTemplate.getSessionFactory().getAllClassMetadata();
        for (String clazz : allData.keySet()) {
            AbstractEntityPersister persister = (AbstractEntityPersister) allData.get(clazz);
            EntityRegionAccessStrategy cas = persister.getCacheAccessStrategy();
            if (cas != null) {
                cachable.add(clazz);
				/*
				 * Gcache ukeyCache = new Gcache<String, Serializable>(500000);
				 * cachedUkeyMap.put(clazz, ukeyCache);
				 */
            }
            idNameMap.put(clazz, persister.getIdentifierPropertyName());
        }
        entityClassList = UnmodifiableList.decorate(new ArrayList<String>(idNameMap.keySet()));
    }

    @Override
    public <T extends BaseObject> void refreshUpdateProperty(T entity, String property, Object value) {
        hibernateTemplate.refresh(entity);
        BeanUtil.set(entity, property, value);
        hibernateTemplate.update(entity);
    }

    @Override
    public <T extends BaseObject> Serializable getUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue) {
		/*
		 * Gcache<String, Serializable> ukeyCache =
		 * cachedUkeyMap.get(clazz.getCanonicalName()); if (ukeyCache != null) {
		 * Serializable idvalue = ukeyCache.getIfPresent(ukeyName + "_" +
		 * ukeyValue); if (idvalue != null) return idvalue; }
		 */
        return null;
    }

    @Override
    public <T extends BaseObject> void setUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue, Serializable id) {
		/*
		 * Gcache<String, Serializable> ukeyCache =
		 * cachedUkeyMap.get(clazz.getCanonicalName()); if (ukeyCache != null) {
		 * if (id != null) { ukeyCache.put(ukeyName + "_" + ukeyValue, id); }
		 * else { ukeyCache.invalidate(ukeyName + "_" + ukeyValue); } }
		 */
    }

    @Override
    public List<Map> getUkeyCacheStats() {
        return null;
		/*List<Map> result = new ArrayList<Map>(cachedUkeyMap.size());
		for (String key : cachedUkeyMap.keySet()) {
			Gcache ukeyCache = cachedUkeyMap.get(key);
			CacheStats stats = ukeyCache.getStartCacheStats();
			if (ukeyCache.size() > 0) {
				Map<String, String> statsMap = new LinkedHashMap<String, String>();
				statsMap.put("size", "" + ukeyCache.size());
				statsMap.put("hits", "" + stats.hitCount());
				statsMap.put("miss", "" + stats.missCount());
				statsMap.put("evicted", "" + stats.evictionCount());
				statsMap.put("class", key);
				result.add(statsMap);
			}
		}
		return result;*/
    }

    @Override
    public List findByCriteria(DetachedCriteria query) {
        return hibernateTemplate.findByCriteria(query);
    }

    @Override
    public List findByCriteria(DetachedCriteria query, int from, int maxnum) {
        return hibernateTemplate.findByCriteria(query, from, maxnum);
    }

    @Override
    public List findByHql(String hql, Object... values) {
        return hibernateTemplate.find(hql, values);
    }

    @Override
    public List<String> getEntityClassList() {
        return new ArrayList<String>(entityClassList);
    }

    public List<String> getCachableEntityClassList() {
        return new ArrayList<String>(cachable);
    }

}
