package youth.freecoder.graduationthesis.entity;

/**
 * Created by freecoder on 15/3/3.
 */
public class User {

    private String username;
    private int userPhotoId;
    private int status;

    public User(String username, int userPhotoId, int status) {
        this.username = username;
        this.userPhotoId = userPhotoId;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserPhotoId() {
        return userPhotoId;
    }

    public void setUserPhotoId(int userPhotoId) {
        this.userPhotoId = userPhotoId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
