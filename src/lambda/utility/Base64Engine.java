package lambda.utility;

import java.util.Base64;

public class Base64Engine {

  public String processRequest(String input, String action) {
    String out = null;
    switch(action) {
      case "encode":
        out = this.encode(input);
        break;
      case "decode":
        out = this.decode(input);
        break;
    }
    return out;
  }

  public String encode(String input) {
    return Base64.getEncoder().encodeToString(input.getBytes());
  }

  public String decode(String input) {
    return new String(Base64.getDecoder().decode(input));
  }

}
