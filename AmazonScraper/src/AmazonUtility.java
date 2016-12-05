import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

		if (mancanoRichiesto.size() > 2) {
			if (mancanoRichiesto != null) {
				String percent = mancanoRichiesto.first().text();

				Matcher matcher = Pattern.compile("\\w+: (\\d+)%").matcher(percent);

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

	public static void log(String log) {
		System.out.println(log);
	}

	public static void logNoNewLine(String log) {
		System.out.print(log);
	}

}
