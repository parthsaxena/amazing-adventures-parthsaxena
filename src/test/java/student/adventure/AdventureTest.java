package student.adventure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

public class AdventureTest {

  /**
   * Testing Strategy
   *
   * - JSON Sanitization
   *    - Broken JSON (missing NECESSARY fields) --> Exception
   *    - Broken Map (directions that lead to rooms that don't exist) --> Exception
   *    - Empty JSON --> Exception
   *
   * - Test each Action thoroughly
   *    - Movement
   *        - Test invalid direction
   *    - Take / Drop
   *        - Cannot take/drop item in store - Make sure inventory updates

   * - Test CUSTOM FEATURE (store)
   *    - Check buy/sell with no money
   *        - Make sure inventory & money update
   *
   * - Test Room Requirements
   *    - Check that player CANNOT enter room without meeting requirements
   *    - Check that player CAN enter room if meets requirements
   */

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyJSON() throws IOException {
    Path path = Paths.get("src/main/java/data/empty.json");
    IO manager = new IO(path);
    manager.start("Ikenberry Commons");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBrokenMap() throws IOException {
    // This map has invalid directions to rooms that don't exist
    Path path = Paths.get("src/main/java/data/broken_map.json");
    IO manager = new IO(path);
    manager.start("Ikenberry Commons");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMapMissingFields() throws IOException {
    // This map is missing required fields
    Path path = Paths.get("src/main/java/data/missing_fields.json");
    IO manager = new IO(path);
    manager.start("Ikenberry Commons");
  }

  @Test
  public void testMissingRoomRequirements() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("The Ike");

    GameEngine engine = manager.getEngine();
    Result res = engine.changeDirection("North");
    assertEquals(State.FAILURE, res.getState());
  }

  @Test
  public void testMeetsRoomRequirements() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Ikenberry Commons");

    GameEngine engine = manager.getEngine();
    engine.takeItem("Dining Hall Key");
    engine.changeDirection("East");
    Result res = engine.changeDirection("North");

    assertEquals(State.SUCCESS, res.getState());
  }

  @Test
  public void validateTakeAction() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Ikenberry Commons");

    GameEngine engine = manager.getEngine();
    Result res = engine.takeItem("Dining Hall Key");

    // Check if item is in player's inventory
    assertTrue(engine.getPlayer().getInventory().hasItem("dining hall key"));
  }

  @Test
  public void invalidMovement() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Ikenberry Commons");

    GameEngine engine = manager.getEngine();
    Result res = engine.changeDirection("South");
    assertEquals(State.FAILURE, res.getState());
  }

  @Test
  public void validateDropAction() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Ikenberry Commons");

    GameEngine engine = manager.getEngine();
    engine.takeItem("Dining Hall Key");
    engine.dropItem("Dining Hall Key");

    // Check if item is NOT in player's inventory
    assertFalse(engine.getPlayer().getInventory().hasItem("dining hall key"));
  }

  @Test
  public void validateCaseSensitivity() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Ikenberry Commons");

    GameEngine engine = manager.getEngine();
    engine.takeItem("diNINg hAll keY");

    // Check if item is in player's inventory
    assertTrue(engine.getPlayer().getInventory().hasItem("dining hall key"));
  }

  @Test
  public void validateBuyAction() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Vending Machine");

    GameEngine engine = manager.getEngine();
    engine.getPlayer().addMoney(100F);
    engine.buyItem("Blood-Stained Wassaja iCard");

    // Check if item is in player's inventory
    assertTrue(engine.getPlayer().getInventory().hasItem("blood-stained wassaja icard"));
  }

  @Test
  public void validateSellAction() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Vending Machine");

    GameEngine engine = manager.getEngine();
    engine.getPlayer().addMoney(110F);
    engine.buyItem("Blood-Stained Wassaja iCard");
    engine.sellItem("Blood-stained wassaja icard");

    // Check if item is NOT in player's inventory
    assertFalse(engine.getPlayer().getInventory().hasItem("blood-stained wassaja icard"));
  }

  @Test
  public void validateSellInventoryManagement() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Ikenberry Commons");

    GameEngine engine = manager.getEngine();
    engine.takeItem("diNINg hAll keY");
    engine.teleport("Vending Machine");
    engine.sellItem("dining hall key");

    // Check that no items are in player's inventory
    assertEquals(0, engine.getPlayer().getInventory().getInventorySet().size());
  }

  @Test
  public void validateBuyInventoryManagement() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Vending Machine");

    GameEngine engine = manager.getEngine();
    engine.getPlayer().addMoney(100F);
    engine.buyItem("Blood-Stained Wassaja iCard");

    // Check that there is exactly one item in player's inventory
    assertEquals(1, engine.getPlayer().getInventory().getInventorySet().size());
  }

  @Test
  public void validateGoAction() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Ikenberry Commons");

    GameEngine engine = manager.getEngine();
    engine.changeDirection("East");

    // Verify name of current room
    assertEquals("The Ike", engine.getCurrentRoom().getName());
  }

  @Test
  public void takeItemInStore() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Vending Machine");

    GameEngine engine = manager.getEngine();
    Result res = engine.takeItem("Blood-Stained Wassaja iCard");

    assertEquals(State.FAILURE, res.getState());
  }

  @Test
  public void dropItemInStore() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Ikenberry Commons");

    GameEngine engine = manager.getEngine();
    // Pick up an item
    engine.takeItem("Dining Hall Key");
    engine.teleport("Vending Machine");

    Result res = engine.dropItem("dining hall key");

    assertEquals(State.FAILURE, res.getState());
  }

  @Test
  public void testVictory() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Vending Machine");

    GameEngine engine = manager.getEngine();

    // Get item necessary for win
    engine.getPlayer().addMoney(100F);
    engine.buyItem("blood-stained wassaja icard");

    // Travel to victory room
    engine.changeDirection("west");
    engine.changeDirection("west");
    Result res = engine.changeDirection("west");

    assertEquals(State.VICTORY, res.getState());
  }

  @Test
  public void testBuyNoMoney() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Vending Machine");

    GameEngine engine = manager.getEngine();
    Result res = engine.buyItem("blood-stained wassaja icard");

    assertEquals(State.FAILURE, res.getState());
  }

  @Test
  public void testBuyMoneyUpdate() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Vending Machine");

    GameEngine engine = manager.getEngine();
    engine.getPlayer().addMoney(100F);
    engine.buyItem("blood-stained wassaja icard");

    assertEquals(0.0, engine.getPlayer().getMoney().doubleValue(), 0.01);
  }

  @Test
  public void testSellMoneyUpdate() throws IOException {
    Path path = Paths.get("src/main/java/data/data.json");
    IO manager = new IO(path);
    manager.start("Vending Machine");

    GameEngine engine = manager.getEngine();
    engine.getPlayer().addMoney(100F);
    engine.buyItem("blood-stained wassaja icard");
    engine.sellItem("blood-stained wassaja icard");

    assertEquals(100.0, engine.getPlayer().getMoney().doubleValue(), 0.01);
  }
}