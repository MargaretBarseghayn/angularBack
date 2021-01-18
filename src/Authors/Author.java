package Authors;



import java.sql.Date;

public class Author {
    private int id;
    private String firstName;
    private String lastName;
    private long birthdate;
    private Long deathdate;


    public Author(int id, String firstName, String lastName, long birthdate, Long deathdate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.deathdate = deathdate;
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
        return this.deathdate != null;
    }

//    public void setAlive(boolean alive) {
//        isAlive = alive;
//    }


    public Date getDeathdate() {
        return deathdate != null ?  new Date(deathdate) : null;
    }

    public void setDeathdate(Long deathdate) {
        this.deathdate = deathdate;
    }

    @Override
    public String toString() {
        return "Author{" +
//                "id=" + id +
                "firstName:'" + firstName + '\'' +
                ", lastName:'" + lastName + '\'' +
                ", birthdate:" + birthdate +
                ", deathdate: " + deathdate + // TODO: 12/25/2020
                ", isAlive:" + this.isAlive()  +
                '}';
    }
}
