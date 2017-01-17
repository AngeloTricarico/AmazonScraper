package com.angelotricarico.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import org.json.JSONArray;
import org.json.JSONException;

import com.angelotricarico.bean.AmazonItem;
import com.angelotricarico.constants.Constants;
import com.angelotricarico.scraper.AmazonScraper;

public class SettingsPreference {

	static Preferences prefs = Preferences.userNodeForPackage(com.angelotricarico.utils.SettingsPreference.class);

	final static String PREF_NATION_LANG = "settings.nationlang";
	final static String PREF_EMAIL_ALERT = "settings.emailaddress";
	final static String PREF_HIGHEST_SCORE_EVER = "data.highestscoreever";
	final static String PREF_LIST_OF_ALREADY_SENT_ITEMS = "data.listofalreadysentitems";

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

	public static void saveEmailAddressAlert(String emailAddress) {
		prefs.put(PREF_EMAIL_ALERT, emailAddress);
	}

	public static String loadEmailAddressAlert() {
		return prefs.get(PREF_EMAIL_ALERT, Constants.DEFAULT_MAIL);
	}

	public static void saveListOfSentProducts(List<String> sentProducts) {
		prefs.put(PREF_LIST_OF_ALREADY_SENT_ITEMS, new JSONArray(sentProducts).toString());
	}

	public static List<String> loadListOfSentProducts() {
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(prefs.get(PREF_LIST_OF_ALREADY_SENT_ITEMS, new JSONArray(new ArrayList<AmazonItem>()).toString()));
		} catch (JSONException e) {
			AmazonUtility.log(e.toString());
		}
		List<String> idList = new ArrayList<String>();
		if (jsonArray != null) {
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					idList.add(jsonArray.getString(i));
				} catch (JSONException e) {
					AmazonUtility.log(e.toString());
				}
			}
		}
		return idList;
	}

}
