package project.nowruz;

import java.util.Date;

public class Comment {
    private User user;
    private String text;
    private Date timestamp;

    public Comment(User user, String text, Date timestamp) {
        this.user = user;
        this.text = text;
        this.timestamp = timestamp;
    }

    public User getUser() { return user; }
    public String getText() { return text; }
    public Date getTimestamp() { return timestamp; }
}
