package youth.freecoder.graduationthesis.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import youth.freecoder.graduationthesis.utils.SqlUtils;

/**
 * Created by freecoder on 15/3/12.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public MySQLiteOpenHelper(Context context) {
        /**
         * 建立数据库名和版本
         */
        super(context, "life_manager.db", null, 1);
    }

    /**
     * 数据库初始化操作，建表等。
     * 第一次连接获取数据库对象执行
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlUtils.crate_table_sql);
    }

    /**
     * 更新数据库的时候调用
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
