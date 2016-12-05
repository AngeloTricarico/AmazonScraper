import java.util.ArrayList;
import java.util.List;

public class AmazonItem {

	String id;
	String title;
	String url;
	String price;
	int percentClaimed;
	List<Integer> percentClaimedHistory = new ArrayList<Integer>();

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

	@Override
	public String toString() {
		return "AmazonItem [id=" + id + ", percentClaimed=" + percentClaimed + ", percentClaimedHistory=" + percentClaimedHistory + ", price=" + price + ", title=" + title
				+ ", url=" + url + "]";
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
