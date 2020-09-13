package student.adventure.Objects;

import java.util.Map;

public class Room {
  private String description;
  private Map<String, Item> items;
  private Map<String, String> directions;

  public String getDescription() {
    return description;
  }

  public Map<String, Item> getItems() {
    return items;
  }

  public Map<String, String> getDirections() {
    return directions;
  }
}
