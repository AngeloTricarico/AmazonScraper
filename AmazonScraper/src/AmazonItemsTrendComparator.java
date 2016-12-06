import java.util.Comparator;
import java.util.List;

public class AmazonItemsTrendComparator implements Comparator<AmazonItem> {

	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}

	public int compare(AmazonItem a1, AmazonItem a2) {
		int comparison = 0;
		List<Integer> listA1 = a1.getPercentClaimedHistory();
		List<Integer> listA2 = a2.getPercentClaimedHistory();
		if (getScoreSeries(listA1) > getScoreSeries(listA2)) {
			comparison = 1;
		} else if (getScoreSeries(listA2) > getScoreSeries(listA1)) {
			comparison = -1;
		}
		return comparison;
	}

	public double getScoreSeries(List<Integer> percentClaimedHistory) {
		double[] percentClaimedHistoryArray = new double[percentClaimedHistory.size()];
		for (int i = 0; i < percentClaimedHistory.size(); i++) {
			percentClaimedHistoryArray[i] = percentClaimedHistory.get(i).doubleValue();
		}
		return new Statistics(percentClaimedHistoryArray).getStdDev();
	}

}
