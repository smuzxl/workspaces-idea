package com.zxl.repository;

import com.zxl.model.BaseObject;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BaseDao {
    <T extends BaseObject> List<T> getAllObjects(Class<T> clazz);

    <T extends BaseObject> T getObject(Class<T> clazz, Serializable id);

    <T extends BaseObject> List<T> getObjectListByField(Class<T> clazz, String fieldName, Serializable fieldValue);

    <T extends BaseObject, S extends Serializable> List<T> getObjectList(Class<T> clazz, Collection<S> idList);

    <T extends BaseObject> T saveObject(T entity);

    <T extends BaseObject> T removeObject(Class<T> clazz, Serializable id);

    <T extends BaseObject> T removeObject(T entity);

    <T extends BaseObject> T updateObject(T entity);

    <T extends BaseObject> void updateObjectList(Collection<T> entity);

    <T extends BaseObject> T addObject(T entity);

    <T extends BaseObject> void removeObjectList(Collection<T> entityList);

    <T extends BaseObject> void saveObjectList(Collection<T> entityList);

    <T extends BaseObject> int getObjectCount(Class<T> clazz);

    <T extends BaseObject> T removeObjectById(Class<T> clazz, Serializable id);

    <T extends BaseObject> void addObjectList(Collection<T> entityList);

    <T extends BaseObject, S extends Serializable> List<S> getObjectPropertyList(Class<T> clazz, String propertyname);

    <T extends BaseObject, S extends Serializable> List<S> getObjectPropertyList(Class<T> clazz, String propertyname, boolean isDistinct);

    <T extends BaseObject> List<T> getObjectList(Class<T> clazz, String orderField, boolean asc, int from, int rows);

    <T extends BaseObject> Map getObjectPropertyMap(Class<T> clazz, String keyname, String valuename);

    <T extends BaseObject, S extends Serializable> Map getObjectPropertyMap(Class<T> clazz, String keyname, String valuename, Collection<S> idList);

    <T extends BaseObject> void saveObjectList(T... entityList);

    /**
     * ���������е�ukey(���ݿ�����Ψһ����)��ȡ���󣨿ɻ���˶���
     *
     * @param clazz     ��ѯ������:TicketOrder.class
     * @param ukeyName  �ֶ������磺tradeNo
     * @param ukeyvalue �ֶ�ֵ���磺1091023141727057
     * @return
     */
    <T extends BaseObject> T getObjectByUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue);

    /**
     * ������ѯ����select * from xxxx where yyy in (....)
     *
     * @param clazz
     * @param propertyName
     * @param valueList
     * @return
     */
    <T extends BaseObject, S extends Serializable> List<T> getObjectBatch(Class<T> clazz, String propertyName, Collection<S> valueList);

    /**
     * ���¶�ȡ������������ֵ
     *
     * @param entity
     * @param property
     * @param value
     * @return
     */
    <T extends BaseObject> void refreshUpdateProperty(T entity, String property, Object value);

    <T extends BaseObject> Serializable getUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue);

    <T extends BaseObject> void setUkey(Class<T> clazz, String ukeyName, Serializable ukeyValue, Serializable id);

    List<Map> getUkeyCacheStats();

    List findByCriteria(DetachedCriteria query);

    List findByCriteria(DetachedCriteria query, int from, int maxnum);

    List findByHql(String hql, Object... values);

    List<String> getEntityClassList();

    List<String> getCachableEntityClassList();
}
