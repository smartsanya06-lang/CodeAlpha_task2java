import java.io.*;
import java.util.*;

class Room {
    int roomNumber;
    String category;
    double price;
    boolean available;

    Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.available = true;
    }

    void displayRoom() {
        System.out.printf("%-10d %-12s ₹%-10.2f %-10s%n",
                roomNumber, category, price,
                available ? "Available" : "Booked");
    }
}

class Reservation {
    int bookingId;
    String customerName;
    int roomNumber;
    String category;
    int nights;
    double totalAmount;
    String paymentStatus;

    Reservation(int bookingId, String customerName,
                int roomNumber, String category,
                int nights, double totalAmount,
                String paymentStatus) {

        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.nights = nights;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

    void displayBooking() {
        System.out.println("\n========== BOOKING DETAILS ==========");
        System.out.println("Booking ID     : " + bookingId);
        System.out.println("Customer Name  : " + customerName);
        System.out.println("Room Number    : " + roomNumber);
        System.out.println("Room Category  : " + category);
        System.out.println("Number of Nights: " + nights);
        System.out.printf("Total Amount   : ₹%.2f%n", totalAmount);
        System.out.println("Payment Status : " + paymentStatus);
        System.out.println("=====================================");
    }

    String toFileString() {
        return bookingId + "," +
               customerName + "," +
               roomNumber + "," +
               category + "," +
               nights + "," +
               totalAmount + "," +
               paymentStatus;
    }
}

public class HotelReservationSystem {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Reservation> reservations = new ArrayList<>();

    static int nextBookingId = 1001;

    static final String FILE_NAME = "bookings.txt";

    public static void main(String[] args) {

        initializeRooms();
        loadBookings();

        int choice;

        do {
            System.out.println("\n====================================");
            System.out.println("       HOTEL RESERVATION SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View Booking Details");
            System.out.println("5. View All Bookings");
            System.out.println("6. Exit");
            System.out.println("====================================");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    searchRooms();
                    break;

                case 2:
                    bookRoom();
                    break;

                case 3:
                    cancelReservation();
                    break;

                case 4:
                    viewBooking();
                    break;

                case 5:
                    viewAllBookings();
                    break;

                case 6:
                    System.out.println("Thank you for using the Hotel Reservation System!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);

        sc.close();
    }

    // Create rooms
    static void initializeRooms() {

        rooms.add(new Room(101, "Standard", 2000));
        rooms.add(new Room(102, "Standard", 2000));
        rooms.add(new Room(103, "Standard", 2000));

        rooms.add(new Room(201, "Deluxe", 3500));
        rooms.add(new Room(202, "Deluxe", 3500));
        rooms.add(new Room(203, "Deluxe", 3500));

        rooms.add(new Room(301, "Suite", 6000));
        rooms.add(new Room(302, "Suite", 6000));
    }

    // Search rooms
    static void searchRooms() {

        System.out.println("\n========== AVAILABLE ROOMS ==========");

        System.out.printf("%-10s %-12s %-12s %-10s%n",
                "Room", "Category", "Price/Night", "Status");

        System.out.println("--------------------------------------------");

        for (Room room : rooms) {

            if (room.available) {
                room.displayRoom();
            }
        }
    }

    // Book room
    static void bookRoom() {

        searchRooms();

        System.out.print("\nEnter room number: ");
        int roomNumber = sc.nextInt();
        sc.nextLine();

        Room selectedRoom = findRoom(roomNumber);

        if (selectedRoom == null) {
            System.out.println("Room not found.");
            return;
        }

        if (!selectedRoom.available) {
            System.out.println("Room is already booked.");
            return;
        }

        System.out.print("Enter customer name: ");
        String name = sc.nextLine();

        System.out.print("Enter number of nights: ");
        int nights = sc.nextInt();

        if (nights <= 0) {
            System.out.println("Invalid number of nights.");
            return;
        }

        double total = selectedRoom.price * nights;

        System.out.println("\n========== PAYMENT ==========");
        System.out.printf("Total Amount: ₹%.2f%n", total);
        System.out.println("1. Pay Now");
        System.out.println("2. Cancel");

        System.out.print("Enter choice: ");
        int paymentChoice = sc.nextInt();

        if (paymentChoice != 1) {
            System.out.println("Booking cancelled.");
            return;
        }

        // Payment simulation
        System.out.println("Processing payment...");
        System.out.println("Payment successful!");

        Reservation reservation = new Reservation(
                nextBookingId,
                name,
                selectedRoom.roomNumber,
                selectedRoom.category,
                nights,
                total,
                "PAID"
        );

        reservations.add(reservation);

        selectedRoom.available = false;

        saveBookings();

        System.out.println("\nRoom booked successfully!");
        System.out.println("Your Booking ID: " + nextBookingId);

        nextBookingId++;
    }

    // Cancel reservation
    static void cancelReservation() {

        System.out.print("Enter Booking ID: ");
        int bookingId = sc.nextInt();

        Reservation found = null;

        for (Reservation r : reservations) {

            if (r.bookingId == bookingId) {
                found = r;
                break;
            }
        }

        if (found == null) {
            System.out.println("Booking not found.");
            return;
        }

        Room room = findRoom(found.roomNumber);

        if (room != null) {
            room.available = true;
        }

        reservations.remove(found);

        saveBookings();

        System.out.println("Reservation cancelled successfully.");
        System.out.println("Refund simulation completed.");
    }

    // View single booking
    static void viewBooking() {

        System.out.print("Enter Booking ID: ");
        int bookingId = sc.nextInt();

        for (Reservation r : reservations) {

            if (r.bookingId == bookingId) {
                r.displayBooking();
                return;
            }
        }

        System.out.println("Booking not found.");
    }

    // View all bookings
    static void viewAllBookings() {

        if (reservations.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        System.out.println("\n========== ALL BOOKINGS ==========");

        for (Reservation r : reservations) {
            r.displayBooking();
        }
    }

    // Find room
    static Room findRoom(int roomNumber) {

        for (Room room : rooms) {

            if (room.roomNumber == roomNumber) {
                return room;
            }
        }

        return null;
    }

    // Save bookings to file
    static void saveBookings() {

        try {
            FileWriter writer = new FileWriter(FILE_NAME);

            for (Reservation r : reservations) {
                writer.write(r.toFileString() + "\n");
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving booking data.");
        }
    }

    // Load bookings from file
    static void loadBookings() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try {

            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();

                String[] data = line.split(",");

                if (data.length == 7) {

                    int bookingId = Integer.parseInt(data[0]);
                    String name = data[1];
                    int roomNumber = Integer.parseInt(data[2]);
                    String category = data[3];
                    int nights = Integer.parseInt(data[4]);
                    double amount = Double.parseDouble(data[5]);
                    String payment = data[6];

                    Reservation r = new Reservation(
                            bookingId,
                            name,
                            roomNumber,
                            category,
                            nights,
                            amount,
                            payment
                    );

                    reservations.add(r);

                    Room room = findRoom(roomNumber);

                    if (room != null) {
                        room.available = false;
                    }

                    if (bookingId >= nextBookingId) {
                        nextBookingId = bookingId + 1;
                    }
                }
            }

            fileScanner.close();

        } catch (Exception e) {
            System.out.println("Error loading booking data.");
        }
    }
}