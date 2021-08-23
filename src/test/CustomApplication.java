package test;

import lambda.utility.Base64Engine;

public class CustomApplication {
  public static void main(String[] args) {
    System.out.println("Do Test...");

    Base64Engine base64Engine = new Base64Engine();
    // System.out.println(base64Engine.encode("test message"));
    System.out.println(base64Engine.decode("dGVzdCBtZXNzYWdl"));
  }

}
