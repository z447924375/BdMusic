package zxh.bdmusic.musiclibrary.mv;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.baseclass.VolleySingleton;
import zxh.bdmusic.musiclibrary.musicllib_baseinfo.URLVlaues;

/**
 * Created by dllo on 16/9/20.
 */
public class MvFragment extends BaseFragment {

    private RecyclerView mc_mv_rv;
    MvBean bean=new MvBean();
    private Button btn_mv_new;
    private Button btn_mv_hot;

    @Override
    protected int setLayout() {
        return R.layout.mc_mv;
    }

    @Override
    protected void initView() {
        mc_mv_rv = getViewLayout(R.id.mc_mv_rv);
        btn_mv_new = getViewLayout(R.id.btn_mv_newest);
        btn_mv_hot = getViewLayout(R.id.btn_mv_hotest);
    }

    @Override
    protected void initDate() {
        getGsonData(URLVlaues.NEWEST_MV);
        btn_mv_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGsonData(URLVlaues.HOTEST_MV);
            }
        });
        btn_mv_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGsonData(URLVlaues.NEWEST_MV);
            }
        });
    }


    private void getGsonData(String url) {
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                bean = gson.fromJson(response, MvBean.class);
                MvRvAdapter adapter = new MvRvAdapter(mContext);
                adapter.setBean(bean);
                mc_mv_rv.setAdapter(adapter);
                StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                mc_mv_rv.setLayoutManager(manager);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance().addRequest(stringRequest);

    }
}
