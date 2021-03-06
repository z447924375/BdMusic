package zxh.bdmusic.playservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RemoteViews;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.MyApp;
import zxh.bdmusic.bean.SongMsgBean;
import zxh.bdmusic.tools.VolleySingleton;
import zxh.bdmusic.tools.eventbus.SendListArrFromServicEvent;
import zxh.bdmusic.tools.eventbus.SendSongMsgBeanEvent;
import zxh.bdmusic.usedvalues.URLVlaues;

/**
 * Created by dllo on 16/10/7.
 */
public class MusicPlayService extends Service {

    private Notification not;
    private NotificationManager notificationManager;
    private RemoteViews remo;

    public MusicPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MusicPlayer player) {
        this.player = player;
    }

    private MusicPlayer player = new MusicPlayer();
    private SongMsgBean bean = new SongMsgBean();
    private Mybinder binder = new Mybinder();
    private ArrayList<String> songIDs;
    private ArrayList<String> songNames;
    private ArrayList<String> authors;
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
        try {
            songIDs = intent.getStringArrayListExtra("songIDs");
            position = (int) intent.getExtras().get("position");
            isFirstEntry = (boolean) intent.getExtras().get("isfirst");

            songNames = intent.getStringArrayListExtra("songNames");
            authors = intent.getStringArrayListExtra("authors");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            playSong(songIDs, position, isFirstEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }

    private void playSong(final ArrayList<String> songIDs, final int position, boolean firstEntry) {
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

                if (songNames != null && authors != null) {
                    SendListArrFromServicEvent fromServicEvent = new SendListArrFromServicEvent();
                    fromServicEvent.setSongNames(songNames);
                    fromServicEvent.setAuthors(authors);
                    EventBus.getDefault().post(fromServicEvent);
                }


                SendSongMsgBeanEvent event = new SendSongMsgBeanEvent();
                event.setSongMsgBean(bean);
                EventBus.getDefault().post(event);

                player.playUrl(bean.getBitrate().getFile_link());
                if (isFirstEntry == true) {
                    isFirstEntry = false;
                } else {
                    player.start();

                }


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
            playSong(songIDs, position + 1, false);
            position += 1;
        } else {
            playSong(songIDs, 0, false);
            position = 0;
        }

    }

    public class Mybinder extends Binder {
        public Mybinder() {
        }

        public MediaPlayer getMediaPlayer() {
            return player.mediaPlayer;
        }

        public void playNext(int playCondition) {

            switch (playCondition) {
                case 0://循环播放
                    if (position < songIDs.size() - 1) {
                        playSong(songIDs, position + 1, false);
                        position += 1;
                    } else {
                        playSong(songIDs, 0, false);
                        position = 0;
                    }
                    break;
                case 1://随机播放
                    Random random = new Random();
                    position = random.nextInt(songIDs.size());
                    playSong(songIDs, position, false);
                    break;

                case 2://单曲循环
                    playSong(songIDs, position, false);
                    break;

            }


        }

        public void playLast() {
            if (position > 0) {
                playSong(songIDs, position - 1, false);
                position -= 1;
            } else {
                playSong(songIDs, songIDs.size() - 1, false);
                position = songIDs.size() - 1;
            }
        }


        public void playPause() {
            player.pause();
        }

        public void playStop() {
            player.stop();
        }


        public void playStart() {
            player.start();
        }


        public boolean isPlaying() {
            if (player.mediaPlayer != null) {
                return player.mediaPlayer.isPlaying();
            }
            return false;
        }

        public int getTotalDuration() {
            if (player.mediaPlayer != null) {
                return player.mediaPlayer.getDuration();
            }
            return -1;
        }

        public int getCurrentPosition() {
            if (player.mediaPlayer != null) {
                return player.mediaPlayer.getCurrentPosition();
            }
            return -1;
        }
    }


    private void showNotification(String title, String singer, final String url) {

        //获取开启通知的Manager
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(title);

        //创建一个RemoteViews
        remo = new RemoteViews(getPackageName(), R.layout.notify_custom);
        ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                remo.setImageViewBitmap(R.id.notify_pic, loadedImage);
                if (not != null) {
                    notificationManager.notify(210, not);
                }
            }
        });
        remo.setTextViewText(R.id.notify_title, title);
        remo.setTextViewText(R.id.notify_author, singer);

//        设置自定义通知栏内button的点击事件
//        Intent notificationIntentexit = new Intent("EXIT");
//        PendingIntent pendingIntentexit = PendingIntent.getBroadcast(this, 0, notificationIntentexit, PendingIntent.FLAG_UPDATE_CURRENT);
//        remo.setOnClickPendingIntent(R.id.exit_notify, pendingIntentexit);

        Intent notificationIntentlast = new Intent("last");
        PendingIntent pendingIntentlast = PendingIntent.getBroadcast(this, 0, notificationIntentlast, PendingIntent.FLAG_UPDATE_CURRENT);
        remo.setOnClickPendingIntent(R.id.notify_prev, pendingIntentlast);

        Intent notificationIntentnext = new Intent("next");
        PendingIntent pendingIntentnext = PendingIntent.getBroadcast(this, 0, notificationIntentnext, PendingIntent.FLAG_UPDATE_CURRENT);
        remo.setOnClickPendingIntent(R.id.notify_next, pendingIntentnext);

        Intent notificationIntentpause = new Intent("pause");
        PendingIntent pendingIntentplay = PendingIntent.getBroadcast(this, 0, notificationIntentpause, PendingIntent.FLAG_UPDATE_CURRENT);
        remo.setOnClickPendingIntent(R.id.notify_pause, pendingIntentplay);

        builder.setContent(remo);
        not = builder.build();
        not.bigContentView = remo;
        notificationManager.notify(210, not);
    }

}
