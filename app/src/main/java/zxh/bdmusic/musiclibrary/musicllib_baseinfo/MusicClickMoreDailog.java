package zxh.bdmusic.musiclibrary.musicllib_baseinfo;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import zxh.bdmusic.R;

/**
 * Created by dllo on 16/10/11.
 */
public class MusicClickMoreDailog {
    private Context context;
    public void showDailog(){

            View view= LayoutInflater.from(context).inflate(R.layout.song_btn_more_click_dialog,null);
            AlertDialog dialog = new AlertDialog.Builder(context, R.style.Dialog_btn_more_clicked).create();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
//            window.setWindowAnimations(R.style.mystyle);
            dialog.setView(view);
            dialog.show();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
    }


}
