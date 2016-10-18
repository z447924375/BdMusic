package zxh.bdmusic.play;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;

/**
 * Created by dllo on 16/10/15.
 */
public class PlayListFragment extends BaseFragment implements View.OnClickListener {
    private ListView play_list;
    private LinearLayout remove_this;
    private FragmentManager fm;
    private ArrayList<String> songNames;
    private ArrayList<String> authors;

    @Override
    protected int setLayout() {
        return R.layout.popup;
    }

    @Override
    protected void initView() {
        play_list = getViewLayout(R.id.play_list);
        remove_this = getViewLayout(R.id.remove_this);
    }

    @Override
    protected void initDate() {
        remove_this.setOnClickListener(this);
        PlayLvAdapter adapter = new PlayLvAdapter(getContext());
        Bundle bundle = getArguments();
        if (bundle!=null){
            songNames = bundle.getStringArrayList("songNames");
            authors = bundle.getStringArrayList("authors");
            if (songNames != null && authors != null) {
                adapter.setAuthors(authors);
                adapter.setSongNames(songNames);
                play_list.setAdapter(adapter);
            }

        }

    }

    @Override
    public void onClick(View v) {
        fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().setCustomAnimations(R.anim.part_no, R.anim.part_downdispear)
                .remove(this).commit();
    }
}
