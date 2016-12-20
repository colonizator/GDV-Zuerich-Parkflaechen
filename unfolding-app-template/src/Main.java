import java.util.ArrayList;
import java.util.List;

import Marker.Property;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class Main extends PApplet {

	private final Location ZURICH_LOCATION = new Location(47.3686f, 8.5392f);
	//in km
	private final float MAX_PANNING_DISTANCE = 10f;
	private final int SPACING_TOP = 50;
	private final int SPACING_MAP_WIDTH = 10;
	private final int SPACING_MAP_HEIGHT = 10;
	
	private UnfoldingMap districtMap;
	private UnfoldingMap natureMapA;
	private UnfoldingMap natureMapB;
	private UnfoldingMap parkingMapA;
	private UnfoldingMap parkingMapB;
	
	private List<Marker> districtMarkers;
	private List<Marker> parkMarkersA;
	private List<Marker> parkMarkersB;
	private List<Marker> grassMarkersA;
	private List<Marker> grassMarkersB;
	private List<Marker> forestMarkersA;
	private List<Marker> forestMarkersB;
	private List<Marker> parkingMarkersA;
	private List<Marker> parkingMarkersB;
	
	private Marker selectedDistrictA;
	private Marker selectedDistrictB;
	
	private int detailsMapHeight;
	
	public static void main(String[] args) {
		PApplet.main(new String[] { Main.class.getName() });
	}
	
	public void settings() {
		size(displayWidth, displayHeight-75, P2D);
	}
	
	public void setup() {
		this.detailsMapHeight = (height-SPACING_TOP)/2;
		initMaps();
		loadData();
		addMarkersToMap();
	}
	
	public void draw() {
		background(0);
		drawMaps();
	}

	public void mouseClicked() {
		Marker marker = this.districtMap.getFirstHitMarker(mouseX, mouseY);
		if(marker != null) {
			if (marker.isSelected()) {
				unselectDistrict(marker);
				if(this.selectedDistrictA == null && this.selectedDistrictB != null) {
					// Maps switchen?
					hideAllMarkersMapA();
					showOnlySelectedDistrictB(this.selectedDistrictB);
				} else if(this.selectedDistrictA != null && this.selectedDistrictB == null) {
					showOnlySelectedDistrictA(selectedDistrictA);
					showAllMarkersMapB();
				} else if(!districtSelected()) {
					hideAllMarkersMapA();
					showAllMarkersMapB();
				}
			} else {
				int selected = selectDistrict(marker);
				if(selected == 1) {
					showOnlySelectedDistrictA(this.selectedDistrictA);
				} else if(selected == 2) {
					showOnlySelectedDistrictB(this.selectedDistrictB);
				}
				
			}
		}
	}
	
	public int getSelectedDistrictCount() {
		int count = 0;
		if(this.selectedDistrictA != null) {
			count++;
		}
		if(this.selectedDistrictB != null) {
			count++;
		}
		return count;
	}
	
	public boolean districtSelected() {
		return this.selectedDistrictA != null || this.selectedDistrictB != null; 
	}
	
	public boolean canSelectDistrict() {
		return this.selectedDistrictA == null || this.selectedDistrictB == null;
	}
	
	public int selectDistrict(Marker marker) {
		int selected = 0;
		if(canSelectDistrict()) {
			if(this.selectedDistrictA == null) {
				this.selectedDistrictA = marker;
				selected = 1;
			} else {
				this.selectedDistrictB = marker;
				selected = 2;
			}
			marker.setSelected(true);
		}
		return selected;
	}
	
	public void unselectDistrict(Marker marker) {
		if(this.selectedDistrictA != null && this.selectedDistrictA == marker) {
			this.selectedDistrictA = null;
			marker.setSelected(false);
		} else if(this.selectedDistrictB != null && this.selectedDistrictB == marker) {
			this.selectedDistrictB = null;
			marker.setSelected(false);
		}
	}
	
	public void initMaps() {
		initDistrictMap();
		initNatureMapA();
		initNatureMapB();
		initParkingMapA();
		initParkingMapB();
	}
	
	public void initDistrictMap() {
		this.districtMap = new UnfoldingMap(this, "districtMap", 
				0, SPACING_TOP, width/3, height);
		this.districtMap.zoomTo(13);
		this.districtMap.panTo(ZURICH_LOCATION);
		this.districtMap.setZoomRange(12, 18);
		this.districtMap.setPanningRestriction(ZURICH_LOCATION, MAX_PANNING_DISTANCE);
		MapUtils.createDefaultEventDispatcher(this, this.districtMap);
	}
	
	public void initNatureMapA() {
		this.natureMapA = new UnfoldingMap(this, "natureMapA", 
				width/3 + SPACING_MAP_WIDTH, SPACING_TOP, 
				width/3, this.detailsMapHeight - (SPACING_MAP_HEIGHT/2));
		this.natureMapA.zoomTo(13);
		this.natureMapA.panTo(ZURICH_LOCATION);
		this.natureMapA.setZoomRange(12, 18);
		this.natureMapA.setPanningRestriction(ZURICH_LOCATION, MAX_PANNING_DISTANCE);
		MapUtils.createDefaultEventDispatcher(this, this.natureMapA);
	}
	
	public void initNatureMapB() {
		this.natureMapB = new UnfoldingMap(this, "natureMapB", 
				width/3*2 + 2*SPACING_MAP_WIDTH, SPACING_TOP, 
				width/3, this.detailsMapHeight - (SPACING_MAP_HEIGHT/2));
		this.natureMapB.zoomTo(13);
		this.natureMapB.panTo(ZURICH_LOCATION);
		this.natureMapB.setZoomRange(12, 18);
		this.natureMapB.setPanningRestriction(ZURICH_LOCATION, MAX_PANNING_DISTANCE);
		MapUtils.createDefaultEventDispatcher(this, this.natureMapB);
	}
	
	public void initParkingMapA() {
		this.parkingMapA = new UnfoldingMap(this, "parkingMapA", 
				width/3 + SPACING_MAP_WIDTH, 
				SPACING_TOP + this.detailsMapHeight + (SPACING_MAP_HEIGHT/2), 
				width/3, this.detailsMapHeight - (SPACING_MAP_HEIGHT/2));
		this.parkingMapA.zoomTo(13);
		this.parkingMapA.panTo(ZURICH_LOCATION);
		this.parkingMapA.setZoomRange(12, 18);
		this.parkingMapA.setPanningRestriction(ZURICH_LOCATION, MAX_PANNING_DISTANCE);
		MapUtils.createDefaultEventDispatcher(this, this.parkingMapA);
	}
	
	public void initParkingMapB() {
		this.parkingMapB = new UnfoldingMap(this, "parkingMapB", 
				width/3*2 + 2*SPACING_MAP_WIDTH, 
				SPACING_TOP + this.detailsMapHeight + (SPACING_MAP_HEIGHT/2), 
				width/3, this.detailsMapHeight - (SPACING_MAP_HEIGHT/2));
		this.parkingMapB.zoomTo(13);
		this.parkingMapB.panTo(ZURICH_LOCATION);
		this.parkingMapB.setZoomRange(12, 18);
		this.parkingMapB.setPanningRestriction(ZURICH_LOCATION, MAX_PANNING_DISTANCE);
		MapUtils.createDefaultEventDispatcher(this, this.parkingMapB);
	}
	
	public void loadData() {
		loadDistricts();
		loadParks();
		loadGrass();
		loadForest();
		//loadParking();
	}
	
	public void loadDistricts() {
		this.districtMarkers = new ArrayList<Marker>();
		List<Marker> districts = loadMarkers("zurich_bezirke_nopoints.geojson",
				color(100, 100, 100, 127), color(220,220,220,127));
		for(Marker marker : districts) {
			if("10".equals(marker.getStringProperty("admin_level"))) {
				this.districtMarkers.add(marker);
			}
		}
	}
	
	public void loadParks() {
		this.parkMarkersA = loadMarkers("zurich_parks.geojson", 
				color(0, 170, 0, 127), color(0, 230, 0, 127));
		this.parkMarkersB = loadMarkers("zurich_parks.geojson", 
				color(0, 170, 0, 127), color(0, 230, 0, 127));
	}
	
	public void loadGrass() {
		this.grassMarkersA = loadMarkers("zurich_grasflaechen.geojson",
				color(0, 220, 0, 127), color(0, 255, 0, 127));
		this.grassMarkersB = loadMarkers("zurich_grasflaechen.geojson",
				color(0, 220, 0, 127), color(0, 255, 0, 127));
	}
	
	public void loadForest() {
		this.forestMarkersA = loadMarkers("zurich_waldflaechen.geojson",
				color(0, 70, 15, 127), color(0, 200, 40, 127));
		this.forestMarkersB = loadMarkers("zurich_waldflaechen.geojson",
				color(0, 70, 15, 127), color(0, 200, 40, 127));
	}
	
	public void loadParking() {
		this.grassMarkersA = loadMarkers("zurich_grasflaechen.geojson",
				color(0, 220, 0, 127), color(0, 255, 0, 127));
		this.parkingMarkersB = loadMarkers("zurich_parkplatz.geojson",
				color(0, 75, 170, 127), color(0, 90, 200, 127));
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
	
	public void addMarkersToMap() {
		addMarkersToMap(this.districtMap, this.districtMarkers);
		addBMarkersToMap();
		addAMarkersToMap();
		hideAllMarkersMapA();
	}
	
	public void addBMarkersToMap() {
		addMarkersToMap(this.natureMapB, this.parkMarkersB);
		addMarkersToMap(this.natureMapB, this.grassMarkersB);
		addMarkersToMap(this.natureMapB, this.forestMarkersB);
		//addMarkersToMap(this.parkingMapB, this.parkingMarkersB);
	}
	
	public void addAMarkersToMap() {
		addMarkersToMap(this.natureMapA, this.parkMarkersA);
		addMarkersToMap(this.natureMapA, this.grassMarkersA);
		addMarkersToMap(this.natureMapA, this.forestMarkersA);
	}
	
	public void addMarkersToMap(UnfoldingMap map, List<Marker> markers) {
		map.addMarkers(markers);
	}
	
	public void removeMarkers(UnfoldingMap map, List<Marker> markers) {
		MarkerManager<Marker> markerManager = map.getDefaultMarkerManager();
		for(Marker marker : markers) {
			markerManager.removeMarker(marker);
		}
	}
	
	public void removeAllMarkersMapA() {
		removeMarkers(this.natureMapA, this.parkMarkersA);
		removeMarkers(this.natureMapA, this.grassMarkersA);
		removeMarkers(this.natureMapA, this.forestMarkersA);
		//removeMarkers(this.parkingMapA, this.parkingMarkersA);
	}
	
	public void removeAllMarkersMapB() {
		removeMarkers(this.natureMapB, this.parkMarkersB);
		removeMarkers(this.natureMapB, this.grassMarkersB);
		removeMarkers(this.natureMapB, this.forestMarkersB);
		//removeMarkers(this.parkingMapB, this.parkingMarkersB);
	}
	
	public void showAllMarkersMapB() {
		showAllMarkers(this.parkMarkersB);
		showAllMarkers(this.grassMarkersB);
		showAllMarkers(this.forestMarkersB);
	}
	
	public void showAllMarkersMapA() {
		showAllMarkers(this.parkMarkersA);
		showAllMarkers(this.grassMarkersA);
		showAllMarkers(this.forestMarkersA);
	}
	
	public void showAllMarkers(List<Marker> markers) {
		for(Marker marker : markers) {
			marker.setHidden(false);
		}
	}
	
	public void hideAllMarkersMapA() {
		hideAllMarkers(this.parkMarkersA);
		hideAllMarkers(this.grassMarkersA);
		hideAllMarkers(this.forestMarkersA);
	}
	
	public void hideAllMarkersMapB() {
		hideAllMarkers(this.parkMarkersB);
		hideAllMarkers(this.grassMarkersB);
		hideAllMarkers(this.forestMarkersB);
	}
	
	public void hideAllMarkers(List<Marker> markers) {
		for(Marker marker : markers) {
			marker.setHidden(true);
		}
	}
	
	public void showOnlySelectedDistrictA(Marker marker) {
		hideAllMarkersMapA();
		showOnlySelectedMarkers(marker, this.parkMarkersA);
		showOnlySelectedMarkers(marker, this.grassMarkersA);
		showOnlySelectedMarkers(marker, this.forestMarkersA);
	}
	
	public void showOnlySelectedDistrictB(Marker marker) {
		hideAllMarkersMapB();
		showOnlySelectedMarkers(marker, this.parkMarkersB);
		showOnlySelectedMarkers(marker, this.grassMarkersB);
		showOnlySelectedMarkers(marker, this.forestMarkersB);
	}
	
	public void showOnlySelectedMarkers(Marker marker, List<Marker> markers) {
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
	
	public void drawMaps() {
		this.districtMap.draw();
		this.natureMapA.draw();
		this.natureMapB.draw();
		this.parkingMapA.draw();
		this.parkingMapB.draw();
	}
	
}
