package com.lomasity.jscore;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UpdateScoreHandlerTest {

    private Map<String, Object> input;
    private UpdateScoreHandler handler;
    private Context ctx;

    @Before
    public void createInput() {

        handler = new UpdateScoreHandler();
        ctx = new TestContext();
        ((TestContext) ctx).setFunctionName("BadmintonMatch");

        input = new HashMap<>();
        input.put("body", "{\"scoring\": { \"finished\": false, \"gamesTarget\": 1, \"pointsTarget\": 21, \"setting\": false, \"games\": [{ \"currentScore\": { \"home\": 0, \"away\": 0}, \"pointsPlayed\": 0, \"pointsHistory\": 0 }]}}");
                          "{\"scoring\": { \"finished\": false,      \"maxGames\":1,\"pointsTarget\": 21, \"setting\": false, \"games\": [{ \"currentScore\": { \"home\": 0, \"away\": 0}, \"pointsHistory\":0, \"pointsPlayed\": 0}],}}
    }

    @Test
    public void testInputsAreCopiedToOutputs() throws IOException {

        Map<String, Object> qsp = new HashMap<>();
        qsp.put("action","home");
        qsp.put("sport","badminton");
        input.put("queryStringParameters", qsp);

        ApiGatewayResponse response = handler.handleRequest(input, ctx);

        String json = response.getBody();

        JsonNode outputJSONNode = new ObjectMapper().readTree(json);

        assertThat(outputJSONNode.get("pointsTarget").asInt(), equalTo(21));
        assertThat(outputJSONNode.get("setting").asBoolean(), equalTo(false));
        assertThat(outputJSONNode.get("gamesTarget").asInt(), equalTo(1));
    }

    @Test
    public void testHomePointWon() throws IOException {

        Map<String, Object> qsp = new HashMap<>();
        qsp.put("action","home");
        qsp.put("sport","badminton");
        input.put("queryStringParameters", qsp);

        ApiGatewayResponse response = handler.handleRequest(input, ctx);

        String json = response.getBody();

        final JsonNode games = new ObjectMapper().readTree(json).get("games");
        for (final JsonNode game : games) {
            System.out.println(game);
            assertThat(game.get("pointsHistory").asInt(), equalTo(0));
            assertThat(game.get("pointsPlayed").asInt(), equalTo(1));

            final JsonNode currentScore = game.get("currentScore");
            System.out.println(currentScore);
            assertThat(currentScore.get("home").asInt(), equalTo(1));
            assertThat(currentScore.get("away").asInt(), equalTo(0));

        }
    }

    @Test
    public void testAwayPointWon() throws IOException {

        Map<String, Object> qsp = new HashMap<>();
        qsp.put("action","away");
        qsp.put("sport","badminton");
        input.put("queryStringParameters", qsp);

        ApiGatewayResponse response = handler.handleRequest(input, ctx);

        String json = response.getBody();

        final JsonNode games = new ObjectMapper().readTree(json).get("games");
        for (final JsonNode game : games) {
            System.out.println(game);
            assertThat(game.get("pointsHistory").asInt(), equalTo(1));
            assertThat(game.get("pointsPlayed").asInt(), equalTo(1));
            JsonNode currentScore = game.get("currentScore");
            assertThat(currentScore.get("home").asInt(), equalTo(0));
            assertThat(currentScore.get("away").asInt(), equalTo(1));
        }
    }

    @Test
    public void testFinished() throws IOException {

        input.put("body", "{\"scoring\": { \"finished\": " + false + ", \"gamesTarget\": 1, \"pointsTarget\": 11, \"setting\": false, \"games\": [{ \"currentScore\": { \"home\": 0, \"away\": 0}, \"pointsPlayed\": 0, \"pointsHistory\": 0 }]}}");
        Map<String, Object> qsp = new HashMap<>();
        qsp.put("action","home");
        qsp.put("sport","badminton");
        input.put("queryStringParameters", qsp);

        for (int i=0; i<=9; i++) {
            ApiGatewayResponse response = handler.handleRequest(input, ctx);
            assertThat(new ObjectMapper().readTree(response.getBody()).get("finished").asBoolean(), equalTo(false));
            input.put("body","{\"scoring\":" + response.getBody() + "}");
        }

        ApiGatewayResponse response = handler.handleRequest(input, ctx);
        assertThat(new ObjectMapper().readTree(response.getBody()).get("finished").asBoolean(), equalTo(true));
    }

    @Test
    public void testUndo() throws IOException {

        Map<String, Object> qsp = new HashMap<>();
        qsp.put("action","undo");
        qsp.put("sport","badminton");
        input.put("queryStringParameters", qsp);
        input.put("body", "{\"scoring\": { \"finished\": false, \"gamesTarget\": 1, \"pointsTarget\": 21, \"setting\": false, \"games\": [{ \"currentScore\": { \"home\": 0, \"away\": 1}, \"pointsPlayed\": 1, \"pointsHistory\": 1 }]}}");

        ApiGatewayResponse response = handler.handleRequest(input, ctx);

        String json = response.getBody();

        final JsonNode games = new ObjectMapper().readTree(json).get("games");
        for (final JsonNode game : games) {
            System.out.println(game);
            assertThat(game.get("pointsHistory").asInt(), equalTo(0));
            assertThat(game.get("pointsPlayed").asInt(), equalTo(0));

            JsonNode currentScore = game.get("currentScore");
            assertThat(currentScore.get("home").asInt(), equalTo(0));
            assertThat(currentScore.get("away").asInt(), equalTo(0));
        }
    }

}
