package Utils;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import marker.ParkingMarker;
import processing.core.PApplet;
import processing.core.PImage;

public class ZPMarkerUtils {

	public static void setParkingMarkerIcons(PApplet pApplet, List<Marker> markers) {
		PImage blaueParkIcon = pApplet.loadImage("img/Blaue_PP.png");
		PImage taxiIcon = pApplet.loadImage("img/Taxi.png");
		PImage disabledIcon = pApplet.loadImage("img/Behindert.png");
		PImage whiteParkIcon = pApplet.loadImage("img/Weisse_PP.png");
		PImage gueterUmschlagIcon = pApplet.loadImage("img/Gueterumschlag.png");
		PImage carIcon = pApplet.loadImage("img/Car.png");
		PImage electroIcon = pApplet.loadImage("img/Elektro.png");
		PImage femaleIcon = pApplet.loadImage("img/Frauen.png");
		PImage gueterOrTaxiIcon = pApplet.loadImage("img/Gueter_oder_Taxi.png");
		
		for(Marker marker : markers) {
			if(marker instanceof ParkingMarker) {
				ParkingMarker pm = (ParkingMarker) marker;
				pm.setBlueParkIcon(blaueParkIcon);
				pm.setTaxiIcon(taxiIcon);
				pm.setDisabledIcon(disabledIcon);
				pm.setWhiteParkIcon(whiteParkIcon);
				pm.setGueterUmschlagIcon(gueterUmschlagIcon);
				pm.setCarIcon(carIcon);
				pm.setElectroIcon(electroIcon);
				pm.setFemaleIcon(femaleIcon);
				pm.setGueterOrTaxiIcon(gueterOrTaxiIcon);
			}
		}
	}
	
	public static void showParkingIcons(UnfoldingMap map, List<Marker> markers, int zoomLevel) {
		if(map.getZoomLevel() > zoomLevel) {
			showParkingIcons(markers, true);
		} else {
			showParkingIcons(markers, false);
		}
	}
	
	private static void showParkingIcons(List<Marker> markers, boolean show) {
		for(Marker marker : markers) {
			if(marker instanceof ParkingMarker) {
				ParkingMarker pm = (ParkingMarker) marker;
				pm.showIcon(show);
			}
		}
	}
	
	public static boolean markerEquals(Marker marker1, Marker marker2) {
		return equalLocations(marker1.getLocation(), marker2.getLocation());
	}
	
	private static boolean equalLocations(Location locations1, Location locations2) {
		boolean equal = false;
		if (locations1.equals(locations2)) {
			equal = true;
		}
		return equal;
	}
	
}
