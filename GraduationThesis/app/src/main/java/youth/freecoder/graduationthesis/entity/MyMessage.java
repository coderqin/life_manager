package youth.freecoder.graduationthesis.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by freecoder on 15/3/10.
 */
public class MyMessage implements Parcelable {
    /**
     * 信息发送者
     */
    private String from;
    /**
     * 信息接受者
     */
    private String to;
    /**
     * 信息接发类型,接收 1 还是 发出 0
     */
    private int type;
    /**
     * 信息内容
     */
    private String content;
    /**
     * 信息发送时间
     */
    private String date;
    /**
     * 信息在线类型,在线1，离线2
     */
    private int online;

    public MyMessage() {
    }

    public MyMessage(String from, String to, int type, String content, String date, int online) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.content = content;
        this.date = date;
        this.online = online;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(from);
        dest.writeString(to);
        dest.writeInt(type);
        dest.writeString(content);
        dest.writeString(date);
        dest.writeInt(online);
    }

    public static final Parcelable.Creator<MyMessage> CREATOR = new Creator<MyMessage>() {
        @Override
        public MyMessage[] newArray(int size) {
            return new MyMessage[size];
        }

        @Override
        public MyMessage createFromParcel(Parcel in) {
            return new MyMessage(in);
        }
    };

    public MyMessage(Parcel in) {
        from = in.readString();
        to = in.readString();
        type = in.readInt();
        content = in.readString();
        date = in.readString();
        online = in.readInt();
    }
}
