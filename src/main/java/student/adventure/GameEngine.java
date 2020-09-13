package student.adventure;

import java.util.HashMap;
import java.util.Map;
import student.adventure.Objects.*;

public class GameEngine {

  private Map<String, Room> rooms;
  private Configuration configuration;
  private Room currentRoom;
  private Map<Item, Integer> inventory;

  /**
   * Constructor to instantiate the Game Engine
   *
   * @param rooms
   * @param configuration
   */
  GameEngine(Map<String, Room> rooms, Configuration configuration) {
    this.rooms = rooms;
    this.configuration = configuration;
    this.inventory = new HashMap<>();

    String currentRoomKey = configuration.getStartingRoom();
    this.currentRoom = rooms.get(currentRoomKey);
  }

  public void changeDirection(Direction direction) {
    String roomKey = currentRoom.getDirections().get(direction.getKey());
    Room room = rooms.get(roomKey);

    this.currentRoom = room;
  }

  public Map<String, Room> getRooms() {
    return rooms;
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  public Room getCurrentRoom() {
    return currentRoom;
  }

  public Map<Item, Integer> getInventory() {
    return inventory;
  }
}
