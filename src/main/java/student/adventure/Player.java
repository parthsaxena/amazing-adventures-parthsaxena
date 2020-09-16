package student.adventure;

import student.adventure.Inventory;
import student.adventure.Objects.*;

public class Player {
  private Inventory inventory;
  private Float money;

  Player() {
    inventory = new Inventory();
    money = 0F;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public Float getMoney() {
    return money;
  }

  public void addMoney(Float add) {
    this.money += add;
  }

  public void subtractMoney(Float subtract) {
    this.money -= subtract;
  }
}
