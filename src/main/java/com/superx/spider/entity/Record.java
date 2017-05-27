package com.superx.spider.entity;

import java.util.Date;

public class Record {
    private Integer id;

    private String methodName;

    private String totalSize;

    private String getIp;

    private Date creatorTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName == null ? null : methodName.trim();
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize == null ? null : totalSize.trim();
    }

    public String getGetIp() {
        return getIp;
    }

    public void setGetIp(String getIp) {
        this.getIp = getIp == null ? null : getIp.trim();
    }

    public Date getCreatorTime() {
        return creatorTime;
    }

    public void setCreatorTime(Date creatorTime) {
        this.creatorTime = creatorTime;
    }
}