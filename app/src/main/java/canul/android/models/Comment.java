package canul.android.models;

/**
 * Created by Chazz on 14/10/15.
 */
public class Comment {

    private final String author;
    private final String published;
    private final String content;

    public Comment(String author, String published, String content) {
        this.author = author;
        this.published = published;
        this.content = content;
    }

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
