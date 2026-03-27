/**
 * RoomSearch.java
 *
 * This program demonstrates room search and availability check
 * using centralized inventory and domain model objects.
 * Search operations are read-only and do not modify system state.
 *
 * @author YourName
 * @version 4.1
 */

import java.util.HashMap;
import java.util.Map;

// Abstract Room class
abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double pricePerNight;

    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public abstract void displayRoomDetails();
}

// Concrete room classes
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000.0);
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Price per Night: ₹" + getPricePerNight());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 1800.0);
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Price per Night: ₹" + getPricePerNight());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 3500.0);
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Price per Night: ₹" + getPricePerNight());
    }
}

// Inventory class
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example: Suite unavailable
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// Search Service class (read-only access)
class SearchService {
    private RoomInventory inventory;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchAvailableRooms(Room[] rooms) {
        System.out.println("Available Rooms:");
        for (Room room : rooms) {
            int availability = inventory.getAvailability(room.getRoomType());
            if (availability > 0) {
                room.displayRoomDetails();
                System.out.println("Available: " + availability + "\n");
            }
        }
    }
}

// Application entry point
public class RoomSearch {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System v4.1");
        System.out.println("Guest initiated room search...\n");

        // Initialize inventory and rooms
        RoomInventory inventory = new RoomInventory();
        Room[] rooms = { new SingleRoom(), new DoubleRoom(), new SuiteRoom() };

        // Perform search using SearchService
        SearchService searchService = new SearchService(inventory);
        searchService.searchAvailableRooms(rooms);

        System.out.println("Search completed. System state unchanged.");
    }
}