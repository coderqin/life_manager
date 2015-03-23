package youth.freecoder.graduationthesis.entity;

/**
 * Created by freecoder on 15/3/8.
 */
public class Friends {

    private String username;
    private String status;

    public Friends() {
    }

    public Friends(String username, String status) {
        this.username = username;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
