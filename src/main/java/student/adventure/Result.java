package student.adventure;

enum State {
  SUCCESS,
  FAILURE,
  VICTORY
}

public class Result {
  private String message;
  private State state;

  Result(String message, State state) {
    this.message = message;
    this.state = state;
  }

  public String getMessage() {
    return message;
  }

  public State getState() {
    return state;
  }
}
