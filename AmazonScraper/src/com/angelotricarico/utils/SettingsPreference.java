package com.angelotricarico.utils;

import java.util.prefs.Preferences;

import com.angelotricarico.scraper.AmazonScraper;

public class SettingsPreference {

	static Preferences prefs = Preferences.userNodeForPackage(com.angelotricarico.utils.SettingsPreference.class);

	final static String PREF_NATION_LANG = "settings.nationlang";
	final static String PREF_HIGHEST_SCORE_EVER = "data.highestscoreever";

	public static void saveNation(String nation) {
		prefs.put(PREF_NATION_LANG, nation);
	}

	public static String loadNation() {
		return prefs.get(PREF_NATION_LANG, AmazonScraper.NATION_ARRAY[0]);
	}

	public static void saveHighestScoreEver(double HighestScoreEver) {
		prefs.putDouble(PREF_HIGHEST_SCORE_EVER, HighestScoreEver);
	}

	public static double loadHighestScoreEver() {
		return prefs.getDouble(PREF_HIGHEST_SCORE_EVER, 0);
	}

}
