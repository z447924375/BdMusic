package zxh.bdmusic.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseActy;
import zxh.bdmusic.eventbus.SendSongMsgBeanEvent;
import zxh.bdmusic.live.LiveFragment;
import zxh.bdmusic.mine.MineFragment;
import zxh.bdmusic.musiclibrary.musicllibbaseinfo.MusicLibFragment;
import zxh.bdmusic.play.PlayFragment;
import zxh.bdmusic.playservice.MusicPlayService;
import zxh.bdmusic.playservice.MyHelper;
import zxh.bdmusic.playservice.SongMsgBean;
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
                intent.putStringArrayListExtra("songIDs", songIDs);
                intent.putExtra("position", position);
                intent.putExtra("isFirstEntry",true);
                startService(intent);

            } else {
                Log.d("kkkk", "song == null ******");
            }
        }


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
        showNotify();
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
                mybinder.playNext();
                break;
            case R.id.btn_play_song_list:

                break;
            case R.id.main_play_ll:
                Bundle bundle = new Bundle();
                bundle.putSerializable("SongMsgBean", bean);
                PlayFragment playFragment = new PlayFragment();
                playFragment.setArguments(bundle);
                manager.beginTransaction().setCustomAnimations(R.anim.part_upshow, R.anim.part_no).replace(R.id.main_all, playFragment).commit();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbindService(connection);


    }

    private void showNotify() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("百度音乐");
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notify_custom);
        builder.setContent(remoteViews);
        Notification notification = builder.build();
        manager.notify(300, notification);

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
}
