package student.adventure;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import student.adventure.Objects.*;

public class GameEngine {

  private Map<String, Room> rooms;
  private Configuration configuration;
  private Room currentRoom;
  private Player player;

  /**
   * Constructor to instantiate the Game Engine
   *
   * @param rooms
   * @param configuration
   */
  GameEngine(Map<String, Room> rooms, Configuration configuration) {
    this.rooms = rooms;
    this.configuration = configuration;
    this.player = new Player();

    String currentRoomKey = configuration.getStartingRoom();
    this.currentRoom = rooms.get(currentRoomKey);
  }

  public String changeDirection(String argument) {
    Direction direction;
    try {
      direction = Direction.valueOf(argument.toUpperCase());
    } catch (IllegalArgumentException e) {
      return "You can't go \"" + argument + "\"!";
    }

    Set<String> directionSet = this.currentRoom.getDirections().keySet();
    for (String validDirection : directionSet) {
      if (validDirection.equals(direction.getKey())) {
        String roomKey = currentRoom.getDirections().get(direction.getKey());
        Room room = rooms.get(roomKey);
        this.currentRoom = room;

        return null;
      }
    }
    return "You can't go \"" + direction.getKey() + "\"!";
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

  public Player getPlayer() {
    return this.player;
  }

  public Result takeItem(String argument) {
    return this.player.getInventory().takeItem(argument, currentRoom);
  }

  public Result dropItem(String argument) {
    return this.player.getInventory().dropItem(argument, currentRoom);
  }

  public Result inspectItem(String argument) {
    return this.player.getInventory().inspectItem(argument);
  }
}
