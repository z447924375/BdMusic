package zxh.bdmusic.musiclibrary.recommend.entry.singer;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.baseclass.VolleySingleton;
import zxh.bdmusic.musiclibrary.musicllib_baseinfo.URLVlaues;

/**
 * Created by dllo on 16/9/26.
 */
public class McSingerFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout chinese_male_singer;
    private RelativeLayout chinese_female_singer;
    private RelativeLayout chinese_group;
    private ImageButton singer_btn_back;
    private SingerListFragment singerListFragment;
    private RelativeLayout usa_male_singer;
    private RelativeLayout usa_female_singer;
    private RelativeLayout usa_group;
    private RelativeLayout korea_male_singer;
    private RelativeLayout korea_female_singer;
    private RelativeLayout korea_group;
    private RelativeLayout japan_male_singer;
    private RelativeLayout japan_female_singer;
    private RelativeLayout japan_group;
    private RelativeLayout other_singer;
    private FragmentManager fm;
    private LinearLayout mc_singer_scrollview;
    HotSingerImgBean bean=new HotSingerImgBean();
    private DisplayImageOptions options;


    @Override
    protected int setLayout() {
        return R.layout.mc_singer_fragment;
    }

    @Override
    protected void initView() {
        singer_btn_back = (ImageButton) getView().findViewById(R.id.singer_btn_back);
        chinese_male_singer = (RelativeLayout) getView().findViewById(R.id.chinese_male_singer);
        chinese_female_singer = (RelativeLayout) getView().findViewById(R.id.chinese_female_singer);
        chinese_group = (RelativeLayout) getView().findViewById(R.id.chinese_group);
        usa_male_singer = (RelativeLayout) getView().findViewById(R.id.usa_male_singer);
        usa_female_singer = (RelativeLayout) getView().findViewById(R.id.usa_female_singer);
        usa_group = (RelativeLayout) getView().findViewById(R.id.usa_group);
        korea_male_singer = (RelativeLayout) getView().findViewById(R.id.korea_male_singer);
        korea_female_singer = (RelativeLayout) getView().findViewById(R.id.korea_female_singer);
        korea_group = (RelativeLayout) getView().findViewById(R.id.korea_group);
        japan_male_singer = (RelativeLayout) getView().findViewById(R.id.japan_male_singer);
        japan_female_singer = (RelativeLayout) getView().findViewById(R.id.japan_female_singer);
        japan_group = (RelativeLayout) getView().findViewById(R.id.japan_group);
        other_singer = (RelativeLayout) getView().findViewById(R.id.other_singer);
        mc_singer_scrollview = getViewLayout(R.id.mc_singer_scrollview);
        options = new DisplayImageOptions
                .Builder()
                .showImageForEmptyUri(R.mipmap.default_live_ic)
                .showImageOnLoading(R.mipmap.default_live_ic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

    }

    @Override
    protected void initDate() {
        chinese_male_singer.setOnClickListener(this);
        chinese_female_singer.setOnClickListener(this);
        chinese_group.setOnClickListener(this);
        singer_btn_back.setOnClickListener(this);
        usa_male_singer.setOnClickListener(this);
        usa_female_singer.setOnClickListener(this);
        usa_group.setOnClickListener(this);
        korea_male_singer.setOnClickListener(this);
        korea_female_singer.setOnClickListener(this);
        korea_group.setOnClickListener(this);
        japan_male_singer.setOnClickListener(this);
        japan_female_singer.setOnClickListener(this);
        japan_group.setOnClickListener(this);
        other_singer.setOnClickListener(this);

      
        getHotSingerImgGsonData(URLVlaues.HOT_SINGER_IMG);
        

    }

    @Override
    public void onClick(View v) {
        fm = getActivity().getSupportFragmentManager();

        switch (v.getId()) {
            case R.id.singer_btn_back:
                fm.beginTransaction().setCustomAnimations(R.anim.part_no, R.anim.part_out).remove(this).commit();

                break;

            case R.id.chinese_male_singer:
                sendURL(URLVlaues.CHINESE_MALE_SINGER,"华语男歌手");
                break;
            case R.id.chinese_female_singer:
                sendURL(URLVlaues.CHINESE_FEMALE_SINGER,"华语女歌手");
                break;
            case R.id.chinese_group:
                sendURL(URLVlaues.CHINESE_GROUP,"华语组合");
                break;
            case R.id.usa_male_singer:
                sendURL(URLVlaues.USA_MALE_SINGER,"欧美男歌手");
                break;
            case R.id.usa_female_singer:
                sendURL(URLVlaues.USA_FEMALE_SINGER,"欧美女歌手");
                break;
            case R.id.usa_group:
                sendURL(URLVlaues.USA_GROUP,"欧美组合");
                break;
            case R.id.korea_male_singer:
                sendURL(URLVlaues.KOREA_MALE_SINGER,"韩国男歌手");
                break;
            case R.id.korea_female_singer:
                sendURL(URLVlaues.KOREA_FEMALE_SINGER,"韩国女歌手");
                break;
            case R.id.korea_group:
                sendURL(URLVlaues.KOREA_GROUP,"韩国组合");
                break;
            case R.id.japan_male_singer:
                sendURL(URLVlaues.JAPAN_MALE_SINGER,"日本男歌手");
                break;
            case R.id.japan_female_singer:
                sendURL(URLVlaues.JAPAN_FEMALE_SINGER,"日本女歌手");
                break;
            case R.id.japan_group:
                sendURL(URLVlaues.JAPAN_GROUP,"日本组合");
                break;
            case R.id.other_singer:
                sendURL(URLVlaues.OTHER_SINGER,"其他歌手");
                break;

        }
    }

    private void sendURL(String URL,String title) {
        singerListFragment = new SingerListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", URL);
        bundle.putString("title",title);
        singerListFragment.setArguments(bundle);
        fm.beginTransaction().setCustomAnimations(R.anim.part_in,R.anim.part_no).add(R.id.mc_singer_ll, singerListFragment).commit();
    }

    private void getHotSingerImgGsonData(String url) {
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                bean = gson.fromJson(response, HotSingerImgBean.class);

                for (int i = 0; i < bean.getArtist().size(); i++) {
                    View view= LayoutInflater.from(getContext()).inflate(R.layout.imglayout,mc_singer_scrollview,false);
                    ImageView img = (ImageView) view.findViewById(R.id.hot_singer_img);
                    img.setAdjustViewBounds(true);
                    ImageLoader.getInstance().displayImage(bean.getArtist().get(i).getAvatar_middle(),img,options);
                    mc_singer_scrollview.addView(view);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance().addRequest(stringRequest);

    }

}