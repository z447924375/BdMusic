package zxh.bdmusic.musiclibrary.chart;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.eventbus.SendChartClickInBeanEvent;
import zxh.bdmusic.playservice.MusicPlayService;

/**
 * Created by dllo on 16/10/10.
 */
public class ChartClickInFragment extends BaseFragment implements View.OnClickListener {
    private ChartClickInBean bean;
    private ImageButton chart_clickin_btn_back;
    private FragmentManager manager;
    private ChartClickInRvAdapter adapter;
    private TextView chart_clickin_text_time;
    private AppBarLayout chart_clickin_appbar_ll;
    private RecyclerView chart_clickin_rv;
    private ImageButton chart_clickin_btn_share;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected int setLayout() {
        return R.layout.mc_chart_clickin;
    }

    @Override
    protected void initView() {
        ShareSDK.initSDK(getContext(), "sharesdk的appkey");
        EventBus.getDefault().register(this);
        chart_clickin_rv = getViewLayout(R.id.chart_clickin_rv);
        chart_clickin_appbar_ll = getViewLayout(R.id.chart_clickin_appbar_ll);
        chart_clickin_btn_back = getViewLayout(R.id.chart_clickin_btn_back);
        chart_clickin_text_time = getViewLayout(R.id.chart_clickin_text_time);
        chart_clickin_btn_share = getViewLayout(R.id.chart_clickin_btn_share);
    }

    @Override
    protected void initDate() {
        Bundle bundle = getArguments();
        String chartClickInPicSrc = bundle.getString("chartClickInPicSrc");
        ImgAscyntask ascyntask = new ImgAscyntask();
        ascyntask.execute(chartClickInPicSrc);
        chart_clickin_btn_back.setOnClickListener(this);
        chart_clickin_btn_share.setOnClickListener(this);
        adapter = new ChartClickInRvAdapter(getContext());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(SendChartClickInBeanEvent event) {
        final ArrayList<String> songIDs = new ArrayList<>();
        final ArrayList<String> songNames = new ArrayList<>();
        final ArrayList<String> authors = new ArrayList<>();
        bean = event.getChartClickInBean();
        for (int i = 0; i < bean.getSong_list().size(); i++) {
            songIDs.add(bean.getSong_list().get(i).getSong_id());

            songNames.add(bean.getSong_list().get(i).getTitle());
            authors.add(bean.getSong_list().get(i).getAuthor());
            Log.d("ChartClickInFragment", bean.getSong_list().get(i).getTitle()
                    + bean.getSong_list().get(i).getAuthor());

        }
        chart_clickin_rv.setAdapter(adapter);
        adapter.setBean(bean);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        chart_clickin_rv.setLayoutManager(manager);
        adapter.setChartClickInListener(new ChartClickInListener() {//点击item
            @Override
            public void click(int position) {

                Intent intent = new Intent(getActivity(), MusicPlayService.class);
                intent.putStringArrayListExtra("songIDs", songIDs);
                intent.putStringArrayListExtra("songNames", songNames);
                intent.putStringArrayListExtra("authors", authors);
                intent.putExtra("position", position);
                getActivity().startService(intent);

            }
        });
        adapter.setChartClickInBtnListener(new ChartClickInBtnListener() {
            private TextView song_btn_more_clickin_title;

            @Override
            public void btnClick(int position) {

                View view = LayoutInflater.from(getContext()).inflate(R.layout.song_btn_more_click_dialog, null);
                AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.Dialog_btn_more_clicked).create();
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
//                window.setWindowAnimations(R.style.mystyle);
                dialog.setView(view);
                dialog.show();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);

                song_btn_more_clickin_title = (TextView) view.findViewById(R.id.song_btn_more_clickin_title);

                song_btn_more_clickin_title.setText(bean.getSong_list().get(position).getTitle());

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chart_clickin_btn_back:
                manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().setCustomAnimations(R.anim.part_no, R.anim.part_out).remove(this).commit();
                break;
            case R.id.chart_clickin_btn_share:
                showShare();
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class ImgAscyntask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String str = params[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(str);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                }
                connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            BitmapDrawable bd = new BitmapDrawable(bitmap);
            chart_clickin_appbar_ll.setBackground(bd);
        }
    }


    //分享  方法
    private void showShare() {
        ShareSDK.initSDK(getContext());
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();

// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
// text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
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


}
