package Utils;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import marker.DistrictMarker;
import marker.ForestMarker;
import marker.GrassMarker;
import marker.ParkMarker;
import marker.ParkingMarker;
import marker.Property;
import processing.core.PApplet;

public class ZPDataUtils {

	public static List<Marker> loadDistricts(PApplet pApplet, int color, 
			int selectedColor, int strokeWeight) {
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
				marker.setStrokeColor(pApplet.color(51));
				marker.setHighlightStrokeColor(pApplet.color(51));
				if(strokeWeight > -1) {
					marker.setStrokeWeight(strokeWeight);
				}
			}
		}
		return districtMarkers;
	}

	public static List<Marker> loadParks(PApplet pApplet, int color, 
			int selectedColor) {
		List<Feature> features = GeoJSONReader.loadData(pApplet, 
				"zurich_parks.geojson");
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPolygonClass(ParkMarker.class);
		List<Marker> parkMarkers = markerFactory.createMarkers(features);
		for(Marker marker : parkMarkers) {
			marker.setColor(color);
			marker.setHighlightColor(selectedColor);
			if (marker instanceof SimplePolygonMarker) {
				SimplePolygonMarker poly = (SimplePolygonMarker) marker;
				marker.setProperty(Property.AREA.toString(), 
						Math.abs(GeoUtils.getArea(poly) * 100000000));
			}
		}
		return parkMarkers;
	}
	
	public static List<Marker> loadGrass(PApplet pApplet, int color, 
			int selectedColor) {
		List<Feature> features = GeoJSONReader.loadData(pApplet, 
				"zurich_grasflaechen.geojson");
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPolygonClass(GrassMarker.class);
		List<Marker> grassMarkers = markerFactory.createMarkers(features);
		for(Marker marker : grassMarkers) {
			marker.setColor(color);
			marker.setHighlightColor(selectedColor);
			if (marker instanceof SimplePolygonMarker) {
				SimplePolygonMarker poly = (SimplePolygonMarker) marker;
				marker.setProperty(Property.AREA.toString(), 
						Math.abs(GeoUtils.getArea(poly) * 100000000));
			}
		}
		return grassMarkers;
	}
	
	public static List<Marker> loadForest(PApplet pApplet, int color, 
			int selectedColor) {
		List<Feature> features = GeoJSONReader.loadData(pApplet, 
				"zurich_waldflaechen.geojson");
		
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPolygonClass(ForestMarker.class);
		List<Marker> forestMarkers = markerFactory.createMarkers(features);
		for(Marker marker : forestMarkers) {
			marker.setColor(color);
			marker.setHighlightColor(selectedColor);
			if (marker instanceof SimplePolygonMarker) {
				SimplePolygonMarker poly = (SimplePolygonMarker) marker;
				marker.setProperty(Property.AREA.toString(), 
						Math.abs(GeoUtils.getArea(poly) * 100000000));
			}
		}
		return forestMarkers;
	}

	public static List<Marker> loadParking(PApplet pApplet) {
		List<Feature> features = GeoJSONReader.loadData(pApplet, 
				"zurich_parkplaetze.geojson");
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPointClass(ParkingMarker.class);
		List<Marker> parkingMarkers = markerFactory.createMarkers(features);
		for(Marker marker : parkingMarkers) {
			marker.setProperty(Property.AREA.toString(), (2.3f*5)/1); // 10 ist ein Platzhalter für den Maßstab
		}

		return parkingMarkers;
	}
	
}
