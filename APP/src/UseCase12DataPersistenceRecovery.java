import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String guestName;
    private String roomType;
    private List<String> rooms;

    public Reservation(String guestName, String roomType, List<String> rooms) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.rooms = rooms;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public List<String> getRooms() {
        return rooms;
    }

    @Override
    public String toString() {
        return guestName + " -> " + roomType + " " + rooms;
    }
}

// Wrapper class for persistence
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    Map<String, Reservation> bookings;

    public SystemState(Map<String, Integer> inventory,
                       Map<String, Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Inventory Service
class InventoryService {
    Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public void load(Map<String, Integer> data) {
        inventory = data;
    }

    public void show() {
        System.out.println("\nInventory:");
        for (String k : inventory.keySet()) {
            System.out.println(k + ": " + inventory.get(k));
        }
    }
}

// Booking Service
class BookingService {
    Map<String, Reservation> bookings = new HashMap<>();

    public void load(Map<String, Reservation> data) {
        bookings = data;
    }

    public void addBooking(String name, String type) {
        List<String> rooms = new ArrayList<>();
        rooms.add(type.charAt(0) + UUID.randomUUID().toString().substring(0, 5));

        Reservation r = new Reservation(name, type, rooms);
        bookings.put(name, r);

        System.out.println("✅ Booking added: " + r);
    }

    public void show() {
        System.out.println("\nBookings:");
        for (Reservation r : bookings.values()) {
            System.out.println(r);
        }
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\n💾 State saved successfully!");

        } catch (IOException e) {
            System.out.println("❌ Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("⚠ No saved data found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("📂 Loading saved state...");
            return (SystemState) ois.readObject();

        } catch (Exception e) {
            System.out.println("❌ Corrupted file. Starting fresh.");
            return null;
        }
    }
}

// MAIN CLASS
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService booking = new BookingService();

        // Step 1: Try loading saved state
        SystemState state = PersistenceService.load();

        if (state != null) {
            inventory.load(state.inventory);
            booking.load(state.bookings);

            System.out.println("✅ System recovered successfully!");
        }

        // Show current state
        inventory.show();
        booking.show();

        // Step 2: Simulate new bookings
        booking.addBooking("Alice", "Single");
        booking.addBooking("Bob", "Double");

        // Step 3: Save state before exit
        SystemState newState =
                new SystemState(inventory.inventory, booking.bookings);

        PersistenceService.save(newState);
    }
}