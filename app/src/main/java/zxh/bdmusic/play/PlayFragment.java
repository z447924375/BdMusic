package zxh.bdmusic.play;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.eventbus.SendPlayConditionEvent;
import zxh.bdmusic.eventbus.SendPlayLastOrNextEvent;
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

    private SeekBar play_seekbar;
    private TextView playing_time;
    private TextView total_time;

    private ImageButton btn_play_clickin_condition;
    private int condition;
    private SharedPreferences con;
    private SharedPreferences sp;
    private SharedPreferences.Editor sped;
    private SharedPreferences getSP;


    @Override
    protected int setLayout() {
        return R.layout.play_clickin;
    }

    @Override
    protected void initView() {
//        EventBus.getDefault().register(this);
        btn_play_clickin_pause = getViewLayout(R.id.btn_play_clickin_pause);
        btn_play_clickin_last = getViewLayout(R.id.btn_play_clickin_last);
        btn_play_clickin_next = getViewLayout(R.id.btn_play_clickin_next);
        btn_play_back = getViewLayout(R.id.btn_play_back);
        play_vp = getViewLayout(R.id.play_vp);
        play_seekbar = getViewLayout(R.id.play_seekbar);
        playing_time = getViewLayout(R.id.playing_time);
        total_time = getViewLayout(R.id.total_time);
        btn_play_clickin_condition = getViewLayout(R.id.btn_play_clickin_condition);
    }

    @Override
    protected void initDate() {


        getSP = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        condition = getSP.getInt("condition", 0);


        switch (condition%3) {
            case 0:
                btn_play_clickin_condition.setImageResource(R.mipmap.bt_list_button_roundplay_normal);
                break;
            case 1:
                btn_play_clickin_condition.setImageResource(R.mipmap.bt_list_random_normal);
                break;
            case 2:
                btn_play_clickin_condition.setImageResource(R.mipmap.bt_list_roundsingle_normal);
                break;
        }

        intent = new Intent(getActivity(), MusicPlayService.class);
        connection = new MyConnection();
        getActivity().bindService(intent, connection, getActivity().BIND_AUTO_CREATE);
        btn_play_back.setOnClickListener(this);
        btn_play_clickin_pause.setOnClickListener(this);
        btn_play_clickin_last.setOnClickListener(this);
        btn_play_clickin_next.setOnClickListener(this);
        btn_play_clickin_condition.setOnClickListener(this);

        Bundle bundleReceive = getArguments();
        SongMsgBean bean = (SongMsgBean) bundleReceive.getSerializable("SongMsgBean");

        Log.d("vvvvv", "bean.getBitrate().getSong_file_id():" + bean.getBitrate().getSong_file_id());

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

    private class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mybinder = (MusicPlayService.Mybinder) service;
            play_seekbar.setMax(mybinder.getTotalDuration());
            total_time.setText(changeTime(mybinder.getMediaPlayer().getDuration()));
            play_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mybinder.getMediaPlayer().seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        // 需要判断一下
                        if (mybinder.isPlaying()) {
                            play_seekbar.post(new Runnable() {
                                @Override
                                public void run() {
                                    play_seekbar.setProgress(mybinder.getCurrentPosition());
                                    playing_time.setText(changeTime(mybinder.getMediaPlayer().getCurrentPosition()));
                                }
                            });
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_play_back:
                manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().setCustomAnimations(R.anim.part_no, R.anim.part_downdispear).remove(this).commit();
                break;
            case R.id.btn_play_clickin_pause:
                if (mybinder.isPlaying()) {
                    SendPlayLastOrNextEvent eventPause = new SendPlayLastOrNextEvent();
                    eventPause.setChoice(11);
                    EventBus.getDefault().post(eventPause);
                    btn_play_clickin_pause.setImageResource(R.mipmap.bt_notificationbar_play);
                } else {
                    SendPlayLastOrNextEvent eventStart = new SendPlayLastOrNextEvent();
                    eventStart.setChoice(22);
                    EventBus.getDefault().post(eventStart);
                    btn_play_clickin_pause.setImageResource(R.mipmap.bt_notificationbar_pause);
                }

                break;
            case R.id.btn_play_clickin_last:

                SendPlayLastOrNextEvent eventLast = new SendPlayLastOrNextEvent();
                eventLast.setChoice(1);
                EventBus.getDefault().post(eventLast);
                break;
            case R.id.btn_play_clickin_next:
                SendPlayLastOrNextEvent eventNext = new SendPlayLastOrNextEvent();
                eventNext.setChoice(2);
                EventBus.getDefault().post(eventNext);
                break;
            case R.id.btn_play_clickin_condition:
                sp = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
                sped = sp.edit();
                switch ((condition+1) % 3) {
                    case 0:

                        Toast.makeText(mContext, "循环播放", Toast.LENGTH_SHORT).show();
                        btn_play_clickin_condition.setImageResource(R.mipmap.bt_list_button_roundplay_normal);
                        SendPlayConditionEvent event = new SendPlayConditionEvent();
                        event.setPlayCondition(0);
                        EventBus.getDefault().post(event);
                        sped.putInt("condition", 0);
                        sped.commit();
                        condition++;
                        break;
                    case 1:
                        Toast.makeText(mContext, "随机播放", Toast.LENGTH_SHORT).show();
                        btn_play_clickin_condition.setImageResource(R.mipmap.bt_list_random_normal);
                        SendPlayConditionEvent event1 = new SendPlayConditionEvent();
                        event1.setPlayCondition(1);
                        EventBus.getDefault().post(event1);
                        sped.putInt("condition", 1);
                        sped.commit();
                        condition++;
                        break;
                    case 2:
                        Toast.makeText(mContext, "单曲循环", Toast.LENGTH_SHORT).show();
                        btn_play_clickin_condition.setImageResource(R.mipmap.bt_list_roundsingle_normal);
                        SendPlayConditionEvent event2 = new SendPlayConditionEvent();
                        event2.setPlayCondition(2);
                        EventBus.getDefault().post(event2);
                        sped.putInt("condition", 2);
                        sped.commit();
                        condition++;
                        break;


                }


                break;


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
    }
}
