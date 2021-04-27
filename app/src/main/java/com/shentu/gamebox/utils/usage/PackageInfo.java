package com.shentu.gamebox.utils.usage;

import java.util.Objects;

public class PackageInfo {
    private int mUsedCount;
    private long mUsedTime;
    private String mPackageName;
    private String mAppName;


    public PackageInfo(int mUsedCount, long mUsedTime, String mPackageName, String mAppName) {
        this.mUsedCount = mUsedCount;
        this.mUsedTime = mUsedTime;
        this.mPackageName = mPackageName;
        this.mAppName = mAppName;
    }
    public  void addCount(){
        mUsedCount++;
    }

    public int getmUsedCount() {
        return mUsedCount;
    }

    public void setmUsedCount(int mUsedCount) {
        this.mUsedCount = mUsedCount;
    }

    public long getmUsedTime() {
        return mUsedTime;
    }

    public void setmUsedTime(long mUsedTime) {
        this.mUsedTime = mUsedTime;
    }

    public String getmPackageName() {
        return mPackageName;
    }

    public void setmPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public String getmAppName() {
        return mAppName;
    }

    public void setmAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageInfo that = (PackageInfo) o;
        return Objects.equals(mPackageName, that.mPackageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUsedTime, mPackageName);
    }

    @Override
    public String toString() {
        return "PackageInfo{" +
                "mUsedCount=" + mUsedCount +
                ", mUsedTime=" + mUsedTime +
                ", mPackageName='" + mPackageName + '\'' +
                ", mAppName='" + mAppName + '\'' +
                '}';
    }
}
