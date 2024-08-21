package flights;

public class Flight {
    // Flight details
    private String flightNumber;

    private String destination;
    private int availableSeats;
    private double ticketPrice;

    // Constructor to initialize Flight
    public Flight(String flightNumber, String destination, int availableSeats, double ticketPrice) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
    }

    // Getters to retrieve flight information
    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDestination() {
        return destination;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    // Method to book a seat on the flight
    public boolean bookSeat() {
        if (availableSeats > 0) {
            availableSeats--;
            return true; // Successfully booked
        }
        return false; // No available seats
    }

    // Display flight details
    public void displayDetails() {
        System.out.println("Flight: " + flightNumber + "\nDestination: " + destination +
                "\nAvailable Seats: " + availableSeats + "\nTicket Price: Rs" + ticketPrice + "\n");
    }
}

