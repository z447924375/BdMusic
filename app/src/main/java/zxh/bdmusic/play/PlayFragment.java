package zxh.bdmusic.play;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;

/**
 * Created by dllo on 16/10/11.
 */
public class PlayFragment extends BaseFragment implements View.OnClickListener {

    private FragmentManager manager;
    private ImageButton btn_play_back;
    private ViewPager play_vp;
    private CheckBox btn_play_clickin_pause;
    private ImageButton btn_play_clickin_last;
    private ImageButton btn_play_clickin_next;

    @Override
    protected int setLayout() {
        return R.layout.play_clickin;
    }

    @Override
    protected void initView() {
        btn_play_clickin_pause = getViewLayout(R.id.btn_play_clickin_pause);
        btn_play_clickin_last = getViewLayout(R.id.btn_play_clickin_last);
        btn_play_clickin_next = getViewLayout(R.id.btn_play_clickin_next);

        btn_play_back = getViewLayout(R.id.btn_play_back);
        play_vp = getViewLayout(R.id.play_vp);
    }

    @Override
    protected void initDate() {
        btn_play_back.setOnClickListener(this);
        btn_play_clickin_pause.setOnClickListener(this);
        btn_play_clickin_last.setOnClickListener(this);
        btn_play_clickin_next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_back:
                manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().setCustomAnimations(R.anim.part_no, R.anim.part_downdispear).remove(this).commit();
                break;
            case R.id.btn_play_clickin_pause:
                Log.d("PlayFragment", "zanting/bofang");
                break;
            case R.id.btn_play_clickin_last:
                Log.d("PlayFragment", "shangyishou ");
                break;
            case R.id.btn_play_clickin_next:
                Log.d("PlayFragment", "xiayishou");
                break;
        }
    }
}
