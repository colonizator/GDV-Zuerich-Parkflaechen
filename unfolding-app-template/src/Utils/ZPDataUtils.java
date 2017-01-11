package Utils;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import marker.DistrictMarker;
import marker.ParkingMarker;
import marker.Property;
import processing.core.PApplet;

public class ZPDataUtils {

	public static List<Marker> loadDistricts(PApplet pApplet, int color, 
			int selectedColor) {
		List<Marker> districtMarkers = new ArrayList<Marker>();
		
		List<Feature> features = GeoJSONReader.loadData(pApplet, 
				"zurich_bezirke_nopoints.geojson");
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPolygonClass(DistrictMarker.class);
		List<Marker> districts = markerFactory.createMarkers(features);
		
		for (Marker marker : districts) {
			if ("10".equals(marker.getStringProperty("admin_level"))) {
				districtMarkers.add(marker);
				marker.setColor(color);
				marker.setHighlightColor(selectedColor);
			}
		}
		return districtMarkers;
	}

	public static List<Marker> loadParks(PApplet pApplet, int color, 
			int selectedColor) {
		List<Marker> parkMarkers = loadMarkers(pApplet, "zurich_parks.geojson", 
				color, selectedColor);
		return parkMarkers;
	}
	
	public static List<Marker> loadGrass(PApplet pApplet, int color, 
			int selectedColor) {
		List<Marker> grassMarkers = loadMarkers(pApplet, "zurich_grasflaechen.geojson", 
				color, selectedColor);
		return grassMarkers;
	}
	
	public static List<Marker> loadForest(PApplet pApplet, int color, 
			int selectedColor) {
		List<Marker> forestMarkers = loadMarkers(pApplet, "zurich_waldflaechen.geojson", 
				color, selectedColor);
		return forestMarkers;
	}

	public static List<Marker> loadParking(PApplet pApplet) {
		List<Feature> features = GeoJSONReader.loadData(pApplet, 
				"zurich_parkplaetze.geojson");
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPointClass(ParkingMarker.class);
		List<Marker> parkingMarkers = markerFactory.createMarkers(features);
		for(Marker marker : parkingMarkers) {
			marker.setProperty(Property.AREA.toString(), (2.3f*5)/10); // 10 ist ein Platzhalter f�r den Ma�stab
		}
		return parkingMarkers;
	}
	
	private static List<Marker> loadMarkers(PApplet pApplet, String fileName, 
			int color, int highlight) {
		List<Feature> data = GeoJSONReader.loadData(pApplet, fileName);
		List<Marker> markers = MapUtils.createSimpleMarkers(data);
		for (Marker marker : markers) {
			marker.setColor(color);
			marker.setHighlightColor(highlight);
			if (marker instanceof SimplePolygonMarker) {
				SimplePolygonMarker poly = (SimplePolygonMarker) marker;
				marker.setProperty(Property.AREA.toString(), Math.abs(GeoUtils.getArea(poly)));
			}
		}
		return markers;
	}
	
}