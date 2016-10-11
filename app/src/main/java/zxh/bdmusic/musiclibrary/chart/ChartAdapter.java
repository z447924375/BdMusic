package zxh.bdmusic.musiclibrary.chart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import zxh.bdmusic.R;

/**
 * Created by dllo on 16/9/22.
 */
public class ChartAdapter extends BaseAdapter {
    DisplayImageOptions options;

    public ChartAdapter(Context context) {
        this.context = context;
    }

    Context context;

    ChartBean bean = new ChartBean();

    public void setBean(ChartBean bean) {
        this.bean = bean;
    }

    @Override
    public int getCount() {
        return bean == null ? 0 : bean.getContent().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        options = new DisplayImageOptions
                .Builder()
                .showImageForEmptyUri(R.mipmap.default_live_ic)
                .showImageOnLoading(R.mipmap.default_live_ic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        MyViewHoler viewHoler = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mc_chart_item, null);
            viewHoler = new MyViewHoler(convertView);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (MyViewHoler) convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(bean.getContent().get(position).getPic_s192(), viewHoler.chart_item_img,options);
        viewHoler.chart_item_text_title.setText(bean.getContent().get(position).getName());
        viewHoler.chart_item_text1.setText(bean.getContent().get(position).getContent().get(0).getTitle());
        viewHoler.chart_item_text2.setText(bean.getContent().get(position).getContent().get(1).getTitle());
        viewHoler.chart_item_text3.setText(bean.getContent().get(position).getContent().get(2).getTitle());
        return convertView;
    }

    class MyViewHoler {
        private ImageView chart_item_img;
        private TextView chart_item_text_title;
        private TextView chart_item_text1;
        private TextView chart_item_text2;
        private TextView chart_item_text3;

        public MyViewHoler(View convertView) {
            chart_item_img = (ImageView) convertView.findViewById(R.id.char_item_img);
            chart_item_text_title = (TextView) convertView.findViewById(R.id.char_item_text_title);
            chart_item_text1 = (TextView) convertView.findViewById(R.id.char_item_text1);
            chart_item_text2 = (TextView) convertView.findViewById(R.id.char_item_text2);
            chart_item_text3 = (TextView) convertView.findViewById(R.id.char_item_text3);

        }
    }


}
