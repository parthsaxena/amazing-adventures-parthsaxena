package student.adventure;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import student.adventure.Objects.*;

public class Inventory {
  private Map<Item, Integer> inventory;

  Inventory() {
    this.inventory = new HashMap<>();
  }

  public Result takeItem(String argument, Room currentRoom) {
    Set<String> itemSet = currentRoom.getItems().keySet();
    for (String item : itemSet) {
      if (argument.equals(item.toLowerCase())) {
        Item itemObject = currentRoom.getItems().get(item);
        // Increase the frequency of this item in inventory
        inventory.put(itemObject, inventory.getOrDefault(item, 0) + 1);

        // Remove item from the room's items
        currentRoom.getItems().remove(itemObject.getName());
        return new Result(itemObject.getDescription(), true);
      }
    }
    return new Result("There is no item \"" + argument + "\" in the room!", false);
  }

  public Result dropItem(String argument, Room currentRoom) {
    Set<String> itemSet = currentRoom.getItems().keySet();
    for (String item : itemSet) {
      if (argument.equals(item.toLowerCase())) {
        return new Result("The item \"" + item +"\" is already in this room!", false);
      }
    }

    Set<Item> inventorySet = inventory.keySet();
    for (Item item : inventorySet) {
      if (argument.equals(item.getName().toLowerCase())) {
        // Decrease frequency of this item in inventory or remove it entirely
        if (inventory.get(item) > 1) {
          inventory.put(item, inventory.get(item) - 1);
        } else {
          inventory.remove(item);
        }
        // Add item to current room's items
        currentRoom.getItems().put(item.getName(), item);
        return new Result("", true);
      }
    }
    return new Result("You do not have \"" + argument + "\" in your inventory!", false);
  }

  public Set<Item> getInventorySet() {
    return inventory.keySet();
  }
}
