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
    // Check if the user inputted an actual cardinal direction
    try {
      direction = Direction.valueOf(argument.toUpperCase());
    } catch (IllegalArgumentException e) {
      return new Result("You can't go \"" + argument + "\"!", State.FAILURE);
    }

    // Check if this room has a route in the given direction
    if (currentRoom.getDirections().containsKey(direction.getKey())) {
      String roomKey = currentRoom.getDirections().get(direction.getKey());

      // Check if player meets requirements to enter this room
      String missingRequirements = Helper.getMissingRequirements(roomKey, this);
      if (missingRequirements != null) {
        return new Result("You need the following items to enter this room: " + missingRequirements, State.FAILURE);
      }
      Room room = rooms.get(roomKey);
      this.currentRoom = room;

      return new Result("", State.SUCCESS);
    } else {
      return new Result("You can't go \"" + direction.getKey() + "\"!", State.SUCCESS);
    }
  }

  /**
   * Returns the result of picking up the given item
   *
   * @param argument
   * @return
   */
  public Result takeItem(String argument) {
    if (currentRoom.getType().equals("store")) {
      return new Result("You must purchase this item!", State.FAILURE);
    }

    return this.player.getInventory().takeItem(argument, currentRoom);
  }

  /**
   * Returns the result of dropping the given item
   *
   * @param argument
   * @return
   */
  public Result dropItem(String argument) {
    if (currentRoom.getType().equals("store")) {
      return new Result("You can't drop this item here!", State.FAILURE);
    }

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

  /**
   * Sells the given item from the Player's inventory
   *
   * @param argument
   * @return
   */
  public Result sellItem(String argument) {
    // Check if current room is of type 'store'
    if (!currentRoom.getType().equals("store")) {
      return new Result("You must be a in a store to sell items!", State.FAILURE);
    }

    // Check if player has this item
    if (!player.getInventory().hasItem(argument)) {
      return new Result("You do not have \"" + argument + "\" in your inventory!", State.FAILURE);
    }

    // Give money to Player
    Item toSell = player.getInventory().getItem(argument);
    player.addMoney(toSell.getValue());
    // "Drop" item into store

    this.player.getInventory().dropItem(toSell.getName(), currentRoom);

    return new Result("Transaction successful!", State.SUCCESS);
  }

  /**
   * Purchases the item from the store into the Player's inventory
   *
   * @param argument
   * @return
   */
  public Result buyItem(String argument) {
    // Check if current room is of type 'store'
    if (!currentRoom.getType().equals("store")) {
      return new Result("You must be a in a store to buy items!", State.FAILURE);
    }
    // Check if store has this item in stock
    if (!currentRoom.getItems().containsKey(argument.toLowerCase())) {
      return new Result("\"" + argument + "\" is not for sale!", State.FAILURE);
    }
    // Take money from Player
    Item toBuy = currentRoom.getItems().get(argument.toLowerCase());
    // Check if player has enough money
    if (player.getMoney() < toBuy.getValue()) {
      return new Result("You don't have enough money!", State.FAILURE);
    }
    player.subtractMoney(toBuy.getValue());
    // "Give" item to Player's inventory
    this.player.getInventory().takeItem(argument, currentRoom);

    return new Result("Transaction successful!", State.SUCCESS);
  }

  /**
   * Returns a Result including the Player's current money
   *
   * @return
   */
  public Result getMoney() {
    return new Result("You have $" + player.getMoney() + ".", State.SUCCESS);
  }

  /**
   * Teleports the player to a given room for testing purposes
   */
  public void teleport(String roomKey) {
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

  public Player getPlayer() {
    return this.player;
  }
}
