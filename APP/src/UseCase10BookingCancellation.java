import java.util.*;

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;
    private List<String> allocatedRooms;

    public Reservation(String guestName, String roomType, List<String> allocatedRooms) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.allocatedRooms = allocatedRooms;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public List<String> getAllocatedRooms() {
        return allocatedRooms;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Rooms: " + allocatedRooms;
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

    public void reduceInventory(String type, int count) {
        inventory.put(type, inventory.get(type) - count);
    }

    public void increaseInventory(String type, int count) {
        inventory.put(type, inventory.get(type) + count);
    }

    public void showInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// Booking Service (for initial allocation)
class BookingService {
    private InventoryService inventory;
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private Map<String, Reservation> confirmedBookings = new HashMap<>();

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(String guestName, String roomType, int count) {

        allocatedRooms.putIfAbsent(roomType, new HashSet<>());
        Set<String> roomSet = allocatedRooms.get(roomType);

        List<String> assigned = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String roomId;
            do {
                roomId = roomType.charAt(0) +
                        UUID.randomUUID().toString().substring(0, 5);
            } while (roomSet.contains(roomId));

            roomSet.add(roomId);
            assigned.add(roomId);
        }

        inventory.reduceInventory(roomType, count);

        Reservation res = new Reservation(guestName, roomType, assigned);
        confirmedBookings.put(guestName, res);

        System.out.println("✅ Booking Confirmed: " + res);
    }

    public Map<String, Reservation> getConfirmedBookings() {
        return confirmedBookings;
    }

    public Map<String, Set<String>> getAllocatedRooms() {
        return allocatedRooms;
    }
}

// Cancellation Service (Core UC10 logic)
class CancellationService {

    private InventoryService inventory;
    private Map<String, Reservation> confirmedBookings;
    private Map<String, Set<String>> allocatedRooms;

    // Stack for rollback (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(InventoryService inventory,
                               Map<String, Reservation> confirmedBookings,
                               Map<String, Set<String>> allocatedRooms) {
        this.inventory = inventory;
        this.confirmedBookings = confirmedBookings;
        this.allocatedRooms = allocatedRooms;
    }

    public void cancelBooking(String guestName) {

        System.out.println("\nAttempting cancellation for: " + guestName);

        // Validate booking exists
        if (!confirmedBookings.containsKey(guestName)) {
            System.out.println("❌ No booking found for " + guestName);
            return;
        }

        Reservation res = confirmedBookings.get(guestName);

        String type = res.getRoomType();
        List<String> rooms = res.getAllocatedRooms();

        Set<String> roomSet = allocatedRooms.get(type);

        // LIFO rollback using stack
        for (String roomId : rooms) {
            rollbackStack.push(roomId);
        }

        while (!rollbackStack.isEmpty()) {
            String roomId = rollbackStack.pop();
            roomSet.remove(roomId);
            System.out.println("↩ Released Room: " + roomId);
        }

        // Restore inventory
        inventory.increaseInventory(type, rooms.size());

        // Remove booking
        confirmedBookings.remove(guestName);

        System.out.println("✅ Cancellation successful for " + guestName);
    }
}

// MAIN CLASS
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);

        // Step 1: Create bookings
        bookingService.confirmBooking("Alice", "Single", 1);
        bookingService.confirmBooking("Bob", "Double", 2);

        inventory.showInventory();

        // Step 2: Cancellation service
        CancellationService cancelService = new CancellationService(
                inventory,
                bookingService.getConfirmedBookings(),
                bookingService.getAllocatedRooms()
        );

        // Step 3: Cancel bookings
        cancelService.cancelBooking("Alice");
        cancelService.cancelBooking("Charlie"); // invalid case

        // Step 4: Final inventory
        inventory.showInventory();
    }
}