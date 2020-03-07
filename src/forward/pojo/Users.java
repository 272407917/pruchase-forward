package forward.pojo;

/**
 * @author 瞿琮
 * @create 2020-02-29 9:50
 */
public class Users {
    private String id;
    private String username;
    private String password;
    private String rule;
    private String email;
    private String qq;

    public Users() {
    }

    public Users(String id, String username, String password, String rule, String email, String qq) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rule = rule;
        this.email = email;
        this.qq = qq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
