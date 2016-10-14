package zxh.bdmusic.playservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dllo on 16/8/24.
 */
public class MyHelper extends SQLiteOpenHelper{
    public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    //在这个方法里进行数据库的创建操作
    //这个方法只有在第一次初始化helper类的时候调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("zzzz", "onCreate");
        db.execSQL("create table music(id integer primary key autoincrement, song text, position text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
