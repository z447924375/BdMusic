package zxh.bdmusic.musiclibrary.recommend.newcd;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import zxh.bdmusic.R;
import zxh.bdmusic.bean.NewCdBean;

/**
 * Created by dllo on 16/10/5.
 */
public class NewCdAdapter extends RecyclerView.Adapter<NewCdAdapter.MyViewHolder> {
    private Context context;
    private DisplayImageOptions options;

    public void setBean(NewCdBean bean) {
        this.bean = bean;
    }

    private NewCdBean bean=new NewCdBean();

    public NewCdAdapter(Context context) {
        this.context = context;
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
