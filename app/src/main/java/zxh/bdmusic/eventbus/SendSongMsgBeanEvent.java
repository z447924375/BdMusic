package zxh.bdmusic.eventbus;

import zxh.bdmusic.musicplay.SongMsgBean;

/**
 * Created by dllo on 16/10/11.
 */
public class SendSongMsgBeanEvent {
    private SongMsgBean songMsgBean;

    public SongMsgBean getSongMsgBean() {
        return songMsgBean;
    }

    public void setSongMsgBean(SongMsgBean songMsgBean) {
        this.songMsgBean = songMsgBean;
    }
}
