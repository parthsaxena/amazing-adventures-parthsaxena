package student.adventure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import student.adventure.Objects.*;

public class Inventory {
  private Map<String, Item> inventory;

  /**
   * Constructor to instantiate Inventory
   */
  Inventory() {
    this.inventory = new HashMap<>();
  }

  /**
   * Adds the given item to Player's inventory and removes it from the room
   *
   * @param argument
   * @param currentRoom
   * @return
   */
  public Result takeItem(String argument, Room currentRoom) {
    argument = argument.toLowerCase();

    if (currentRoom.getItems().containsKey(argument)) {
      Item itemObject = currentRoom.getItems().get(argument);

      // Add this item to inventory
      inventory.put(itemObject.getName().toLowerCase(), itemObject);

      // Remove item from the room's items
      currentRoom.getItems().remove(argument);
      return new Result(itemObject.getDescription(), true);
    }

    return new Result("There is no item \"" + argument + "\" in the room!", false);
  }

  /**
   * Removes the given item from inventory and adds it to the room
   *
   * @param argument
   * @param currentRoom
   * @return
   */
  public Result dropItem(String argument, Room currentRoom) {
    argument = argument.toLowerCase();
    // Check if player has this item
    if (!inventory.containsKey(argument)) {
      return new Result("You do not have \"" + argument + "\" in your inventory!", false);
    }
    // Check if room already has this item
    if (currentRoom.getItems().containsKey(argument)) {
      return new Result("The item \"" + argument + "\" is already in this room!", false);
    }

    Item item = inventory.get(argument);
    inventory.remove(argument);
    currentRoom.getItems().put(argument, item);

    return new Result("", true);
  }

  /**
   * Returns a description of the given Item in inventory
   *
   * @param argument
   * @return
   */
  public Result inspectItem(String argument) {
    argument = argument.toLowerCase();
    // Check if player has this item
    if (!inventory.containsKey(argument)) {
      return new Result("You do not have \"" + argument + "\" in your inventory!", false);
    }
    return new Result(inventory.get(argument).getDescription(), true);
  }

  public Collection<Item> getInventorySet() {
    return inventory.values();
  }
}
