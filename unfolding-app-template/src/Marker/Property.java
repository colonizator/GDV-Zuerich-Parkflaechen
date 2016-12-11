package Marker;

public enum Property {

	AREA("area");
	
	private String key;
	
	private Property(String key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		return this.key;
	}
	
}
