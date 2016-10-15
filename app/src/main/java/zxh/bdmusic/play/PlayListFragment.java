package zxh.bdmusic.play;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;

/**
 * Created by dllo on 16/10/15.
 */
public class PlayListFragment extends BaseFragment implements View.OnClickListener {
    private ListView play_list;
    private LinearLayout remove_this;
    private FragmentManager fm;

    @Override
    protected int setLayout() {
        return R.layout.popup;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        play_list = getViewLayout(R.id.play_list);
        remove_this = getViewLayout(R.id.remove_this);
    }

    @Override
    protected void initDate() {
        remove_this.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        fm = getChildFragmentManager();
        fm.beginTransaction().setCustomAnimations(R.anim.part_no, R.anim.part_downdispear).remove(this).commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
