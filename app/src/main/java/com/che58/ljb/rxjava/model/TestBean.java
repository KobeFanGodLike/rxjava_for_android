package com.che58.ljb.rxjava.model;

/**
 * Created by wangmaobo on 2017/4/25 0025.
 */
public class TestBean {
    public boolean ok;
    public long serverTime;
    public int maxPage;
    public int pageNum;
    public int rp;
    public int totalSize;

    @Override
    public String toString() {
        return "TestBean{" +
                "ok=" + ok +
                ", serverTime=" + serverTime +
                ", maxPage=" + maxPage +
                ", pageNum=" + pageNum +
                ", rp=" + rp +
                ", totalSize=" + totalSize +
                '}';
    }
}
