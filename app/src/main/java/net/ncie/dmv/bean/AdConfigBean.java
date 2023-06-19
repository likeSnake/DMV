package net.ncie.dmv.bean;

import java.util.List;

public class AdConfigBean {

    /**
     * interval : 10
     * clickLimit : 300
     * showLimit : 50
     * fbId : 1152359378860889
     * closeAd : false
     * closeVersions : []
     * closeInter : false
     * refreshTime : 60
     * interCoverRate : 50
     * nativeCoverRate : 50
     */

    private int interval;
    private int clickLimit;
    private int showLimit;
    private String fbId;
    private boolean closeAd;
    private boolean closeInter;
    private int refreshTime;
    private int interCoverRate;
    private int nativeCoverRate;
    private List<?> closeVersions;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getClickLimit() {
        return clickLimit;
    }

    public void setClickLimit(int clickLimit) {
        this.clickLimit = clickLimit;
    }

    public int getShowLimit() {
        return showLimit;
    }

    public void setShowLimit(int showLimit) {
        this.showLimit = showLimit;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public boolean isCloseAd() {
        return closeAd;
    }

    public void setCloseAd(boolean closeAd) {
        this.closeAd = closeAd;
    }

    public boolean isCloseInter() {
        return closeInter;
    }

    public void setCloseInter(boolean closeInter) {
        this.closeInter = closeInter;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getInterCoverRate() {
        return interCoverRate;
    }

    public void setInterCoverRate(int interCoverRate) {
        this.interCoverRate = interCoverRate;
    }

    public int getNativeCoverRate() {
        return nativeCoverRate;
    }

    public void setNativeCoverRate(int nativeCoverRate) {
        this.nativeCoverRate = nativeCoverRate;
    }

    public List<?> getCloseVersions() {
        return closeVersions;
    }

    public void setCloseVersions(List<?> closeVersions) {
        this.closeVersions = closeVersions;
    }
}
