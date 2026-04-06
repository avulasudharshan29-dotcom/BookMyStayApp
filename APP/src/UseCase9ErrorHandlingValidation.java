import java.util.*;

// Custom exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNights() { return nights; }

    @Override
    public String toString() {
        return "Reservation [Guest=" + guestName +
                ", Room=" + roomType +
                ", Nights=" + nights + "]";
    }
}

// Validator class
class BookingValidator {
    private static final Set<String> VALID_ROOM_TYPES =
            new HashSet<>(Arrays.asList("Standard", "Deluxe", "Suite"));

    public static void validateReservation(Reservation reservation, Map<String, Integer> inventory)
            throws InvalidBookingException {

        // Validate room type
        if (!VALID_ROOM_TYPES.contains(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        // Validate nights
        if (reservation.getNights() <= 0) {
            throw new InvalidBookingException("Number of nights must be positive.");
        }

        // Validate inventory availability
        int available = inventory.getOrDefault(reservation.getRoomType(), 0);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + reservation.getRoomType());
        }
    }
}

// Booking system with error handling
class BookingSystem {
    private Map<String, Integer> inventory;
    private List<Reservation> confirmedReservations;

    public BookingSystem() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 0); // no suites available initially
        confirmedReservations = new ArrayList<>();
    }

    public void processBooking(Reservation reservation) {
        try {
            BookingValidator.validateReservation(reservation, inventory);
            // Deduct inventory
            inventory.put(reservation.getRoomType(), inventory.get(reservation.getRoomType()) - 1);
            confirmedReservations.add(reservation);
            System.out.println("Booking confirmed: " + reservation);
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }

    public void showConfirmedBookings() {
        System.out.println("\n--- Confirmed Bookings ---");
        for (Reservation r : confirmedReservations) {
            System.out.println(r);
        }
    }
}

// Main class
public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {
        BookingSystem system = new BookingSystem();

        // Valid booking
        Reservation r1 = new Reservation("Alice", "Deluxe", 2);
        system.processBooking(r1);

        // Invalid room type
        Reservation r2 = new Reservation("Bob", "Penthouse", 3);
        system.processBooking(r2);

        // Invalid nights
        Reservation r3 = new Reservation("Charlie", "Standard", 0);
        system.processBooking(r3);

        // No inventory available
        Reservation r4 = new Reservation("David", "Suite", 1);
        system.processBooking(r4);

        // Another valid booking
        Reservation r5 = new Reservation("Eve", "Standard", 1);
        system.processBooking(r5);

        system.showConfirmedBookings();
    }
}