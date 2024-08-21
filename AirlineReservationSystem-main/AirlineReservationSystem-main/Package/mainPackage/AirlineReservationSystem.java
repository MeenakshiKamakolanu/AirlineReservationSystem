package mainPackage;

import flights.Flight;
import passenger.Passenger;
import reservations.Reservation;

import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSet;

public class AirlineReservationSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static Flight[] flights = new Flight[3]; // Array to hold Flight objects

    public static void main(String[] args) {
        initializeFlights(); // Initialize Flight objects

        int choice;
        do {
            displayMenu(); // Display menu options to the user
            choice = scanner.nextInt(); // Read user choice

            switch (choice) { // Switch statement to handle user choices
                case 1:
                    displayAvailableFlights();
                    break;
                case 2:
                    bookFlight();
                    break;
                case 3:
                    exitSystem();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 3);
    }

    // Method to initialize Flight objects
    private static void initializeFlights() {
        flights[0] = new Flight("H101", "Hyderabad", 50, 5000.0);
        flights[1] = new Flight("B102", "Bangalore", 30, 7000.0);
        flights[2] = new Flight("C103", "Chennai", 40, 8000.0);
      
    }



    // Method to display the menu options
    private static void displayMenu() {
        System.out.println("\nAirline Reservation System Menu:");
        System.out.println("1. View Available Flights");
        System.out.println("2. Book a Flight");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }



    // Method to display available flights
    private static void displayAvailableFlights() {
        for (Flight flight : flights) {
            if (flight != null) {
                flight.displayDetails();
            }
        }
    }



    // Method to exit the system
    private static void exitSystem() {
        System.out.println("Exiting Airline Reservation System. Thank you!");
        scanner.close(); // Close scanner
        System.exit(0); // Exit program
    }



    private static void bookFlight() {
        scanner.nextLine(); 

        // Gather passenger details
        System.out.print("Enter your name: ");
        String passengerName = scanner.nextLine();
        if (!passengerName.matches("[a-z A-Z]+")) {
            System.out.println("Invalid name. Please enter alphabetic characters only.");
            return;
        }

        System.out.print("Enter your passport number (should contain exactly three digits): ");
        String passportNumber = scanner.nextLine();
        if (!passportNumber.matches("\\d{3}")) {
            System.out.println("Invalid passport number. Please enter exactly three digits.");
            return;
        }

        Passenger passenger = new Passenger(passengerName, passportNumber); // Create Passenger object

        // Display available flights
        System.out.println("Available Flights:");
        displayAvailableFlights();

        // Get user's selected flight number
        System.out.print("Enter the flight number you want to book (H101, B102, C103): ");
        String flightNumber = scanner.nextLine();
        if (!flightNumber.matches("[HBC]\\d{3}")) {
            System.out.println("Invalid flight number. Please enter H101, B102, C103.");
            return;
        }

        Flight selectedFlight = findFlight(flightNumber); // Find the selected Flight object

        if (selectedFlight != null && selectedFlight.bookSeat()) {
            // Establish database connection
            Connection connection = establishConnection();

            // Check if the passport number already exists in the database
            boolean passportExists = checkPassportExists(connection, passportNumber);
            if (passportExists) {
                System.out.println("Passport number already exists. Two individuals cannot have the same passport number.");
                return;
            }

            // Insert reservation details into the 'reservations' table
            String insertQuery = "INSERT INTO reservations (flight_number, passenger_name, passport_number) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, flightNumber);
                preparedStatement.setString(2, passengerName);
                preparedStatement.setString(3, passportNumber);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Reservation details added to the database");
                    Reservation reservation = new Reservation(selectedFlight, passenger);
                    reservation.displayReservationDetails(); // Display reservation details

                    // Write reservation details to the file
                    writeReservationToFile(flightNumber, passengerName, passportNumber);
                } else {
                    System.out.println("Failed to add reservation details");
                }
            } catch (SQLException e) {
                System.out.println("Error inserting data into the database");
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid flight number or no available seats. Please try again.");
        }
    }




    // Method to check if the passport number already exists in the 'reservations' table
    private static boolean checkPassportExists(Connection connection, String passportNumber) {
        String checkQuery = "SELECT passport_number FROM reservations WHERE passport_number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(checkQuery)) {
            preparedStatement.setString(1, passportNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // Returns true if the passport number already exists in the database
        } catch (SQLException e) {
            System.out.println("Error checking passport number in the database");
            e.printStackTrace();
        }
        return false;
    }




    // Method to write reservation details to the file
    private static void writeReservationToFile(String flightNumber, String passengerName, String passportNumber) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("reservations.txt", true))) {
            writer.println("Flight Number: " + flightNumber + ", Passenger Name: " + passengerName + ", Passport Number: " + passportNumber);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Method to display reservations from the file
    private static void displayReservationsFromFile() {
        try (Scanner fileScanner = new Scanner(new File("reservations.txt"))) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }



    // Method to find a Flight object by its number
    private static Flight findFlight(String flightNumber) {
        for (Flight flight : flights) {
            if (flight != null && flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }



    // Method to establish a connection to the database
    private static Connection establishConnection() {
        Connection connection = null;
        String DB_URL = "jdbc:mysql://localhost:3306/meenakshidb";
        String USER = "root";
        String PASSWORD = "meenakshi15";

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
        return connection;
    }
}
