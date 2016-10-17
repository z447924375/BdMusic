package zxh.bdmusic.play;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.eventbus.SendSongMsgBeanEvent;
import zxh.bdmusic.playservice.SongMsgBean;

/**
 * Created by dllo on 16/10/12.
 */
public class PlayClickInMidFragment extends BaseFragment {

    private ImageView play_clickin_mid_img;
    private TextView play_clickin_mid_title;
    private TextView play_clickin_mid_author;
    private DisplayImageOptions options;
    private LinearLayout play_clickin_mid_background;


    @Override
    protected int setLayout() {
        return R.layout.play_clickin_mid;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        play_clickin_mid_img = getViewLayout(R.id.play_clickin_mid_img);
        play_clickin_mid_title = getViewLayout(R.id.play_clickin_mid_title);
        play_clickin_mid_author = getViewLayout(R.id.play_clickin_mid_author);
        play_clickin_mid_background = getViewLayout(R.id.play_clickin_mid_background);
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.default_live_ic)
                .showImageOnLoading(R.mipmap.default_live_ic)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
    }

    @Override
    protected void initDate() {
        Bundle bundle = getArguments();
        SongMsgBean bean = (SongMsgBean) bundle.getSerializable("SongMsgBean");
        ImageLoader.getInstance().displayImage(bean.getSonginfo().getPic_big(), play_clickin_mid_img, options);
        play_clickin_mid_title.setText(bean.getSonginfo().getTitle());
        play_clickin_mid_author.setText(bean.getSonginfo().getAuthor());
        ImgAscyntask ascyntask=new ImgAscyntask();
        ascyntask.execute(bean.getSonginfo().getPic_big());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(SendSongMsgBeanEvent sendSongMsgBeanEvent) {
       SongMsgBean mSongMsgBean = sendSongMsgBeanEvent.getSongMsgBean();
        play_clickin_mid_author.setText(mSongMsgBean.getSonginfo().getAuthor());
        play_clickin_mid_title.setText(mSongMsgBean.getSonginfo().getTitle());
        ImageLoader.getInstance().displayImage(mSongMsgBean.getSonginfo().getPic_big(), play_clickin_mid_img, options);
        ImgAscyntask ascyntask=new ImgAscyntask();
        ascyntask.execute(mSongMsgBean.getSonginfo().getPic_big());
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
            play_clickin_mid_background.setBackground(bd);
        }
    }


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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
