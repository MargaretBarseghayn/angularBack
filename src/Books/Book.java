package Books;

public class Book extends BookData {

    private final int id;

    public Book(int id, String name) {
        super(name);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
