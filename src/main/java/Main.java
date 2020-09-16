import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import student.adventure.*;

public class Main {

    public static void main(String[] args) throws IOException {
        // TODO: Run an Adventure game on the console
        Path path = Paths.get("src/main/java/data/data.json");
        IO manager = new IO(path);
        manager.start();
    }
}