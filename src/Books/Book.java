package Books;

public class Book {

    private final int id;
    private final String name;

    private final String author;
    private final int authorId;

    public Book(Integer id, String name, String author, Integer authorId) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.authorId = authorId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getAuthorId() {
        return authorId;
    }
}
