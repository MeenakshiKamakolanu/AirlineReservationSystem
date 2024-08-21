package passenger;

public class Passenger {
    private String name; // Stores passenger name
    private String passportNumber; // Stores passport number


    public Passenger(String name, String passportNumber) {
        this.name = name;
        this.passportNumber = passportNumber;
    }

    public String getName() {
        return name;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

  
}


