package zxh.bdmusic.eventbus;

import java.util.ArrayList;

/**
 * Created by dllo on 16/10/15.
 */
public class SendListArrFromServicEvent {
   private ArrayList<String> songIDs;
   private ArrayList<String> authors;

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public ArrayList<String> getSongIDs() {

        return songIDs;
    }

    public void setSongIDs(ArrayList<String> songIDs) {
        this.songIDs = songIDs;
    }
}
