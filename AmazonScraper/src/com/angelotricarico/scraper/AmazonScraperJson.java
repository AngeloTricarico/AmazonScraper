package com.angelotricarico.scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.angelotricarico.SensitiveData;
import com.angelotricarico.bean.AmazonItem;
import com.angelotricarico.comparators.AmazonItemsComparator;
import com.angelotricarico.utils.AmazonUtility;

public class AmazonScraperJson {

	public static void main(String[] args) throws IOException {

		List<AmazonItem> amazonItemList = new ArrayList<AmazonItem>();

		setProxy();

		int page = 20;
		String url = "https://www.amazon.it/gp/goldbox/ref=gbps_ftr_s-6_e3a8_page_" + page
				+ "?ie=UTF8&gb_f_LD=dealStates:AVAILABLE%252CWAITLIST%252CWAITLISTFULL%252CUPCOMING,dealTypes:LIGHTNING_DEAL,sortOrder:BY_SCORE&pf_rd_p=56c2d6ff-d484-4140-9f3e-4e8437c2e3a8&pf_rd_s=slot-6&pf_rd_t=701&pf_rd_i=gb_main&pf_rd_m=A11IL2PNWYJU7H&pf_rd_r=4HEMNJP1WJPDE4GAKWZ7&gb_f_GB-SUPPLE=page:"
				+ page + ",sortOrder:BY_SCORE,dealsPerPage:20";

		Document doc = Jsoup.connect(url).timeout(0).get();

		JSONObject completeJson = fromSourceToJsonObject(doc);

		try {
			JSONObject dcsServerResponse = (JSONObject) completeJson.get("dcsServerResponse");
			JSONObject dealDetails = (JSONObject) dcsServerResponse.get("dealDetails");
			JSONObject dealStatus = (JSONObject) dcsServerResponse.get("dealStatus");

			Iterator<?> keys = dealDetails.keys();

			while (keys.hasNext()) {
				String key = (String) keys.next();
				if (dealDetails.get(key) instanceof JSONObject) {

					AmazonItem ai = new AmazonItem();
					JSONObject singleDetail = (JSONObject) dealDetails.get(key);
					ai.setUrl(getUrl(singleDetail));
					ai.setTitle(getTitle(singleDetail));
					ai.setPrice(getPrice(singleDetail));
					ai.setId(getId(singleDetail));
					ai.setPercentClaimed(getPercentualeRichiesta(dealStatus, ai.getId()));

					amazonItemList.add(ai);
				}
			}

		} catch (JSONException e) {
			AmazonUtility.log("ERROR: " + e);
		}

		Collections.sort(amazonItemList, new AmazonItemsComparator());
		Collections.reverse(amazonItemList);

		for (AmazonItem ai : amazonItemList) {
			System.out.println(ai);
		}

	}

	private static void setProxy() {

		System.setProperty("http.proxyHost", SensitiveData.proxy1);
		System.setProperty("http.proxyPort", "" + SensitiveData.port1);

	}

	private static String getPrice(JSONObject dealDetails) {

		String prezzo = null;
		try {

			String prezzoMin = (String) dealDetails.get("minDealPrice");
			String prezzoMax = (String) dealDetails.get("maxDealPrice");
			if (prezzoMin.equals(prezzoMax)) {
				prezzo = prezzoMin;
			} else {
				prezzo = prezzoMin + "-" + prezzoMax;
			}
		} catch (JSONException e) {
			AmazonUtility.log("ERROR: " + e);
		}
		return prezzo;

	}

	private static String getTitle(JSONObject dealDetails) {

		String title = null;
		try {
			title = (String) dealDetails.get("title");
		} catch (JSONException e) {
			AmazonUtility.log("ERROR: " + e);
		}
		return title;

	}

	private static String getUrl(JSONObject dealDetails) {
		String url = null;
		try {
			url = (String) dealDetails.get("egressUrl");
		} catch (JSONException e) {
			AmazonUtility.log("ERROR: " + e);
		}
		return url;
	}

	private static String getId(JSONObject dealDetails) {
		String url = null;
		try {
			url = (String) dealDetails.get("dealID");
		} catch (JSONException e) {
			AmazonUtility.log("ERROR: " + e);
		}
		return url;
	}

	private static int getPercentualeRichiesta(JSONObject dealStatus, String dealID) {

		int percentClaimed = 0;
		try {
			JSONObject jo = (JSONObject) dealStatus.get(dealID);
			percentClaimed = (int) jo.get("percentClaimed");

		} catch (JSONException e) {
			AmazonUtility.log("ERROR: " + e);
		}
		return percentClaimed;

	}

	private static JSONObject fromSourceToJsonObject(Element doc) {

		JSONObject jsonObject = null;

		Elements itemsInOfferta = doc.select("script[type$=text/javascript]");
		for (Element element : itemsInOfferta) {
			String scriptConJson = element.outerHtml();

			if (scriptConJson.contains("LIGHTNING_DEAL") && scriptConJson.contains("GB-SHOVELER")) {
				scriptConJson = scriptConJson.replaceAll("(\\n|\\r|\\t)", "");
				Matcher m = Pattern.compile("^.*var widgetToRegister = (.+)<\\/script>.*$").matcher(scriptConJson);
				m.find();
				String json = m.group(1);
				try {
					jsonObject = new JSONObject(json);
				} catch (JSONException e) {
					AmazonUtility.log("ERROR: " + e);
				}

			}
		}
		return jsonObject;

	}

}