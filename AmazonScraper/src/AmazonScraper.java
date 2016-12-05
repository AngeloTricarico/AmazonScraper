import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AmazonScraper {
	public static void main(String[] args) throws IOException, InterruptedException {

		// The higher the longer it will take to execute
		final int PAGES_TO_PARSE_SEARCH_DEAPNESS = 8;
		final int PERCENT_CLAIMED_HISTORY_SIZE = 8; // Min 2
		final int MINUTES_PAUSE_FOR_HISTORY_BUILDING = 1;

		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);

		List<AmazonItem> amazonItemList = new ArrayList<AmazonItem>();

		// History building loop
		for (int i = 0; i < PERCENT_CLAIMED_HISTORY_SIZE; i++) {

			// Page deepness loop
			for (int pageNumber = 1; pageNumber < (PAGES_TO_PARSE_SEARCH_DEAPNESS + 1); pageNumber++) {

				long start = System.currentTimeMillis();

				AmazonUtility.logNoNewLine("" + pageNumber + "... ");

				String url = "https://www.amazon.it/gp/goldbox/ref=gbps_ftr_s-6_e3a8_page_" + pageNumber
						+ "?ie=UTF8&gb_f_LD=dealStates:AVAILABLE%252CWAITLIST%252CWAITLISTFULL%252CUPCOMING,dealTypes:LIGHTNING_DEAL,sortOrder:BY_SCORE&pf_rd_m=A11IL2PNWYJU7H&gb_f_GB-SUPPLE=page:"
						+ pageNumber + ",sortOrder:BY_SCORE,dealsPerPage:20";

				WebClient webClient = AmazonUtility.createWebClient();

				HtmlPage page = webClient.getPage(url);

				Elements discountedItems = new Elements();
				do {

					String pageAsXml = page.asXml();

					Document doc = Jsoup.parse(pageAsXml);

					Elements widgetContent = doc.select("div#widgetContent");
					if (widgetContent != null && !widgetContent.isEmpty()) {
						discountedItems = widgetContent.get(widgetContent.size() - 1).select("div.a-row.dealContainer.dealTile");
					}

				} while (discountedItems.size() < 16);

				webClient.close();

				for (Element item : discountedItems) {

					AmazonItem ai = new AmazonItem();
					ai.setUrl(AmazonUtility.getUrl(item)); // Set before ID
					ai.setId(AmazonUtility.getId(ai));

					// Check if we already fetched this item before
					if (i >= 1) {
						for (AmazonItem amazonItem : amazonItemList) {
							if (ai.getId().equals(amazonItem.getId())) {
								ai = amazonItem;
								break;
							}
						}
					}

					ai.setPercentClaimed(AmazonUtility.getPercentClaimed(item));
					ai.getPercentClaimedHistory().add(ai.getPercentClaimed());
					ai.setTitle(AmazonUtility.getTitle(item));
					ai.setPrice(AmazonUtility.getPrice(item));

					if (ai.getUrl() != null && ai.getId() != null && !ai.getId().equals("")) {
						boolean found = false;
						for (AmazonItem amz : amazonItemList) {
							if (amz.getId().equals(ai.getId())) {
								found = true;
							}
						}
						if (!found) {
							amazonItemList.add(ai);
						}
					}
				}

				long end = System.currentTimeMillis();
				AmazonUtility.logNoNewLine("[" + (end - start) + " ms] ");

			}

			AmazonUtility.log(" - Waiting for " + MINUTES_PAUSE_FOR_HISTORY_BUILDING + " minutes before next fetch...\n");

			if (i < (PERCENT_CLAIMED_HISTORY_SIZE - 1)) {
				Thread.sleep(MINUTES_PAUSE_FOR_HISTORY_BUILDING * 60 * 1000);
			}

		}

		AmazonUtility.log("\n");

		Collections.sort(amazonItemList, new AmazonItemsTrendComparator());
		Collections.reverse(amazonItemList);

		for (AmazonItem ai : amazonItemList) {
			AmazonUtility.log(ai.toString());
		}

	}

}