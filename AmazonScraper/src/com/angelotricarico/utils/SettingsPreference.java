package com.angelotricarico.utils;

import java.util.prefs.Preferences;

import com.angelotricarico.AmazonScraper;

public class SettingsPreference {

	Preferences prefs = Preferences.userNodeForPackage(com.angelotricarico.utils.SettingsPreference.class);

	final static String PREF_NAME = "settings.nationlang";

	public void saveNation(String nation) {
		prefs.put(PREF_NAME, nation);
	};

	public String loadNation() {
		return prefs.get(PREF_NAME, AmazonScraper.NATION_ARRAY[0]);
	};

}
