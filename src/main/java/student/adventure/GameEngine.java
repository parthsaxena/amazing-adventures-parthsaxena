package student.adventure;

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

  /**
   * Constructor to instantiate the Game Engine with the specified starting
   * room for testing purposes
   *
   * @param rooms
   * @param configuration
   * @param startingRoom
   */
  GameEngine(Map<String, Room> rooms, Configuration configuration, String startingRoom) {
    this.rooms = rooms;
    this.configuration = configuration;
    this.player = new Player();

    this.currentRoom = rooms.get(startingRoom);
  }

  /**
   * Returns the result of changing rooms in the given direction
   *
   * @param argument
   * @return
   */
  public Result changeDirection(String argument) {
    Direction direction;
    try {
      direction = Direction.valueOf(argument.toUpperCase());
    } catch (IllegalArgumentException e) {
      return new Result("You can't go \"" + argument + "\"!", false);
    }

    Set<String> directionSet = this.currentRoom.getDirections().keySet();
    if (directionSet.contains(direction.getKey())) {
      String roomKey = currentRoom.getDirections().get(direction.getKey());
      Room room = rooms.get(roomKey);
      this.currentRoom = room;

      return new Result("", true);
    } else {
      return new Result("You can't go \"" + direction.getKey() + "\"!", false);
    }
  }

  /**
   * Returns the result of picking up the given item
   *
   * @param argument
   * @return
   */
  public Result takeItem(String argument) {
    return this.player.getInventory().takeItem(argument, currentRoom);
  }

  /**
   * Returns the result of dropping the given item
   *
   * @param argument
   * @return
   */
  public Result dropItem(String argument) {
    return this.player.getInventory().dropItem(argument, currentRoom);
  }

  /**
   * Returns description of the given item
   *
   * @param argument
   * @return
   */
  public Result inspectItem(String argument) {
    return this.player.getInventory().inspectItem(argument);
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
}
