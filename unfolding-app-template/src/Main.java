import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
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
	}
	
	public void draw() {
		background(0);
		drawMaps();
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

	}
	
	public void loadDistricts() {
		
	}
	
	public void addMarkersToMap() {
		
	}
	
	public void drawMaps() {
		this.districtMap.draw();
		this.natureMapA.draw();
		this.natureMapB.draw();
		this.parkingMapA.draw();
		this.parkingMapB.draw();
	}
	
}
