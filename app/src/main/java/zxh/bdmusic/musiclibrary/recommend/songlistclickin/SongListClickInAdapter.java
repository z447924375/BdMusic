package zxh.bdmusic.musiclibrary.recommend.songlistclickin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import zxh.bdmusic.R;
import zxh.bdmusic.bean.SongListClickInBean;
import zxh.bdmusic.musiclibrary.recommend.recbaseinfo.OnListenerCallBack;

/**
 * Created by dllo on 16/9/29.
 */
public class SongListClickInAdapter extends RecyclerView.Adapter<SongListClickInAdapter.MyViewHolder> {
    private DisplayImageOptions options;
    private OnListenerCallBack onListenerCallBack;
    private SongClickInBtnMoreMsgCallback songClickInBtnMoreMsgCallback;

    public void setSongClickInBtnMoreMsgCallback(SongClickInBtnMoreMsgCallback songClickInBtnMoreMsgCallback) {
        this.songClickInBtnMoreMsgCallback = songClickInBtnMoreMsgCallback;
    }

    public void setOnListenerCallBack(OnListenerCallBack onListenerCallBack) {
        this.onListenerCallBack = onListenerCallBack;
    }

    Context context;
    SongListClickInBean bean = new SongListClickInBean();

    public void setBean(SongListClickInBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    public SongListClickInAdapter(Context context) {
        this.context = context;
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.default_live_ic).showImageOnLoading(R.mipmap.default_live_ic)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.songlist_clickin_rv_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.songlist_clickin_rv_item_title.setText(bean.getContent().get(position).getTitle());
        holder.songlist_clickin_rv_item_author.setText(bean.getContent().get(position).getAuthor());
        if (bean.getContent().get(position).getHas_mv() == 1) {
            holder.songlist_clickin_rv_item_mv.setImageResource(R.mipmap.ic_mv);
        }
        if (bean.getContent().get(position).getIs_ksong().equals("1")) {
            holder.songlist_clickin_rv_item_btnmike.setImageResource(R.mipmap.ic_mike_normal);
        }
        if (bean.getContent().get(position).getHavehigh() == 2) {
            holder.songlist_clickin_rv_item_sq.setImageResource(R.mipmap.ic_sq);
        }
        holder.songlist_clickin_rv_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListenerCallBack.CallBack(position);
            }
        });
        holder.songlist_clickin_rv_item_btnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songClickInBtnMoreMsgCallback.BtnMoreCallback(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.getContent().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView songlist_clickin_rv_item_title;
        private final TextView songlist_clickin_rv_item_author;
        private final ImageButton songlist_clickin_rv_item_btnmore;
        private final ImageButton songlist_clickin_rv_item_btnmike;
        private final ImageView songlist_clickin_rv_item_sq;
        private final ImageView songlist_clickin_rv_item_mv;
        private final RelativeLayout songlist_clickin_rv_item_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            songlist_clickin_rv_item_title = (TextView) itemView.findViewById(R.id.songlist_clickin_rv_item_title);
            songlist_clickin_rv_item_author = (TextView) itemView.findViewById(R.id.songlist_clickin_rv_item_author);
            songlist_clickin_rv_item_btnmore = (ImageButton) itemView.findViewById(R.id.songlist_clickin_rv_item_btnmore);
            songlist_clickin_rv_item_btnmike = (ImageButton) itemView.findViewById(R.id.songlist_clickin_rv_item_btnmike);
            songlist_clickin_rv_item_sq = (ImageView) itemView.findViewById(R.id.songlist_clickin_rv_item_sq);
            songlist_clickin_rv_item_mv = (ImageView) itemView.findViewById(R.id.songlist_clickin_rv_item_mv);
            songlist_clickin_rv_item_layout = (RelativeLayout) itemView.findViewById(R.id.songlist_clickin_rv_item_layout);
        }
    }


}
