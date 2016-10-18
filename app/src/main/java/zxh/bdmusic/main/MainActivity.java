package zxh.bdmusic.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseActy;
import zxh.bdmusic.bean.SongMsgBean;
import zxh.bdmusic.live.LiveFragment;
import zxh.bdmusic.mine.MineFragment;
import zxh.bdmusic.musiclibrary.musicllibbaseinfo.MusicLibFragment;
import zxh.bdmusic.play.PlayFragment;
import zxh.bdmusic.play.PlayListFragment;
import zxh.bdmusic.playservice.MusicPlayService;
import zxh.bdmusic.playservice.MyHelper;
import zxh.bdmusic.tools.eventbus.SendListArrFromServicEvent;
import zxh.bdmusic.tools.eventbus.SendPlayConditionEvent;
import zxh.bdmusic.tools.eventbus.SendPlayLastOrNextEvent;
import zxh.bdmusic.tools.eventbus.SendSongMsgBeanEvent;
import zxh.bdmusic.trends.TrendsFragment;

public class MainActivity extends BaseActy implements View.OnClickListener {
    private MusicPlayService.Mybinder mybinder;
    private TabLayout tb;
    private ViewPager vp;
    private ImageView play_song_pic;
    private TextView play_song_singer;
    private TextView play_song_name;
    private MyConnection connection;
    private Intent intent;
    private ImageButton btn_play_pause;
    private ImageButton btn_play_next;
    private ImageButton btn_play_list;
    private LinearLayout main_play_ll;
    private RelativeLayout main_rl;
    private FragmentManager manager = getSupportFragmentManager();
    private SongMsgBean bean;
    private DisplayImageOptions options;
    private SQLiteDatabase database;
    private String song;
    private int position;
    private int condition;
    private int con;
    private SharedPreferences sp;
    private SharedPreferences.Editor sped;


    private int con2;
    private ArrayList<String> songNames;
    private ArrayList<String> authors;
    private String singers;
    private String titles;
    private MyBroadCastReceiver receiver;
    private NotificationManager notificationManager;
    private RemoteViews remo;
    private Notification not;


    @Override
    protected int setlayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.default_live_ic)
                .showImageOnLoading(R.mipmap.default_live_ic)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        tb = bindView(R.id.tb);
        vp = bindView(R.id.vp);
        play_song_pic = bindView(R.id.play_song_pic);
        play_song_singer = bindView(R.id.play_song_singer);
        play_song_name = bindView(R.id.play_song_name);
        btn_play_pause = bindView(R.id.btn_play_song_pause);
        btn_play_next = bindView(R.id.btn_play_song_next);
        btn_play_list = bindView(R.id.btn_play_song_list);
        main_play_ll = bindView(R.id.main_play_ll);
        main_rl = bindView(R.id.main_rl);
    }


    public ArrayList<String> turnToSongIdsList(String str) {
        String d[] = str.split(","); //把这个字符串按"，" 分隔开存入String类型数组d中。
        ArrayList<String> list = new ArrayList<>(); //创建一个集合
        //便利字符串数组，把值放入list集合中
        for (int i = 0; i < d.length; i++) {
            list.add(d[i]);
        }
        return list;
    }

    @Override
    protected void inidate() {
        receiver = new MyBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("pause");
        filter.addAction("next");
        filter.addAction("last");
        registerReceiver(receiver, filter);

        EventBus.getDefault().register(this);
        MyHelper helper = new MyHelper(this, "music.db", null, 1);
        database = helper.getWritableDatabase();
        Cursor cursor = database.query("music", null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                song = cursor.getString(cursor.getColumnIndex("song"));
                song = song.replace("[", "");
                song = song.replace("]", "");
                position = cursor.getInt(cursor.getColumnIndex("position"));

            }
            if (song != null) {
                ArrayList<String> songIDs = turnToSongIdsList(song);
                Intent intent = new Intent(this, MusicPlayService.class);
                intent.putExtra("isfirst", true);
                intent.putStringArrayListExtra("songIDs", songIDs);
                intent.putExtra("position", position);
                startService(intent);
                btn_play_pause.setImageResource(R.mipmap.bt_minibar_play_normal);

            } else {
                Log.d("kkkk", "song == null ******");
            }
        }
        SharedPreferences sharedPreferences = getSharedPreferences("con", MODE_PRIVATE);
        con = sharedPreferences.getInt("condition", 0);

        btn_play_pause.setOnClickListener(this);
        btn_play_next.setOnClickListener(this);
        btn_play_list.setOnClickListener(this);
        main_play_ll.setOnClickListener(this);
        main_rl.setOnClickListener(this);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MineFragment());
        fragments.add(new MusicLibFragment());
        fragments.add(new TrendsFragment());
        fragments.add(new LiveFragment());
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(myAdapter);
        vp.setCurrentItem(1);
        tb.setupWithViewPager(vp);
        tb.setSelectedTabIndicatorColor(Color.WHITE);
        intent = new Intent(this, MusicPlayService.class);
        connection = new MyConnection();
        bindService(intent, connection, BIND_AUTO_CREATE);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(SendSongMsgBeanEvent sendSongMsgBeanEvent) {
        bean = sendSongMsgBeanEvent.getSongMsgBean();
        play_song_singer.setText(bean.getSonginfo().getAuthor());
        play_song_name.setText(bean.getSonginfo().getTitle());
        ImageLoader.getInstance().displayImage(bean.getSonginfo().getPic_big(), play_song_pic, options);
        showNotification(bean.getSonginfo().getTitle(),bean.getSonginfo().getAuthor(),bean.getSonginfo().getPic_big());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPlayConditionEvent(SendPlayConditionEvent playConditionEvent) {
        condition = playConditionEvent.getPlayCondition();
        sp = getSharedPreferences("con", MODE_PRIVATE);
        sped = sp.edit();
        sped.putInt("condition", condition);
        sped.commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getevnet(SendListArrFromServicEvent fromServicEvent) {
        Log.d("dddd", "haha");
        songNames = fromServicEvent.getSongNames();
        authors = fromServicEvent.getAuthors();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_play_song_pause:

                if (mybinder.isPlaying()) {
                    mybinder.playPause();
                    btn_play_pause.setImageResource(R.mipmap.bt_minibar_play_normal);
                } else {
                    btn_play_pause.setImageResource(R.mipmap.bt_minibar_pause_normal);
                    mybinder.playStart();
                }
                break;
            case R.id.btn_play_song_next:

                if (mybinder != null) {
                    mybinder.playNext(con % 3);
                    btn_play_pause.setImageResource(R.mipmap.bt_minibar_pause_normal);
                }


                break;
            case R.id.btn_play_song_list:
                FragmentManager fm = getSupportFragmentManager();
                Bundle bd = new Bundle();
                PlayListFragment listFragment = new PlayListFragment();
                if (songNames != null && authors != null) {
                    bd.putStringArrayList("songNames", songNames);
                    bd.putStringArrayList("authors", authors);
                    listFragment.setArguments(bd);
                }

                fm.beginTransaction().setCustomAnimations(R.anim.part_upshow, R.anim.part_no)
                        .replace(R.id.main_all, listFragment).commit();


                break;
            case R.id.main_play_ll:

                if (bean != null) {
                    Bundle bundle = new Bundle();
                    if (bundle != null) {
                        bundle.putSerializable("SongMsgBean", bean);
                        PlayFragment playFragment = new PlayFragment();
                        playFragment.setArguments(bundle);
                        manager.beginTransaction().setCustomAnimations(R.anim.part_upshow, R.anim.part_no)
                                .replace(R.id.main_all, playFragment).commit();
                    }

                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPlayLastOrNextEvent(SendPlayLastOrNextEvent playLastOrNextEvent) {
        int k = playLastOrNextEvent.getChoice();
        switch (k) {
            case 1:
                mybinder.playLast();
                btn_play_pause.setImageResource(R.mipmap.bt_minibar_pause_normal);
                break;
            case 2:
                SharedPreferences sharedPreferences2 = getSharedPreferences("con", MODE_PRIVATE);
                con2 = sharedPreferences2.getInt("condition", 0);
                mybinder.playNext(con2 % 3);
                btn_play_pause.setImageResource(R.mipmap.bt_minibar_pause_normal);
                break;
            case 11:
                mybinder.playPause();
                btn_play_pause.setImageResource(R.mipmap.bt_minibar_play_normal);
                break;
            case 22:
                mybinder.playStart();
                btn_play_pause.setImageResource(R.mipmap.bt_minibar_pause_normal);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbindService(connection);
        unregisterReceiver(receiver);
    }


    //连接music服务
    class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mybinder = (MusicPlayService.Mybinder) service;

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }


    private void showNotification(String title, String singer, final String url) {

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.icon);
        builder.setTicker(title);

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

    private class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            switch (action) {
                case "pause":
                    if (mybinder.getMediaPlayer().isPlaying()){
                        mybinder.playPause();
                    }else {
                        mybinder.playStart();
                    }
                    break;
                case "last":
                    mybinder.playLast();
                    break;
                case "next":
                    mybinder.playNext(con%3);
                    break;
            }

        }
    }
}
