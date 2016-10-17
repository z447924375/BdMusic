package zxh.bdmusic.musiclibrary.mv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import zxh.bdmusic.R;

/**
 * Created by dllo on 16/9/28.
 */
public class MvRvAdapter extends RecyclerView.Adapter<MvRvAdapter.MyViewHolder> {

    MvBean bean = new MvBean();

    public void setBean(MvBean bean) {
        this.bean = bean;
    }

    private DisplayImageOptions options;

    public MvRvAdapter(Context context) {
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

    Context context;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mc_mv_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(bean.getResult().getMv_list().get(position).getThumbnail(), holder.mv_img,options);
        holder.mv_text1.setText(bean.getResult().getMv_list().get(position).getTitle());
        holder.mv_text2.setText(bean.getResult().getMv_list().get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.getResult().getMv_list().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mv_img;
        private final TextView mv_text1;
        private final TextView mv_text2;

        public MyViewHolder(View itemView) {
            super(itemView);
            mv_img = (ImageView) itemView.findViewById(R.id.mc_mv_img);
            mv_text1 = (TextView) itemView.findViewById(R.id.mc_mv_text1);
            mv_text2 = (TextView) itemView.findViewById(R.id.mc_mv_text2);
        }
    }
}
