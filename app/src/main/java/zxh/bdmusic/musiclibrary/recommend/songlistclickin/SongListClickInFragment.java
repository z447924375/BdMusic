package zxh.bdmusic.musiclibrary.recommend.songlistclickin;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import zxh.bdmusic.baseclass.VolleySingleton;
import zxh.bdmusic.musiclibrary.recommend.recbaseinfo.OnListenerCallBack;
import zxh.bdmusic.musiclibrary.songlist.SongListRvAdapter;
import zxh.bdmusic.playservice.MusicPlayService;

/**
 * Created by dllo on 16/9/29.
 */
public class SongListClickInFragment extends BaseFragment implements View.OnClickListener {
    ArrayList<String>songNames=new ArrayList<>();
    ArrayList<String>authors=new ArrayList<>();
    ArrayList<String> songIDs = new ArrayList<>();
    SongListClickInBean bean = new SongListClickInBean();
    private RecyclerView songlist_clickin_rv;
    private ImageView songlist_clickin_pic;
    private TextView songlist_clickin_tag;
    private ImageView songlist_clickin_tiny_pic;
    private TextView songlist_clickin_listennum;
    private TextView songlist_clickin_author;
    private Button songlist_clickin_btn_playall;
    private Button songlist_clickin_btn_download;
    private TextView songlist_clickin_songnum;
    private ImageView songlist_clickin_btn_back;
    private TextView songlist_clickin_title;
    private CollapsingToolbarLayout songlist_clickin_toolbar_ll;
    private AppBarLayout songlist_clickin_appbar_ll;
    private LinearLayout songlist_clickin_ll_three_imgbtn;
    private ImageButton songlist_clickin_btn_share;


    @Override
    protected int setLayout() {
        return R.layout.mc_songlist_clickin;
    }

    @Override
    protected void initView() {
        ShareSDK.initSDK(getContext(),"sharesdk的appkey");

        songlist_clickin_rv = getViewLayout(R.id.songlist_clickin_rv);
        songlist_clickin_pic = getViewLayout(R.id.songlist_clickin_pic);
        songlist_clickin_tag = getViewLayout(R.id.songlist_clickin_tag);
        songlist_clickin_tiny_pic = getViewLayout(R.id.songlist_clickin_tiny_pic);
        songlist_clickin_listennum = getViewLayout(R.id.songlist_clickin_listennum);
        songlist_clickin_author = getViewLayout(R.id.songlist_clickin_author);
        songlist_clickin_btn_playall = getViewLayout(R.id.songlist_clickin_btn_playall);
        songlist_clickin_btn_download = getViewLayout(R.id.songlist_clickin_btn_download);
        songlist_clickin_songnum = getViewLayout(R.id.songlist_clickin_songnum);
        songlist_clickin_btn_back = getViewLayout(R.id.songlist_clickin_btn_back);
        songlist_clickin_title = getViewLayout(R.id.songlist_clickin_title);
        songlist_clickin_toolbar_ll = getViewLayout(R.id.songlist_clickin_toolbar_ll);
        songlist_clickin_appbar_ll = getViewLayout(R.id.songlist_clickin_appbar_ll);
        songlist_clickin_ll_three_imgbtn = getViewLayout(R.id.songlist_clickin_ll_three_imgbtn);
        songlist_clickin_btn_share = getViewLayout(R.id.songlist_clickin_btn_share);
    }

    @Override
    protected void initDate() {
        Bundle bundle = getArguments();
        String url = bundle.getString("clickin_url");
        getGosonData(url);

        songlist_clickin_btn_back.setOnClickListener(this);
        songlist_clickin_btn_share.setOnClickListener(this);
        songlist_clickin_appbar_ll.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                if (percentage > 0.5f) {
                    songlist_clickin_ll_three_imgbtn.setVisibility(View.INVISIBLE);
                } else {
                    songlist_clickin_ll_three_imgbtn.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.songlist_clickin_btn_back:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().setCustomAnimations(R.anim.part_no, R.anim.part_out).remove(this).commit();
                break;
            case R.id.songlist_clickin_btn_share:
                showShare();
                break;
        }

    }

    private void getGosonData(String url) {
        final StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                bean = gson.fromJson(response, SongListClickInBean.class);

                DoInGetGosonData();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance().addRequest(request);
    }

    private void DoInGetGosonData() {

        for (int i = 0; i < bean.getContent().size(); i++) {
            songIDs.add(bean.getContent().get(i).getSong_id());
            songNames.add(bean.getContent().get(i).getTitle());
            authors.add(bean.getContent().get(i).getAuthor());
        }

        SongListClickInAdapter adapter = new SongListClickInAdapter(mContext);
        adapter.setBean(bean);
        songlist_clickin_rv.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(mContext, 1);
        songlist_clickin_rv.setLayoutManager(manager);

        ImageLoader.getInstance().displayImage(bean.getPic_300(), songlist_clickin_pic);
        songlist_clickin_tag.setText("标签" + bean.getTag());
        songlist_clickin_listennum.setText(bean.getListenum());
        songlist_clickin_songnum.setText("/" + bean.getContent().size() + "首");
        songlist_clickin_title.setText(bean.getTitle());

        adapter.setOnListenerCallBack(new OnListenerCallBack() {

            @Override
            public void CallBack(int position) {

                Intent intent=new Intent(getActivity(),MusicPlayService.class);
                intent.putStringArrayListExtra("songIDs", songIDs);
                intent.putStringArrayListExtra("songNames", songNames);
                intent.putStringArrayListExtra("authors", authors);
                intent.putExtra("position", position);
                getActivity().startService(intent);
            }

            @Override//没用
            public void CallBack(int position, SongListRvAdapter.MyViewHolder holder) {
                /******************/
            }
        });
        adapter.setSongClickInBtnMoreMsgCallback(new SongClickInBtnMoreMsgCallback() {

            private TextView song_btn_more_clickin_title;

            @Override
            public void BtnMoreCallback(int position) {
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

                song_btn_more_clickin_title.setText(bean.getContent().get(position).getTitle());
            }
        });

        ImgAscyntask ascyntask = new ImgAscyntask();
        ascyntask.execute(bean.getPic_300());
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
            BitmapDrawable bd = new BitmapDrawable(changeBackgroundImage(bitmap));
            songlist_clickin_appbar_ll.setBackground(bd);
        }
    }


    //图片虚化
    public Bitmap changeBackgroundImage(Bitmap sentBitmap) {
        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            final RenderScript rs = RenderScript.create(mContext);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(20.0f);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }
        return null;
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
