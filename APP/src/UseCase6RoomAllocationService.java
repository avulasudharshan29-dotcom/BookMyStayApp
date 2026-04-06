import java.util.*;

// Reservation class (same as UC5)
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

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType, int count) {
        return inventory.getOrDefault(roomType, 0) >= count;
    }

    public void reduceInventory(String roomType, int count) {
        inventory.put(roomType, inventory.get(roomType) - count);
    }

    public void showInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms: " + inventory.get(type));
        }
    }
}

// Booking Service (Core Logic)
class BookingService {

    private InventoryService inventoryService;

    // Map<RoomType, Set<RoomIDs>>
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void processReservation(Reservation reservation) {

        String type = reservation.getRoomType();
        int roomsNeeded = reservation.getNumberOfRooms();

        System.out.println("\nProcessing: " + reservation);

        // Step 1: Check availability
        if (!inventoryService.isAvailable(type, roomsNeeded)) {
            System.out.println("❌ Not enough rooms available for " + type);
            return;
        }

        // Step 2: Initialize set if not exists
        allocatedRooms.putIfAbsent(type, new HashSet<>());

        Set<String> roomSet = allocatedRooms.get(type);

        // Step 3: Allocate unique room IDs
        List<String> assignedRooms = new ArrayList<>();

        for (int i = 0; i < roomsNeeded; i++) {

            String roomId;
            do {
                roomId = type.substring(0, 1).toUpperCase() + UUID.randomUUID().toString().substring(0, 5);
            } while (roomSet.contains(roomId)); // ensure uniqueness

            roomSet.add(roomId);
            assignedRooms.add(roomId);
        }

        // Step 4: Update inventory (atomic step)
        inventoryService.reduceInventory(type, roomsNeeded);

        // Step 5: Confirm booking
        System.out.println("✅ Reservation Confirmed for " + reservation.getGuestName());
        System.out.println("Allocated Rooms: " + assignedRooms);
    }
}

// Main class (as required)
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        // Step 1: Create queue and add requests (from UC5)
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Alice", "Single", 1));
        queue.addRequest(new Reservation("Bob", "Double", 2));
        queue.addRequest(new Reservation("Charlie", "Suite", 1));
        queue.addRequest(new Reservation("David", "Suite", 1)); // should fail

        // Step 2: Inventory + Booking Service
        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);

        // Step 3: Process queue (FIFO)
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processReservation(r);
        }

        // Step 4: Show remaining inventory
        inventory.showInventory();
    }
}