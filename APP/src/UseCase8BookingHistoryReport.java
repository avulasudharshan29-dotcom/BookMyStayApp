import java.util.ArrayList;
import java.util.List;

// Reservation class represents a confirmed booking
class Reservation {
    private String guestName;
    private String roomType;
    private String checkInDate;
    private String checkOutDate;

    public Reservation(String guestName, String roomType, String checkInDate, String checkOutDate) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }

    @Override
    public String toString() {
        return "Reservation [Guest=" + guestName +
                ", Room=" + roomType +
                ", Check-In=" + checkInDate +
                ", Check-Out=" + checkOutDate + "]";
    }
}

// BookingHistory maintains confirmed reservations in insertion order
class BookingHistory {
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}

// BookingReportService generates reports from booking history
class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Generate a simple summary report
    public void generateSummaryReport() {
        List<Reservation> reservations = bookingHistory.getReservations();
        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Bookings: " + reservations.size());
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }
}

// Main class to demonstrate Use Case 8
public class UseCase8BookingHistoryReport {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("Alice", "Deluxe", "2026-04-10", "2026-04-12");
        Reservation r2 = new Reservation("Bob", "Suite", "2026-04-11", "2026-04-15");
        Reservation r3 = new Reservation("Charlie", "Standard", "2026-04-12", "2026-04-14");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin requests report
        BookingReportService reportService = new BookingReportService(history);
        reportService.generateSummaryReport();
    }
}