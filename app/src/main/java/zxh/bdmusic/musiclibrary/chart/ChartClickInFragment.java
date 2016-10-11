package zxh.bdmusic.musiclibrary.chart;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.eventbus.SendChartClickInBeanEvent;
import zxh.bdmusic.musicplay.MusicPlayService;

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

    @Override
    protected int setLayout() {
        return R.layout.mc_chart_clickin;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        chart_clickin_rv = getViewLayout(R.id.chart_clickin_rv);
        chart_clickin_appbar_ll = getViewLayout(R.id.chart_clickin_appbar_ll);
        chart_clickin_btn_back = getViewLayout(R.id.chart_clickin_btn_back);
        chart_clickin_text_time = getViewLayout(R.id.chart_clickin_text_time);
    }

    @Override
    protected void initDate() {
        Bundle bundle = getArguments();
        String chartClickInPicSrc = bundle.getString("chartClickInPicSrc");
        ImgAscyntask ascyntask = new ImgAscyntask();
        ascyntask.execute(chartClickInPicSrc);
        chart_clickin_btn_back.setOnClickListener(this);
        adapter = new ChartClickInRvAdapter(getContext());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(SendChartClickInBeanEvent event) {
        final ArrayList<String> songIDs = new ArrayList<>();

        bean = event.getChartClickInBean();
        for (int i = 0; i < bean.getSong_list().size(); i++) {
            songIDs.add(bean.getSong_list().get(i).getSong_id());
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
                intent.putExtra("position", position);
                getActivity().startService(intent);

            }
        });
        adapter.setChartClickInBtnListener(new ChartClickInBtnListener() {
            private TextView song_btn_more_clickin_title;

            @Override
            public void btnClick(int position) {

                View view= LayoutInflater.from(getContext()).inflate(R.layout.song_btn_more_click_dialog,null);
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


}
