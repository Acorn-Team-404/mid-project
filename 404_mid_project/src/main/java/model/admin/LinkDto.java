package model.admin;

public class LinkDto {
    private int linkNum;
    private String linkTitle;
    private String linkUrl;
    private String linkAction;

    public int getLinkNum() {
        return linkNum;
    }
    public void setLinkNum(int linkNum) {
        this.linkNum = linkNum;
    }
    public String getLinkTitle() {
        return linkTitle;
    }
    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }
    public String getLinkUrl() {
        return linkUrl;
    }
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public String getLinkAction() {
        return linkAction;
    }
    public void setLinkAction(String linkAction) {
        this.linkAction = linkAction;
    }
}
