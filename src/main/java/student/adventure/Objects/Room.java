package student.adventure.Objects;

import java.util.Map;

public class Room {
  private String description;
  private String type;
  private Map<String, Item> items;
  private Map<String, String> directions;

  public String getType() {
    return type;
  }

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
