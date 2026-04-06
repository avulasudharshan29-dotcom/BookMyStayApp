import java.util.*;

// Reservation class
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
        return guestName + " -> " + roomType + " (" + numberOfRooms + ")";
    }
}

// Thread-safe Booking Queue
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
        notifyAll(); // wake up waiting threads
    }

    public synchronized Reservation getNextRequest() {
        while (queue.isEmpty()) {
            try {
                wait(); // wait until request arrives
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return queue.poll();
    }
}

// Thread-safe Inventory
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public synchronized boolean allocateRoom(String type, int count) {
        int available = inventory.getOrDefault(type, 0);

        if (available >= count) {
            inventory.put(type, available - count);
            return true;
        }
        return false;
    }

    public synchronized void showInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {

    private BookingRequestQueue queue;
    private InventoryService inventory;
    private Set<String> allocatedRooms;

    public BookingProcessor(String name,
                            BookingRequestQueue queue,
                            InventoryService inventory,
                            Set<String> allocatedRooms) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
        this.allocatedRooms = allocatedRooms;
    }

    @Override
    public void run() {
        while (true) {
            Reservation r = queue.getNextRequest();

            processReservation(r);
        }
    }

    private void processReservation(Reservation r) {

        synchronized (inventory) { // critical section

            System.out.println(Thread.currentThread().getName() +
                    " processing: " + r);

            boolean success = inventory.allocateRoom(
                    r.getRoomType(),
                    r.getNumberOfRooms()
            );

            if (!success) {
                System.out.println("❌ Booking failed for " + r.getGuestName());
                return;
            }

            // Allocate unique room IDs
            List<String> rooms = new ArrayList<>();

            for (int i = 0; i < r.getNumberOfRooms(); i++) {
                String roomId;
                do {
                    roomId = r.getRoomType().charAt(0) +
                            UUID.randomUUID().toString().substring(0, 5);
                } while (allocatedRooms.contains(roomId));

                allocatedRooms.add(roomId);
                rooms.add(roomId);
            }

            System.out.println("✅ Confirmed: " + r.getGuestName()
                    + " Rooms: " + rooms);
        }
    }
}

// MAIN CLASS
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();
        InventoryService inventory = new InventoryService();

        // Shared set for uniqueness
        Set<String> allocatedRooms =
                Collections.synchronizedSet(new HashSet<>());

        // Create multiple worker threads
        BookingProcessor t1 =
                new BookingProcessor("Thread-1", queue, inventory, allocatedRooms);

        BookingProcessor t2 =
                new BookingProcessor("Thread-2", queue, inventory, allocatedRooms);

        t1.start();
        t2.start();

        // Simulate concurrent booking requests
        new Thread(() -> {
            queue.addRequest(new Reservation("Alice", "Single", 1));
            queue.addRequest(new Reservation("Bob", "Double", 2));
            queue.addRequest(new Reservation("Charlie", "Suite", 1));
            queue.addRequest(new Reservation("David", "Suite", 1)); // should fail
        }).start();

        // Let threads run for a while then print inventory
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.showInventory();
        System.exit(0);
    }
}