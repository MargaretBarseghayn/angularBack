package Books;

public class Book {

    private final Integer id;
    private final String name;

    private final String author;
    private final Integer authorId;

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
