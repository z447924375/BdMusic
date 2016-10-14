package zxh.bdmusic.playservice;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import zxh.bdmusic.baseclass.MyApp;
import zxh.bdmusic.baseclass.VolleySingleton;
import zxh.bdmusic.eventbus.SendSongMsgBeanEvent;
import zxh.bdmusic.musiclibrary.musicllibbaseinfo.URLVlaues;

/**
 * Created by dllo on 16/10/7.
 */
public class MusicPlayService extends Service {
    private MusicPlayer player = new MusicPlayer();
    private SongMsgBean bean = new SongMsgBean();
    private Mybinder binder = new Mybinder();
    private ArrayList<String> songIDs;
    private int position;
    private SQLiteDatabase sqLiteDatabase;
    private ContentValues values;
    private boolean isFirstEntry;


    @Override
    public void onCreate() {
        super.onCreate();

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        songIDs = intent.getStringArrayListExtra("songIDs");
        position = (int) intent.getExtras().get("position");
        isFirstEntry = (boolean) intent.getExtras().get("isFirstEntry");

        if (isFirstEntry){

        }
        playSong(songIDs, position);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }

    private void playSong(final ArrayList<String> songIDs, final int position) {
        final StringRequest stringRequest = new StringRequest(URLVlaues.PLAY_FRONT + songIDs.get(position) + URLVlaues.PLAY_BEHIND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.substring(1, response.length() - 2);
                Gson gson = new Gson();
                bean = gson.fromJson(response, SongMsgBean.class);
                MyHelper helper = new MyHelper(MyApp.getContext(), "music.db", null, 1);
                sqLiteDatabase = helper.getWritableDatabase();
                values = new ContentValues();
                values.put("song", String.valueOf(songIDs));
                values.put("position", position);

                sqLiteDatabase.delete("music", null, null);
                sqLiteDatabase.insert("music", "song", values);

                SendSongMsgBeanEvent event = new SendSongMsgBeanEvent();
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
            playSong(songIDs, position + 1);
            position += 1;
        } else {
            playSong(songIDs, 0);
            position = 0;
        }

    }

    public class Mybinder extends Binder {


        public void playNext() {
            if (position < songIDs.size() - 1) {
                playSong(songIDs, position + 1);
                position += 1;
            } else {
                playSong(songIDs, 0);
                position = 0;
            }
        }

        public boolean isPlaying() {
            return player.isPlaying();
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


        public void seekTo(int msec) {
            player.mediaPlayer.seekTo(msec);
        }


    }
}
