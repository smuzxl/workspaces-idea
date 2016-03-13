package com.zxl.support;

import java.io.Serializable;

public class CacheElement<T> implements Serializable {
    private static final long serialVersionUID = 2864438984575469862L;
    private Long validtime;
    private T value;
    private Long updatetime;

    public CacheElement() {
    }

    public CacheElement(T value, Integer cacheSeconds) {
        this.updatetime = System.currentTimeMillis();
        this.validtime = System.currentTimeMillis() + cacheSeconds * 1000;
        this.value = value;
    }

    public Long getValidtime() {
        return validtime;
    }

    public void setValidtime(Long validtime) {
        this.validtime = validtime;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Long getUpdatetime() {
        return updatetime;
    }

}
