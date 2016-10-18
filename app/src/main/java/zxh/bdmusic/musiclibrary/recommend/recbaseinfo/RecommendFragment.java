package zxh.bdmusic.musiclibrary.recommend.recbaseinfo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.tools.VolleySingleton;
import zxh.bdmusic.bean.RecommendBean;
import zxh.bdmusic.tools.eventbus.SendVpChangePosEvent;
import zxh.bdmusic.usedvalues.URLVlaues;
import zxh.bdmusic.musiclibrary.recommend.entry.singer.McSingerFragment;
import zxh.bdmusic.musiclibrary.recommend.newcd.NewCdFragment;
import zxh.bdmusic.musiclibrary.recommend.songlistclickin.SongListClickInFragment;
import zxh.bdmusic.musiclibrary.songlist.SongListRvAdapter;

/**
 * Created by dllo on 16/9/20.
 */
public class RecommendFragment extends BaseFragment {
   private ArrayList<RecommendBean> arrayList = new ArrayList<>();
    private RecommendFragment recommendFragment;
    private RecommendBean bean;
    private RecyclerView rv;
    private FragmentManager fm;

    @Override
    protected int setLayout() {
        return R.layout.mc_recommend;
    }

    @Override
    protected void initView() {
        rv = getViewLayout(R.id.mc_recommend_rv);
    }


    @Override
    protected void initDate() {
        getGsonData("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=1413c&operator=1&method=baidu.ting.plaza.index&cuid=8ADCB33F64DBB1F5314036551C922491");

    }

    private void getGsonData(String url) {
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                bean = gson.fromJson(response, RecommendBean.class);
                arrayList.add(bean);
                RecommendAdapter adapter = new RecommendAdapter(getContext());
                adapter.setBean(bean);
                rv.setAdapter(adapter);
                final LinearLayoutManager manager = new LinearLayoutManager(getContext());
                rv.setLayoutManager(manager);
                fm = getActivity().getSupportFragmentManager();

                adapter.setOnListenerCallBack(new OnListenerCallBack() {


                    @Override
                    public void CallBack(int PosValues) {
                        switch (PosValues) {

                            case 1:
                                recommendFragment = new RecommendFragment();
                                fm.beginTransaction().hide(recommendFragment).setCustomAnimations(R.anim.part_in, R.anim.part_no).
                                        add(R.id.main_frame_ll, new McSingerFragment()).commit();

                                break;
                            case 2:
                                break;
                            case 3:
                                //radio
                                break;
                            case 4:
                                //member
                                break;
                            case 5://songlist_recommmend_btn_more

                                SendVpChangePosEvent event=new SendVpChangePosEvent();
                                event.setPosition(1);
                                EventBus.getDefault().post(event);

                                break;
                            case 6://songlist_recommmend_img1
                                sendSonglistClickInURL(0);
                                break;
                            case 7://songlist_recommmend_img2
                                sendSonglistClickInURL(1);
                                break;
                            case 8://songlist_recommmend_img3
                                sendSonglistClickInURL(2);
                                break;
                            case 9://songlist_recommmend_img4
                                sendSonglistClickInURL(3);
                                break;
                            case 10://songlist_recommmend_img5
                                sendSonglistClickInURL(4);
                                break;
                            case 11://songlist_recommmend_img6
                                sendSonglistClickInURL(5);
                                break;
                            case 12://newcd btn more
                                recommendFragment = new RecommendFragment();
                                fm.beginTransaction().hide(recommendFragment).setCustomAnimations(R.anim.part_in, R.anim.part_no).
                                        add(R.id.main_frame_ll, new NewCdFragment()).commit();
                                break;
                            case 13://newcd img1

                                break;
                            case 14:
                                break;
                            case 15:
                                break;
                            case 16:
                                break;
                            case 17:
                                break;
                            case 18://newcd img6
                                break;

                        }
                    }

                    @Override
                    public void CallBack(int position, SongListRvAdapter.MyViewHolder holder) {

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

    private void sendSonglistClickInURL(int i) {
        SongListClickInFragment songListClickInFragment = new SongListClickInFragment();
        Bundle bundle = new Bundle();
        bundle.putString("clickin_url", URLVlaues.SONGLIST_DETAIL_Front
                + bean.getResult().getDiy().getResult().get(i).getListid() + URLVlaues.SONGLIST_DETAIL_BEHIND);

        songListClickInFragment.setArguments(bundle);
        fm.beginTransaction().setCustomAnimations(R.anim.part_in, R.anim.part_no).add(R.id.main_frame_ll, songListClickInFragment).commit();
    }


}
