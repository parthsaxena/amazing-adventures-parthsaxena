package student.adventure;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import student.adventure.Objects.*;

public class IO {

  private GameEngine engine;
  private final Gson gson;
  private Data data;
  private final Scanner scanner;

  public IO(Path path) throws IOException {
    gson = new Gson();
    scanner = new Scanner(System.in);

    Reader reader = Files.newBufferedReader(path);
    Data data = gson.fromJson(reader, Data.class);
    sanitizeData(data);
  }

  public void start() {
    System.out.println(data.getConfiguration().getInitializationText() + "\n");

    engine = new GameEngine(data.getRooms(), data.getConfiguration());
    examine();
  }

  /**
   * Prints out details of the current room
   */
  private void examine() {
    Map<String, String> directions = engine.getCurrentRoom().getDirections();
    Map<String, Item> items = engine.getCurrentRoom().getItems();

    // Print room description
    System.out.println(engine.getCurrentRoom().getDescription());

    // Print directions guide
    System.out.println(buildStringFromList(StringList.DIRECTIONS));

    // Print items guide
    System.out.println(buildStringFromList(StringList.ITEMS));

    // Prompt user for input
    prompt();
  }

  /**
   * Prompts the user to input a command*
   */
  private void prompt() {
    System.out.print("> ");

    String command = scanner.nextLine();

    dissectCommand(command);
  }

  private void dissectCommand(String command) {
    command = command.toLowerCase().trim();
    if (command == "exit" || command == "quit") {
      System.exit(0);
    }

    String action = "";
    String argument = "";
    int i;
    for (i = 0; i < command.length(); i++) {
      if (command.charAt(i) == ' ') {
        action = command.substring(0, i).trim();
        argument = command.substring(i).trim();
        break;
      }
    }

    if (action.length() == 0 || argument.length() == 0) {
      handleInvalidCommand(command);
    } else {
      handleCommand(command, action, argument);
    }
  }

  /**
   * Parses action and argument of command
   *
   * @param command
   * @param action
   * @param argument
   */
  private void handleCommand(String command, String action, String argument) {
    if (action.equals("go")) {
      handleGoAction(argument);
    } else if (action.equals("take")) {
      handleTakeAction(argument);
    } else if (action.equals("drop")) {
      handleDropAction(argument);
    }
    handleInvalidCommand(command);
  }

  private void handleGoAction(String argument) {
    Set<String> directionSet = engine.getCurrentRoom().getDirections().keySet();
    for (String direction : directionSet) {
      if (argument.equals(direction.toLowerCase())) {
        changeDirection(direction);
        return;
      }
    }
    System.out.println("You can't go \"" + argument + "\"!");
    prompt();
  }

  private void handleTakeAction(String argument) {
    Set<String> itemSet = engine.getCurrentRoom().getItems().keySet();
    for (String item : itemSet) {
      if (argument.equals(item.toLowerCase())) {
        Item itemObject = engine.getCurrentRoom().getItems().get(item);
        engine.takeItem(itemObject);
        prompt();
        return;
      }
    }

    System.out.println("There is no item \"" + argument + "\" in the room!");
    prompt();
  }

  private void handleDropAction(String argument) {
    Item itemToDrop = null;
    Set<Item> inventorySet = engine.getInventory().keySet();
    for (Item item : inventorySet) {
      if (argument.equals(item.getName().toLowerCase())) {
        itemToDrop = item;
        break;
      }
    }

    if (itemToDrop == null) {
      System.out.println("You do not have \"" + argument + "\" in your inventory!");
      prompt();
      return;
    }

    Set<String> itemSet = engine.getCurrentRoom().getItems().keySet();
    for (String item : itemSet) {
      if (argument.equals(item.toLowerCase())) {
        System.out.println("The item \"" + item +"\" is already in this room!");
        prompt();
        return;
      }
    }

    engine.dropItem(itemToDrop);
    prompt();
  }

  /**
   * Handles case of invalid command entered by user
   *
   * @param command
   */
  private void handleInvalidCommand(String command) {
    System.out.println("I don't quite understand \"" + command + "\"!");
    prompt();
  }

  /**
   * Generates an Enum value from the String direction and passes
   * it along to GameEngine
   *
   * @param direction
   */
  private void changeDirection(String direction) {
    Direction directionEnum = Direction.valueOf(direction.toUpperCase());
    engine.changeDirection(directionEnum);

    examine();
  }

  /**
   * Converts a list to a pretty-print String for I/O
   *
   * @param type the type of list to convert
   * @return String
   */
  private String buildStringFromList(StringList type) {
    String stringList = "From here, you can go: ";
    Set<String> keySet = engine.getCurrentRoom().getDirections().keySet();

    if (type == StringList.ITEMS) {
      stringList = "Items visible: ";
      keySet = engine.getCurrentRoom().getItems().keySet();
    }
    // Add items in list to String
    for (String key : keySet) {
      stringList += key + ", ";
    }

    // Cut off the last two characters because of extra comma
    return stringList.substring(0, stringList.length()-2);
  }

  /**
   * Checks sanity of data, all subfields, and the map itself
   *
   * @param data
   */
  private void sanitizeData(Data data) {
    try {

      checkNull(data);
      this.data = data;

    } catch(Exception e) {
      throw new IllegalArgumentException("Input JSON has invalid schema / data.");
    }
  }

  /**
   * Helper method that throws an Exception if given object is null
   *
   * @param o
   * @throws Exception
   */
  private void checkNull(Object o) throws Exception {
    if (o == null) {
      throw new Exception();
    }
  }
}

enum StringList {
  DIRECTIONS,
  ITEMS
}