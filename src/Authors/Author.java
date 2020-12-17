package Authors;



import java.sql.Date;

public class Author {
    private int id;
    private String firstName;
    private String lastName;
    private long birthdate;
    private byte isAlive;


    public Author(int id, String firstName, String lastName, long birthdate, byte isAlive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.isAlive = isAlive;
    }

    public Author(int id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthdate() {
        return new Date(birthdate);
    }

    public void setBirthdate(long birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isAlive() {
        return isAlive == 1;
    }

//    public void setAlive(boolean alive) {
//        isAlive = alive;
//    }

    @Override
    public String toString() {
        return "Author{" +
//                "id=" + id +
                "firstName:'" + firstName + '\'' +
                ", lastName:'" + lastName + '\'' +
                ", birthdate:" + birthdate +
                ", isAlive:" + isAlive  +
                '}';
    }
}
