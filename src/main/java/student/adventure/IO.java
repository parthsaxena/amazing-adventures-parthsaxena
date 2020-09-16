package student.adventure;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import student.adventure.Objects.Data;
import student.adventure.Objects.Item;
import student.adventure.Objects.Room;

/*
 * Enum that specifies what values to separate in pretty print list
 */
enum StringList {
  DIRECTIONS,
  ITEMS,
  INVENTORY
}

public class IO {

  protected static GameEngine engine;
  private static Scanner scanner;
  private static Map<String, Action> actionMap;
  private final Gson gson;
  private Data data;

  /**
   * Constructor to load JSON game data from the given path
   *
   * @param path
   * @throws IOException
   */
  public IO(Path path) throws IOException {
    gson = new Gson();
    scanner = new Scanner(System.in);

    // Read JSON from file
    Reader reader = Files.newBufferedReader(path);
    Data data = gson.fromJson(reader, Data.class);
    sanitizeData(data);

    // Build action HashMap to map String actions to their corresponding functions
    actionMap = IOHandler.buildActionMap();
  }

  /**
   * Prints out details of the current room
   */
  protected static void examine() {
    Map<String, String> directions = engine.getCurrentRoom().getDirections();
    Map<String, Item> items = engine.getCurrentRoom().getItems();

    // Print room description
    System.out.println(engine.getCurrentRoom().getDescription());

    // Print directions guide
    System.out.println(Helper.buildStringFromList(StringList.DIRECTIONS, engine));

    // Print items guide
    System.out.println(Helper.buildStringFromList(StringList.ITEMS, engine));

    // Prompt user for input
    prompt();
  }

  /**
   * Prompts the user to input a command
   */
  protected static void prompt() {
    System.out.print("> ");

    // Collect input
    String command = scanner.nextLine();

    // Process command
    dissectCommand(command);
  }

  /**
   * Dissects action and arguments from inputted command
   *
   * @param command
   */
  private static void dissectCommand(String command) {
    command = command.toLowerCase().trim();
    // Separate the action from the argument
    int firstArgIndex = command.indexOf(' ');
    String action;
    String argument = "";

    // Handle single word commands
    if (firstArgIndex == -1) {
      action = command;
    } else {
      action = command.substring(0, firstArgIndex);
      argument = command.substring(firstArgIndex).trim();
    }

    // If actionMap contains this action, command is potentially valid (argument may be invalid)
    if (actionMap.containsKey(action)) {
      actionMap.get(action).performAction(argument);
    } else {
      handleInvalidCommand(command);
    }
  }

  /**
   * Handles case of invalid command entered by user
   *
   * @param command
   */
  private static void handleInvalidCommand(String command) {
    System.out.println("I don't quite understand \"" + command + "\"!");
    prompt();
  }

  /**
   * Handle player victory
   */
  protected static void handleVictory(String message) {
    // Print pretty divider text that matches the length of the message
    char[] dividerText = new char[message.length()];
    Arrays.fill(dividerText, '-');
    System.out.println(new String(dividerText));

    // Print message and victory text
    System.out.println(message + "\n");
    System.out.println(engine.getConfiguration().getVictoryText());

    // Gracefully exit
    System.exit(0);
  }

  /**
   * Begins the game with preliminary output
   */
  public void start() {
    // Print out Game information
    System.out.println(data.getConfiguration().getInitializationText() + "\n");

    // Instantiate Game Engine
    engine = new GameEngine(data.getRooms(), data.getConfiguration());
    examine();
  }

  /**
   * Begins the game in the given room in testing mode (no I/O)
   *
   * @param startingRoom
   */
  public void start(String startingRoom) {
    engine = new GameEngine(data.getRooms(), data.getConfiguration(), startingRoom);
  }

  /**
   * Checks sanity of data, all subfields, and the map itself
   *
   * @param data
   */
  private void sanitizeData(Data data) {
    try {
      // Sanitize game configuration
      Helper.checkNull(data.getConfiguration().getInitializationText());
      Helper.checkNull(data.getConfiguration().getVictoryText());
      Helper.checkNull(data.getConfiguration().getStartingRoom());

      // Sanitize Rooms and Items
      for (Room room : data.getRooms().values()) {
        // Check if room has necessary fields
        Helper.checkNull(room.getDirections());
        Helper.checkNull(room.getDescription());
        Helper.checkNull(room.getType());
        Helper.checkNull(room.getName());
        Helper.checkNull(room.getRequirements());

        // Check if all directions point to valid rooms
        for (String roomKey : room.getDirections().values()) {
          Helper.checkNull(data.getRooms().get(roomKey));
        }
        // Check if all items have necessary fields
        for (Item item : room.getItems().values()) {
          Helper.checkNull(item.getName());
          Helper.checkNull(item.getDescription());
          Helper.checkNull(item.getValue());
        }
      }
      this.data = data;

    } catch (Exception e) {
      throw new IllegalArgumentException("Input JSON has invalid schema / data.");
    }
  }

  protected GameEngine getEngine() {
    return this.engine;
  }
}