package zxh.bdmusic.mine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zxh.bdmusic.R;
import zxh.bdmusic.bean.LocalSongBean;

/**
 * Created by dllo on 16/10/18.
 */
public class LocalSongAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<LocalSongBean> mArrayList;

    public void setArrayList(ArrayList<LocalSongBean> arrayList) {
        mArrayList = arrayList;
    }

    public LocalSongAdapter(Context context) {
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mArrayList == null ? 0 : mArrayList.size();
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
        LocalSongViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.local_song_lv_item, null);
            viewHolder = new LocalSongViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LocalSongViewHolder) convertView.getTag();
        }

        viewHolder.songAuthor.setText(mArrayList.get(position).getAuthor());
        viewHolder.songTitle.setText(mArrayList.get(position).getTitle());
        return convertView;
    }


    class LocalSongViewHolder {
        private TextView songTitle;
        private TextView songAuthor;

        public LocalSongViewHolder(View convertView) {
            songTitle = (TextView) convertView.findViewById(R.id.local_song_title);
            songAuthor = (TextView) convertView.findViewById(R.id.local_song_author);
        }
    }

}
