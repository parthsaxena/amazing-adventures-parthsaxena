package student.adventure;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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

    if (type == StringList.ITEMS) {
      stringList = "Items visible: ";
      if (engine.getCurrentRoom().getType().equals("store")) {
        stringList = "Items for sale: ";
      }
      String list = commaSeparateItemList(engine.getCurrentRoom().getItems().values());
      if (list.length() == 0) {
        return "There are no items here.";
      }
      stringList += list;
    } else if (type == StringList.INVENTORY) {
      stringList = "You have: ";
      String list = commaSeparateItemList(engine.getPlayer().getInventory().getInventorySet());
      if (list.length() == 0) {
        return "You currently have no items.";
      }
      stringList += list;
    } else {
      for (String key : engine.getCurrentRoom().getDirections().keySet()) {
        stringList += key + ", ";
      }
    }

    // Cut off the last two characters because of extra comma
    return stringList.substring(0, stringList.length()-2);
  }

  /**
   * Separates a list of Item objects by commas and includes name and price
   * of item
   *
   * @param collection
   * @return
   */
  private static String commaSeparateItemList(Collection<Item> collection) {
    String res = "";
    for (Item key : collection) {
      res += key.getName() + " - $" + key.getValue() + ", ";
    }
    return res;
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
