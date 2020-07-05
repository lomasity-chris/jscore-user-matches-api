package com.lomasity.jscore;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UpdateScoreHandlerTest {

    private Map<String, Object> input;
    private UpdateScoreHandler handler;
    private Context ctx;

    @Before
    public void createInput() throws IOException {

        handler = new UpdateScoreHandler();
        ctx = new TestContext();
        ((TestContext) ctx).setFunctionName("BadmintonMatch");

        input = new HashMap<>();
        input.put("body", "{\"match\": {\"games\": [{ \"currentScore\": [0,0], \"pointsPlayed\": 0, \"pointsHistory\": 0 }], \"maxGames\": 1, \"pointsTarget\": 21, \"players\": [\"A\", \"B\"], \"finished\": false, \"setting\": false}}");
    }

    @Test
    public void testInputsAreCopiedToOutputs() throws IOException {

        Map<String, Object> qsp = new HashMap<>();
        qsp.put("pointWonBy","home");
        input.put("queryStringParameters", qsp);

        ApiGatewayResponse response = handler.handleRequest(input, ctx);

        String json = response.getBody();

        JsonNode outputJSONNode = new ObjectMapper().readTree(json);

        assertThat(outputJSONNode.get("pointsTarget").asInt(), equalTo(21));
        assertThat(outputJSONNode.get("setting").asBoolean(), equalTo(false));
        assertThat(outputJSONNode.get("maxGames").asInt(), equalTo(1));
    }

    @Test
    public void testHomePointWon() throws IOException {

        Map<String, Object> qsp = new HashMap<>();
        qsp.put("pointWonBy","home");
        input.put("queryStringParameters", qsp);

        ApiGatewayResponse response = handler.handleRequest(input, ctx);

        String json = response.getBody();

        final JsonNode games = new ObjectMapper().readTree(json).get("games");
        for (final JsonNode game : games) {
            System.out.println(game);
            assertThat(game.get("pointsHistory").asInt(), equalTo(0));
            assertThat(game.get("pointsPlayed").asInt(), equalTo(1));

            List<Integer> scores = new ObjectMapper().convertValue(game.get("currentScore"), ArrayList.class);
            assertThat(scores.get(0), equalTo(1));
            assertThat(scores.get(1), equalTo(0));

        }
    }

    @Test
    public void testAwayPointWon() throws IOException {

        Map<String, Object> qsp = new HashMap<>();
        qsp.put("pointWonBy","away");
        input.put("queryStringParameters", qsp);

        ApiGatewayResponse response = handler.handleRequest(input, ctx);

        String json = response.getBody();

        final JsonNode games = new ObjectMapper().readTree(json).get("games");
        for (final JsonNode game : games) {
            System.out.println(game);
            assertThat(game.get("pointsHistory").asInt(), equalTo(1));
            assertThat(game.get("pointsPlayed").asInt(), equalTo(1));

            List<Integer> scores = new ObjectMapper().convertValue(game.get("currentScore"), ArrayList.class);
            assertThat(scores.get(0), equalTo(0));
            assertThat(scores.get(1), equalTo(1));
        }
    }

    @Test
    public void testFinished() throws IOException {

        input.put("body", "{\"match\": {\"games\": [{ \"currentScore\": [0,0], \"pointsPlayed\": 0, \"pointsHistory\": 0 }], \"maxGames\": 1, \"pointsTarget\": 11, \"players\": [\"A\", \"B\"], \"finished\": false, \"setting\": false}}");
        Map<String, Object> qsp = new HashMap<>();
        qsp.put("pointWonBy","home");
        input.put("queryStringParameters", qsp);

        for (int i=0; i<=9; i++) {
            ApiGatewayResponse response = handler.handleRequest(input, ctx);
            assertThat(new ObjectMapper().readTree(response.getBody()).get("finished").asBoolean(), equalTo(false));
            input.put("body","{\"match\":" + response.getBody() + "}");
        }

        ApiGatewayResponse response = handler.handleRequest(input, ctx);
        assertThat(new ObjectMapper().readTree(response.getBody()).get("finished").asBoolean(), equalTo(true));
    }

    @Test
    public void testUndo() throws IOException {

        Map<String, Object> qsp = new HashMap<>();
        qsp.put("pointWonBy","undo");
        input.put("queryStringParameters", qsp);
        input.put("body", "{\"match\": {\"games\": [{ \"currentScore\": [0,1], \"pointsPlayed\": 1, \"pointsHistory\": 1 }], \"maxGames\": 1, \"pointsTarget\": 21, \"players\": [\"A\", \"B\"], \"finished\": false, \"setting\": false}}");

        ApiGatewayResponse response = handler.handleRequest(input, ctx);

        String json = response.getBody();

        final JsonNode games = new ObjectMapper().readTree(json).get("games");
        for (final JsonNode game : games) {
            System.out.println(game);
            assertThat(game.get("pointsHistory").asInt(), equalTo(0));
            assertThat(game.get("pointsPlayed").asInt(), equalTo(0));

            List<Integer> scores = new ObjectMapper().convertValue(game.get("currentScore"), ArrayList.class);
            assertThat(scores.get(0), equalTo(0));
            assertThat(scores.get(1), equalTo(0));
        }
    }

}
