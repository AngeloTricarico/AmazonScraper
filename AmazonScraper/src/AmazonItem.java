public class AmazonItem {

	String id;
	String title;
	String url;
	String price;
	int percentClaimed;

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

	@Override
	public String toString() {
		return "AmazonItem [percentClaimed=" + percentClaimed + ", price=" + price + ", title=" + title + ", url=" + url + ", id=" + id + "]";
	}

}
