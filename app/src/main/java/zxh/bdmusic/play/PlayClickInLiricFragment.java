package zxh.bdmusic.play;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.eventbus.SendPlayTimeEvent;
import zxh.bdmusic.eventbus.SendSongMsgBeanEvent;
import zxh.bdmusic.play.lrc.DefaultLrcBuilder;
import zxh.bdmusic.play.lrc.ILrcBuilder;
import zxh.bdmusic.play.lrc.ILrcView;
import zxh.bdmusic.play.lrc.ILrcViewListener;
import zxh.bdmusic.play.lrc.LrcRow;
import zxh.bdmusic.playservice.MusicPlayService;
import zxh.bdmusic.playservice.SongMsgBean;

/**
 * Created by dllo on 16/10/12.
 */
public class PlayClickInLiricFragment extends BaseFragment {

    private Handler handler;
    //自定义LrcView，用来展示歌词
    private ILrcView mLrcView;
    private MusicPlayService.Mybinder myBinder;
    private SongMsgBean mSongMsgBean;
    private MyConnection connection;
    private Intent intent;

    @Override
    protected int setLayout() {
        return R.layout.play_clickin_liric;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mLrcView = getViewLayout(R.id.lrcView);
    }

    @Override
    protected void initDate() {
        Bundle bundle = getArguments();
        mSongMsgBean = (SongMsgBean) bundle.getSerializable("SongMsgBean");
        getFromAssets(mSongMsgBean.getSonginfo().getLrclink());
        intent = new Intent(getActivity(), MusicPlayService.class);
        connection = new MyConnection();
        getActivity().bindService(intent, connection, getActivity().BIND_AUTO_CREATE);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 1) {
                    String str = (String) message.obj;
                    //解析歌词构造器
                    ILrcBuilder builder = new DefaultLrcBuilder();
                    //解析歌词返回LrcRow集合
                    List<LrcRow> rows = builder.getLrcRows(str);
                    //将得到的歌词集合传给mLrcView用来展示1
                    mLrcView.setLrc(rows);
                }
                return false;
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(SendSongMsgBeanEvent sendSongMsgBeanEvent) {
        SongMsgBean songMsgBean = sendSongMsgBeanEvent.getSongMsgBean();
        getFromAssets(songMsgBean.getSonginfo().getLrclink());
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 1) {
                    String str = (String) message.obj;
                    //解析歌词构造器
                    ILrcBuilder builder = new DefaultLrcBuilder();
                    //解析歌词返回LrcRow集合
                    List<LrcRow> rows = builder.getLrcRows(str);
                    //将得到的歌词集合传给mLrcView用来展示1
                    mLrcView.setLrc(rows);
                }
                return false;
            }
        });


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveTime(SendPlayTimeEvent timeEvent) {
        int playtime = timeEvent.getPlayingTime();

        mLrcView.seekLrcToTime(playtime);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myBinder.getMediaPlayer() != null) {
            myBinder.getMediaPlayer().stop();
        }
        EventBus.getDefault().unregister(this);

    }


    public void getFromAssets(final String str) {
        // 不必用json去解析,只要去读流就可以了
        Log.d("aaaaa", "aaa");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(str);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream stream = connection.getInputStream();
                        InputStreamReader inputReader = new InputStreamReader(stream);
                        BufferedReader bufReader = new BufferedReader(inputReader);
                        String line = "";
                        String result = "";
                        while ((line = bufReader.readLine()) != null) {
                            // 取出字符左右两边的空格
                            if (line.trim().equals(""))
                                continue;
                            result += line + "\r\n";
                        }
                        bufReader.close();
                        inputReader.close();
                        stream.close();

                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        handler.sendMessage(msg);


                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (MusicPlayService.Mybinder) iBinder;


//            // 设置歌词当前位置
//            mLrcView.seekLrcToTime(myBinder.getMediaPlayer().getCurrentPosition());

            //开始播放歌曲并同步展示歌词
            //  beginLrcPlay();

            // 设置自定义的LrcView上下拖动歌词时监听
            mLrcView.setListener(new ILrcViewListener() {
                //当歌词被用户上下拖动的时候回调该方法,从高亮的那一句歌词开始播放
                public void onLrcSeeked(int newPosition, LrcRow row) {
                    if (myBinder.getMediaPlayer() != null) {

                        myBinder.getMediaPlayer().seekTo((int) row.time);
                    }

                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    public String changeTime(int duration) {
        int minute = duration / 1000 / 60;
        int seconds = duration / 1000 % 60;
        String m = String.valueOf(minute);
        String s = seconds >= 10 ? String.valueOf(seconds) : "0" + String.valueOf(seconds);
        return m + ":" + s;
    }

}
