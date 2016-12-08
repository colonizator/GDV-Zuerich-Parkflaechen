import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.interactions.MouseHandler;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.*;

/**
 * Hello Unfolding World.
 * 
 * Download the distribution with examples for many more examples and features.
 */
public class HelloUnfoldingWorld extends PApplet {

	UnfoldingMap map;

	UnfoldingMap mapDetail;
	UnfoldingMap mapOverview;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		//map = new UnfoldingMap(this, new Microsoft.AerialProvider());
		//map.zoomAndPanTo(10, new Location(52.5f, 13.4f));
		
		Location zurichLocation = new Location (47.3686f,8.5392f);

		map.zoomTo(12);
		map.panTo(zurichLocation);
		
		map.setZoomRange(10, 20);
		float maxPanningDistance = 10; // in km
		map.setPanningRestriction(zurichLocation, maxPanningDistance);
		
		//map.setTweening(true);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		
		//Data
		// Parks
		List<Feature> park =  GeoJSONReader.loadData(this, "zurich_parks.geojson");
		List<Marker> parkMarkers = MapUtils.createSimpleMarkers(park);
		for (Marker marker : parkMarkers) {
			marker.setColor(color(255, 0, 0, 127));
			
			if (marker instanceof SimplePolygonMarker) {
				SimplePolygonMarker poly = (SimplePolygonMarker) marker;
				marker.setProperty("area", GeoUtils.getArea(poly));
			}
			
		}
		
		map.addMarkers(parkMarkers);  
		
	} 

	public void draw() {
		background(0);
		map.draw();

	
	}

	
	public void mouseClicked() {
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		marker.setSelected(true);
		println(marker.getProperty("area"));
	}
	
	public static void main(String[] args) {
		PApplet.main(new String[] { HelloUnfoldingWorld.class.getName() });
	}

}
