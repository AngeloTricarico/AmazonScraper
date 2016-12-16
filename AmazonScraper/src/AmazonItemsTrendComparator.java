import java.util.Comparator;

public class AmazonItemsTrendComparator implements Comparator<AmazonItem> {

	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}

	public int compare(AmazonItem a1, AmazonItem a2) {
		int comparison = 0;
		if (a1.getScore() > a2.getScore()) {
			comparison = 1;
		} else if (a2.getScore() > a1.getScore()) {
			comparison = -1;
		}
		return comparison;
	}

}
