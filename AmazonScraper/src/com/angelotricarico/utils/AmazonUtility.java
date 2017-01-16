package com.angelotricarico.utils;

import java.awt.Color;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.angelotricarico.SensitiveData;
import com.angelotricarico.bean.AmazonItem;
import com.angelotricarico.constants.Constants;
import com.angelotricarico.scraper.AmazonScraper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

public class AmazonUtility {

	public static String getUrl(Element element) {
		String url = null;
		Elements links = element.select("a[href]");
		if (links.size() == 3) {
			if (links != null) {
				url = links.get(1).attr("abs:href");
			}
		}
		return url;
	}

	public static int getPercentClaimed(Element element) {
		int percentuale = 0;
		Elements mancanoRichiesto = element.select("span.a-size-mini.a-color-secondary.inlineBlock.unitLineHeight");
		if (mancanoRichiesto != null) {
			if (mancanoRichiesto.size() > 2) {
				String percent = mancanoRichiesto.first().text();
				Matcher matcher = Pattern.compile("\\w*(\\d+)%\\w*").matcher(percent);
				if (matcher.find()) {
					percentuale = Integer.valueOf(matcher.group(1));
				}
			}
		}
		return percentuale;
	}

	public static String getTitle(Element element) {
		Element dealTitle = element.select("#dealTitle").first();
		return dealTitle == null ? null : dealTitle.text();
	}

	public static String getPrice(Element element) {
		Elements price = element.select("span.a-size-medium.a-color-base.inlineBlock.unitLineHeight");
		if (!price.isEmpty()) {
			price.first();
		}
		return price.text();
	}

	public static String getId(AmazonItem ai) {
		String id = "";
		if (ai.getUrl() != null) {
			Matcher m = Pattern.compile("https:\\/{2}(?:.*\\/){3}(\\w+)\\/.*").matcher(ai.getUrl());
			if (m.find()) {
				id = m.group(1);
			}
		}
		return id;
	}

	public static WebClient createWebClient() {
		WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED, SensitiveData.proxy1, SensitiveData.port1);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		return webClient;
	}

	public static void log(String log) {
		System.out.println(log);
	}

	public static void logNoNewLine(String log) {
		System.out.print(log);
	}

	public static double getScoreSeriesByStdDev(List<Integer> percentClaimedHistory) {
		double[] percentClaimedHistoryArray = new double[percentClaimedHistory.size()];
		for (int i = 0; i < percentClaimedHistory.size(); i++) {
			percentClaimedHistoryArray[i] = percentClaimedHistory.get(i).doubleValue();
		}
		return new Statistics(percentClaimedHistoryArray).getStdDev();
	}

	public static double getScoreSeriesByMinMaxAndDiff(List<Integer> percentClaimedHistory) {
		Integer first = percentClaimedHistory.get(0);
		Integer current = percentClaimedHistory.get(percentClaimedHistory.size() - 1);
		int diff = current.intValue() - first.intValue();
		double score = diff * Math.pow(retrieveMinimumNotZeroDifference(percentClaimedHistory), -1);
		return score;
	}

	private static int retrieveMinimumNotZeroDifference(List<Integer> percentClaimedHistory) {
		// Let's assume the higher difference
		int minimumDifference = 100;
		for (int i = 0; i < percentClaimedHistory.size(); i++) {
			Integer outerPercent = percentClaimedHistory.get(i);
			for (int j = 0; j < percentClaimedHistory.size(); j++) {
				Integer innerPercent = percentClaimedHistory.get(j);
				int currentDifference = Math.abs(outerPercent - innerPercent);
				if (currentDifference != 0 && currentDifference < minimumDifference) {
					minimumDifference = currentDifference;
				}
			}
		}
		return minimumDifference;
	}

	public static Color getColorForScore(double score, double highestScore) {
		// highestScore:128=score:x
		int green = (int) (128 * score / highestScore);
		return new Color(255 - green, 255, 255 - green);
	}

	public static void sendEmailIfNewExcellentProductWasFound(AmazonScraper as) {
		double globalHighestScore = as.getHighestScoreAmongAllProducts();
		if (globalHighestScore >= SettingsPreference.loadHighestScoreEver()) {
			SettingsPreference.saveHighestScoreEver(globalHighestScore);
			if (as.getAmazonItemList().size() == AmazonScraper.MAX_HISTORY_SIZE && as.getAmazonItemList().get(0) != null) {
				SendMail.sendMail(Constants.MAIL_TITLE, AmazonUtility.formatAmazonItemForEmail(as.getAmazonItemList().get(0)));
			}
		}
	}

	public static String formatAmazonItemForEmail(AmazonItem ai) {
		String message = boldString("Highest Score = ") + String.format("%.1f", ai.getHighestScore()) + brNewLine() + boldString("\nPrice = ") + ai.getPrice() + brNewLine()
				+ boldString("\nTitle = ") + ai.getTitle() + brNewLine() + boldString("\nURL = ") + ai.getUrl() + brNewLine() + boldString("\nPercent Claimed = ")
				+ ai.getPercentClaimed();
		return message;
	}

	public static String boldString(String string) {
		return "<b>" + string + "</b>";
	}

	public static String brNewLine() {
		return "<br>";
	}

}
