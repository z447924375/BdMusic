package zxh.bdmusic.musiclibrary.recommend.recbaseinfo;

import zxh.bdmusic.musiclibrary.songlist.SongListRvAdapter;

/**
 * Created by dllo on 16/9/26.
 */
public interface OnListenerCallBack {
   void CallBack(int PosValues);
   void CallBack(int position, SongListRvAdapter.MyViewHolder holder);
}
