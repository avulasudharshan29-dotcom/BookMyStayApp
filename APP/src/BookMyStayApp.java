import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a guest's booking request
class Reservation {
    private String guestName;
    private String roomType;
    private int numberOfRooms;

    public Reservation(String guestName, String roomType, int numberOfRooms) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Rooms: " + numberOfRooms;
    }
}

// Booking Request Queue Manager
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added: " + reservation);
    }

    // View all queued requests
    public void viewRequests() {
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\nBooking Requests in Queue (FIFO Order):");
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }

    // Process next request (FIFO)
    public Reservation processNextRequest() {
        if (requestQueue.isEmpty()) {
            System.out.println("No requests to process.");
            return null;
        }

        Reservation r = requestQueue.poll();
        System.out.println("Processing request: " + r);
        return r;
    }
}

// Main class renamed as requested
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        // Simulating multiple booking requests (arrival order)
        Reservation r1 = new Reservation("Alice", "Single", 1);
        Reservation r2 = new Reservation("Bob", "Double", 2);
        Reservation r3 = new Reservation("Charlie", "Suite", 1);

        // Step 1: Add requests (FIFO order maintained)
        queue.addRequest(r1);
        queue.addRequest(r2);
        queue.addRequest(r3);

        // Step 2: View queued requests
        queue.viewRequests();

        // Step 3: Process requests (no allocation logic here)
        System.out.println("\n--- Processing Requests ---");
        queue.processNextRequest();
        queue.processNextRequest();

        // Step 4: Remaining requests
        queue.viewRequests();
    }
}