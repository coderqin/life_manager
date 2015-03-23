package youth.freecoder.graduationthesis.utils;

/**
 * Created by freecoder on 15/3/12.
 */
public class SqlUtils {

    /**
     * 建立信息表的sql语句
     */
    public static String crate_table_sql = "create table if not exists message" +
            "(_id integer primary key autoincrement,from_user varchar,to_user varchar,type integer," +
            "content varchar,create_time time,online integer)";
    /**
     * 向message表添加数据sql语句
     */
    public static String insert_message_sql = "insert into message values(null,?,?,?,?,?,?)";

    /**
     * 查询message表所有信息
     */
    public static String query_message_sql = "select * from message  where type=1 group by from_user " +
            "order by create_time desc";


    /**
     * 查询一对联系人其中一方的信息
     */
    public static String query_one_message_sql = "select * from message " +
            "where from_user=? and to_user=? order by create_time desc";

    /**
     * 查询一对联系人其中另一方的信息
     */
    public static String query_other_message_sql = "select * from message " +
            "where from_user=? and to_user=? order by create_time desc";


}
