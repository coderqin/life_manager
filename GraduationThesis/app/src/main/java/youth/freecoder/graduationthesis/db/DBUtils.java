package youth.freecoder.graduationthesis.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import youth.freecoder.graduationthesis.entity.MyMessage;
import youth.freecoder.graduationthesis.utils.SqlUtils;

/**
 * Created by freecoder on 15/3/12.
 */
public class DBUtils {
    private static MySQLiteOpenHelper mySQLiteOpenHelper;
    private static SQLiteDatabase db;

    public DBUtils(Context context) {
        //构造方法里:建立数据库
        mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        //得到数据库连接对象
        db = mySQLiteOpenHelper.getWritableDatabase();
    }

    /**
     * 添加信息链表
     *
     * @param messages
     */
    public void insert(final List<MyMessage> messages) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //开始事务
                db.beginTransaction();
                try {
                    for (MyMessage offlineMessage : messages) {
                        db.execSQL(SqlUtils.insert_message_sql, new Object[]{
                                offlineMessage.getFrom(), offlineMessage.getTo(),
                                offlineMessage.getType(), offlineMessage.getContent(),
                                offlineMessage.getDate(), offlineMessage.getOnline()
                        });
                    }
                    //设置事务结束的标志点
                    db.setTransactionSuccessful();
                } finally {
                    //结束事务
                    db.endTransaction();
                }
            }
        }).start();
    }

    /**
     * 添加信息对象
     *
     * @param myMessage
     */
    public void insert(final MyMessage myMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.execSQL(SqlUtils.insert_message_sql, new Object[]{
                        myMessage.getFrom(), myMessage.getTo(),
                        myMessage.getType(), myMessage.getContent(),
                        myMessage.getDate(), myMessage.getOnline()
                });
            }
        }).start();
    }

    /**
     * 查询所有信息
     *
     * @return
     */
    public List<MyMessage> query() {
        final ArrayList<MyMessage> messages = new ArrayList<MyMessage>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyMessage offlineMessage;
                Cursor cursor = db.rawQuery(SqlUtils.query_message_sql, null);
                while (cursor.moveToNext()) {
                    offlineMessage = new MyMessage(
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getInt(6)
                    );
                    messages.add(offlineMessage);
                }
                cursor.close();

            }
        }).start();
        return messages;
    }

    /**
     * 查询信息
     *
     * @return
     */
    public List<MyMessage> queryOneCouple(final String fromUser, final String toUser) {
        final ArrayList<MyMessage> messages = new ArrayList<MyMessage>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyMessage offlineMessage;
                Cursor cursor = db.rawQuery(SqlUtils.query_one_message_sql, new String[]{fromUser, toUser});
                while (cursor.moveToNext()) {
                    offlineMessage = new MyMessage(
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getInt(6)
                    );
                    messages.add(offlineMessage);
                }
                cursor.close();
                Cursor otherCursor = db.rawQuery(SqlUtils.query_other_message_sql, new String[]{toUser, fromUser});
                while (otherCursor.moveToNext()) {
                    offlineMessage = new MyMessage(
                            otherCursor.getString(1),
                            otherCursor.getString(2),
                            otherCursor.getInt(3),
                            otherCursor.getString(4),
                            otherCursor.getString(5),
                            otherCursor.getInt(6)
                    );
                    messages.add(offlineMessage);
                }
                otherCursor.close();
            }
        }).start();
        return messages;
    }
}
