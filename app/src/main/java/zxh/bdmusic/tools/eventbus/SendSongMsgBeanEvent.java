package zxh.bdmusic.tools.eventbus;

import java.util.ArrayList;

import zxh.bdmusic.bean.SongMsgBean;

/**
 * Created by dllo on 16/10/11.
 */
public class SendSongMsgBeanEvent {
    private SongMsgBean songMsgBean;
    private ArrayList<String>songIDs;

    public SongMsgBean getSongMsgBean() {
        return songMsgBean;
    }

    public void setSongMsgBean(SongMsgBean songMsgBean) {
        this.songMsgBean = songMsgBean;
    }
}
