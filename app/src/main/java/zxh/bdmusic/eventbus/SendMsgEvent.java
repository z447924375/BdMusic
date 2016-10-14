package zxh.bdmusic.eventbus;

import zxh.bdmusic.musiclibrary.recommend.songlistclickin.SongListClickInBean;

/**
 * Created by dllo on 16/10/8.
 */
public class SendMsgEvent {

    private String songName;
    private String singer;
    private String picSrc;
    private SongListClickInBean bean;

    public SongListClickInBean getBean() {
        return bean;
    }

    public void setBean(SongListClickInBean bean) {
        this.bean = bean;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }
}
