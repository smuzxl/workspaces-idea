package com.zxl.support;

import java.io.Serializable;

public class CachePair implements Serializable {
    private static final long serialVersionUID = -1078604784250306692L;
    private Long version;
    private Object value;

    public CachePair() {
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
