package student.adventure;

import java.util.HashMap;
import java.util.Map;

interface Action {

  void performAction(String argument);
}

public class IOHandler {

  /**
   * Populates a HashMap that Maps a String action (i.e. "go", "take") to a function that handles
   * that action
   */
  protected static Map<String, Action> buildActionMap() {
    HashMap<String, Action> actionMap = new HashMap<>();
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
        System.out.println(Helper.buildStringFromList(StringList.INVENTORY, IO.engine));
        IO.prompt();
      }
    });
    actionMap.put("examine", new Action() {
      @Override
      public void performAction(String argument) {
        IO.examine();
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

    return actionMap;
  }

  /**
   * I/O management to parse and perform "take" action
   *
   * @param argument
   */
  private static Result handleTakeAction(String argument) {
    argument = argument.toLowerCase();

    // Get response from Game Engine
    Result res = IO.engine.takeItem(argument);
    System.out.println(res.getMessage());
    IO.prompt();

    return res;
  }

  /**
   * I/O management to parse and perform "drop" action
   *
   * @param argument
   */
  private static Result handleDropAction(String argument) {
    argument = argument.toLowerCase();

    // Get response from Game Engine
    Result res = IO.engine.dropItem(argument);
    // We only need to print response message if failure
    if (res.getState() == State.FAILURE) {
      System.out.println(res.getMessage());
    }

    IO.prompt();
    return res;
  }

  /**
   * I/O management to parse and perform "money" action
   *
   * @param argument
   * @return
   */
  private static Result handleMoneyAction(String argument) {
    argument = argument.toLowerCase();

    // Get response from Game Engine
    Result res = IO.engine.getMoney();
    System.out.println(res.getMessage());

    IO.prompt();
    return res;
  }

  /**
   * I/O management to parse and perform "go" action
   *
   * @param argument
   */
  private static Result handleGoAction(String argument) {
    argument = argument.toLowerCase();

    // Get response from Game Engine
    Result res = IO.engine.changeDirection(argument);
    if (res.getState() == State.FAILURE) {
      System.out.println(res.getMessage());

    } else if (res.getState() == State.VICTORY) {
      IO.handleVictory(res.getMessage());
      return res;

    } else {
      // Examine the new room
      IO.examine();
    }

    IO.prompt();
    return res;
  }

  /**
   * I/O management to parse and perform "inspect" action
   *
   * @param argument
   */
  private static Result handleInspectAction(String argument) {
    argument = argument.toLowerCase();

    // Get response from Game Engine
    Result res = IO.engine.inspectItem(argument);
    System.out.println(res.getMessage());

    IO.prompt();
    return res;
  }

  /**
   * I/O management to parse and perform "sell" action
   *
   * @param argument
   */
  private static Result handleSellAction(String argument) {
    argument = argument.toLowerCase();

    // Get response from Game Engine
    Result res = IO.engine.sellItem(argument);
    System.out.println(res.getMessage());

    IO.prompt();
    return res;
  }

  /**
   * I/O management to parse and perform "buy" action
   *
   * @param argument
   */
  private static Result handleBuyAction(String argument) {
    argument = argument.toLowerCase();

    // Get response from Game Engine
    Result res = IO.engine.buyItem(argument);
    System.out.println(res.getMessage());

    IO.prompt();
    return res;
  }

}
