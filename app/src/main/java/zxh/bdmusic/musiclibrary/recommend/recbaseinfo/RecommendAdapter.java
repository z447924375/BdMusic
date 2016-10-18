package zxh.bdmusic.musiclibrary.recommend.recbaseinfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;

import zxh.bdmusic.R;
import zxh.bdmusic.bean.RecommendBean;
import zxh.bdmusic.usedvalues.RecItemControlValues;

/**
 * Created by dllo on 16/9/20.
 */
public class RecommendAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    OnListenerCallBack onListenerCallBack;

    public void setOnListenerCallBack(OnListenerCallBack onListenerCallBack) {
        this.onListenerCallBack = onListenerCallBack;
    }

    /*****/
    DisplayImageOptions options;
    private Context context;
    private RecommendBean bean = new RecommendBean();

    public void setBean(RecommendBean bean) {
        this.bean = bean;
    }

    public RecommendAdapter(Context context) {
        this.context = context;
        options = new DisplayImageOptions
                .Builder()
                .showImageForEmptyUri(R.mipmap.default_live_ic)
                .showImageOnLoading(R.mipmap.default_live_ic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public int getItemViewType(int position) {
        return bean.getModule().get(position).getPos();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View focusView = LayoutInflater.from(context).inflate(R.layout.mc_recommend_focus_item, parent, false);
                FocusViewHolder focusViewHolder = new FocusViewHolder(focusView);
                return focusViewHolder;
            case 2:
                View entryView = LayoutInflater.from(context).inflate(R.layout.mc_recommend_entry_item, parent, false);
                EntryViewHolder entryViewHolder = new EntryViewHolder(entryView);
                return entryViewHolder;
            case 3:// TODO: 16/9/23 null
                View adViewDayHot = LayoutInflater.from(context).inflate(R.layout.mc_recommend_advertisement_item, parent, false);
                ADViewHolder adViewHolder1 = new ADViewHolder(adViewDayHot);
                return adViewHolder1;
            case 4:
                View songlistRecommendView = LayoutInflater.from(context).inflate(R.layout.mc_recommend_six_img_item, parent, false);
                SixImgViewHolder sixImgViewHolder1 = new SixImgViewHolder(songlistRecommendView);
                return sixImgViewHolder1;
            case 5:// TODO: 16/9/23 null
                View adViewBig = LayoutInflater.from(context).inflate(R.layout.mc_recommend_advertisement_item, parent, false);
                ADViewHolder adViewHolder2 = new ADViewHolder(adViewBig);
                return adViewHolder2;
            case 6:
                View newcdView = LayoutInflater.from(context).inflate(R.layout.mc_recommend_six_img_item, parent, false);
                SixImgViewHolder sixImgViewHolder2 = new SixImgViewHolder(newcdView);
                return sixImgViewHolder2;

            case 7:
                View hotAlbumView = LayoutInflater.from(context).inflate(R.layout.mc_recommend_three_img_item, parent, false);
                ThreeImgViewHolder hotAlbumViewHolder = new ThreeImgViewHolder(hotAlbumView);
                return hotAlbumViewHolder;
            case 8:
                View adView = LayoutInflater.from(context).inflate(R.layout.mc_recommend_advertisement_item, parent, false);
                ADViewHolder adViewHolder = new ADViewHolder(adView);
                return adViewHolder;
            case 9:
                View sceneView = LayoutInflater.from(context).inflate(R.layout.mc_recommend_scene_radio_item, parent, false);
                SceneViewHolder sceneViewHolder = new SceneViewHolder(sceneView);
                return sceneViewHolder;

            case 10:
                View recsongView = LayoutInflater.from(context).inflate(R.layout.mc_recommend_recsong_item, parent, false);
                RecsongViewHolder recsongViewHolder = new RecsongViewHolder(recsongView);
                return recsongViewHolder;

            case 11:
                View originalView = LayoutInflater.from(context).inflate(R.layout.mc_recommend_three_img_item, parent, false);
                ThreeImgViewHolder originalViewHolder = new ThreeImgViewHolder(originalView);
                return originalViewHolder;
            case 12:
                View hotMvView = LayoutInflater.from(context).inflate(R.layout.mc_recommend_six_img_item, parent, false);
                SixImgViewHolder sixImgViewHolder3 = new SixImgViewHolder(hotMvView);
                return sixImgViewHolder3;
            case 13:
                View leboRadioView = LayoutInflater.from(context).inflate(R.layout.mc_recommend_six_img_item, parent, false);
                SixImgViewHolder sixImgViewHolder4 = new SixImgViewHolder(leboRadioView);
                return sixImgViewHolder4;
            case 14:
                View columnView = LayoutInflater.from(context).inflate(R.layout.mc_recommend_column_item, parent, false);
                ColumnViewHolder columnViewHolder = new ColumnViewHolder(columnView);
                return columnViewHolder;

            case 15:// TODO: 16/9/23 null
                View adView15 = LayoutInflater.from(context).inflate(R.layout.mc_recommend_advertisement_item, parent, false);
                ADViewHolder adViewHolder15 = new ADViewHolder(adView15);
                return adViewHolder15;

        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int pos = getItemViewType(position);
        switch (pos) {
            case 1://设置录播图
                ArrayList<String> imgsrcs = new ArrayList<>();
                FocusViewHolder focusViewHolder = (FocusViewHolder) holder;
                for (int i = 0; i < bean.getResult().getFocus().getResult().size(); i++) {
                    String imgsrc = bean.getResult().getFocus().getResult().get(i).getRandpic();
                    imgsrcs.add(imgsrc);
                }
                focusViewHolder.recommend_banner.setImages(imgsrcs);


                break;
            case 2:
                EntryViewHolder entryViewHolder = (EntryViewHolder) holder;
                ImageLoader.getInstance().displayImage(bean.getResult().getEntry().getResult().get(0).getIcon(), entryViewHolder.img_entry_singer, options);
                ImageLoader.getInstance().displayImage(bean.getResult().getEntry().getResult().get(1).getIcon(), entryViewHolder.img_entry_classify, options);
                ImageLoader.getInstance().displayImage(bean.getResult().getEntry().getResult().get(2).getIcon(), entryViewHolder.img_entry_radio, options);
                ImageLoader.getInstance().displayImage(bean.getResult().getEntry().getResult().get(3).getIcon(), entryViewHolder.img_entry_member, options);
                entryViewHolder.text_entry_singer.setText(bean.getResult().getEntry().getResult().get(0).getTitle());
                entryViewHolder.text_entry_classify.setText(bean.getResult().getEntry().getResult().get(1).getTitle());
                entryViewHolder.text_entry_radio.setText(bean.getResult().getEntry().getResult().get(2).getTitle());
                entryViewHolder.text_entry_member.setText(bean.getResult().getEntry().getResult().get(3).getTitle());
                entryViewHolder.img_entry_singer.setOnClickListener(this);
                entryViewHolder.img_entry_classify.setOnClickListener(this);
                entryViewHolder.img_entry_member.setOnClickListener(this);
                entryViewHolder.img_entry_radio.setOnClickListener(this);


                break;
            case 3://////
                ADViewHolder adViewHolder2 = (ADViewHolder) holder;
                break;
            case 4://设置歌单推荐
                SixImgViewHolder diyViewHolder = (SixImgViewHolder) holder;
                ImageLoader.getInstance().displayImage(bean.getModule().get(pos - 1).getPicurl(), diyViewHolder.songlist_recommend_pictrul, options);
                diyViewHolder.songlist_recommend_title.setText(bean.getModule().get(pos - 1).getTitle());
                diyViewHolder.songlist_recommend_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onListenerCallBack.CallBack(RecItemControlValues.SONGLIST_RECOMMEND_BTNMORE_NUM);
                    }
                });

                for (int i = 0; i < bean.getResult().getDiy().getResult().size(); i++) {
                    ImageLoader.getInstance().displayImage(bean.getResult().getDiy().getResult().get(i).getPic(), diyViewHolder.songlist_recommend_imgs[i], options);
                    final int finalI = i;
                    diyViewHolder.songlist_recommend_imgs[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onListenerCallBack.CallBack(RecItemControlValues.SONGLIST_RECOMMEND_IMG_NUMS[finalI]);
                        }
                    });
                    diyViewHolder.songlist_recommend_texts[i].setText(bean.getResult().getDiy().getResult().get(i).getTitle());
                }

                break;
            case 5:////
                ADViewHolder adViewHolder1 = (ADViewHolder) holder;
                break;
            case 6:
                SixImgViewHolder newCdViewHolder = (SixImgViewHolder) holder;
                ImageLoader.getInstance().displayImage(bean.getModule().get(pos - 1).getPicurl(), newCdViewHolder.songlist_recommend_pictrul, options);
                newCdViewHolder.songlist_recommend_title.setText(bean.getModule().get(position).getTitle());
                newCdViewHolder.songlist_recommend_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onListenerCallBack.CallBack(RecItemControlValues.NEW_CD_BTNMORE_NUM);
                    }
                });

                for (int i = 0; i < 6; i++) {
                    ImageLoader.getInstance().displayImage(bean.getResult().getMix_1().getResult().get(i).getPic(), newCdViewHolder.songlist_recommend_imgs[i], options);
                    final int finalI = i;
                    newCdViewHolder.songlist_recommend_imgs[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onListenerCallBack.CallBack(RecItemControlValues.NEW_CD_IMG_NUMS[finalI]);
                        }
                    });

                    newCdViewHolder.songlist_recommend_texts[i].setText(bean.getResult().getMix_1().getResult().get(i).getTitle());
                    newCdViewHolder.songlist_recommend_singers[i].setText(bean.getResult().getMix_1().getResult().get(i).getAuthor());
                }

                break;
            case 7:
                ThreeImgViewHolder hotAlbumViewHolder = (ThreeImgViewHolder) holder;
                ImageLoader.getInstance().displayImage(bean.getModule().get(pos - 1).getPicurl(), hotAlbumViewHolder.hot_album_pictrul, options);
                hotAlbumViewHolder.hot_album_title.setText(bean.getModule().get(position).getTitle());

                for (int i = 0; i < 3; i++) {
                    ImageLoader.getInstance().displayImage(bean.getResult().getMix_22().getResult().get(i).getPic(), hotAlbumViewHolder.hot_album_pics[i], options);
                    hotAlbumViewHolder.hot_album_texts[i].setText(bean.getResult().getMix_22().getResult().get(i).getTitle());
                    hotAlbumViewHolder.hot_album_singers[i].setText(bean.getResult().getMix_22().getResult().get(i).getAuthor());

                }

                break;
            case 8:
                ADViewHolder adViewHolder = (ADViewHolder) holder;
                break;
            case 9:
                SceneViewHolder sceneViewHolder = (SceneViewHolder) holder;
                ImageLoader.getInstance().displayImage(bean.getModule().get(pos - 1).getPicurl(), sceneViewHolder.scene_radio_icon, options);
                sceneViewHolder.scene_radio_title.setText(bean.getModule().get(pos - 1).getTitle());
                for (int i = 0; i < 4; i++) {
                    ImageLoader.getInstance().displayImage(bean.getResult().getScene().getResult().getAction().get(i).getIcon_ios()
                            , sceneViewHolder.scene_radio_btns[i], options);
                    sceneViewHolder.scene_radio_texts[i].setText(bean.getResult().getScene().getResult().getAction().get(i).getScene_name());

                }
                break;
            case 10:
                RecsongViewHolder recsongViewHolder = (RecsongViewHolder) holder;
                recsongViewHolder.recsong_title.setText(bean.getModule().get(pos - 1).getTitle());
                ImageLoader.getInstance().displayImage(bean.getModule().get(pos - 1).getPicurl(), recsongViewHolder.recsong_pictrul, options);
                for (int i = 0; i < 3; i++) {
                    ImageLoader.getInstance().displayImage(bean.getResult().getRecsong().getResult().get(i).getPic_premium()
                            , recsongViewHolder.recsong_pics[i], options);
                    recsongViewHolder.recsong_text_items[i].setText(bean.getResult().getRecsong().getResult().get(i).getTitle());
                    recsongViewHolder.recsong_singers[i].setText(bean.getResult().getRecsong().getResult().get(i).getAuthor());
                }
                break;
            case 11:
                ThreeImgViewHolder originalViewHolder = (ThreeImgViewHolder) holder;

                ImageLoader.getInstance().displayImage(bean.getModule().get(pos - 1).getPicurl(), originalViewHolder.hot_album_pictrul, options);
                originalViewHolder.hot_album_title.setText(bean.getModule().get(position).getTitle());

                for (int i = 0; i < 3; i++) {
                    ImageLoader.getInstance().displayImage(bean.getResult().getMix_9().getResult().get(i).getPic(), originalViewHolder.hot_album_pics[i], options);
                    originalViewHolder.hot_album_texts[i].setText(bean.getResult().getMix_9().getResult().get(i).getTitle());
                }
                break;
            case 12:
                SixImgViewHolder hotMVViewHolder = (SixImgViewHolder) holder;

                ImageLoader.getInstance().displayImage(bean.getModule().get(pos - 1).getPicurl(), hotMVViewHolder.songlist_recommend_pictrul, options);
                hotMVViewHolder.songlist_recommend_title.setText(bean.getModule().get(pos - 1).getTitle());

                for (int i = 0; i < bean.getResult().getMix_5().getResult().size(); i++) {
                    ImageLoader.getInstance().displayImage(bean.getResult().getMix_5().getResult().get(i).getPic(), hotMVViewHolder.songlist_recommend_imgs[i], options);
                    hotMVViewHolder.songlist_recommend_texts[i].setText(bean.getResult().getMix_5().getResult().get(i).getTitle());
                    hotMVViewHolder.songlist_recommend_singers[i].setText(bean.getResult().getMix_5().getResult().get(i).getAuthor());
                }


                break;
            case 13:
                SixImgViewHolder leboRadioViewHolder = (SixImgViewHolder) holder;
                ImageLoader.getInstance().displayImage(bean.getModule().get(pos - 1).getPicurl(), leboRadioViewHolder.songlist_recommend_pictrul, options);
                leboRadioViewHolder.songlist_recommend_title.setText(bean.getModule().get(pos - 1).getTitle());

                for (int i = 0; i < bean.getResult().getRadio().getResult().size(); i++) {
                    ImageLoader.getInstance().displayImage(bean.getResult().getRadio().getResult().get(i).getPic(), leboRadioViewHolder.songlist_recommend_imgs[i], options);
                    leboRadioViewHolder.songlist_recommend_texts[i].setText(bean.getResult().getRadio().getResult().get(i).getTitle());
                }

                break;
            case 14:
                ColumnViewHolder columnViewHolder = (ColumnViewHolder) holder;
                ImageLoader.getInstance().displayImage(bean.getModule().get(pos - 1).getPicurl(), columnViewHolder.column_pictrul, options);
                columnViewHolder.column_title.setText(bean.getModule().get(pos - 1).getTitle());
                for (int i = 0; i < 4; i++) {
                    ImageLoader.getInstance().displayImage(bean.getResult().getMod_7().getResult().get(i).getPic()
                            , columnViewHolder.column_pics[i], options);
                    columnViewHolder.column_texts[i].setText(bean.getResult().getMod_7().getResult().get(i).getTitle());
                    columnViewHolder.column_descs[i].setText(bean.getResult().getMod_7().getResult().get(i).getDesc());
                }

                break;
            case 15:
                ADViewHolder adViewHolder3 = (ADViewHolder) holder;
                break;

        }
    }

    @Override
    public void onClick(View v) {
        SixImgViewHolder six = new SixImgViewHolder(v);

        switch (v.getId()) {
            case R.id.img_entry_singer:
                onListenerCallBack.CallBack(RecItemControlValues.ENTRY_SINGER_NUM);
                break;
            case R.id.img_entry_classify:
                onListenerCallBack.CallBack(RecItemControlValues.ENTRY_CLASSIFY_NUM);
                break;
            case R.id.img_entry_member:
                onListenerCallBack.CallBack(RecItemControlValues.ENTRY_MEMBER_NUM);
                break;
            case R.id.img_entry_radio:
                onListenerCallBack.CallBack(RecItemControlValues.ENTRY_RADIO_NUM);
                break;


        }
    }


    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.getModule().size();
    }


    /*****************/
    class FocusViewHolder extends RecyclerView.ViewHolder {

        private Banner recommend_banner;

        public FocusViewHolder(View itemView) {
            super(itemView);
            recommend_banner = (Banner) itemView.findViewById(R.id.recommend_banner);
        }
    }

    class EntryViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img_entry_singer;
        private final ImageView img_entry_classify;
        private final ImageView img_entry_radio;
        private final ImageView img_entry_member;
        private final TextView text_entry_singer;
        private final TextView text_entry_classify;
        private final TextView text_entry_radio;
        private final TextView text_entry_member;

        public EntryViewHolder(View itemView) {
            super(itemView);
            img_entry_singer = (ImageView) itemView.findViewById(R.id.img_entry_singer);
            img_entry_classify = (ImageView) itemView.findViewById(R.id.img_entry_classify);
            img_entry_radio = (ImageView) itemView.findViewById(R.id.img_entry_radio);
            img_entry_member = (ImageView) itemView.findViewById(R.id.img_entry_member);
            text_entry_singer = (TextView) itemView.findViewById(R.id.text_entry_singer);
            text_entry_classify = (TextView) itemView.findViewById(R.id.text_entry_classify);
            text_entry_radio = (TextView) itemView.findViewById(R.id.text_entry_radio);
            text_entry_member = (TextView) itemView.findViewById(R.id.text_entry_member);
        }
    }


    class SixImgViewHolder extends RecyclerView.ViewHolder {

        private final ImageView songlist_recommend_pictrul;
        private final TextView songlist_recommend_title;
        private final Button songlist_recommend_more;
        private ImageView songlist_recommend_pic1;
        private ImageView songlist_recommend_pic3;
        private ImageView songlist_recommend_pic5;
        private ImageView songlist_recommend_pic2;
        private ImageView songlist_recommend_pic4;
        private ImageView songlist_recommend_pic6;
        ImageView songlist_recommend_imgs[] = {songlist_recommend_pic1,
                songlist_recommend_pic2, songlist_recommend_pic3,
                songlist_recommend_pic4, songlist_recommend_pic5,
                songlist_recommend_pic6};
        private TextView songlist_recommend_text2;
        private TextView songlist_recommend_text1;
        private TextView songlist_recommend_text3;
        private TextView songlist_recommend_text5;
        private TextView songlist_recommend_text6;
        private TextView songlist_recommend_text4;
        TextView songlist_recommend_texts[] = {songlist_recommend_text1,
                songlist_recommend_text2, songlist_recommend_text3,
                songlist_recommend_text4, songlist_recommend_text5,
                songlist_recommend_text6};
        int songlist_recommend_img_id[] = {R.id.songlist_recommend_pic1, R.id.songlist_recommend_pic2,
                R.id.songlist_recommend_pic3, R.id.songlist_recommend_pic4,
                R.id.songlist_recommend_pic5, R.id.songlist_recommend_pic6};
        int songlist_recommend_text_id[] = {R.id.songlist_recommend_text1, R.id.songlist_recommend_text2,
                R.id.songlist_recommend_text3, R.id.songlist_recommend_text4,
                R.id.songlist_recommend_text5, R.id.songlist_recommend_text6};

        int sonlist_recommend_singer_id[] = {R.id.songlist_recommend_singer1, R.id.songlist_recommend_singer2, R.id.songlist_recommend_singer3,
                R.id.songlist_recommend_singer4, R.id.songlist_recommend_singer5, R.id.songlist_recommend_singer6};

        private TextView songlist_recommend_singer1;
        private TextView songlist_recommend_singer2;
        private TextView songlist_recommend_singer3;
        private TextView songlist_recommend_singer4;
        private TextView songlist_recommend_singer5;
        private TextView songlist_recommend_singer6;
        TextView songlist_recommend_singers[] = {songlist_recommend_singer1, songlist_recommend_singer2, songlist_recommend_singer3,
                songlist_recommend_singer4, songlist_recommend_singer5, songlist_recommend_singer6};

        public SixImgViewHolder(View itemView) {
            super(itemView);


            songlist_recommend_pictrul = (ImageView) itemView.findViewById(R.id.songlist_recommend_picrul);
            songlist_recommend_title = (TextView) itemView.findViewById(R.id.songlist_recommend_title);
            songlist_recommend_more = (Button) itemView.findViewById(R.id.songlist_recommend_more);
            for (int i = 0; i < songlist_recommend_imgs.length; i++) {
                songlist_recommend_imgs[i] = (ImageView) itemView.findViewById(songlist_recommend_img_id[i]);
                songlist_recommend_texts[i] = (TextView) itemView.findViewById(songlist_recommend_text_id[i]);
                songlist_recommend_singers[i] = (TextView) itemView.findViewById(sonlist_recommend_singer_id[i]);
            }
        }
    }

    class ThreeImgViewHolder extends RecyclerView.ViewHolder {

        private ImageView hot_album_pictrul;
        private TextView hot_album_title;
        private ImageView hot_album_pic1;
        private ImageView hot_album_pic2;
        private ImageView hot_album_pic3;
        private TextView hot_album_text1;
        private TextView hot_album_text2;
        private TextView hot_album_text3;
        private TextView hot_album_singer1;
        private TextView hot_album_singer2;
        private TextView hot_album_singer3;


        int hot_album_pic_id[] = {R.id.hot_album_pic1, R.id.hot_album_pic2, R.id.hot_album_pic3};
        ImageView hot_album_pics[] = {hot_album_pic1, hot_album_pic2, hot_album_pic3};
        int hot_album_text_id[] = {R.id.hot_album_text1, R.id.hot_album_text2, R.id.hot_album_text3};
        TextView hot_album_texts[] = {hot_album_text1, hot_album_text2, hot_album_text3};
        int hot_album_singer_id[] = {R.id.hot_album_singer1, R.id.hot_album_singer2, R.id.hot_album_singer3};
        TextView hot_album_singers[] = {hot_album_singer1, hot_album_singer2, hot_album_singer3};

        public ThreeImgViewHolder(View itemView) {
            super(itemView);
            hot_album_pictrul = (ImageView) itemView.findViewById(R.id.hot_album_pictrul);
            hot_album_title = (TextView) itemView.findViewById(R.id.hot_album_title);

            for (int i = 0; i < 3; i++) {
                hot_album_pics[i] = (ImageView) itemView.findViewById(hot_album_pic_id[i]);
                hot_album_texts[i] = (TextView) itemView.findViewById(hot_album_text_id[i]);
                hot_album_singers[i] = (TextView) itemView.findViewById(hot_album_singer_id[i]);

            }

        }
    }

    class SceneViewHolder extends RecyclerView.ViewHolder {

        private final ImageView scene_radio_icon;
        private final TextView scene_radio_title;
        int scene_radio_btn_id[] = {R.id.scene_radio_btn_item1, R.id.scene_radio_btn_item2, R.id.scene_radio_btn_item3, R.id.scene_radio_btn_item4};
        private ImageButton scene_radio_btn4;

        private ImageButton scene_radio_btn1;
        private ImageButton scene_radio_btn2;
        private ImageButton scene_radio_btn3;
        ImageButton scene_radio_btns[] = {scene_radio_btn1, scene_radio_btn2, scene_radio_btn3, scene_radio_btn4};

        int scene_radio_text_id[] = {R.id.scene_radio_btn_text1, R.id.scene_radio_btn_text2, R.id.scene_radio_btn_text3, R.id.scene_radio_btn_text4};
        private TextView scene_radio_btn_text1;
        private TextView scene_radio_btn_text2;
        private TextView scene_radio_btn_text3;
        private TextView scene_radio_btn_text4;
        TextView scene_radio_texts[] = {scene_radio_btn_text1, scene_radio_btn_text2, scene_radio_btn_text3, scene_radio_btn_text4};

        public SceneViewHolder(View itemView) {
            super(itemView);
            scene_radio_icon = (ImageView) itemView.findViewById(R.id.scene_radio_icon);
            scene_radio_title = (TextView) itemView.findViewById(R.id.scene_radio_title);
            for (int i = 0; i < 4; i++) {
                scene_radio_btns[i] = (ImageButton) itemView.findViewById(scene_radio_btn_id[i]);
                scene_radio_texts[i] = (TextView) itemView.findViewById(scene_radio_text_id[i]);
            }
        }
    }

    class ADViewHolder extends RecyclerView.ViewHolder {
        public ADViewHolder(View itemView) {
            super(itemView);
        }
    }


    class RecsongViewHolder extends RecyclerView.ViewHolder {

        private final ImageView recsong_pictrul;
        private final TextView recsong_title;
        private final Button recsong_btn_more;

        int recsong_pic_id[] = {R.id.recsong_pic1, R.id.recsong_pic2, R.id.recsong_pic3};

        private ImageView recsong_pic1;
        private ImageView recsong_pic2;
        private ImageView recsong_pic3;
        ImageView recsong_pics[] = {recsong_pic1, recsong_pic2, recsong_pic3};


        private TextView recsong_text_item1;
        private TextView recsong_text_item2;
        private TextView recsong_text_item3;
        TextView recsong_text_items[] = {recsong_text_item1, recsong_text_item2, recsong_text_item3};
        int recsong_text_item_id[] = {R.id.recsong_text_item1, R.id.recsong_text_item2, R.id.recsong_text_item3};

        private TextView recsong_singer1;
        private TextView recsong_singer2;
        private TextView recsong_singer3;
        TextView recsong_singers[] = {recsong_singer1, recsong_singer2, recsong_singer3};
        int recsong_singer_id[] = {R.id.recsong_singer1, R.id.recsong_singer2, R.id.recsong_singer3};

        public RecsongViewHolder(View itemView) {
            super(itemView);
            recsong_pictrul = (ImageView) itemView.findViewById(R.id.recsong_pictrul);
            recsong_title = (TextView) itemView.findViewById(R.id.recsong_title);
            recsong_btn_more = (Button) itemView.findViewById(R.id.recsong_btn_more);
            for (int i = 0; i < 3; i++) {
                recsong_pics[i] = (ImageView) itemView.findViewById(recsong_pic_id[i]);
                recsong_text_items[i] = (TextView) itemView.findViewById(recsong_text_item_id[i]);
                recsong_singers[i] = (TextView) itemView.findViewById(recsong_singer_id[i]);
            }
        }
    }


    class ColumnViewHolder extends RecyclerView.ViewHolder {

        private final ImageView column_pictrul;
        private final TextView column_title;
        private ImageView column_pic1;
        private ImageView column_pic2;
        private ImageView column_pic3;
        private ImageView column_pic4;
        ImageView column_pics[] = {column_pic1, column_pic2, column_pic3, column_pic4};
        int column_pic_id[] = {R.id.column_pic1, R.id.column_pic2, R.id.column_pic3, R.id.column_pic4};


        private TextView column_text1;
        private TextView column_text2;
        private TextView column_text3;
        private TextView column_text4;
        TextView column_texts[] = {column_text1, column_text2, column_text3, column_text4};
        int column_text_id[] = {R.id.column_text1, R.id.column_text2, R.id.column_text3, R.id.column_text4};

        private TextView column_desc1;
        private TextView column_desc2;
        private TextView column_desc3;
        private TextView column_desc4;
        TextView column_descs[] = {column_desc1, column_desc2, column_desc3, column_desc4};
        int column_desc_id[] = {R.id.column_desc1, R.id.column_desc2, R.id.column_desc3, R.id.column_desc4};

        public ColumnViewHolder(View itemView) {
            super(itemView);
            column_pictrul = (ImageView) itemView.findViewById(R.id.column_pictrul);
            column_title = (TextView) itemView.findViewById(R.id.column_title);


            for (int i = 0; i < 4; i++) {
                column_pics[i] = (ImageView) itemView.findViewById(column_pic_id[i]);
                column_texts[i] = (TextView) itemView.findViewById(column_text_id[i]);
                column_descs[i] = (TextView) itemView.findViewById(column_desc_id[i]);
            }

        }
    }


}
