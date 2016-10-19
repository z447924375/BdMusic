package zxh.bdmusic.tools.eventbus;

/**
 * Created by dllo on 16/10/19.
 */
public class SendDownloadEvent {
    boolean isDownload;

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }
}
