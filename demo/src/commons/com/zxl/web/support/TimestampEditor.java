package com.zxl.web.support;

import org.apache.commons.lang.StringUtils;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * �̰߳�ȫ
 *
 * @author
 */
public class TimestampEditor extends PropertyEditorSupport {
    private String full_timeformat = "yyyy-MM-dd HH:mm:ss";

    public TimestampEditor() {
    }

    public TimestampEditor(String dateFormat) {
        this.full_timeformat = dateFormat;
    }

    protected Timestamp format(String formtText, String value) {
        try {
            DateFormat format = new SimpleDateFormat(formtText);
            Timestamp date = new Timestamp(format.parse(value).getTime());
            return date;
        } catch (ParseException e) {
        }
        return null;
    }

    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? new SimpleDateFormat(full_timeformat).format(value) : "");
    }

    @Override
    public void setAsText(String value) {
        if (StringUtils.isBlank(value)) {
            this.setValue(null);
            return;
        }
        String formtText = full_timeformat;
        if (value.length() <= full_timeformat.length()) {
            formtText = StringUtils.substring(full_timeformat, 0, value.length());
        }
        Timestamp date = format(formtText, value);
        this.setValue(date);
    }
}