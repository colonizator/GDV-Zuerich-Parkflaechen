package marker;

import java.util.Arrays;
import java.util.List;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;
import processing.core.PImage;

public class ParkingMarker extends SimplePointMarker {

	private boolean showIcon;
	
	private PImage blueParkIcon;
	private PImage whiteParkIcon;
	private PImage taxiIcon;
	private PImage disabledIcon;
	private PImage gueterUmschlagIcon;
	private PImage carIcon;
	private PImage electroIcon;
	private PImage femaleIcon;
	private PImage gueterOrTaxiIcon;
	
	private List<String> filter;
	
	public ParkingMarker() {
		super();
	}

	public ParkingMarker(Location location) {
		super(location);
		this.filter = Arrays.asList("Blaue_PP", "Weisse_PP", "Behindert",
				"Gueterumschlag", "Car", "Elektro", "Frauen", "Gueter oder Taxi",
				"Taxi");
	}
	
	@Override
	public void draw(PGraphics pg, float x, float y) {
		if(!this.hidden) {
			pg.noStroke();
			String type = (String) this.properties.get("ART");
			if(this.showIcon) {
				if (type.equals("Blaue_PP")) {
					if(this.filter.contains("Blaue_PP"))
						pg.image(this.blueParkIcon, x, y);
				} else if(type.equals("Weisse_PP")) {
					if(this.filter.contains("Weisse_PP"))
						pg.image(this.whiteParkIcon, x, y);
				} else if(type.equals("Behindert")) {
					if(this.filter.contains("Behindert"))
						pg.image(this.disabledIcon, x, y);
				} else if(type.equals("Gueterumschlag")) {
					if(this.filter.contains("Gueterumschlag"))
						pg.image(this.gueterUmschlagIcon, x, y);
				} else if(type.equals("Car")) {
					if(this.filter.contains("Car"))
						pg.image(this.carIcon, x, y);
				} else if(type.equals("Elektro")) {
					if(this.filter.contains("Elektro"))
						pg.image(this.electroIcon, x, y);
				} else if(type.equals("Frauen")) {
					if(this.filter.contains("Frauen"))
						pg.image(this.femaleIcon, x, y);
				} else if(type.equals("Gueter oder Taxi")) {
					if(this.filter.contains("Gueter oder Taxi"))
						pg.image(this.gueterOrTaxiIcon, x, y);
				} else if(type.equals("Taxi")) {
					if(this.filter.contains("Taxi"))
						pg.image(this.taxiIcon, x, y);
				}
			} else {
				if (type.equals("Blaue_PP")) {
					if(this.filter.contains("Blaue_PP")) {
						pg.fill(0,160,227);
						pg.triangle(x-2, y+2, x+2, y+2, x, y-2);
					}
				} else if(type.equals("Weisse_PP")) {
					if(this.filter.contains("Weisse_PP")) {
						pg.fill(23,174,148);
						pg.triangle(x-2, y+2, x+2, y+2, x, y-2);
					}
				} else if(type.equals("Behindert")) {
					if(this.filter.contains("Behindert")) {
						pg.fill(47,60,132);
						pg.triangle(x-2, y+2, x+2, y+2, x, y-2);
					}
				} else if(type.equals("Gueterumschlag")) {
					if(this.filter.contains("Gueterumschlag")) {
						pg.fill(102,157,210);
						pg.triangle(x-2, y+2, x+2, y+2, x, y-2);
					}
				} else if(type.equals("Car")) {
					if(this.filter.contains("Car")) {
						pg.fill(167,158,205);
						pg.triangle(x-2, y+2, x+2, y+2, x, y-2);
					}
				} else if(type.equals("Elektro")) {
					if(this.filter.contains("Elektro")) {
						pg.fill(0,160,227);
						pg.triangle(x-2, y+2, x+2, y+2, x, y-2);
					}
				} else if(type.equals("Frauen")) {
					if(this.filter.contains("Frauen")) {
						pg.fill(235,134,181);
						pg.triangle(x-2, y+2, x+2, y+2, x, y-2);
					}
				} else if(type.equals("Gueter oder Taxi")) {
					if(this.filter.contains("Gueter oder Taxi")) {
						pg.fill(142,0,193);
						pg.triangle(x-2, y+2, x+2, y+2, x, y-2);
					}
				} else if(type.equals("Taxi")) {
					if(this.filter.contains("Taxi")) {
						pg.fill(254,191,12);
						pg.triangle(x-2, y+2, x+2, y+2, x, y-2);
					}
				}
			}
		}
	}
	
	public void showIcon(boolean showIcon) {
		this.showIcon = showIcon;
	}
	
	public void setBlueParkIcon(PImage parkIcon) {
		this.blueParkIcon = parkIcon;
	}
	
	public void setTaxiIcon(PImage taxiIcon) {
		this.taxiIcon = taxiIcon;
	}
	
	public void setDisabledIcon(PImage disabledIcon) {
		this.disabledIcon = disabledIcon;
	}

	public void setShowIcon(boolean showIcon) {
		this.showIcon = showIcon;
	}

	public void setWhiteParkIcon(PImage whiteParkIcon) {
		this.whiteParkIcon = whiteParkIcon;
	}

	public void setGueterUmschlagIcon(PImage gueterUmschlagIcon) {
		this.gueterUmschlagIcon = gueterUmschlagIcon;
	}

	public void setCarIcon(PImage carIcon) {
		this.carIcon = carIcon;
	}

	public void setElectroIcon(PImage electroIcon) {
		this.electroIcon = electroIcon;
	}

	public void setFemaleIcon(PImage femaleIcon) {
		this.femaleIcon = femaleIcon;
	}

	public void setGueterOrTaxiIcon(PImage gueterOrTaxiIcon) {
		this.gueterOrTaxiIcon = gueterOrTaxiIcon;
	}
	
	public void setFilter(List<String> filter) {
		this.filter = filter;
	}
}
