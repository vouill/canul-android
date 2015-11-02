package canul.android.models;

/**
 * Created by Chazz on 14/10/15.
 */
public class Comment {

    private final String author;
    private final String published;
    private final String id;
    private final String content;

    public Comment(String id, String author, String published, String content) {
        this.author = author;
        this.published = published;
        this.content = content;
        this.id = id;
    }
    public String getId() {return id;}

    public String getAuthor() {
        return author;
    }

    public String getPublished() {
        return published;
    }

    public String getContent() {
        return content;
    }
}
