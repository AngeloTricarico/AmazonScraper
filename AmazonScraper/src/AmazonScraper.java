import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AmazonScraper {
	public static void main(String[] args) throws IOException, InterruptedException {

		final int PAGES_TO_PARSE = 10;

		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);

		List<AmazonItem> amazonItemList = new ArrayList<AmazonItem>();

		int pageNumber = 1;

		do {
			long start = System.currentTimeMillis();

			AmazonUtility.logNoNewLine("" + pageNumber + "... ");

			String url = "https://www.amazon.it/gp/goldbox/ref=gbps_ftr_s-6_e3a8_page_" + pageNumber
					+ "?ie=UTF8&gb_f_LD=dealStates:AVAILABLE%252CWAITLIST%252CWAITLISTFULL%252CUPCOMING,dealTypes:LIGHTNING_DEAL,sortOrder:BY_SCORE&pf_rd_m=A11IL2PNWYJU7H&gb_f_GB-SUPPLE=page:"
					+ pageNumber + ",sortOrder:BY_SCORE,dealsPerPage:20";

			WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED, SensitiveData.proxy1, SensitiveData.port1);
			webClient.getOptions().setCssEnabled(false);
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
				ai.setPercentClaimed(AmazonUtility.getPercentClaimed(item));
				ai.setUrl(AmazonUtility.getUrl(item));
				ai.setTitle(AmazonUtility.getTitle(item));
				ai.setPrice(AmazonUtility.getPrice(item));
				ai.setId(AmazonUtility.getId(ai));

				if (ai.getUrl() != null) {
					amazonItemList.add(ai);
				}
			}

			pageNumber++;

			long end = System.currentTimeMillis();
			AmazonUtility.logNoNewLine("[" + (end - start) + " ms] ");

		} while (pageNumber < (PAGES_TO_PARSE + 1));

		AmazonUtility.log("\n");

		Collections.sort(amazonItemList, new AmazonItemsComparator());
		Collections.reverse(amazonItemList);

		for (AmazonItem ai : amazonItemList) {
			AmazonUtility.log(ai.toString());
		}

	}

}