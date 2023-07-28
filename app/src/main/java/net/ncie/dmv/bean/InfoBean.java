package net.ncie.dmv.bean;

public class InfoBean {
    private String aid;
    private String position;
    private String status;
    private String uid;
    private String info;
    private String currency;
    private String cicros;
    private String responseId;
    private String responseInfo;
    private String model;
    private String androidVersionCode;
    private String pkg;
    private String appVersionCode;

    public InfoBean(String aid, String position, String status, String uid, String info, String currency, String cicros, String responseId, String responseInfo, String model, String androidVersionCode, String pkg, String appVersionCode) {
        this.aid = aid;
        this.position = position;
        this.status = status;
        this.uid = uid;
        this.info = info;
        this.currency = currency;
        this.cicros = cicros;
        this.responseId = responseId;
        this.responseInfo = responseInfo;
        this.model = model;
        this.androidVersionCode = androidVersionCode;
        this.pkg = pkg;
        this.appVersionCode = appVersionCode;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCicros() {
        return cicros;
    }

    public void setCicros(String cicros) {
        this.cicros = cicros;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(String responseInfo) {
        this.responseInfo = responseInfo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAndroidVersionCode() {
        return androidVersionCode;
    }

    public void setAndroidVersionCode(String androidVersionCode) {
        this.androidVersionCode = androidVersionCode;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }
}
