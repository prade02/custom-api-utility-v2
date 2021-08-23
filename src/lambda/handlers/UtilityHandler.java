package lambda.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

import lambda.utility.Base64Engine;

public class UtilityHandler implements RequestStreamHandler {

  private final String BASE64 = "base64";
  private final String ENCODE = "encode";
  private final String DECODE = "decode";

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
    LambdaLogger logger = context.getLogger();
    BufferedReader bufferedReader = null;
    OutputStreamWriter writer = null;
    String handlerResponse = null;
    try {
      JSONParser parser = new JSONParser();
      bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      JSONObject event = (JSONObject) parser.parse(bufferedReader);

      if (event.get("body") != null) {
        String sBody = (String) event.get("body");
        logger.log("Body: " + sBody);
        JSONObject body = (JSONObject) parser.parse(sBody);
        String resource = (String) body.get("resource");
        String action = (String) body.get("action");
        String message = (String) body.get("message");
        logger.log("Resource: " + resource + " Action: " + action + " Message: " + message);
        if (resource != null && action != null) {
          String response = null;
          if (resource.equals(BASE64)) {
            if (action.equals(ENCODE)) {
              response = (new Base64Engine()).encode(message);
            } else if (action.equals(DECODE)) {
              response = (new Base64Engine()).decode(message);
            }
            if (response != null) {
              String out = processOutput(200, response);
              logger.log("Return: " + out);
              handlerResponse = out;
            }
          }
        } else {
          logger.log("resource or action empty");
          handlerResponse = processOutput(400, "resource or action empty");
        }
      } else {
        logger.log("body empty");
        handlerResponse = processOutput(400, "body empty");
      }
    } catch (ParseException parseException) {
      logger.log("parse exception " + parseException.getMessage());
      handlerResponse = processOutput(503, parseException.getMessage());
    } catch (Exception exception) {
      logger.log("Exception occured: " + exception.getMessage());
      handlerResponse = processOutput(503, exception.getMessage());
    }
    writer = new OutputStreamWriter(outputStream);
    writer.write(handlerResponse);
    if (bufferedReader != null)
      bufferedReader.close();
    if (writer != null)
      writer.close();
    logger.log("handler completed");
  }

  public String processOutput(int statusCode, String message) {
    JSONObject response = new JSONObject();

    // set status statusCode
    response.put("statusCode", statusCode);

    // set headers
    JSONObject headers = new JSONObject();
    headers.put("Access-Control-Allow-Headers", "Content-Type");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Methods", "GET");
    response.put("headers", headers);

    // set body
    JSONObject jsonBody = new JSONObject();
    jsonBody.put("result", message);
    response.put("body", jsonBody.toString());

    return response.toString();
  }

}
