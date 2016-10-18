package zxh.bdmusic.bean;

import java.util.ArrayList;

/**
 * Created by dllo on 16/10/13.
 */
public class SongIdBean {
    private ArrayList<String> songIDs;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setSongIDs(ArrayList<String> songIDs) {
        this.songIDs = songIDs;
    }

    public ArrayList<String> getSongIDs() {

        return songIDs;
    }
}
