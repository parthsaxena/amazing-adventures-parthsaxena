package student.adventure;

public class Result {
  private String message;
  private boolean successful;

  Result(String message, boolean successful) {
    this.message = message;
    this.successful = successful;
  }

  public String getMessage() {
    return message;
  }

  public boolean isSuccessful() {
    return successful;
  }
}
