package zxh.bdmusic.tools.eventbus;

import java.util.ArrayList;

/**
 * Created by dllo on 16/10/15.
 */
public class SendListArrFromServicEvent {
   private ArrayList<String> songNames;
   private ArrayList<String> authors;

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public ArrayList<String> getSongNames() {
        return songNames;
    }

    public void setSongNames(ArrayList<String> songNames) {
        this.songNames = songNames;
    }
}
