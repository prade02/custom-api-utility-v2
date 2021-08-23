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
  private final String BODY_IS_EMPTY = "BODY IS NULL";
  private final String RESOURCE_ATTRIBUTE_IS_EMPTY = "RESOURCE ATTRIBUTE IS NULL";
  private final String MESSAGE_ATTRIBUTE_IS_EMPTY = "MESSAGE ATTRIBUTE IS NULL";
  private final String ACTION_ATTRIBURE_IS_EMPTY = "ACTION ATTRIBUTE IS NULL";
  private final String PARSE_EXCEPTION = "PARSE EXCEPTION";
  private final String EXCEPTION = "EXCEPTION";
  private final String HANDLER_COMPLETED = "HANDLER COMPLETED";

  private String awsRequestID;
  private LambdaLogger logger;

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
    awsRequestID = context.getAwsRequestId();
    logger = context.getLogger();
    BufferedReader bufferedReader = null;
    OutputStreamWriter writer = null;
    String handlerResponse = null;
    try {
      JSONParser parser = new JSONParser();
      bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      JSONObject event = (JSONObject) parser.parse(bufferedReader);

      if (event.get("body") != null) {
        String sBody = (String) event.get("body");
        this.logMessage("Body: " + sBody);
        JSONObject body = (JSONObject) parser.parse(sBody);
        String resource = (String) body.get("resource");
        String action = (String) body.get("action");
        String message = (String) body.get("message");
        this.logMessage("Resource: " + resource + " Action: " + action + " Message: " + message);
        if (resource != null) {
          if (resource.equals(BASE64))
            handlerResponse = this.handleBase64Request(message, action);
        } else
          handlerResponse = processOutput(400, RESOURCE_ATTRIBUTE_IS_EMPTY);
      } else
        handlerResponse = processOutput(400, BODY_IS_EMPTY);
    } catch (ParseException parseException) {
      this.logMessage(PARSE_EXCEPTION);
      handlerResponse = processOutput(503, parseException.getMessage());
    } catch (Exception exception) {
      this.logMessage(EXCEPTION);
      handlerResponse = processOutput(503, exception.getMessage());
    }
    writer = new OutputStreamWriter(outputStream);
    writer.write(handlerResponse);
    if (bufferedReader != null)
      bufferedReader.close();
    if (writer != null)
      writer.close();
    this.logMessage(HANDLER_COMPLETED);
  }

  public String handleBase64Request(String message, String action) {
    String output = null;
    if(message == null)
      output = this.processOutput(400, MESSAGE_ATTRIBUTE_IS_EMPTY);
    else if(action == null)
      output = this.processOutput(400, ACTION_ATTRIBURE_IS_EMPTY);
    else
      output = this.processOutput(200, (new Base64Engine()).processRequest(message, action));
    return output;
  }

  public void logMessage(String message) {
    this.logger.log(awsRequestID + "^" + message);
  }

  public String processOutput(int statusCode, String message) {
    this.logMessage("StatusCode: " + statusCode + " Message: " + message);
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
