package canul.android.models;

import java.util.Map;

/**
 * Created by Chazz on 15/10/15.
 */
public class Article {

    private final String author;
    private final String title;
    private final String published;
    private final String extract;
    private final String id;

    public Article(String id, String title, String author, String published, String extract) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.published = published;
        this.extract = extract;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublished() {
        return published;
    }

    public String getExtract() {
        return extract;
    }
}
