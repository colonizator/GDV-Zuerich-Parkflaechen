package marker;

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
	
	public ParkingMarker() {
		super();
	}

	public ParkingMarker(Location location) {
		super(location);
	}
	
	@Override
	public void draw(PGraphics pg, float x, float y) {
		if(!this.hidden) {
			pg.noStroke();
			String type = (String) this.properties.get("ART");
			if(this.showIcon) {
				if (type.equals("Blaue_PP")) {
					pg.image(this.blueParkIcon, x, y);
				} else if(type.equals("Weisse_PP")) {
					pg.image(this.whiteParkIcon, x, y);
				} else if(type.equals("Behindert")) {
					pg.image(this.disabledIcon, x, y);
				} else if(type.equals("Gueterumschlag")) {
					pg.image(this.gueterUmschlagIcon, x, y);
				} else if(type.equals("Car")) {
					pg.image(this.carIcon, x, y);
				} else if(type.equals("Elektro")) {
					pg.image(this.electroIcon, x, y);
				} else if(type.equals("Frauen")) {
					pg.image(this.femaleIcon, x, y);
				} else if(type.equals("Gueter oder Taxi")) {
					pg.image(this.gueterOrTaxiIcon, x, y);
				} else if(type.equals("Taxi")) {
					pg.image(this.taxiIcon, x, y);
				}
			} else {
				if (type.equals("Blaue_PP")) {
					pg.fill(0,160,227);
				} else if(type.equals("Weisse_PP")) {
					pg.fill(23,174,148);
				} else if(type.equals("Behindert")) {
					pg.fill(47,60,132);
				} else if(type.equals("Gueterumschlag")) {
					pg.fill(102,157,210);
				} else if(type.equals("Car")) {
					pg.fill(167,158,205);
				} else if(type.equals("Elektro")) {
					pg.fill(0,160,227);
				} else if(type.equals("Frauen")) {
					pg.fill(235,134,181);
				} else if(type.equals("Gueter oder Taxi")) {
					pg.fill(142,0,193);
				} else if(type.equals("Taxi")) {
					pg.fill(254,191,12);
				}
				pg.triangle(x-2, y+2, x+2, y+2, x, y-2);
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
}
