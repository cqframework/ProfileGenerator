package org.socraticgrid.uml;

public class CardinalityRange implements Cloneable {
	
	private Integer low;
	private Integer high;
	
	public CardinalityRange() {}

	public CardinalityRange(Integer low, Integer high) {
		this();
		this.low = low;
		this.high = high;
	}

	public Integer getLow() {
		return low;
	}

	public void setLow(Integer low) {
		this.low = low;
	}

	public Integer getHigh() {
		return high;
	}

	public void setHigh(Integer high) {
		this.high = high;
	}
	
	public void validate(CardinalityRange range) {
		if(range.getLow() != null && getLow() != null && getLow() > range.getLow()) {
			throw new RuntimeException("Invalid Constraint - Cannot loosen low cardinality bound");
		}
		if(range.getHigh() != null && getHigh() != null && getHigh() != -1) {
			if (range.getHigh() == -1 || getHigh() < range.getHigh()) {
				throw new RuntimeException("Invalid Constraint - Cannot loosen high cardinality bound");
			}
		}
	}
	
	public CardinalityRange clone() {
		CardinalityRange range = new CardinalityRange();
		range.setLow(this.low);
		range.setHigh(this.high);
		return range;
	}
}
