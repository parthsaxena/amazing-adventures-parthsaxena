package student.adventure.Objects;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class Data {

  @SerializedName("Configuration")
  private Configuration configuration;
  @SerializedName("Rooms")
  private Map<String, Room> rooms;

  public Configuration getConfiguration() {
    return configuration;
  }

  public Map<String, Room> getRooms() {
    return rooms;
  }
}
