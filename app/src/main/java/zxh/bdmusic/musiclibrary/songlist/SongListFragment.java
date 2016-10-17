package zxh.bdmusic.musiclibrary.songlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.baseclass.VolleySingleton;
import zxh.bdmusic.musiclibrary.musicllibbaseinfo.URLVlaues;
import zxh.bdmusic.musiclibrary.recommend.recbaseinfo.OnListenerCallBack;
import zxh.bdmusic.musiclibrary.recommend.songlistclickin.SongClickInMsgCallback;
import zxh.bdmusic.musiclibrary.recommend.songlistclickin.SongListClickInBean;
import zxh.bdmusic.musiclibrary.recommend.songlistclickin.SongListClickInFragment;
import zxh.bdmusic.playservice.MusicPlayService;

/**
 * Created by dllo on 16/9/20.
 */
public class SongListFragment extends BaseFragment {
    private SongListBean bean;
    private RecyclerView rv;

    private Button btn_hotest;
    private Button btn_newest;
    private FragmentManager fm;



    @Override
    protected int setLayout() {
        return R.layout.mc_hotsonglist_fragment;
    }

    @Override
    protected void initView() {
        rv = getViewLayout(R.id.songlist_rv);
        btn_hotest = getViewLayout(R.id.btn_hotest);
        btn_newest = getViewLayout(R.id.btn_newest);
    }

    @Override
    protected void initDate() {
        getGsonData(URLVlaues.HOTEST_SONGLIST);

        btn_hotest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGsonData(URLVlaues.HOTEST_SONGLIST);
            }
        });
        btn_newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGsonData(URLVlaues.NEWEST_SONGLIST);
            }
        });
    }


    private void getGsonData(String url) {
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                bean = gson.fromJson(response, SongListBean.class);
                SongListRvAdapter adapter = new SongListRvAdapter(mContext);
                adapter.setBean(bean);
                rv.setAdapter(adapter);
                StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rv.setLayoutManager(manager);
                adapter.setOnListenerCallBack(new OnListenerCallBack() {
                    @Override
                    public void CallBack(int position, SongListRvAdapter.MyViewHolder holder) {
                        fm = getActivity().getSupportFragmentManager();
                        SongListClickInFragment songListClickInFragment = new SongListClickInFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("clickin_url", URLVlaues.SONGLIST_DETAIL_Front
                                + bean.getDiyInfo().get(position).getList_id() + URLVlaues.SONGLIST_DETAIL_BEHIND);
                        songListClickInFragment.setArguments(bundle);
                        fm.beginTransaction().setCustomAnimations(R.anim.part_in, R.anim.part_no).add(R.id.main_frame_ll, songListClickInFragment).commit();
                    }
                    @Override
                    public void CallBack(int PosValues) {

                    }
                });
                adapter.setSongClickInMsgCallback(new SongClickInMsgCallback() {
                    @Override
                    public void MsgBack(int position) {

                        getClickInGosonData(URLVlaues.SONGLIST_DETAIL_Front
                                + bean.getDiyInfo().get(position).getList_id() + URLVlaues.SONGLIST_DETAIL_BEHIND);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        VolleySingleton.getInstance().addRequest(stringRequest);

    }

    private void getClickInGosonData(String url) {

        final StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            SongListClickInBean clickInBean = new SongListClickInBean();
            @Override
            public void onResponse(String response) {
                ArrayList<String>songIDs = new ArrayList<>();
                ArrayList<String>songNames=new ArrayList<>();
                ArrayList<String>authors=new ArrayList<>();
                Gson gson = new Gson();
                clickInBean = gson.fromJson(response, SongListClickInBean.class);
                for (int i = 0; i < clickInBean.getContent().size(); i++) {
                    songIDs.add(clickInBean.getContent().get(i).getSong_id());
                    Log.d("SongListFragment", clickInBean.getContent().get(i).getSong_id());
                }

                Intent intent = new Intent(getActivity(), MusicPlayService.class);
                intent.putExtra("isfirst",false);
                intent.putStringArrayListExtra("songIDs", songIDs);
                intent.putStringArrayListExtra("songNames", songNames);
                intent.putStringArrayListExtra("authors", authors);
                intent.putExtra("position", 0);
                getActivity().startService(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance().addRequest(request);
    }

}
