package zxh.bdmusic.play;

import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.baseclass.MyApp;
import zxh.bdmusic.eventbus.SendPlayConditionEvent;
import zxh.bdmusic.eventbus.SendPlayLastOrNextEvent;
import zxh.bdmusic.eventbus.SendPlayTimeEvent;
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
    private SharedPreferences sp;
    private SharedPreferences.Editor sped;
    private SharedPreferences getSP;
    private ImageButton btn_play_clickin_share;
    private SongMsgBean songMsgBean;
    private ImageButton btn_play_clickin_download;


    @Override
    protected int setLayout() {
        return R.layout.play_clickin;
    }

    @Override
    protected void initView() {
        btn_play_clickin_download = getViewLayout(R.id.btn_play_clickin_download);
        btn_play_clickin_share = getViewLayout(R.id.btn_play_clickin_share);
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


        switch (condition % 3) {
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
        btn_play_clickin_share.setOnClickListener(this);
        btn_play_clickin_download.setOnClickListener(this);

        Bundle bundleReceive = getArguments();
        songMsgBean = (SongMsgBean) bundleReceive.getSerializable("SongMsgBean");

        PlayClickInMidFragment midFragment = new PlayClickInMidFragment();
        PlayClickInLiricFragment liricFragment = new PlayClickInLiricFragment();
        midFragment.setArguments(bundleReceive);
        liricFragment.setArguments(bundleReceive);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new PlayClickInLeftFragment());
        fragments.add(midFragment);
        fragments.add(liricFragment);
        adapter = new PlayVpAdapter(getChildFragmentManager(), fragments);
        play_vp.setAdapter(adapter);
        play_vp.setCurrentItem(1);
        play_vp.setOffscreenPageLimit(2);


    }

    private class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mybinder = (MusicPlayService.Mybinder) service;

//            total_time.setText(changeTime(mybinder.getMediaPlayer().getDuration()));
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
                                    play_seekbar.setMax(mybinder.getMediaPlayer().getDuration());
                                    play_seekbar.setProgress(mybinder.getMediaPlayer().getCurrentPosition());
                                    playing_time.setText(changeTime(mybinder.getMediaPlayer().getCurrentPosition()));
                                    total_time.setText(changeTime(mybinder.getMediaPlayer().getDuration()));

                                    SendPlayTimeEvent timeEvent = new SendPlayTimeEvent();
                                    timeEvent.setPlayingTime(mybinder.getMediaPlayer().getCurrentPosition());
                                    EventBus.getDefault().post(timeEvent);


                                }
                            });
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();

            if (mybinder.isPlaying()) {
                btn_play_clickin_pause.setImageResource(R.mipmap.bt_notificationbar_pause);
            } else {
                btn_play_clickin_pause.setImageResource(R.mipmap.bt_notificationbar_play);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_play_clickin_download:

                break;

            case R.id.btn_play_clickin_share:
                showShare();
                break;

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
                btn_play_clickin_pause.setImageResource(R.mipmap.bt_notificationbar_pause);
                break;
            case R.id.btn_play_clickin_next:
                SendPlayLastOrNextEvent eventNext = new SendPlayLastOrNextEvent();
                eventNext.setChoice(2);
                EventBus.getDefault().post(eventNext);
                btn_play_clickin_pause.setImageResource(R.mipmap.bt_notificationbar_pause);

                break;
            case R.id.btn_play_clickin_condition:
                sp = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
                sped = sp.edit();
                switch ((condition + 1) % 3) {
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


    //分享  方法
    private void showShare() {
        ShareSDK.initSDK(getContext());
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();

// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("分享歌曲");
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(songMsgBean.getBitrate().getFile_link());
// text是分享文本，所有平台都需要这个字段
        oks.setText(songMsgBean.getSonginfo().getTitle());
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(getContext());
    }

    public static void downloadSong(SongMsgBean songMsgBean) {
        if (songMsgBean != null && songMsgBean.getBitrate() != null && songMsgBean.getBitrate().getFile_link() != null) {
            DownloadManager downloadManager = (DownloadManager) MyApp.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Log.d("Tools", songMsgBean.getBitrate().getFile_link());
            Uri mDownloadUri = Uri.parse(songMsgBean.getBitrate().getFile_link());
            DownloadManager.Request request = new DownloadManager.Request(mDownloadUri);
//            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

            // TODO: 16/10/16
//            File folder = new File(StringVlaues.downloadPath);
//            if (!(folder.exists() && folder.isDirectory())) {
//                folder.mkdirs();
//            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,
                    songMsgBean.getSonginfo().getTitle() + "-" + songMsgBean.getSonginfo().getAuthor() + ".mp3");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            long downloadId = downloadManager.enqueue(request);
            DownloadSongBean downloadSongBean = new DownloadSongBean();
            downloadSongBean.setDownloadId(downloadId);
            downloadSongBean.setAuthor(songMsgBean.getSonginfo().getAuthor());
            downloadSongBean.setTitle(songMsgBean.getSonginfo().getTitle());
            downloadSongBean.setSongId(songMsgBean.getSonginfo().getSong_id());
//            DBtool.getmDBtools().insertDownloadSong(downloadSongBean);
        }
    }


}
