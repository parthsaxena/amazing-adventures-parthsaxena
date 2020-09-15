package student.adventure;

import java.util.HashSet;
import java.util.Set;
import student.adventure.Objects.Data;
import student.adventure.Objects.Item;

public class Helper {

  /**
   * Converts a list to a pretty-print String for I/O
   *
   * @param type the type of list to convert
   * @return String
   */
  public static String buildStringFromList(StringList type, GameEngine engine) {
    String stringList = "From here, you can go: ";
    Set<String> keySet = engine.getCurrentRoom().getDirections().keySet();

    if (type == StringList.ITEMS) {
      stringList = "Items visible: ";
      keySet = engine.getCurrentRoom().getItems().keySet();
      if (keySet.size() == 0) {
        return "There are no items here.";
      }
    } else if (type == StringList.INVENTORY) {
      stringList = "You have: ";
      keySet = new HashSet<String>();
      for (Item key : engine.getPlayer().getInventory().getInventorySet()) {
        keySet.add(key.getName());
      }
      if (keySet.size() == 0) {
        return "You currently have no items.";
      }
    }

    // Add items in list to String
    for (String key : keySet) {
      stringList += key + ", ";
    }
    // Cut off the last two characters because of extra comma
    return stringList.substring(0, stringList.length()-2);
  }

  /**
   * Helper method that throws an Exception if given object is null
   *
   * @param o
   * @throws Exception
   */
  public static void checkNull(Object o) throws Exception {
    if (o == null) {
      throw new Exception();
    }
  }
}
