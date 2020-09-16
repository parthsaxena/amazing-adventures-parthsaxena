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

    // Read JSON from file
    Reader reader = Files.newBufferedReader(path);
    Data data = gson.fromJson(reader, Data.class);
    sanitizeData(data);

    populateActionMap();
  }

  /**
   * Populates a HashMap that Maps a String action (i.e. "go", "take") to
   * a function that handles that action
   */
  private void populateActionMap() {
    actionMap = new HashMap<>();
    actionMap.put("go", new Action() {
      public void performAction(String argument) {
        handleGoAction(argument);
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
    actionMap.put("inspect", new Action() {
      @Override
      public void performAction(String argument) {
        handleInspectAction(argument);
      }
    });
    actionMap.put("buy", new Action() {
      @Override
      public void performAction(String argument) {
        handleBuyAction(argument);
      }
    });
    actionMap.put("sell", new Action() {
      @Override
      public void performAction(String argument) {
        handleSellAction(argument);
      }
    });
    actionMap.put("money", new Action() {
      @Override
      public void performAction(String argument) {
        handleMoneyAction(argument);
      }
    });
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
   * Begins the game in the given room for testing purposes
   *
   * @param startingRoom
   */
  public void start(String startingRoom) {
    engine = new GameEngine(data.getRooms(), data.getConfiguration(), startingRoom);
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
    // Separate the action from the argument
    int firstArgIndex = command.indexOf(' ');
    String action = "";
    String argument = "";

    // Handle single word commands
    if (firstArgIndex == -1) {
      action = command;
    } else {
      action = command.substring(0, firstArgIndex);
      argument = command.substring(firstArgIndex).trim();
    }

    // If actionMap contains this action, it's potentially valid
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
  private Result handleTakeAction(String argument) {
    // Get response from Game Engine
    Result res = engine.takeItem(argument);
    System.out.println(res.getMessage());
    prompt();

    return res;
  }

  /**
   * I/O management to parse and perform "drop" action
   *
   * @param argument
   */
  private Result handleDropAction(String argument) {
    // Get response from Game Engine
    Result res = engine.dropItem(argument);
    // We only need to print response message if failure
    if (res.getState() == State.FAILURE) {
      System.out.println(res.getMessage());
    }

    prompt();
    return res;
  }

  /**
   * I/O management to parse and perform "money" action
   *
   * @param argument
   * @return
   */
  private Result handleMoneyAction(String argument) {
    // Get response from Game Engine
    Result res = engine.getMoney();
    System.out.println(res.getMessage());

    prompt();
    return res;
  }

  /**
   * I/O management to parse and perform "go" action
   *
   * @param direction
   */
  private Result handleGoAction(String direction) {
    // Get response from Game Engine
    Result res = engine.changeDirection(direction);
    if (res.getState() == State.FAILURE) {
      System.out.println(res.getMessage());
    } else {
      // Examine the new room
      examine();
    }

    prompt();
    return res;
  }

  /**
   * I/O management to parse and perform "inspect" action
   *
   * @param argument
   */
  private Result handleInspectAction(String argument) {
    // Get response from Game Engine
    Result res = engine.inspectItem(argument);
    System.out.println(res.getMessage());

    prompt();
    return res;
  }

  /**
   * I/O management to parse and perform "sell" action
   *
   * @param argument
   */
  private Result handleSellAction(String argument) {
    // Get response from Game Engine
    Result res = engine.sellItem(argument);
    System.out.println(res.getMessage());

    prompt();
    return res;
  }

  /**
   * I/O management to parse and perform "buy" action
   *
   * @param argument
   */
  private Result handleBuyAction(String argument) {
    // Get response from Game Engine
    Result res = engine.buyItem(argument);
    System.out.println(res.getMessage());

    prompt();
    return res;
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
   * Checks sanity of data, all subfields, and the map itself
   *
   * @param data
   */
  private void sanitizeData(Data data) {
    try {
      // Sanitize game configuration
      Helper.checkNull(data.getConfiguration().getInitializationText());

      // Sanitize Rooms and Items
      for (Room room : data.getRooms().values()) {
        // Check if room has necessary fields
        Helper.checkNull(room.getDirections());
        Helper.checkNull(room.getDescription());
        Helper.checkNull(room.getType());
        Helper.checkNull(room.getName());

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
    } catch(Exception e) {
      throw new IllegalArgumentException("Input JSON has invalid schema / data.");
    }
  }

  protected GameEngine getEngine() {
    return this.engine;
  }
}

/*
 * Enum that specifies what values to separate in pretty print list
 */
enum StringList {
  DIRECTIONS,
  ITEMS,
  INVENTORY
}