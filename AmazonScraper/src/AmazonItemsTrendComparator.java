import java.util.Comparator;

public class AmazonItemsTrendComparator implements Comparator<AmazonItem> {

	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}

	public int compare(AmazonItem a1, AmazonItem a2) {
		int comparison = 0;
		if (a1.getHighestScore() > a2.getHighestScore()) {
			comparison = 1;
		} else if (a2.getHighestScore() > a1.getHighestScore()) {
			comparison = -1;
		}
		return comparison;
	}

}
