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
    actionMap.put("inspect", new Action() {
      @Override
      public void performAction(String argument) {
        handleInspectCommand(argument);
      }
    });
    actionMap.put("buy", new Action() {
      @Override
      public void performAction(String argument) {
        handleBuyCommand(argument);
      }
    });
    actionMap.put("sell", new Action() {
      @Override
      public void performAction(String argument) {
        handleSellCommand(argument);
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
    System.out.println(data.getConfiguration().getInitializationText() + "\n");

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
  private Result handleTakeAction(String argument) {
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
    Result res = engine.dropItem(argument);
    if (res.getState() == State.FAILURE) {
      System.out.println(res.getMessage());
    }

    prompt();
    return res;
  }

  private Result handleMoneyAction(String argument) {
    Result res = engine.getMoney();
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
   * I/O management to parse and perform "go" action
   *
   * @param direction
   */
  private Result handleGoCommand(String direction) {
    Result res = engine.changeDirection(direction);
    if (res.getState() == State.FAILURE) {
      System.out.println(res.getMessage());
    } else {
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
  private Result handleInspectCommand(String argument) {
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
  private Result handleSellCommand(String argument) {
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
  private Result handleBuyCommand(String argument) {
    Result res = engine.buyItem(argument);
    System.out.println(res.getMessage());

    prompt();
    return res;
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

  protected GameEngine getEngine() {
    return this.engine;
  }
}

enum StringList {
  DIRECTIONS,
  ITEMS,
  INVENTORY
}