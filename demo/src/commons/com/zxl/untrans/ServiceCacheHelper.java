//package com.zxl.untrans;
//
//import java.util.Map;
//
//public interface ServiceCacheHelper {
//	public Map getStats();
//	public Map getAndResetStats();
//	public boolean isLocal();
//	
//	public <T> T cacheCall(Integer cacheSeconds, CachableCall<T> call, String ukey, Object...params);
//	public <T> T cacheCallRefresh(Integer cacheSeconds, CachableCall<T> call, String ukey, boolean forceRefresh, Object...params);
//	public void updateCache(Object value, Integer cacheSeconds, String ukey, Object...params);
//}
