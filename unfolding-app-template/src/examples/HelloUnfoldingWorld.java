package examples;
import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;

import org.gicentre.utils.stat.BarChart;

import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import marker.Property;

public class HelloUnfoldingWorld extends PApplet {

	private UnfoldingMap map;
	private UnfoldingMap parkMap;
	private UnfoldingMap forestMap;
	private Location zurichLocation;
	private float maxPanningDistance;
	
	private List<Marker> parkMarkers;
	private List<Marker> districtMarkers;
	private List<Marker> forestMarkers;
	
	private BarChart areaBarChart;
	private BarChart parkBarChart;
	private BarChart forestBarChart;

	public static void main(String[] args) {
		PApplet.main(new String[] { HelloUnfoldingWorld.class.getName() });
	}
	
	public void settings() {
		size(1220, 800, P2D);
	}

	public void setup() {
		this.areaBarChart = new BarChart(this);
		this.parkBarChart = new BarChart(this);
		this.forestBarChart = new BarChart(this);
		this.zurichLocation = new Location(47.3686f, 8.5392f);
		this.maxPanningDistance = 10f; // in km
		
		initMap();
		initParkMap();
		initForestMap();
		
		loadParks();
		loadDistricts();
		loadForests();
		
		calcAreaBarChart();
		calcParkBarChart();
		calcForestBarChart();
		
		this.map.addMarkers(this.districtMarkers);
		this.parkMap.addMarkers(this.parkMarkers);
		this.forestMap.addMarkers(this.forestMarkers);
	}
	
	public void draw() {
		background(255);
		this.map.draw();
		this.parkMap.draw();
		this.forestMap.draw();
		
		this.areaBarChart.draw(10, 450, 400, 325);
		this.parkBarChart.draw(410, 450, 400, 325);
		this.forestBarChart.draw(820, 450, 400, 325);
	}
	
	private void initMap() {
		this.map = new UnfoldingMap(this, 0, 0, 400, 400);
		this.map.zoomTo(11);
		this.map.panTo(this.zurichLocation);
		this.map.setZoomRange(10, 18);
		this.map.setPanningRestriction(this.zurichLocation, this.maxPanningDistance);
		//this.map.setTweening(true);
		MapUtils.createDefaultEventDispatcher(this, this.map);
	}

	private void initParkMap() {
		this.parkMap = new UnfoldingMap(this, 410, 0, 400, 400);
		this.parkMap.zoomTo(13);
		this.parkMap.panTo(this.zurichLocation);
		this.parkMap.setZoomRange(12, 18);
		this.parkMap.setPanningRestriction(this.zurichLocation, this.maxPanningDistance);
		MapUtils.createDefaultEventDispatcher(this, this.parkMap);
	}
	
	private void initForestMap() {
		this.forestMap = new UnfoldingMap(this, 820, 0, 400, 400);
		this.forestMap.zoomTo(11);
		this.forestMap.panTo(this.zurichLocation);
		this.forestMap.setZoomRange(11, 18);
		this.forestMap.setPanningRestriction(this.zurichLocation, this.maxPanningDistance);
		MapUtils.createDefaultEventDispatcher(this, this.forestMap);
	}
	
	private void loadParks() {
		this.parkMarkers = loadMarkers("zurich_parks.geojson", 
				color(0, 200, 0, 127), color(0, 255, 0, 127));
	}
	
	private void loadDistricts() {
		this.districtMarkers = loadMarkers("zurich_bezirke_nopoints.geojson",
				color(100, 100, 100, 127), color(220,220,220,127));
	}

	private void loadForests() {
		this.forestMarkers = loadMarkers("zurich_waldflaechen.geojson",
				color(0, 70, 15, 127), color(0, 200, 40, 127));
	}
	
	private List<Marker> loadMarkers(String fileName, int color, int highlight) {
		List<Feature> data = GeoJSONReader.loadData(this, fileName);
		List<Marker> markers = MapUtils.createSimpleMarkers(data);
		for(Marker marker : markers) {
			marker.setColor(color);
			marker.setHighlightColor(highlight);
			if (marker instanceof SimplePolygonMarker) {
				SimplePolygonMarker poly = (SimplePolygonMarker) marker;
				marker.setProperty(Property.AREA.toString(), 
						Math.abs(GeoUtils.getArea(poly)));
			} 
		}
		return markers;
	}
	
	private void calcAreaBarChart() {
		float selectedDistrictArea = getSelectedArea(this.districtMarkers);
		float selectedParkArea = getVisibleArea(this.parkMarkers);
		float selectedForestArea = getVisibleArea(this.forestMarkers);
		
		this.areaBarChart = new BarChart(this);
		this.areaBarChart.setData(new float[]{selectedDistrictArea,
				selectedParkArea, selectedForestArea});
		this.areaBarChart.showValueAxis(true);
		this.areaBarChart.setBarLabels(new String[]{"Bezirke", "Parks", "Wälder"});
		this.areaBarChart.showCategoryAxis(true);
	}
	
	private void calcParkBarChart() {
		float parkArea = getArea(this.parkMarkers);
		float selectedParkArea = getVisibleArea(this.parkMarkers);
		this.parkBarChart = new BarChart(this);
		this.parkBarChart.setBarColour(color(0, 200, 0, 127));
		this.parkBarChart.setData(new float[]{1, selectedParkArea/parkArea});
		this.parkBarChart.setMinValue(0);
		this.parkBarChart.setMaxValue(1);
		this.parkBarChart.showValueAxis(true);
		this.parkBarChart.setValueFormat("#%");
		this.parkBarChart.setBarLabels(new String[]{"Total", "Selected"});
		this.parkBarChart.showCategoryAxis(true);
	}
	
	private void calcForestBarChart() {
		float forestArea = getArea(this.forestMarkers);
		float selectedForestArea = getVisibleArea(this.forestMarkers);
		this.forestBarChart = new BarChart(this);
		this.forestBarChart.setBarColour(color(0, 70, 15));
		this.forestBarChart.setData(new float[]{1, selectedForestArea/forestArea});
		this.forestBarChart.setMinValue(0);
		this.forestBarChart.setMaxValue(1);
		this.forestBarChart.showValueAxis(true);
		this.forestBarChart.setValueFormat("#%");
		this.forestBarChart.setBarLabels(new String[]{"Total", "Selected"});
		this.forestBarChart.showCategoryAxis(true);
	}
	
	private float getVisibleArea(List<Marker> markers) {
		List<Marker> visibleMarkers = new ArrayList<Marker>();
		for(Marker marker : markers) {
			if(!marker.isHidden()) {
				visibleMarkers.add(marker);
			}
		}
		return getArea(visibleMarkers);
	}
	
	private float getSelectedArea(List<Marker> markers) {
		List<Marker> selectedMarkers = getSelectedMarkers(markers);
		float area;
		if(selectedMarkers.size() > 0) {
			area = getArea(selectedMarkers);
		} else {
			area = getArea(markers);
		}
		return area;
	}
	
	private float getArea(List<Marker> markers) {
		float sum = 0;
		for(Marker marker : markers) {
			sum += (float) marker.getProperty(Property.AREA.toString());
		}
		return sum;
	}
	
	public void mouseClicked() {
		Marker marker = this.map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null) {
			if (marker.isSelected()) {
				marker.setSelected(false);
			} else {
				marker.setSelected(true);
			}
		}
		showOnlySelected(this.parkMarkers, this.districtMarkers);
		showOnlySelected(this.forestMarkers, this.districtMarkers);
		calcAreaBarChart();
		calcParkBarChart();
		calcForestBarChart();
	}
	
	private void showOnlySelected(List<Marker> markers, List<Marker> selected) {
		hideMarkers(markers);
		List<Marker> selectedMarkers = getSelectedMarkers(selected);
		if(selectedMarkers.size() > 0) {
			for(Marker marker : selectedMarkers) {
				if (marker instanceof SimplePolygonMarker) {
					SimplePolygonMarker poly = (SimplePolygonMarker) marker;
					for(Marker m : markers) {
						if(m instanceof SimplePolygonMarker) {
							SimplePolygonMarker p = (SimplePolygonMarker) m;
							for(Location l : p.getLocations()) {
								if(poly.isInsideByLocation(l)) {
									m.setHidden(false);
								}
							}
						}
					}
				}
			}
		} else {
			showMarkers(markers);
		}
	}
	
	private List<Marker> getSelectedMarkers(List<Marker> markers) {
		List<Marker> selectedMarkers = new ArrayList<Marker>();
		for(Marker marker : markers) {
			if(marker.isSelected()) {
				selectedMarkers.add(marker);
			}
		}
		return selectedMarkers;
	}
	
	private void hideMarkers(List<Marker> markers, boolean hide) {
		for(Marker marker : markers) {
			marker.setHidden(hide);
		}
	}
	
	private void hideMarkers(List<Marker> markers) {
		hideMarkers(markers, true);
	}

	private void showMarkers(List<Marker> markers) {
		hideMarkers(markers, false);
	}
	
	/*
	private void barChart(int xPos, int yPos, int barWidth, int barHeight) {
		
		drawAxis(xPos, yPos+barHeight, barWidth, barHeight);
	}
	
	public void drawAxis(int axisXPos, int axisYPos, int axisWidth, int axisHeight) {
		fill(0);
		line(axisXPos, axisYPos, axisXPos, axisYPos - axisHeight);
		line(axisXPos, axisYPos, axisWidth - axisXPos, axisYPos);
	}
	
	public void mapChanged(MapEvent mapEvent) {
		ZoomMapEvent zoomMapEvent = (ZoomMapEvent) mapEvent;
		if(zoomMapEvent.getZoomLevelDelta() == 1 
				&& this.map.getZoomLevel() == 14) {
			removeDistrictMarkers();
			this.map.addMarkers(this.parkMarkers);
		} else if(zoomMapEvent.getZoomLevelDelta() == -1 
				&& this.map.getZoomLevel() == 13) {
			removeParkMarkers();
			this.map.addMarkers(this.districtMarkers);
		}
	}
	
	public void removeParkMarkers() {
		MarkerManager<Marker> markerManager = this.map.getDefaultMarkerManager();
		for(Marker marker : this.parkMarkers) {
			markerManager.removeMarker(marker);
		}
	}
	
	public void removeDistrictMarkers() {
		MarkerManager<Marker> markerManager = this.map.getDefaultMarkerManager();
		for(Marker marker : this.districtMarkers) {
			markerManager.removeMarker(marker);
		}
	}
	*/
}
