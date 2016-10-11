package zxh.bdmusic.musiclibrary.recommend.entry.singer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import zxh.bdmusic.R;

/**
 * Created by dllo on 16/9/27.
 */
public class SingerListAdapter extends BaseAdapter {
    DisplayImageOptions options;

    Context context;

    public void setBean(SingerListBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    SingerListBean bean = new SingerListBean();

    public SingerListAdapter(Context context) {

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
    public int getCount() {
        return bean == null ? 0 : bean.getArtist().size();
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


        MyViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.singer_list_item, null);
            viewHolder = new MyViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(bean.getArtist().get(position).getAvatar_middle(), viewHolder.singer_list_item_pic, options);
        viewHolder.singer_list_item_name.setText(bean.getArtist().get(position).getName());
        return convertView;
    }

    class MyViewHolder {

        private final ImageView singer_list_item_pic;
        private final TextView singer_list_item_name;
        private final LinearLayout singer_list_item_ll;

        public MyViewHolder(View convertView) {

            singer_list_item_pic = (ImageView) convertView.findViewById(R.id.singer_list_item_pic);
            singer_list_item_name = (TextView) convertView.findViewById(R.id.singer_list_item_name);
            singer_list_item_ll = (LinearLayout) convertView.findViewById(R.id.singer_list_item_ll);
        }
    }

}
