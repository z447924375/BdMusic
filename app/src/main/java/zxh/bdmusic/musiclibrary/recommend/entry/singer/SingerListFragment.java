package zxh.bdmusic.musiclibrary.recommend.entry.singer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.tools.VolleySingleton;
import zxh.bdmusic.bean.SingerListBean;

/**
 * Created by dllo on 16/9/27.
 */
public class SingerListFragment extends BaseFragment implements View.OnClickListener {
    SingerListBean bean = new SingerListBean();
    private ListView singer_list_lv;
    private ImageButton singerlist_btn_back;
    FragmentManager fragmentManager ;
    private PullToRefreshListView singer_list_refresh_lv;
    private SingerListAdapter adapter;
    private TextView singer_list_title;

    @Override
    protected int setLayout() {
        return R.layout.singer_list_fragment;
    }

    @Override
    protected void initView() {
        singer_list_lv = (ListView) getView().findViewById(R.id.singer_list_lv);
        singerlist_btn_back = (ImageButton) getView().findViewById(R.id.singerlist_btn_back);
        singer_list_title = (TextView)getView().findViewById(R.id.singer_list_title);

    }

    @Override
    protected void initDate() {
        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        String title=bundle.getString("title");
        singer_list_title.setText(title+"");
        getGsonData(url);
        singerlist_btn_back.setOnClickListener(this);
        View  v= LayoutInflater.from(getContext()).inflate(R.layout.footview,null);
        singer_list_lv.addFooterView(v);
    }

    private void getGsonData(String url) {
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                bean = gson.fromJson(response, SingerListBean.class);
                adapter = new SingerListAdapter(getContext());
                adapter.setBean(bean);
                singer_list_lv.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance().addRequest(stringRequest);

    }

    @Override
    public void onClick(View v) {
        fragmentManager=getActivity().getSupportFragmentManager();
        switch (v.getId()) {
            case R.id.singerlist_btn_back:
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.part_no, R.anim.part_out).remove(this).commit();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       singer_list_lv.setAdapter(adapter);
    }
}
