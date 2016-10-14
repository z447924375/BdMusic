package zxh.bdmusic.eventbus;

/**
 * Created by dllo on 16/10/12.
 */
public class SendSongPlayPositionEvent {
    private int position;
    private int duration;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
