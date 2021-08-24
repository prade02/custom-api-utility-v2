package invoker;

import lambda.utility.Base64Engine;
import org.json.simple.parser.JSONParser;

public class CustomApplication {
  public static void main(String[] args) {
    JSONParser parser = new JSONParser();
    System.out.println("Do Test...");

    Base64Engine base64Engine = new Base64Engine();
    // System.out.println(base64Engine.encode("test message"));
    System.out.println(base64Engine.decode("dGVzdCBtZXNzYWdl"));
  }

}
