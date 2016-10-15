package zxh.bdmusic.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zxh.bdmusic.R;

/**
 * Created by dllo on 16/10/15.
 */
public class PlayLvAdapter extends BaseAdapter {
    ArrayList<String> songIDs = new ArrayList<>();
    ArrayList<String> authors=new ArrayList<>();
    Context context;

    public PlayLvAdapter(Context context) {
        this.context = context;
    }

    public void setSongIDs(ArrayList<String> songIDs) {
        this.songIDs = songIDs;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    @Override
    public int getCount() {
        return songIDs == null ? 0 : songIDs.size();
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
        ListViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.play_list_item, null);
            viewHolder = new ListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(songIDs.get(position));
        viewHolder.author.setText(authors.get(position));
        return convertView;
    }

    class ListViewHolder {

        private final TextView title;
        private final TextView author;

        public ListViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.play_list_title);
            author = (TextView) view.findViewById(R.id.play_list_author);
        }
    }

}
