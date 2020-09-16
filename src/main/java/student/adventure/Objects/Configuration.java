package student.adventure.Objects;

public class Configuration {
  private String startingRoom;
  private String winningRoom;
  private String[] winningItems;
  private String initializationText;
  private String victoryText;

  public String getVictoryText() {
    return victoryText;
  }

  public String getStartingRoom() {
    return startingRoom;
  }

  public String getWinningRoom() {
    return winningRoom;
  }

  public String[] getWinningItems() {
    return winningItems;
  }

  public String getInitializationText() {
    return initializationText;
  }
}
