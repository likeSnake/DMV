package net.ncie.dmv.bean;

public class OptionsBean {
    private String content;
    private String Option;

    public OptionsBean(String content, String option) {
        this.content = content;
        Option = option;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOption() {
        return Option;
    }

    public void setOption(String option) {
        Option = option;
    }
}
