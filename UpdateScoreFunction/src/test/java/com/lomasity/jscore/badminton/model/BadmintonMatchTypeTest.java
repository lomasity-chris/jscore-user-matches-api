package com.lomasity.jscore.badminton.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.lomasity.jscore.badminton.model.BadmintonMatchType;

public class BadmintonMatchTypeTest {

	@Test
	public void singleGame() {
		assertThat(BadmintonMatchType.SINGLE_GAME.getGamesTarget(), equalTo(1));
	}

	@Test
	public void bestOfThree() {
		assertThat(BadmintonMatchType.BEST_OF_3.getGamesTarget(), equalTo(2));
	}

}
