package zxh.bdmusic.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseActy;
import zxh.bdmusic.eventbus.SendSongMsgBeanEvent;
import zxh.bdmusic.live.LiveFragment;
import zxh.bdmusic.mine.MineFragment;
import zxh.bdmusic.musiclibrary.musicllib_baseinfo.MusicLibFragment;
import zxh.bdmusic.musicplay.MusicPlayService;
import zxh.bdmusic.musicplay.SongMsgBean;
import zxh.bdmusic.play.PlayFragment;
import zxh.bdmusic.trends.TrendsFragment;

public class MainActivity extends BaseActy implements View.OnClickListener {
    private MusicPlayService.Mybinder mybinder;
    private TabLayout tb;
    private ViewPager vp;
    private ImageView play_song_pic;
    private TextView play_song_singer;
    private TextView play_song_name;
    private MyConnection connection;
    Intent intent;
    private CheckBox btn_play_pause;
    private ImageButton btn_play_next;
    private ImageButton btn_play_list;
    private LinearLayout main_play_ll;
    private RelativeLayout main_rl;
    FragmentManager manager=getSupportFragmentManager();


    @Override
    protected int setlayout() {
        return R.layout.activity_main;


    }

    @Override
    protected void initView() {

        tb = bindView(R.id.tb);
        vp = bindView(R.id.vp);
        play_song_pic = bindView(R.id.play_song_pic);
        play_song_singer = bindView(R.id.play_song_singer);
        play_song_name = bindView(R.id.play_song_name);
        btn_play_pause = bindView(R.id.btn_play_song_pause);
        btn_play_next = bindView(R.id.btn_play_song_next);
        btn_play_list = bindView(R.id.btn_play_song_list);
        main_play_ll=bindView(R.id.main_play_ll);
        main_rl=bindView(R.id.main_rl);
    }

    @Override
    protected void inidate() {
        EventBus.getDefault().register(this);
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
        tb.setupWithViewPager(vp);
        tb.setSelectedTabIndicatorColor(Color.WHITE);

        intent = new Intent(this, MusicPlayService.class);

        connection = new MyConnection();
        bindService(intent, connection, BIND_AUTO_CREATE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_song_pause:
                if (btn_play_pause.isChecked()){
                    mybinder.playPause();
                }else {
                    mybinder.playStart();
                }
                break;
            case R.id.btn_play_song_next:
                mybinder.playNext();
                break;
            case R.id.btn_play_song_list:

                break;
            case R.id.main_play_ll:
                Log.d("MainActivity", "dianji ");
                PlayFragment playFragment=new PlayFragment();
                manager.beginTransaction().setCustomAnimations(R.anim.part_upshow,R.anim.part_no).replace(R.id.main_all,playFragment).commit();

                break;
            case R.id.main_rl:
                break;
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(SendSongMsgBeanEvent sendSongMsgBeanEvent) {
        SongMsgBean bean= sendSongMsgBeanEvent.getSongMsgBean();
        play_song_singer.setText(bean.getSonginfo().getAuthor());
        play_song_name.setText(bean.getSonginfo().getTitle());

        Picasso.with(this).load(bean.getSonginfo().getPic_big()).into(play_song_pic);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbindService(connection);
    }


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
