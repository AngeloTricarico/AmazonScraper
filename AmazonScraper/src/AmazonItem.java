import java.util.ArrayList;
import java.util.List;

public class AmazonItem {

	private double highestScore = -1.0;
	private double score = 0.0;
	private String price;
	private List<Integer> percentClaimedHistory = new ArrayList<Integer>();
	private String title;
	private String url;
	private String id;
	private int percentClaimed;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getPercentClaimed() {
		return percentClaimed;
	}

	public void setPercentClaimed(int percentClaimed) {
		this.percentClaimed = percentClaimed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Integer> getPercentClaimedHistory() {
		return percentClaimedHistory;
	}

	public void setPercentClaimedHistory(List<Integer> percentClaimedHistory) {
		this.percentClaimedHistory = percentClaimedHistory;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getHighestScore() {
		return highestScore;
	}

	public void setHighestScore(double highestScore) {
		this.highestScore = highestScore;
	}

	@Override
	public String toString() {
		return "AmazonItem [highestScore=" + String.format("%.1f", highestScore) + ", score=" + String.format("%.1f", score) + ", price=" + price + ", percentClaimedHistory="
				+ percentClaimedHistory + ", title=" + title + ", url=" + url + ", id=" + id + ", percentClaimed=" + percentClaimed + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + percentClaimed;
		result = prime * result + ((percentClaimedHistory == null) ? 0 : percentClaimedHistory.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AmazonItem other = (AmazonItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
