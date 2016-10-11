package zxh.bdmusic.musicplay;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import zxh.bdmusic.baseclass.VolleySingleton;
import zxh.bdmusic.eventbus.SendSongMsgBeanEvent;
import zxh.bdmusic.musiclibrary.musicllib_baseinfo.URLVlaues;

/**
 * Created by dllo on 16/10/7.
 */
public class MusicPlayService extends Service {
    MusicPlayer player = new MusicPlayer();
    SongMsgBean bean = new SongMsgBean();
    Mybinder binder = new Mybinder();
    private ArrayList<String> songIDs;
    private int position;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MusicPlayService", "cjl0");
        songIDs = intent.getStringArrayListExtra("songIDs");
        position = (int) intent.getExtras().get("position");

        getSongMsgGsonData(URLVlaues.PLAY_FRONT + songIDs.get(position) + URLVlaues.PLAY_BEHIND);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }

    private void getSongMsgGsonData(String url) {
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.substring(1, response.length() - 2);
                Gson gson = new Gson();
                bean = gson.fromJson(response, SongMsgBean.class);

                SendSongMsgBeanEvent event=new SendSongMsgBeanEvent();
                event.setSongMsgBean(bean);

                EventBus.getDefault().post(event);

                player.playUrl(bean.getBitrate().getFile_link());
                player.start();
                player.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Next();
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance().addRequest(stringRequest);

    }

    public void Next() {
        if (position < songIDs.size() - 1) {
            getSongMsgGsonData(URLVlaues.PLAY_FRONT + songIDs.get(position + 1) + URLVlaues.PLAY_BEHIND);
            position += 1;
        } else {
            getSongMsgGsonData(URLVlaues.PLAY_FRONT + songIDs.get(0) + URLVlaues.PLAY_BEHIND);
            position = 0;
        }

    }

    public class Mybinder extends Binder {

        public void playNext() {

            if (position < songIDs.size() - 1) {
                getSongMsgGsonData(URLVlaues.PLAY_FRONT + songIDs.get(position + 1) + URLVlaues.PLAY_BEHIND);
                position += 1;
            } else {
                getSongMsgGsonData(URLVlaues.PLAY_FRONT + songIDs.get(0) + URLVlaues.PLAY_BEHIND);
                position = 0;
            }

        }

        public void playPause() {
            player.pause();

        }

        public void playStop() {
            player.stop();
        }

        public void playRelease() {
            player.release();
        }

        public void playStart() {
            player.start();
        }
    }
}
