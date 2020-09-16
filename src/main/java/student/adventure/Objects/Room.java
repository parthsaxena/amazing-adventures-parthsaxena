package student.adventure.Objects;

import java.util.Map;

public class Room {
  private String name;
  private String description;
  private String type;
  private Map<String, Item> items;
  private Map<String, String> directions;
  private String[] requirements;

  public String[] getRequirements() {
    return requirements;
  }

  public String getName() {
    return name;
  }

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
