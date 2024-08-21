package reservations;

import flights.Flight;
import passenger.Passenger;

public class Reservation {
    private Flight flight;
    private Passenger passenger;

    public Reservation(Flight flight, Passenger passenger) {
        this.flight = flight;
        this.passenger = passenger;
    }



    public void displayReservationDetails() {
        System.out.println("Reservation Details:");
        System.out.println("Passenger Name: " + passenger.getName());
        System.out.println("Passport Number: " + passenger.getPassportNumber());
        System.out.println("Flight Number: " + flight.getFlightNumber());
        System.out.println("Destination: " + flight.getDestination());
        System.out.println("Ticket Price: Rs" + flight.getTicketPrice());
    }
}


