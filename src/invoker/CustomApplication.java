package invoker;

import lambda.utility.Base64Engine;
import com.google.gson.JsonParser;

public class CustomApplication {
  public static void main(String[] args) {
    JsonParser parser;
    System.out.println("Do Test...");

    Base64Engine base64Engine = new Base64Engine();
    // System.out.println(base64Engine.encode("test message"));
    System.out.println(base64Engine.decode("dGVzdCBtZXNzYWdl"));
  }

}
