package zxh.bdmusic.mine;

import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.bean.LocalSongBean;

/**
 * Created by dllo on 16/9/20.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private TextView localSongNum;
    private ImageButton btnLocalPlay;
    private ListView localLv;
    private LocalSongBean bean;
    private ArrayList<LocalSongBean> mLocalSongArr;
    private ArrayList<String> mSongIDs;

    @Override
    protected int setLayout() {
        return R.layout.mine_fragment;
    }

    @Override
    protected void initView() {
        localSongNum = getViewLayout(R.id.local_song_num);
        btnLocalPlay = getViewLayout(R.id.btn_local_play);
        localLv = getViewLayout(R.id.local_lv);
    }

    @Override
    protected void initDate() {

        btnLocalPlay.setOnClickListener(this);
        mLocalSongArr = new ArrayList<>();
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String author = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String songid = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                if (author != null && title != null) {
                    bean = new LocalSongBean();
                    bean.setAuthor(author);
                    bean.setTitle(title);
                    bean.setSongid(songid);
                    mLocalSongArr.add(bean);
                }

            }
        }
        mSongIDs = new ArrayList<>();
        for (int i = 0; i < mLocalSongArr.size(); i++) {
            mSongIDs.add(mLocalSongArr.get(i).getSongid());
        }

        LocalSongAdapter adapter = new LocalSongAdapter(getContext());
        adapter.setArrayList(mLocalSongArr);
        localLv.setAdapter(adapter);
        localSongNum.setText(mLocalSongArr.size() + "首");

        localLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }

    @Override
    public void onClick(View v) {

    }
}
