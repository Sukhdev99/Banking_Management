import java.io.Serializable;

class Person implements Serializable {
    private String name;

    public Person() {
        name = null;
    }

    public Person(String n) {
        name = n;
    }

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }
}
