package student.adventure;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import student.adventure.Objects.*;

public class IO {

  private GameEngine engine;
  private final Gson gson;
  private Data data;
  private final Scanner scanner;

  public IO(Path path) throws IOException {
    gson = new Gson();
    scanner = new Scanner(System.in);

    Reader reader = Files.newBufferedReader(path);
    Data data = gson.fromJson(reader, Data.class);
    sanitizeData(data);
  }

  private void sanitizeData(Data data) {
    try {

      checkNull(data);
      this.data = data;

    } catch(Exception e) {
      throw new IllegalArgumentException("Input JSON has invalid schema / data.");
    }
  }

  private void checkNull(Object o) throws Exception {
    if (o == null) {
      throw new Exception();
    }
  }
}