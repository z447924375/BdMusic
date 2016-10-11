package zxh.bdmusic.musiclibrary.songlist;

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
import zxh.bdmusic.musiclibrary.recommend.rec_baseinfo.OnListenerCallBack;
import zxh.bdmusic.musiclibrary.recommend.songlist_clickin.SongClickInMsgCallback;

/**
 * Created by dllo on 16/9/22.
 */
public class SongListRvAdapter extends RecyclerView.Adapter<SongListRvAdapter.MyViewHolder>{

    private DisplayImageOptions options;

    SongClickInMsgCallback songClickInMsgCallback;

    public void setSongClickInMsgCallback(SongClickInMsgCallback songClickInMsgCallback) {
        this.songClickInMsgCallback = songClickInMsgCallback;
    }

    public void setOnListenerCallBack(OnListenerCallBack onListenerCallBack) {
        this.onListenerCallBack = onListenerCallBack;
    }

    OnListenerCallBack onListenerCallBack;


    Context context;
    SongListBean bean = new SongListBean();

    public void setBean(SongListBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    public SongListRvAdapter(Context context) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.mc_songlist_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(bean.getDiyInfo().get(position).getList_pic_small(), holder.mc_songlist_img, options);
        holder.mc_songlist_text1.setText(bean.getDiyInfo().get(position).getTitle());
        holder.mc_songlist_listennum.setText(bean.getDiyInfo().get(position).getListen_num() + "");

        holder.mc_songlist_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListenerCallBack.CallBack(position, holder);
            }
        });

        holder.mc_songlist_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songClickInMsgCallback.MsgBack(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.getDiyInfo().size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mc_songlist_img;
        private final TextView mc_songlist_text1;
        private final TextView mc_songlist_listennum;
        private final ImageView mc_songlist_play_btn;

        public MyViewHolder(View itemView) {
            super(itemView);
            mc_songlist_img = (ImageView) itemView.findViewById(R.id.mc_songlist_img);
            mc_songlist_text1 = (TextView) itemView.findViewById(R.id.mc_songlist_text1);
            mc_songlist_listennum = (TextView) itemView.findViewById(R.id.mc_songlist_listennum);
            mc_songlist_play_btn = (ImageView) itemView.findViewById(R.id.mc_songlist_play_btn);

        }
    }

}
