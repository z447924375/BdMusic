package zxh.bdmusic.musiclibrary.recommend.newcd;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import zxh.bdmusic.R;
import zxh.bdmusic.baseclass.BaseFragment;

/**
 * Created by dllo on 16/10/5.
 */
public class NewCdFragment extends BaseFragment implements View.OnClickListener {

    private ImageButton new_cd_btn_back;
    private FragmentManager fragmentManager;
    private RecyclerView new_cd_rv;


    @Override
    protected int setLayout() {
        return R.layout.mc_recommend_new_cd;
    }

    @Override
    protected void initView() {
        new_cd_rv = getViewLayout(R.id.new_cd_rv);
        new_cd_btn_back = getViewLayout(R.id.new_cd_btn_back);
        new_cd_btn_back.setOnClickListener(this);
    }

    @Override
    protected void initDate() {

    }

    @Override
    public void onClick(View v) {
        fragmentManager=getActivity().getSupportFragmentManager();

        switch (v.getId()){
            case R.id.new_cd_btn_back:
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.part_no, R.anim.part_out).remove(this).commit();
                break;
        }
    }
}
