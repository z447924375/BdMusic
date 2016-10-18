package zxh.bdmusic.tools.eventbus;

/**
 * Created by dllo on 16/10/12.
 */
public class SendNextPlayMsgEvent {
    private String title;
    private String author;
    private String picSrc;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }
}
