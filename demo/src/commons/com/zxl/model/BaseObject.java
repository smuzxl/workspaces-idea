package com.zxl.model;

import java.io.Serializable;

public abstract class BaseObject implements Serializable {
    private static final long serialVersionUID = -5468083603103874826L;

    public abstract Serializable realId();

    @Override
    public final int hashCode() {
        return (realId() == null) ? 0 : realId().hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        BaseObject other = (BaseObject) obj;
        return !(this.realId() != null ? !(this.realId().equals(other.realId())) : (other.realId() != null));
    }
}
