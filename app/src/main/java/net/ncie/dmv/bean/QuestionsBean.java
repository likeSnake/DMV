package net.ncie.dmv.bean;

public class QuestionsBean {
    private int id;
    private String test_id;
    private String title;
    private String type;
    private String state;
    private String desc;
    private int questionNum;
    private String passNum;
    private int acceptableErrNum;
    private String passingScore;
    private String testStartUrl;
    private int order;

    public QuestionsBean(int id, String test_id, String title, String type, String state, String desc, int questionNum, String passNum, int acceptableErrNum, String passingScore, String testStartUrl, int order) {
        this.id = id;
        this.test_id = test_id;
        this.title = title;
        this.type = type;
        this.state = state;
        this.desc = desc;
        this.questionNum = questionNum;
        this.passNum = passNum;
        this.acceptableErrNum = acceptableErrNum;
        this.passingScore = passingScore;
        this.testStartUrl = testStartUrl;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public String getPassNum() {
        return passNum;
    }

    public void setPassNum(String passNum) {
        this.passNum = passNum;
    }

    public int getAcceptableErrNum() {
        return acceptableErrNum;
    }

    public void setAcceptableErrNum(int acceptableErrNum) {
        this.acceptableErrNum = acceptableErrNum;
    }

    public String getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(String passingScore) {
        this.passingScore = passingScore;
    }

    public String getTestStartUrl() {
        return testStartUrl;
    }

    public void setTestStartUrl(String testStartUrl) {
        this.testStartUrl = testStartUrl;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "QuestionsBean{" +
                "id=" + id +
                ", test_id='" + test_id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", state='" + state + '\'' +
                ", desc='" + desc + '\'' +
                ", questionNum=" + questionNum +
                ", passNum='" + passNum + '\'' +
                ", acceptableErrNum=" + acceptableErrNum +
                ", passingScore='" + passingScore + '\'' +
                ", testStartUrl='" + testStartUrl + '\'' +
                ", order=" + order +
                '}';
    }
}
