package student.adventure;

import student.adventure.Objects.*;

public class Player {
  private Inventory inventory;
  private Float money;

  Player() {
    inventory = new Inventory();
  }

  public Inventory getInventory() {
    return inventory;
  }

  public Float getMoney() {
    return money;
  }
}
