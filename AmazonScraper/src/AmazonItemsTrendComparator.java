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
		if (listA1.get(listA1.size() - 1) - listA1.get(0) > listA2.get(listA2.size() - 1) - listA2.get(0)) {
			comparison = 1;
		} else if (listA1.get(listA1.size() - 1) - listA1.get(0) > listA2.get(listA2.size() - 1) - listA2.get(0)) {
			comparison = -1;
		}
		return comparison;
	}

}
