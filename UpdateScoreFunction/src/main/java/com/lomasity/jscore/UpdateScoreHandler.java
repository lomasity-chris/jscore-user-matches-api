package com.lomasity.jscore;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lomasity.jscore.badminton.model.BadmintonMatch;
import com.lomasity.jscore.badminton.model.BadmintonMatchManager;
import com.lomasity.jscore.model.TeamType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UpdateScoreHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(UpdateScoreHandler.class);

    private static final String HOME = "home";
    private static final String AWAY = "away";
    private static final String UNDO = "undo";

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Access-Control-Allow-Origin", "*");
        responseHeaders.put("Access-Control-Allow-Credentials", "true");
        responseHeaders.put("X-Powered-By", "AWS Lambda & serverless");

        try {
            System.out.println("handleRequest: " + input);

            try (InputStream inputStream = getClass().getResourceAsStream("/schemas/badminton-match-schema.json")) {
                JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
                Schema schema = SchemaLoader.load(rawSchema);
                schema.validate(new JSONObject((String) input.get("body")));
            }

            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            JsonNode matchNode = body.get("match");
            Map<String, Object> qsp = (Map<String, Object>) input.get("queryStringParameters");
            String pointWonBy = (String) qsp.get("pointWonBy");

            BadmintonMatch match = new BadmintonMatch(matchNode);

            BadmintonMatchManager manager = new BadmintonMatchManager(match);

            switch (pointWonBy) {
                case HOME:
                    manager.pointWon(TeamType.HOME);
                    break;
                case AWAY:
                    manager.pointWon(TeamType.AWAY);
                    break;
                case UNDO:
                    manager.undoScoreChange();
                    break;
                default:
                    throw new IllegalArgumentException(pointWonBy + " is not a valid team type");
            }

            return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(match)
                    .setHeaders(responseHeaders).build();


        } catch (IllegalAccessException ex) {
            LOG.error("Error in updating score: ", ex);
            Response responseBody = new Response(ex.getMessage() + " - parameters passed: ", input);
            return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody)
                    .setHeaders(responseHeaders).build();
        } catch (Exception ex) {
            LOG.error("Error in updating score: ", ex);
            Response responseBody = new Response("Error whilst updating score: ", input);
            return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody)
                    .setHeaders(responseHeaders).build();
        }
    }


}
