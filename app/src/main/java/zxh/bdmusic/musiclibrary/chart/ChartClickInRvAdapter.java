package zxh.bdmusic.musiclibrary.chart;

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
import com.nostra13.universalimageloader.core.ImageLoader;

import zxh.bdmusic.R;

/**
 * Created by dllo on 16/10/10.
 */
public class ChartClickInRvAdapter extends RecyclerView.Adapter<ChartClickInRvAdapter.MyViewHolder>{
    ChartClickInBean bean = new ChartClickInBean();
    Context context;
    DisplayImageOptions options;
    ChartClickInListener chartClickInListener;
    ChartClickInBtnListener chartClickInBtnListener;

    public void setChartClickInBtnListener(ChartClickInBtnListener chartClickInBtnListener) {
        this.chartClickInBtnListener = chartClickInBtnListener;
    }

    public void setChartClickInListener(ChartClickInListener chartClickInListener) {
        this.chartClickInListener = chartClickInListener;
    }

    public ChartClickInRvAdapter(Context context) {
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

    public void setBean(ChartClickInBean bean) {
        this.bean = bean;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chart_clickin_rv_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(bean.getSong_list().get(position).getPic_big(),holder.chart_clickin_rv_item_pic,options);
        if (bean.getSong_list().get(position).getHas_mv()==1){
            holder.chart_clickin_rv_item_mv.setImageResource(R.mipmap.ic_mv);
        }
        if (bean.getSong_list().get(position).getHavehigh()==2){
            holder.chart_clickin_rv_item_sq.setImageResource(R.mipmap.ic_sq);
        }
        holder.chart_clickin_rv_item_title.setText(bean.getSong_list().get(position).getTitle());
        holder.chart_clickin_rv_item_author.setText(bean.getSong_list().get(position).getAuthor());
        holder.chart_clickin_rv_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartClickInListener.click(position);
            }
        });
        holder.chart_clickin_rv_item_btnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartClickInBtnListener.btnClick(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.getSong_list().size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView chart_clickin_rv_item_pic;
        private final TextView chart_clickin_rv_item_title;
        private final ImageView chart_clickin_rv_item_mv;
        private final ImageView chart_clickin_rv_item_sq;
        private final TextView chart_clickin_rv_item_author;
        private final ImageButton chart_clickin_rv_item_btnmore;
        private final RelativeLayout chart_clickin_rv_item_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            chart_clickin_rv_item_layout = (RelativeLayout) itemView.findViewById(R.id.chart_clickin_rv_item_layout);
            chart_clickin_rv_item_pic = (ImageView) itemView.findViewById(R.id.chart_clickin_rv_item_pic);
            chart_clickin_rv_item_title = (TextView) itemView.findViewById(R.id.chart_clickin_rv_item_title);
            chart_clickin_rv_item_mv = (ImageView) itemView.findViewById(R.id.chart_clickin_rv_item_mv);
            chart_clickin_rv_item_sq = (ImageView) itemView.findViewById(R.id.chart_clickin_rv_item_sq);
            chart_clickin_rv_item_author = (TextView) itemView.findViewById(R.id.chart_clickin_rv_item_author);
            chart_clickin_rv_item_btnmore = (ImageButton) itemView.findViewById(R.id.chart_clickin_rv_item_btnmore);
        }
    }
}
