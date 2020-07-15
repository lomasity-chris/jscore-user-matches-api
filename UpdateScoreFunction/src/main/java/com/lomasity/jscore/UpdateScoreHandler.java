package com.lomasity.jscore;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lomasity.jscore.manager.BadmintonManager;
import com.lomasity.jscore.manager.Manager;
import com.lomasity.jscore.model.TeamType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateScoreHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(UpdateScoreHandler.class);

    private static final String INCREMENT_HOME = "home";
    private static final String INCREMENT_AWAY = "away";
    private static final String UNDO = "undo";

    private static final String BADMINTON = "badminton";


    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Access-Control-Allow-Origin", "*");
        responseHeaders.put("Access-Control-Allow-Credentials", "true");
        responseHeaders.put("X-Powered-By", "AWS Lambda & serverless");

        try {
            System.out.println("handleRequest: " + input);

            JsonNode scoring = getScoring(input);
            String action = getAction(input);
            String sport = getSport(input);

            Manager manager = getManager(scoring, sport);

            switch (action) {
                case INCREMENT_HOME:
                    manager.incrementScoreOf(TeamType.HOME);
                    break;
                case INCREMENT_AWAY:
                    manager.incrementScoreOf(TeamType.AWAY);
                    break;
                case UNDO:
                    manager.undoScoreChange();
                    break;
                default:
                    throw new IllegalArgumentException(action + " is not a valid action");
            }

            return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(manager.getScoring())
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

    private Manager getManager(JsonNode scoring, @NotNull String sport) throws IllegalAccessException {
        Manager manager;
        switch (sport) {
            case BADMINTON:
                manager = new BadmintonManager(scoring);
                break;
            default:
                throw new IllegalArgumentException("Unknown sport: " + sport);
        }
        return manager;
    }

    private JsonNode getScoring(@NotNull Map<String, Object> input) throws IOException {
        JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
        return body.get("scoring");
    }

    private String getAction(@NotNull Map<String, Object> input) {
        Map<String, Object> queryStringParameters = (Map<String, Object>) input.get("queryStringParameters");
        return (String) queryStringParameters.get("action");
    }

    private String getSport(Map<String, Object> input) {
        Map<String, Object> queryStringParameters = (Map<String, Object>) input.get("queryStringParameters");
        return ((String) queryStringParameters.get("sport")).toLowerCase();
    }

}
