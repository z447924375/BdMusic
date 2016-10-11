package zxh.bdmusic.musiclibrary.chart;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.baseclass.VolleySingleton;
import zxh.bdmusic.eventbus.SendChartClickInBeanEvent;
import zxh.bdmusic.musiclibrary.musicllib_baseinfo.URLVlaues;

/**
 * Created by dllo on 16/9/20.
 */
public class ChartFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ChartBean bean;
    private ListView chart_lv;
    private FragmentManager manager;

    @Override
    protected int setLayout() {
        return R.layout.mc_chart;
    }

    @Override
    protected void initView() {
        chart_lv = getViewLayout(R.id.chart_lv);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.footview, null);
        chart_lv.addFooterView(v);
        chart_lv.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getChartClickInUrl(URLVlaues.MUSICSTORE_TOP, position);
    }

    @Override
    protected void initDate() {
        getGsonData(URLVlaues.MUSICSTORE_TOP);
    }


    private void getGsonData(String url) {
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                bean = gson.fromJson(response, ChartBean.class);
                ChartAdapter adapter = new ChartAdapter(mContext);
                adapter.setBean(bean);
                chart_lv.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleySingleton.getInstance().addRequest(stringRequest);
    }

    private void getChartClickInUrl(String url, final int position) {
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                bean = gson.fromJson(response, ChartBean.class);
                String chartClickInUrl = URLVlaues.TOP_SONG_FRONT + bean.getContent().get(position).getType() + "" + URLVlaues.TOP_SONG_BEHIND;
                String chartClickInPicSrc = bean.getContent().get(position).getPic_s210();
                getChartClickInGsonData(chartClickInUrl);
                ChartClickInFragment fragment = new ChartClickInFragment();
                manager = getActivity().getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("chartClickInPicSrc", chartClickInPicSrc);
                fragment.setArguments(bundle);
                manager.beginTransaction().setCustomAnimations(R.anim.part_in, R.anim.part_no).add(R.id.main_frame_ll, fragment).commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleySingleton.getInstance().addRequest(stringRequest);
    }

    private void getChartClickInGsonData(String url) {
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ChartClickInBean chartClickInBean = new ChartClickInBean();
                Gson gson = new Gson();
                chartClickInBean = gson.fromJson(response, ChartClickInBean.class);

                SendChartClickInBeanEvent event = new SendChartClickInBeanEvent();
                event.setChartClickInBean(chartClickInBean);
                EventBus.getDefault().post(event);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleySingleton.getInstance().addRequest(stringRequest);
    }


}
