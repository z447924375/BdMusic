package zxh.bdmusic.musiclibrary.musicllibbaseinfo;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;
import zxh.bdmusic.tools.eventbus.SendVpChangePosEvent;
import zxh.bdmusic.musiclibrary.chart.ChartFragment;
import zxh.bdmusic.musiclibrary.karaoke.KaraokeFragment;
import zxh.bdmusic.musiclibrary.mv.MvFragment;
import zxh.bdmusic.musiclibrary.recommend.recbaseinfo.RecommendFragment;
import zxh.bdmusic.musiclibrary.songlist.SongListFragment;

/**
 * Created by dllo on 16/9/20.
 */
public class MusicLibFragment extends BaseFragment {

    private TabLayout mc_tb;
    private ViewPager mc_vp;

    @Override
    protected int setLayout() {
        return R.layout.musiclib_fragment;
    }

    @Override
    protected void initView() {
        mc_tb = getViewLayout(R.id.mc_tb);
        mc_vp = getViewLayout(R.id.mc_vp);
    }

    @Override
    protected void initDate() {
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new RecommendFragment());
        fragments.add(new SongListFragment());
        fragments.add(new ChartFragment());
        fragments.add(new MvFragment());
        fragments.add(new KaraokeFragment());
        MusicLibAdapter adapter = new MusicLibAdapter(getChildFragmentManager(), fragments);
        mc_vp.setAdapter(adapter);
        mc_tb.setupWithViewPager(mc_vp);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeVpEvent(SendVpChangePosEvent event) {
        if (event != null) {
            mc_vp.setCurrentItem(event.getPosition());
        } else {
            Log.d("MusicLibFragment", "event null");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
