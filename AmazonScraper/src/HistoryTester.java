import java.util.ArrayList;
import java.util.List;

public class HistoryTester {

	public static void main(String[] args) {

		List<Integer> percentClaimedHistory = new ArrayList<Integer>();
		percentClaimedHistory.add(0);
		percentClaimedHistory.add(1);
		percentClaimedHistory.add(2);
		percentClaimedHistory.add(3);
		AmazonUtility.log((String.valueOf(AmazonUtility.getScoreSeriesByMinMaxAndDiff(percentClaimedHistory))));

		List<Integer> percentClaimedHistory1 = new ArrayList<Integer>();
		percentClaimedHistory1.add(0);
		percentClaimedHistory1.add(2);
		percentClaimedHistory1.add(4);
		percentClaimedHistory1.add(6);
		AmazonUtility.log((String.valueOf(AmazonUtility.getScoreSeriesByMinMaxAndDiff(percentClaimedHistory))));

	}

}
