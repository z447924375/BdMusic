package zxh.bdmusic.usedvalues;

/**
 * Created by dllo on 16/5/23.
 */
public class URLVlaues {
    public static final String HOT_SINGER_IMG="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=6&offset=0&area=0&sex=0&abc=&from=ios&version=5.2.1&from=ios&channel=appstore";
    public static final String RECOMMAND_CAROUSE = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.plaza.getFocusPic&format=json&from=ios&version=5.2.3&from=ios&channel=appstore";
    public static final String RECOMMAND_HOT = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.getHotGeDanAndOfficial&num=4&version=5.2.3&from=ios&channel=appstore";
    public static final String MUSICSTORE_TOP = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billCategory&format=json&from=ios&version=5.2.1&from=ios&channel=appstore";
    public static final String ALL_SONGLIST = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.gedan&page_no=1&page_size=30&from=ios&version=5.2.3&from=ios&channel=appstore";
    public static final String SONGLIST_DETAIL_Front = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.gedanInfo&from=ios&listid=";
    public static final String SONGLIST_DETAIL_BEHIND = "&format=json&offset=0&size=50&from=ios&fields=title,song_id,author,resource_type,havehigh,is_new,has_mv_mobile,album_title,ting_uid,album_id,charge,all_rate&version=5.2.1&from=ios&channel=appstore";
    public static final String TOP_SONG_FRONT = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&type=";
    public static final String TOP_SONG_BEHIND = "&format=json&offset=0&size=50&from=ios&fields=title,song_id,author,resource_type,havehigh,is_new,has_mv_mobile,album_title,ting_uid,album_id,charge,all_rate&version=5.2.1&from=ios&channel=appstore";
    public static final String RECOMMAND_CAROUSE_SONG_FRONT = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.album.getAlbumInfo&album_id=";
    public static final String RECOMMAND_CAROUSE_SONG_BEHIND = "&format=json&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String PLAY_FRONT = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music&method=baidu.ting.song.play&format=json&callback=&songid=";
    public static final String PLAY_BEHIND = "&_=1413017198449";
    public static final String CHINESE_MALE_SINGER = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=6&sex=1&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String NEWEST_SONGLIST="http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=1413c&operator=1&method=baidu.ting.ugcdiy.getChanneldiy&param=yjRdAP5CIbiPMNExUqI3SGXU7pV1WOf3frmA3GbLDNz4EszOnoRVPR1VMsmQFXZur20re7LDrJEOpokAOYk0IQksp8rZZZ0UXowy%2BmW1M4fmURfS3wXLHrmTJmEqeS3Y&timestamp=1474958470&sign=0032e3eeb679b5bb5722517930274a27";
    public static final String HOTEST_SONGLIST="http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=1413c&operator=1&method=baidu.ting.ugcdiy.getChanneldiy&param=ehU89KYswcMRWNDWGfdd01ugcS8%2B1c891aJ47TFOGx%2FWu9YwjM9BVMZC4pzzHqXFSEbPI3z3BoOyi7hsZ2v%2FjKuV8DCOoHQKVKSdOa4T7hCsNrhDFklfo6iniJSvzBQl&timestamp=1474958544&sign=6432275fd0a0bdbb6678be4fd5764be9";
    public static final String CHINESE_FEMALE_SINGER="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=6&sex=2&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String CHINESE_GROUP="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=6&sex=3&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String USA_MALE_SINGER="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=3&sex=1&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String USA_FEMALE_SINGER="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=3&sex=2&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String USA_GROUP="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=3&sex=3&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String KOREA_MALE_SINGER="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=7&sex=1&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String KOREA_FEMALE_SINGER="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=7&sex=2&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String KOREA_GROUP="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=7&sex=3&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String JAPAN_MALE_SINGER="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=60&sex=1&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String JAPAN_FEMALE_SINGER="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=60&sex=2&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String JAPAN_GROUP="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=60&sex=3&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String OTHER_SINGER="http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.artist.getList&format=json&order=1&limit=50&offset=0&area=5&sex=4&abc=%E7%83%AD%E9%97%A8&from=ios&version=5.2.5&from=ios&channel=appstore";
    public static final String NEWEST_MV="http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=1413c&operator=1&provider=11%2C12&method=baidu.ting.mv.searchMV&format=json&order=1&page_num=1&page_size=20&query=%E5%85%A8%E9%83%A8";
    public static final String HOTEST_MV="http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=1413c&operator=1&provider=11%2C12&method=baidu.ting.mv.searchMV&format=json&order=0&page_num=1&page_size=20&query=%E5%85%A8%E9%83%A8";

}
