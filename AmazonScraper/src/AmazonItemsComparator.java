import java.util.Comparator;

public class AmazonItemsComparator implements Comparator<AmazonItem> {

	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}

	public int compare(AmazonItem a1, AmazonItem a2) {
		int comparison = 0;
		if (a1.getPercentClaimed() > a2.getPercentClaimed()) {
			comparison = 1;
		} else if (a1.getPercentClaimed() < a2.getPercentClaimed()) {
			comparison = -1;
		}
		return comparison;
	}

}
