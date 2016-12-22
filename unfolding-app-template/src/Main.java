import java.util.ArrayList;
import java.util.List;

import marker.Property;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.interactions.KeyboardHandler;
import de.fhpotsdam.unfolding.interactions.MouseHandler;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class Main extends PApplet {

	private final Location ZURICH_LOCATION = new Location(47.3686f, 8.5392f);
	private final int STANDARD_ZOOM_LEVEL = 12;
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
	private List<UnfoldingMap> mapsA;
	private List<UnfoldingMap> mapsB;
	
	private List<Marker> districtMarkers;
	private List<Marker> districtMarkersA;
	private List<Marker> districtMarkersB;
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
	
	private EventDispatcher eventDispatcherA;
	private EventDispatcher eventDispatcherB;
	
	public static void main(String[] args) {
		PApplet.main(new String[] { Main.class.getName() });
	}
	
	public void settings() {
		size(displayWidth, displayHeight-75, P2D);
	}
	
	public void setup() {
		this.mapsA = new ArrayList<UnfoldingMap>();
		this.mapsB = new ArrayList<UnfoldingMap>();
		this.detailsMapHeight = (height-SPACING_TOP)/2;
		this.eventDispatcherA = new EventDispatcher();
		this.eventDispatcherB = new EventDispatcher();
		
		initMaps();
		loadData();
		addMarkersToMap();
		
		MouseHandler mouseHandlerA = new MouseHandler(this, mapsA);
		//KeyboardHandler keyboardHandlerA = new KeyboardHandler(this, mapsA);
		this.eventDispatcherA.addBroadcaster(mouseHandlerA);
		//this.eventDispatcherA.addBroadcaster(keyboardHandlerA);
		this.eventDispatcherA.register(this.natureMapA, "pan", 
				this.parkingMapA.getId(), this.natureMapA.getId());
		this.eventDispatcherA.register(this.parkingMapA, "pan", 
				this.natureMapA.getId(), this.parkingMapA.getId());
		this.eventDispatcherA.register(this.natureMapA, "zoom", 
				this.parkingMapA.getId(), this.natureMapA.getId());
		this.eventDispatcherA.register(this.parkingMapA, "zoom", 
				this.natureMapA.getId(), this.parkingMapA.getId());

		
		MouseHandler mouseHandlerB = new MouseHandler(this, mapsB);
		//KeyboardHandler keyboardHandlerB = new KeyboardHandler(this, mapsB);
		this.eventDispatcherB.addBroadcaster(mouseHandlerB);
		//this.eventDispatcherB.addBroadcaster(keyboardHandlerB);
		this.eventDispatcherB.register(this.natureMapB, "pan", 
				this.parkingMapB.getId(), this.natureMapB.getId());
		this.eventDispatcherB.register(this.parkingMapB, "pan",
				this.natureMapB.getId(), this.parkingMapB.getId());
		this.eventDispatcherB.register(this.natureMapB, "zoom", 
				this.parkingMapB.getId(), this.natureMapB.getId());
		this.eventDispatcherB.register(this.parkingMapB, "zoom",
				this.natureMapB.getId(), this.parkingMapB.getId());
		
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
					switchMaps();
					showAllMarkersMapB();
					Marker tmp = this.selectedDistrictA;
					this.selectedDistrictA = this.selectedDistrictB;
					this.selectedDistrictB = null;
					this.selectedDistrictB = tmp;
					this.natureMapA.zoomAndPanToFit(this.selectedDistrictA);
					this.parkingMapA.zoomAndPanToFit(this.selectedDistrictA);
					this.natureMapB.zoomAndPanTo(STANDARD_ZOOM_LEVEL, ZURICH_LOCATION);
					this.parkingMapB.zoomAndPanTo(STANDARD_ZOOM_LEVEL, ZURICH_LOCATION);
				} else if(this.selectedDistrictA != null && this.selectedDistrictB == null) {
					showOnlySelectedDistrictA(selectedDistrictA);
					showAllMarkersMapB();
					this.natureMapB.zoomAndPanTo(STANDARD_ZOOM_LEVEL, ZURICH_LOCATION);
					this.parkingMapB.zoomAndPanTo(STANDARD_ZOOM_LEVEL, ZURICH_LOCATION);
				} else if(!districtSelected()) {
					hideAllMarkersMapA();
					showAllMarkersMapB();
					this.natureMapA.zoomAndPanTo(STANDARD_ZOOM_LEVEL, ZURICH_LOCATION);
					this.parkingMapA.zoomAndPanTo(STANDARD_ZOOM_LEVEL, ZURICH_LOCATION);
					this.natureMapB.zoomAndPanTo(STANDARD_ZOOM_LEVEL, ZURICH_LOCATION);
					this.parkingMapB.zoomAndPanTo(STANDARD_ZOOM_LEVEL, ZURICH_LOCATION);
				}
			} else {
				int selected = selectDistrict(marker);
				if(selected == 1) {
					showOnlySelectedDistrictA(this.selectedDistrictA);
					this.natureMapA.zoomAndPanToFit(this.selectedDistrictA);
					this.parkingMapA.zoomAndPanToFit(this.selectedDistrictA);
				} else if(selected == 2) {
					showOnlySelectedDistrictB(this.selectedDistrictB);
					this.natureMapB.zoomAndPanToFit(this.selectedDistrictB);
					this.parkingMapB.zoomAndPanToFit(this.selectedDistrictB);
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
		this.districtMap.zoomTo(STANDARD_ZOOM_LEVEL);
		this.districtMap.panTo(ZURICH_LOCATION);
		this.districtMap.setZoomRange(11, 18);
		this.districtMap.setPanningRestriction(ZURICH_LOCATION, MAX_PANNING_DISTANCE);
		MapUtils.createDefaultEventDispatcher(this, this.districtMap);
	}
	
	public void initNatureMapA() {
		this.natureMapA = new UnfoldingMap(this, "natureMapA", 
				width/3 + SPACING_MAP_WIDTH, SPACING_TOP, 
				width/3, this.detailsMapHeight - (SPACING_MAP_HEIGHT/2));
		this.natureMapA.zoomTo(STANDARD_ZOOM_LEVEL);
		this.natureMapA.panTo(ZURICH_LOCATION);
		this.natureMapA.setZoomRange(12, 18);
		this.natureMapA.setPanningRestriction(ZURICH_LOCATION, MAX_PANNING_DISTANCE);
		//MapUtils.createDefaultEventDispatcher(this, this.natureMapA);
		this.mapsA.add(this.natureMapA);
	}
	
	public void initNatureMapB() {
		this.natureMapB = new UnfoldingMap(this, "natureMapB", 
				width/3*2 + 2*SPACING_MAP_WIDTH, SPACING_TOP, 
				width/3, this.detailsMapHeight - (SPACING_MAP_HEIGHT/2));
		this.natureMapB.zoomTo(STANDARD_ZOOM_LEVEL);
		this.natureMapB.panTo(ZURICH_LOCATION);
		this.natureMapB.setZoomRange(12, 18);
		this.natureMapB.setPanningRestriction(ZURICH_LOCATION, MAX_PANNING_DISTANCE);
		//MapUtils.createDefaultEventDispatcher(this, this.natureMapB);
		this.mapsB.add(this.natureMapB);
	}
	
	public void initParkingMapA() {
		this.parkingMapA = new UnfoldingMap(this, "parkingMapA", 
				width/3 + SPACING_MAP_WIDTH, 
				SPACING_TOP + this.detailsMapHeight + (SPACING_MAP_HEIGHT/2), 
				width/3, this.detailsMapHeight - (SPACING_MAP_HEIGHT/2));
		this.parkingMapA.zoomTo(STANDARD_ZOOM_LEVEL);
		this.parkingMapA.panTo(ZURICH_LOCATION);
		this.parkingMapA.setZoomRange(12, 18);
		this.parkingMapA.setPanningRestriction(ZURICH_LOCATION, MAX_PANNING_DISTANCE);
		//MapUtils.createDefaultEventDispatcher(this, this.parkingMapA);
		this.mapsA.add(this.parkingMapA);
	}
	
	public void initParkingMapB() {
		this.parkingMapB = new UnfoldingMap(this, "parkingMapB", 
				width/3*2 + 2*SPACING_MAP_WIDTH, 
				SPACING_TOP + this.detailsMapHeight + (SPACING_MAP_HEIGHT/2), 
				width/3, this.detailsMapHeight - (SPACING_MAP_HEIGHT/2));
		this.parkingMapB.zoomTo(STANDARD_ZOOM_LEVEL);
		this.parkingMapB.panTo(ZURICH_LOCATION);
		this.parkingMapB.setZoomRange(12, 18);
		this.parkingMapB.setPanningRestriction(ZURICH_LOCATION, MAX_PANNING_DISTANCE);
		//MapUtils.createDefaultEventDispatcher(this, this.parkingMapB);
		this.mapsB.add(this.parkingMapB);
	}
	
	public void loadData() {
		loadDistricts();
		loadDistrictsA();
		loadDistrictsB();
		loadParks();
		loadGrass();
		loadForest();
		loadParking();
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
	
	public void loadDistrictsA() {
		this.districtMarkersA = new ArrayList<Marker>();
		List<Marker> districts = loadMarkers("zurich_bezirke_nopoints.geojson",
				color(100, 100, 100, 127), color(220,220,220,127));
		for(Marker marker : districts) {
			if("10".equals(marker.getStringProperty("admin_level"))) {
				this.districtMarkersA.add(marker);
			}
		}
		
	}
	
	public void loadDistrictsB() {
		this.districtMarkersB = new ArrayList<Marker>();
		List<Marker> districts = loadMarkers("zurich_bezirke_nopoints.geojson",
				color(100, 100, 100, 127), color(220,220,220,127));
		for(Marker marker : districts) {
			if("10".equals(marker.getStringProperty("admin_level"))) {
				this.districtMarkersB.add(marker);
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
		List<Feature> featuresA = GeoJSONReader.loadData(this, "zurich_parkplaetze.geojson");
		List<Feature> featuresB = GeoJSONReader.loadData(this, "zurich_parkplaetze.geojson");
		this.parkingMarkersA = MapUtils.createSimpleMarkers(featuresA);
		this.parkingMarkersB = MapUtils.createSimpleMarkers(featuresB);
		
		for(int i = 0; i < this.parkingMarkersA.size(); i++) {
			this.parkingMarkersA.get(i).setStrokeColor(0);
			this.parkingMarkersA.get(i).setColor(color(0, 160, 227, 220));
			this.parkingMarkersB.get(i).setColor(color(0, 160, 227, 220));
			
		}
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
		addMarkersToMap(this.parkingMapB, this.parkingMarkersB);
		addMarkersToMap(this.natureMapB, this.districtMarkersB);
		hideAllMarkers(this.districtMarkersA);
	}
	
	public void addAMarkersToMap() {
		addMarkersToMap(this.natureMapA, this.parkMarkersA);
		addMarkersToMap(this.natureMapA, this.grassMarkersA);
		addMarkersToMap(this.natureMapA, this.forestMarkersA);
		addMarkersToMap(this.parkingMapA, this.parkingMarkersA);
		addMarkersToMap(this.natureMapA, this.districtMarkersA);
		hideAllMarkers(this.districtMarkersB);
	}
	
	public void addMarkersToMap(UnfoldingMap map, List<Marker> markers) {
		map.addMarkers(markers);
	}
	
	/*
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
		removeMarkers(this.parkingMapA, this.parkingMarkersA);
	}
	
	public void removeAllMarkersMapB() {
		removeMarkers(this.natureMapB, this.parkMarkersB);
		removeMarkers(this.natureMapB, this.grassMarkersB);
		removeMarkers(this.natureMapB, this.forestMarkersB);
		removeMarkers(this.parkingMapB, this.parkingMarkersB);
	}
	*/
	
	public void showAllMarkersMapB() {
		showAllMarkers(this.parkMarkersB);
		showAllMarkers(this.grassMarkersB);
		showAllMarkers(this.forestMarkersB);
		showAllMarkers(this.parkingMarkersB);
	}
	
	public void showAllMarkersMapA() {
		showAllMarkers(this.parkMarkersA);
		showAllMarkers(this.grassMarkersA);
		showAllMarkers(this.forestMarkersA);
		showAllMarkers(this.parkingMarkersA);
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
		hideAllMarkers(this.parkingMarkersA);
	}
	
	public void hideAllMarkersMapB() {
		hideAllMarkers(this.parkMarkersB);
		hideAllMarkers(this.grassMarkersB);
		hideAllMarkers(this.forestMarkersB);
		hideAllMarkers(this.parkingMarkersB);
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
		showOnlySelectedMarkers(marker, this.parkingMarkersA);
	}
	
	public void showOnlySelectedDistrictB(Marker marker) {
		hideAllMarkersMapB();
		showOnlySelectedMarkers(marker, this.parkMarkersB);
		showOnlySelectedMarkers(marker, this.grassMarkersB);
		showOnlySelectedMarkers(marker, this.forestMarkersB);
		showOnlySelectedMarkers(marker, this.parkingMarkersB);
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
				} else {
					if(poly.isInsideByLocation(m.getLocation())) {
						m.setHidden(false);
					}
				}
			}
		}
	}
	
	public void switchMaps() {
		switchMaps(this.parkMarkersA, this.parkMarkersB);
		switchMaps(this.grassMarkersA, this.grassMarkersB);
		switchMaps(this.forestMarkersA, this.forestMarkersB);
		switchMaps(this.parkingMarkersA, this.parkingMarkersB);
	}
	
	public void switchMaps(List<Marker> markers1, List<Marker> markers2) {
		for(Marker marker2 : markers2) {
			for(Marker marker1 : markers1) {
				if(equalLocations(marker1.getLocation(), marker2.getLocation())) {
					if(!marker2.isHidden()) {
						marker1.setHidden(false);
					}
				}
			}
		}
	}
	
	public boolean equalLocations(Location locations1, Location locations2) {
		boolean equal = false;
		if(locations1.equals(locations2)) {
			equal = true;
		}
		return equal;
	}
	
	public void drawMaps() {
		this.districtMap.draw();
		this.natureMapA.draw();
		this.natureMapB.draw();
		this.parkingMapA.draw();
		this.parkingMapB.draw();
	}
	
}
