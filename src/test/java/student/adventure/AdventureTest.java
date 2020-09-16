package student.adventure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

public class AdventureTest {

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

    @Test
    public void validateTakeAction() throws IOException {
        Path path = Paths.get("src/main/java/data/data.json");
        IO manager = new IO(path);
        manager.start("Ikenberry Commons");

        GameEngine engine = manager.getEngine();
        Result res = engine.takeItem("Dining Hall Key");
        assertEquals(1, engine.getPlayer().getInventory().getInventorySet().size());
    }

    @Test
    public void validateDropAction() throws IOException {
        Path path = Paths.get("src/main/java/data/data.json");
        IO manager = new IO(path);
        manager.start("Ikenberry Commons");

        GameEngine engine = manager.getEngine();
        engine.takeItem("Dining Hall Key");
        engine.dropItem("Dining Hall Key");
        assertEquals(0, engine.getPlayer().getInventory().getInventorySet().size());
    }

    @Test
    public void validateCaseSensitivity() throws IOException {
        Path path = Paths.get("src/main/java/data/data.json");
        IO manager = new IO(path);
        manager.start("Ikenberry Commons");

        GameEngine engine = manager.getEngine();
        engine.takeItem("diNINg hAll keY");
        assertEquals(1, engine.getPlayer().getInventory().getInventorySet().size());
    }

    @Test
    public void validateBuyAction() throws IOException {
        Path path = Paths.get("src/main/java/data/data.json");
        IO manager = new IO(path);
        manager.start("Vending Machine");

        GameEngine engine = manager.getEngine();
        engine.getPlayer().addMoney(100F);
        engine.buyItem("Blood-Stained Wassaja iCard");
        assertEquals(0.0, engine.getPlayer().getMoney().doubleValue(), 0.01);
    }

    @Test
    public void validateSellAction() throws IOException {
        Path path = Paths.get("src/main/java/data/data.json");
        IO manager = new IO(path);
        manager.start("Vending Machine");

        GameEngine engine = manager.getEngine();
        engine.getPlayer().addMoney(110F);
        engine.buyItem("Blood-Stained Wassaja iCard");
        engine.buyItem("Blood-Stained Wassaja iCard");
        assertEquals(10.0, engine.getPlayer().getMoney().doubleValue(), 0.01);
    }

    @Test
    public void validateGoAction() throws IOException {
        Path path = Paths.get("src/main/java/data/data.json");
        IO manager = new IO(path);
        manager.start("Ikenberry Commons");

        GameEngine engine = manager.getEngine();
        engine.changeDirection("East");
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
        engine.takeItem("Dining Hall Key");
        // go to store
        engine.changeDirection("east");
        engine.changeDirection("east");

        Result res = engine.dropItem("dining hall key");
        assertEquals(State.FAILURE, res.getState());
    }
}