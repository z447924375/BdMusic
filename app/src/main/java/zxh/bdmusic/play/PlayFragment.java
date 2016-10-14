package zxh.bdmusic.play;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.eventbus.SendSongPlayPositionEvent;
import zxh.bdmusic.playservice.MusicPlayService;
import zxh.bdmusic.playservice.SongMsgBean;

/**
 * Created by dllo on 16/10/11.
 */
public class PlayFragment extends BaseFragment implements View.OnClickListener {
    private MyConnection connection;
    private Intent intent;
    private FragmentManager manager;
    private ImageButton btn_play_back;
    private ViewPager play_vp;
    private ImageButton btn_play_clickin_pause;
    private ImageButton btn_play_clickin_last;
    private ImageButton btn_play_clickin_next;
    private MusicPlayService.Mybinder mybinder;
    private PlayVpAdapter adapter;
    private SongMsgBean bean;
    private SeekBar play_seekbar;
    private TextView playing_time;
    private TextView total_time;
    private int duration;
    private int progress;
    private int position;
    @Override
    protected int setLayout() {
        return R.layout.play_clickin;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        btn_play_clickin_pause = getViewLayout(R.id.btn_play_clickin_pause);
        btn_play_clickin_last = getViewLayout(R.id.btn_play_clickin_last);
        btn_play_clickin_next = getViewLayout(R.id.btn_play_clickin_next);
        btn_play_back = getViewLayout(R.id.btn_play_back);
        play_vp = getViewLayout(R.id.play_vp);
        play_seekbar = getViewLayout(R.id.play_seekbar);
        playing_time = getViewLayout(R.id.playing_time);
        total_time = getViewLayout(R.id.total_time);
    }

    @Override
    protected void initDate() {

        intent = new Intent(getActivity(), MusicPlayService.class);
        connection = new MyConnection();
        getActivity().bindService(intent, connection, getActivity().BIND_AUTO_CREATE);
        btn_play_back.setOnClickListener(this);
        btn_play_clickin_pause.setOnClickListener(this);
        btn_play_clickin_last.setOnClickListener(this);
        btn_play_clickin_next.setOnClickListener(this);

        Bundle bundleReceive = getArguments();
        bean = (SongMsgBean) bundleReceive.getSerializable("SongMsgBean");
        PlayClickInMidFragment playClickInMidFragment = new PlayClickInMidFragment();
        playClickInMidFragment.setArguments(bundleReceive);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new PlayClickInLeftFragment());
        fragments.add(playClickInMidFragment);
        fragments.add(new PlayClickInLiricFragment());
        adapter = new PlayVpAdapter(getChildFragmentManager(), fragments);
        play_vp.setAdapter(adapter);
        play_vp.setCurrentItem(1);



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(SendSongPlayPositionEvent songPlayPositionEvent) {
        position = songPlayPositionEvent.getPosition();
        duration=songPlayPositionEvent.getDuration();
        playing_time.setText(changeTime(position));
        progress = position * 100 / duration;
        play_seekbar.setProgress(progress);
        play_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mybinder.seekTo((int) (progress / 100f * duration));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_back:
                manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().setCustomAnimations(R.anim.part_no, R.anim.part_downdispear).remove(this).commit();
                break;
            case R.id.btn_play_clickin_pause:
                if (mybinder.isPlaying()){
                    mybinder.playPause();
                    btn_play_clickin_pause.setImageResource(R.mipmap.bt_notificationbar_play);
                }else {
                    btn_play_clickin_pause.setImageResource(R.mipmap.bt_notificationbar_pause);
                    mybinder.playStart();
                }

                break;
            case R.id.btn_play_clickin_last:

                break;
            case R.id.btn_play_clickin_next:

                mybinder.playNext();

                break;
        }
    }

    private class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mybinder = (MusicPlayService.Mybinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }




    public String changeTime(int duration) {
        int minute = duration / 1000 / 60;
        int seconds = duration / 1000 % 60;
        String m = String.valueOf(minute);
        String s = seconds >= 10 ? String.valueOf(seconds) : "0" + String.valueOf(seconds);
        return m + ":" + s;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
