package edu.uci.ics.crawler4j.data;

/**
 * This generic class pairs the content of 
 * a page support file (jpg/js/css etc) with
 * its URL to enable saving the file using a
 * naming similar to the original
 * 
 * @author saleemhalipoto
 *
 */
public class SupportFileWithURL <R, S> {
	private R r;
	private S s;
	
	public R getR() {
		return r;
	}
	public void setR(R r) {
		this.r = r;
	}
	public S getS() {
		return s;
	}
	public void setS(S s) {
		this.s = s;
	}
	
}
