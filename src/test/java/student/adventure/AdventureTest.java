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

    @Test
    public void validateTakeAction() throws IOException {
        Path path = Paths.get("src/main/java/data.json");
        IO manager = new IO(path);
        manager.start("The Ike");

        GameEngine engine = manager.getEngine();
        Result res = engine.takeItem("Blood-Stained iCard");
        assertEquals(1, engine.getPlayer().getInventory().getInventorySet().size());
    }

    @Test
    public void validateDropAction() throws IOException {
        Path path = Paths.get("src/main/java/data.json");
        IO manager = new IO(path);
        manager.start("The Ike");

        GameEngine engine = manager.getEngine();
        engine.takeItem("Blood-Stained iCard");
        engine.dropItem("Blood-Stained iCard");
        assertEquals(0, engine.getPlayer().getInventory().getInventorySet().size());
    }

    @Test
    public void validateCaseSensitivity() throws IOException {
        Path path = Paths.get("src/main/java/data.json");
        IO manager = new IO(path);
        manager.start("The Ike");

        GameEngine engine = manager.getEngine();
        engine.takeItem("blOOd-sTained iCARd");
        assertEquals(1, engine.getPlayer().getInventory().getInventorySet().size());
    }

    @Test
    public void validateBuyAction() throws IOException {
        Path path = Paths.get("src/main/java/data.json");
        IO manager = new IO(path);
        manager.start("Vending Machine");

        GameEngine engine = manager.getEngine();
        engine.getPlayer().addMoney(100F);
        engine.buyItem("Blood-Stained Wassaja iCard");
        assertEquals(0.0, engine.getPlayer().getMoney().doubleValue(), 0.01);
    }

    @Test
    public void validateSellAction() throws IOException {
        Path path = Paths.get("src/main/java/data.json");
        IO manager = new IO(path);
        manager.start("Vending Machine");

        GameEngine engine = manager.getEngine();
        engine.getPlayer().addMoney(110F);
        engine.buyItem("Blood-Stained Wassaja iCard");
        engine.buyItem("Blood-Stained Wassaja iCard");
        assertEquals(10.0, engine.getPlayer().getMoney().doubleValue(), 0.01);
    }
}