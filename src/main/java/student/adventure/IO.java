package student.adventure;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import student.adventure.Objects.*;

interface Action {
  void performAction(String argument);
}

public class IO {

  private GameEngine engine;
  private final Gson gson;
  private Data data;
  private final Scanner scanner;

  private Map<String, Action> actionMap;

  /**
   * Constructor to load JSON game data from the given path
   *
   * @param path
   * @throws IOException
   */
  public IO(Path path) throws IOException {
    gson = new Gson();
    scanner = new Scanner(System.in);

    Reader reader = Files.newBufferedReader(path);
    Data data = gson.fromJson(reader, Data.class);
    sanitizeData(data);

    populateActionMap();
  }

  /**
   * Populates a HashMap that relates a String action to a function that
   * handles that action
   */
  private void populateActionMap() {
    actionMap = new HashMap<>();
    actionMap.put("go", new Action() {
      public void performAction(String argument) {
        handleGoCommand(argument);
      }
    });
    actionMap.put("take", new Action() {
      public void performAction(String argument) {
        handleTakeAction(argument);
      }
    });
    actionMap.put("drop", new Action() {
      public void performAction(String argument) {
        handleDropAction(argument);
      }
    });
    actionMap.put("exit", new Action() {
      public void performAction(String argument) {
        System.exit(0);
      }
    });
    actionMap.put("quit", new Action() {
      public void performAction(String argument) {
        System.exit(0);
      }
    });
    actionMap.put("take", new Action() {
      public void performAction(String argument) {
        handleTakeAction(argument);
      }
    });
    actionMap.put("inventory", new Action() {
      @Override
      public void performAction(String argument) {
        System.out.println(Helper.buildStringFromList(StringList.INVENTORY, engine));
        prompt();
      }
    });
    actionMap.put("examine", new Action() {
      @Override
      public void performAction(String argument) {
        examine();
      }
    });
  }

  /**
   * Begins the game with preliminary output
   */
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
    System.out.println(Helper.buildStringFromList(StringList.DIRECTIONS, engine));

    // Print items guide
    System.out.println(Helper.buildStringFromList(StringList.ITEMS, engine));

    // Prompt user for input
    prompt();
  }

  /**
   * Prompts the user to input a command
   */
  private void prompt() {
    System.out.print("> ");

    String command = scanner.nextLine();

    dissectCommand(command);
  }

  /**
   * Dissects action and arguments from inputted command
   *
   * @param command
   */
  private void dissectCommand(String command) {
    command = command.toLowerCase().trim();
    int firstArgIndex = command.indexOf(' ');

    String action = "";
    String argument = "";
    if (firstArgIndex == -1) {
      action = command;
    } else {
      action = command.substring(0, firstArgIndex);
      argument = command.substring(firstArgIndex).trim();
    }

    if (actionMap.containsKey(action)) {
      actionMap.get(action).performAction(argument);
    } else {
      handleInvalidCommand(command);
    }
  }

  /**
   * I/O management to parse and perform "take" action
   *
   * @param argument
   */
  private void handleTakeAction(String argument) {
    Result res = engine.takeItem(argument);
    System.out.println(res.getMessage());
    prompt();
  }

  /**
   * I/O management to parse and perform "drop" action
   *
   * @param argument
   */
  private void handleDropAction(String argument) {
    Result res = engine.dropItem(argument);
    if (!res.isSuccessful()) {
      System.out.println(res.getMessage());
    }

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
   * I/O management to parse and perform "go" action
   *
   * @param direction
   */
  private void handleGoCommand(String direction) {
    String message = engine.changeDirection(direction);
    if (message != null) {
      System.out.println(message);
    } else {
      examine();
    }

    prompt();
  }

  /**
   * Checks sanity of data, all subfields, and the map itself
   *
   * @param data
   */
  private void sanitizeData(Data data) {
    try {
      Helper.checkNull(data);
      this.data = data;
    } catch(Exception e) {
      throw new IllegalArgumentException("Input JSON has invalid schema / data.");
    }
  }
}

enum StringList {
  DIRECTIONS,
  ITEMS,
  INVENTORY
}