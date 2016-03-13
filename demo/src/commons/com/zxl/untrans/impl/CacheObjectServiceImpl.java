//package com.zxl.untrans.impl;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.hibernate.criterion.DetachedCriteria;
//import org.hibernate.criterion.Projections;
//import org.hibernate.criterion.Restrictions;
//
//import com.zxl.model.BaseObject;
//import com.zxl.service.DaoService;
//import com.zxl.support.LocalCachable;
//import com.zxl.untrans.CacheObjectService;
//import com.zxl.util.DateUtil;
//import com.zxl.util.Gcache;
//import com.zxl.util.GcacheManger;
//import com.google.common.cache.Cache;
//
//public final class CacheObjectServiceImpl implements CacheObjectService {
//	private Map<String/* entityClass.getCanonicalName */, Gcache<String, Serializable>> cachedUkeyMap = new HashMap<String, Gcache<String, Serializable>>();
//
//	private DaoService daoService;
//
//	public void setDaoService(DaoService daoService) {
//		this.daoService = daoService;
//	}
//
//	@Override
//	public <T extends BaseObject, S extends Serializable> T getObject(Class<T> clazz, S id) {
//		if (id == null) {
//			return null;
//		}
//		Cache cacheObjectMap = GcacheManger.getCacheObjectMap(clazz);
//		if (cacheObjectMap == null) {
//			return daoService.getObject(clazz, id);
//		}
//		T result = (T) cacheObjectMap.getIfPresent(id);
//		if (result == null) {
//			result = daoService.getObject(clazz, id);
//			if (result instanceof LocalCachable) {
//				LocalCachable object = (LocalCachable) result;
//				if (object.cachable()) {
//					object.fix2Cache();
//					cacheObjectMap.put(id, result);
//				}
//			}
//		}
//		return result;
//	}
//
//	@Override
//	public <T extends BaseObject, S extends Serializable> List<T> getObjectList(Class<T> clazz, Collection<S> idList) {
//		if (idList == null || idList.isEmpty()) {
//			return new ArrayList(0);
//		}
//		List<T> resultList = getObjectListUsingCache(clazz, idList);
//		return resultList;
//	}
//
//	@Override
//	public <T extends BaseObject, S extends Serializable> T getObjectByUkey(Class<T> clazz, String ukeyName, S ukeyValue) {
//		Cache cacheObjectMap = GcacheManger.getCacheObjectMap(clazz);
//		if (cacheObjectMap == null) {
//			return getByUkey(clazz, ukeyName, ukeyValue);
//		} else {
//			Serializable id = getUkey(clazz, ukeyName, ukeyValue);
//			T result = null;
//			if (id == null) {
//				id = getIdByUkey(clazz, ukeyName, ukeyValue);
//				if (id != null) {
//					result = getObject(clazz, id);
//					setUkey(clazz, ukeyName, ukeyValue, id);
//				}
//			} else {
//				result = getObject(clazz, id);
//				if (result == null) {// 不存在，可能是ID变化重查一次
//					id = getIdByUkey(clazz, ukeyName, ukeyValue);
//					if (id != null) {// 对象id变更了，重新设置缓存
//						setUkey(clazz, ukeyName, ukeyValue, id);
//						result = getObject(clazz, id);
//					} else {// 对象不存在，清除
//						setUkey(clazz, ukeyName, ukeyValue, null);
//					}
//				}
//			}
//			return result;
//		}
//	}
//
//	@Override
//	public <T extends BaseObject> List<T> getObjectListByField(Class<T> clazz, String fieldname, Serializable fieldvalue) {
//		DetachedCriteria query = DetachedCriteria.forClass(clazz);
//		query.add(Restrictions.eq(fieldname, fieldvalue));
//		Cache cacheObjectMap = GcacheManger.getCacheObjectMap(clazz);
//		if (cacheObjectMap != null) {// 缓存对象，先查询ID
//			query.setProjection(Projections.id());
//			List idList = daoService.findByCriteria(query);
//			return getObjectListUsingCache(clazz, idList);
//		} else {
//			List result = daoService.findByCriteria(query);
//			return result;
//		}
//	}
//
//	private <T extends BaseObject, S extends Serializable> List<T> getObjectListUsingCache(Class<T> clazz, Collection<S> idList) {
//		List<T> result = new ArrayList<T>(idList.size());
//		T obj = null;
//		long t = System.currentTimeMillis();
//		int count = 0;
//		for (S id : idList) {
//			obj = getObject(clazz, id);
//			if (obj != null) {
//				result.add(obj);
//			}
//			count++;
//			if (System.currentTimeMillis() - t > DateUtil.m_minute * 2 && count < 200) {
//				// 100个以内，超过2min，缓存有问题，直接抛异常
//				throw new IllegalArgumentException("cache too slow!!");
//			}
//		}
//		return result;
//	}
//
//	private <T extends BaseObject, S extends Serializable> T getByUkey(Class<T> clazz, String ukeyName, S ukeyValue) {
//		DetachedCriteria query = DetachedCriteria.forClass(clazz);
//		query.add(Restrictions.eq(ukeyName, ukeyValue));
//		List result = daoService.findByCriteria(query);
//		if (result.isEmpty())
//			return null;
//		if (result.size() > 1)
//			throw new IllegalStateException("查询出多个记录：" + clazz.getName() + ", ukey=" + ukeyName + ", value=" + ukeyValue);
//		return (T) result.get(0);
//	}
//
//	private <T extends BaseObject, S extends Serializable> S getIdByUkey(Class<T> clazz, String ukeyName, S ukeyValue) {
//		DetachedCriteria query = DetachedCriteria.forClass(clazz);
//		query.add(Restrictions.eq(ukeyName, ukeyValue));
//		query.setProjection(Projections.id());
//		List result = daoService.findByCriteria(query);
//		if (result.isEmpty())
//			return null;
//		if (result.size() > 1)
//			throw new IllegalStateException("查询出多个记录：" + clazz.getName() + ", ukey=" + ukeyName + ", value=" + ukeyValue);
//		return (S) result.get(0);
//	}
//
//	private <T extends BaseObject> Serializable getUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue) {
//		Gcache<String, Serializable> ukeyCache = cachedUkeyMap.get(clazz.getCanonicalName());
//		if (ukeyCache != null) {
//			Serializable idvalue = ukeyCache.getIfPresent(ukeyName + "_" + ukeyValue);
//			if (idvalue != null)
//				return idvalue;
//		}
//		return null;
//	}
//
//	private <T extends BaseObject> void setUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue, Serializable id) {
//		Gcache<String, Serializable> ukeyCache = cachedUkeyMap.get(clazz.getCanonicalName());
//		if (ukeyCache != null) {
//			if (id != null) {
//				ukeyCache.put(ukeyName + "_" + ukeyValue, id);
//			} else {
//				ukeyCache.invalidate(ukeyName + "_" + ukeyValue);
//			}
//		}
//	}
//}
