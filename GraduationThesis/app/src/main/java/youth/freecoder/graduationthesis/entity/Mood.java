package youth.freecoder.graduationthesis.entity;

/**
 * Created by freecoder on 15/3/13.
 */
public class Mood {
    private int friend_icon;
    private String friend_username;
    private String friend_stars;
    private String time_interval;
    private String mood_content;

    public Mood() {
    }

    public Mood(int friend_icon, String friend_username,
                String friend_stars,
                String time_interval,
                String mood_content) {
        this.friend_icon = friend_icon;
        this.friend_username = friend_username;
        this.friend_stars = friend_stars;
        this.time_interval = time_interval;
        this.mood_content = mood_content;
    }

    public int getFriend_icon() {
        return friend_icon;
    }

    public void setFriend_icon(int friend_icon) {
        this.friend_icon = friend_icon;
    }

    public String getFriend_username() {
        return friend_username;
    }

    public void setFriend_username(String friend_username) {
        this.friend_username = friend_username;
    }

    public String getFriend_stars() {
        return friend_stars;
    }

    public void setFriend_stars(String friend_stars) {
        this.friend_stars = friend_stars;
    }

    public String getTime_interval() {
        return time_interval;
    }

    public void setTime_interval(String time_interval) {
        this.time_interval = time_interval;
    }

    public String getMood_content() {
        return mood_content;
    }

    public void setMood_content(String mood_content) {
        this.mood_content = mood_content;
    }
}
