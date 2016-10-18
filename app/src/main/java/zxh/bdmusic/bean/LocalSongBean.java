package zxh.bdmusic.bean;

/**
 * Created by dllo on 16/10/18.
 */
public class LocalSongBean {
   private String author;
    private String title;
    private String songid;

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

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
}
