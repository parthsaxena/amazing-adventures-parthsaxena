package student.adventure.Objects;

public enum Direction {
  NORTH {
    @Override
    public String getKey() {
      return "North";
    }
  },
  SOUTH {
    @Override
    public String getKey() {
      return "South";
    }
  },
  EAST {
    @Override
    public String getKey() {
      return "East";
    }
  },
  WEST {
    @Override
    public String getKey() {
      return "West";
    }
  };

  public abstract String getKey();
}
