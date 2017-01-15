package zp;
import java.util.Arrays;
import java.util.List;

import Utils.ZPDataUtils;
import Utils.ZPMapUtils;
import Utils.ZPMarkerUtils;
import charts.NatureVsParkingChart;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import events.CustomEventDispatcher;
import filter.Filter;
import marker.DistrictMarker;
import processing.core.PApplet;

public class Main extends PApplet {

	private UnfoldingMap districtMap;
	private UnfoldingMap natureMapA;
	private UnfoldingMap natureMapB;
	private UnfoldingMap parkingMapA;
	private UnfoldingMap parkingMapB;

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
	private String selectedDistrictAName;
	private String selectedDistrictBName;

	private DistrictMarker hoveredDistrict;
	
	private Filter filterNatureMapA;
	private Filter filterNatureMapB;
	private Filter filterParkingMapA;
	private Filter filterParkingMapB;
	
	private NatureVsParkingChart natureVsParkingChart;
	
	int detailsMapHeight;
	int diagramX;
	int diagramY;
	int diagramWidth;
	int diagramHeight;
	
	public static void main(String[] args) {
		PApplet.main(new String[] { Main.class.getName() });
	}

	public void settings() {
		size(displayWidth, displayHeight - 75, P2D);
		fullScreen();
	}

	public void setup() {
		this.selectedDistrictAName = "";
		this.selectedDistrictBName = "Z\u00FCrich";

		initMaps();
		loadData();
		setMarkerIcons();
		addMarkersToMap();
		registerMapEvents();
		
		this.natureVsParkingChart = new NatureVsParkingChart(this, parkingMarkersA, 
				parkingMarkersB, parkMarkersA, parkMarkersB, forestMarkersA, 
				forestMarkersB, grassMarkersA, grassMarkersB);
		
		detailsMapHeight = (height - Const.SPACING_TOP) / 2;
		diagramX = 0;
		diagramY = Const.SPACING_TOP + detailsMapHeight + (Const.SPACING_MAP_HEIGHT / 2);
		diagramWidth = width / 3;
		diagramHeight = detailsMapHeight - (Const.SPACING_MAP_HEIGHT / 2);
		
		this.filterNatureMapA = new Filter(this, 
				(width*2/3)-40 + Const.SPACING_MAP_WIDTH, 
				70, 16, 16,
				Arrays.asList("Gras", "Park", "Wald"), 
				"landuse", natureVsParkingChart,
				this.grassMarkersA, this.forestMarkersA, this.parkMarkersA);
		
		this.filterNatureMapB = new Filter(this, 
				(width*3/3)-40 + Const.SPACING_MAP_WIDTH, 
				70, 16, 16,
				Arrays.asList("Gras", "Park", "Wald"), 
				"landuse", natureVsParkingChart,
				this.grassMarkersB, this.forestMarkersB, this.parkMarkersB);
		
		this.filterParkingMapA = new Filter(this, 
				(width*2/3)-40 + Const.SPACING_MAP_WIDTH, 
				detailsMapHeight + 70, 16, 16,
				Arrays.asList("Blaue", "Weisse", "Behinderten",
				"Gueterumschlag", "Car", "Elektro", "Frauen", "Gueter oder Taxi",
				"Taxi"), "ART", natureVsParkingChart,
				this.parkingMarkersA);
		
		this.filterParkingMapB = new Filter(this, 
				(width*3/3)-40 + Const.SPACING_MAP_WIDTH, 
				detailsMapHeight + 70, 16, 16,
				Arrays.asList("Blaue", "Weisse", "Behinderten",
				"Gueterumschlag", "Car", "Elektro", "Frauen", "Gueter oder Taxi",
				"Taxi"), "ART", natureVsParkingChart,
				this.parkingMarkersB);
	}

	public void draw() {
		background(0);
		drawMaps();
		
		fill(249);
		rect(diagramX, diagramY, diagramWidth, diagramHeight);
		
		fill(255);
		this.natureVsParkingChart.draw(width, height);
		textSize(26);
		textFont(createFont("Segoe UI", 26));
		text("Stadtbezirke", 
				50, 
				35);
		text(this.selectedDistrictAName, 
				(width / 3) + Const.SPACING_MAP_WIDTH + 50, 
				35);
		text(this.selectedDistrictBName, 
				(width*2 / 3) + 2*Const.SPACING_MAP_WIDTH + 50, 
				35);
		textFont(createFont("Segoe UI", 14));
		
		ZPMarkerUtils.showParkingIcons(this.parkingMapA, this.parkingMarkersA, 14);
		ZPMarkerUtils.showParkingIcons(this.parkingMapB, this.parkingMarkersB, 14);

		this.filterNatureMapA.draw();
		this.filterNatureMapB.draw();
		this.filterParkingMapA.draw();
		this.filterParkingMapB.draw();
	}

	public void mouseClicked() {
		this.filterNatureMapA.mouseClicked(mouseX, mouseY);
		this.filterNatureMapB.mouseClicked(mouseX, mouseY);
		this.filterParkingMapA.mouseClicked(mouseX, mouseY);
		this.filterParkingMapB.mouseClicked(mouseX, mouseY);
		
		boolean isDistrictHit = this.districtMap.isHit(mouseX, mouseY);
		if (isDistrictHit) {
			Marker marker = this.districtMap.getFirstHitMarker(mouseX, mouseY);
			if (marker != null) {
				if (marker.isSelected()) {
					unselectDistrict(marker);
					if (this.selectedDistrictA == null && this.selectedDistrictB != null) {
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
						this.natureMapB.zoomAndPanTo(Const.STANDARD_ZOOM_LEVEL, Const.ZURICH_LOCATION);
						this.parkingMapB.zoomAndPanTo(Const.STANDARD_ZOOM_LEVEL, Const.ZURICH_LOCATION);
						
						this.selectedDistrictAName = (String) 
								this.selectedDistrictA.getProperty("name");
						this.selectedDistrictBName = "Z\u00FCrich";
						
						showOnlySelectedDistrict(selectedDistrictA, districtMarkersA);
						showOnlySelectedDistrict(null, districtMarkersB);
						
						this.natureVsParkingChart.setMapASelected(true);
						this.natureVsParkingChart.setMapBSelected(false);
						this.natureVsParkingChart.setDistrictMapAName((String) 
								this.selectedDistrictA.getProperty("name"));
						this.natureVsParkingChart.init();
					} else if (this.selectedDistrictA != null && this.selectedDistrictB == null) {
						showOnlySelectedDistrictA(selectedDistrictA);
						showAllMarkersMapB();
						this.natureMapB.zoomAndPanTo(Const.STANDARD_ZOOM_LEVEL, Const.ZURICH_LOCATION);
						this.parkingMapB.zoomAndPanTo(Const.STANDARD_ZOOM_LEVEL, Const.ZURICH_LOCATION);
						
						this.selectedDistrictAName = (String) 
								this.selectedDistrictA.getProperty("name");
						this.selectedDistrictBName = "Z\u00FCrich";
						
						showOnlySelectedDistrict(selectedDistrictA, districtMarkersA);
						showOnlySelectedDistrict(null, districtMarkersB);
						
						this.natureVsParkingChart.setMapASelected(true);
						this.natureVsParkingChart.setMapBSelected(false);
						this.natureVsParkingChart.setDistrictMapAName(
								this.selectedDistrictAName);
						this.natureVsParkingChart.init();
					} else if (!districtSelected()) {
						hideAllMarkersMapA();
						showAllMarkersMapB();
						this.natureMapA.zoomAndPanTo(Const.STANDARD_ZOOM_LEVEL, Const.ZURICH_LOCATION);
						this.parkingMapA.zoomAndPanTo(Const.STANDARD_ZOOM_LEVEL, Const.ZURICH_LOCATION);
						this.natureMapB.zoomAndPanTo(Const.STANDARD_ZOOM_LEVEL, Const.ZURICH_LOCATION);
						this.parkingMapB.zoomAndPanTo(Const.STANDARD_ZOOM_LEVEL, Const.ZURICH_LOCATION);
						
						this.selectedDistrictAName = "";
						this.selectedDistrictBName = "Z\u00FCrich";
						
						showOnlySelectedDistrict(null, districtMarkersA);
						showOnlySelectedDistrict(null, districtMarkersB);
						
						this.natureVsParkingChart.setMapASelected(false);
						this.natureVsParkingChart.setMapBSelected(false);
						this.natureVsParkingChart.init();
					}
				} else {
					int selected = selectDistrict(marker);
					if (selected == 1) {
						showOnlySelectedDistrictA(this.selectedDistrictA);
						this.natureMapA.zoomAndPanToFit(this.selectedDistrictA);
						this.parkingMapA.zoomAndPanToFit(this.selectedDistrictA);
						
						this.selectedDistrictAName = (String) 
								this.selectedDistrictA.getProperty("name");
						this.selectedDistrictBName = "Z\u00FCrich";
						
						showOnlySelectedDistrict(selectedDistrictA, districtMarkersA);
						
						this.natureVsParkingChart.setMapASelected(true);
						this.natureVsParkingChart.setMapBSelected(false);
						this.natureVsParkingChart.setDistrictMapAName(
								this.selectedDistrictAName);
						this.natureVsParkingChart.init();
					} else if (selected == 2) {
						showOnlySelectedDistrictB(this.selectedDistrictB);
						this.natureMapB.zoomAndPanToFit(this.selectedDistrictB);
						this.parkingMapB.zoomAndPanToFit(this.selectedDistrictB);
						
						this.selectedDistrictAName = (String) 
								this.selectedDistrictA.getProperty("name");
						this.selectedDistrictBName = (String) 
								this.selectedDistrictB.getProperty("name");
						
						showOnlySelectedDistrict(selectedDistrictB, districtMarkersB);
						
						this.natureVsParkingChart.setMapASelected(true);
						this.natureVsParkingChart.setMapBSelected(true);
						this.natureVsParkingChart.setDistrictMapAName(
								this.selectedDistrictAName);
						this.natureVsParkingChart.setDistrictMapBName(
								this.selectedDistrictBName);
						this.natureVsParkingChart.init();
					}

				}
			}
		}
	}

	public void mouseMoved() {
		boolean isDistrictHit = this.districtMap.isHit(mouseX, mouseY);
		if(isDistrictHit) {
			Marker marker = this.districtMap.getFirstHitMarker(mouseX, mouseY);
			if(marker instanceof DistrictMarker) {
				DistrictMarker m = (DistrictMarker) marker;
				if(this.hoveredDistrict == null) {
					m.showLabel(true);
					m.setLabelPosX(mouseX);
					m.setLabelPosY(mouseY-60);
					this.hoveredDistrict = m;
				} else if(m.equals(this.hoveredDistrict)) {
					m.setLabelPosX(mouseX);
					m.setLabelPosY(mouseY-60);
				} else {
					this.hoveredDistrict.showLabel(false);
					m.showLabel(true);
					m.setLabelPosX(mouseX);
					m.setLabelPosY(mouseY-60);
					this.hoveredDistrict = m;
				}
			} else {
				if(this.hoveredDistrict != null) {
					this.hoveredDistrict.showLabel(false);
					this.hoveredDistrict = null;
				}
			}
		}
	}
	
	public int getSelectedDistrictCount() {
		int count = 0;
		if (this.selectedDistrictA != null) {
			count++;
		}
		if (this.selectedDistrictB != null) {
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
		if (canSelectDistrict()) {
			if (this.selectedDistrictA == null) {
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
		if (this.selectedDistrictA != null && this.selectedDistrictA == marker) {
			this.selectedDistrictA = null;
			marker.setSelected(false);
		} else if (this.selectedDistrictB != null && this.selectedDistrictB == marker) {
			this.selectedDistrictB = null;
			marker.setSelected(false);
		}
	}

	public void initMaps() {
		this.districtMap = ZPMapUtils.initDistrictMap(this, width, height);
		this.natureMapA = ZPMapUtils.initNatureMapA(this, width, height);
		this.natureMapB = ZPMapUtils.initNatureMapB(this, width, height);
		this.parkingMapA = ZPMapUtils.initParkingMapA(this, width, height);
		this.parkingMapB = ZPMapUtils.initParkingMapB(this, width, height);
	}

	public void loadData() {
		int transparent = color(100, 100, 100, 0);
		int grey = color(124, 124, 124, 127);
		int lightGrey = color(220, 220, 220, 127);
		int greenPark = color(0, 170, 0, 127);
		int lightGreenPark = color(0, 230, 0, 127);
		int greenGrass = color(0, 220, 0, 127);
		int lightGreenGrass = color(0, 255, 0, 127);
		int greenForest = color(0, 70, 15, 127);
		int lightGreenForest = color(0, 200, 40, 127);
		
		this.districtMarkers = ZPDataUtils.loadDistricts(this, grey, lightGrey, -1);
		this.districtMarkersA = ZPDataUtils.loadDistricts(this, transparent, transparent, 2);
		this.districtMarkersB = ZPDataUtils.loadDistricts(this, transparent, transparent, 2);
		this.parkMarkersA = ZPDataUtils.loadParks(this, greenPark, lightGreenPark);
		this.parkMarkersB = ZPDataUtils.loadParks(this, greenPark, lightGreenPark);
		this.grassMarkersA = ZPDataUtils.loadGrass(this, greenGrass, lightGreenGrass);
		this.grassMarkersB = ZPDataUtils.loadGrass(this, greenGrass, lightGreenGrass);
		this.forestMarkersA = ZPDataUtils.loadForest(this, greenForest, lightGreenForest);
		this.forestMarkersB = ZPDataUtils.loadForest(this, greenForest, lightGreenForest);
		this.parkingMarkersA = ZPDataUtils.loadParking(this);
		this.parkingMarkersB = ZPDataUtils.loadParking(this);
	}

	public void setMarkerIcons() {
		ZPMarkerUtils.setParkingMarkerIcons(this, this.parkingMarkersA);
		ZPMarkerUtils.setParkingMarkerIcons(this, this.parkingMarkersB);
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
		addMarkersToMap(this.parkingMapB, this.districtMarkersB);
		hideAllMarkers(this.districtMarkersB);
	}

	public void addAMarkersToMap() {
		addMarkersToMap(this.natureMapA, this.parkMarkersA);
		addMarkersToMap(this.natureMapA, this.grassMarkersA);
		addMarkersToMap(this.natureMapA, this.forestMarkersA);
		addMarkersToMap(this.parkingMapA, this.parkingMarkersA);
		addMarkersToMap(this.natureMapA, this.districtMarkersA);
		addMarkersToMap(this.parkingMapA, this.districtMarkersA);
		hideAllMarkers(this.districtMarkersA);
	}

	public void addMarkersToMap(UnfoldingMap map, List<Marker> markers) {
		map.addMarkers(markers);
	}

	public void registerMapEvents() {
		new CustomEventDispatcher(this, this.natureMapA, this.parkingMapA)
			.registerAllEvents();
		new CustomEventDispatcher(this, this.natureMapB, this.parkingMapB)
			.registerAllEvents();
	}
	
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
		for (Marker marker : markers) {
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
		for (Marker marker : markers) {
			marker.setHidden(true);
		}
	}

	public void showOnlySelectedDistrict(Marker marker, List<Marker> markers) {
		hideAllMarkers(markers);
		if(marker != null) {
			for(Marker m : markers) {
				if(ZPMarkerUtils.markerEquals(marker, m)) {
					m.setHidden(false);
				}
			}
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
			for (Marker m : markers) {
				if (m instanceof SimplePolygonMarker) {
					SimplePolygonMarker p = (SimplePolygonMarker) m;
					for (Location l : p.getLocations()) {
						if (poly.isInsideByLocation(l)) {
							m.setHidden(false);
						}
					}
				} else {
					if (poly.isInsideByLocation(m.getLocation())) {
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
		for (Marker marker2 : markers2) {
			for (Marker marker1 : markers1) {
				if (equalLocations(marker1.getLocation(), marker2.getLocation())) {
					if (!marker2.isHidden()) {
						marker1.setHidden(false);
					}
				}
			}
		}
	}

	public boolean equalLocations(Location locations1, Location locations2) {
		boolean equal = false;
		if (locations1.equals(locations2)) {
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
