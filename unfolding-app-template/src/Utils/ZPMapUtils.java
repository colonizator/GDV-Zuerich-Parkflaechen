package Utils;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import zp.Const;

public class ZPMapUtils {

	public static UnfoldingMap initDistrictMap(PApplet pApplet, int width, int height) {
		int mapX = 0;
		int mapY = Const.SPACING_TOP;
		int mapWidth = width / 3;
		int mapHeight = height;
		
		UnfoldingMap map = initBigMap(pApplet, mapX, mapY, mapWidth, mapHeight);
		return map;
	}
	
	public static UnfoldingMap initNatureMapA(PApplet pApplet, int width, int height, 
			List<UnfoldingMap> maps) {
		int detailsMapHeight = (height - Const.SPACING_TOP) / 2;
		int mapX = width / 3 + Const.SPACING_MAP_WIDTH;
		int mapY = Const.SPACING_TOP;
		int mapWidth = width / 3;
		int mapHeight = detailsMapHeight - (Const.SPACING_MAP_HEIGHT / 2);
		
		UnfoldingMap map = initSmallMap(pApplet, mapX, mapY, mapWidth, mapHeight);
		maps.add(map);
		return map;
	}

	public static UnfoldingMap initNatureMapB(PApplet pApplet, int width, int height, 
			List<UnfoldingMap> maps) {
		int detailsMapHeight = (height - Const.SPACING_TOP) / 2;
		int mapX = width / 3 * 2 + 2 * Const.SPACING_MAP_WIDTH;
		int mapY = Const.SPACING_TOP;
		int mapWidth = width / 3;
		int mapHeight = detailsMapHeight - (Const.SPACING_MAP_HEIGHT / 2);
		
		UnfoldingMap map = initSmallMap(pApplet, mapX, mapY, mapWidth, mapHeight);
		maps.add(map);
		return map;
	}

	public static UnfoldingMap initParkingMapA(PApplet pApplet, int width, int height, 
			List<UnfoldingMap> maps) {
		int detailsMapHeight = (height - Const.SPACING_TOP) / 2;
		int mapX = width / 3 + Const.SPACING_MAP_WIDTH;
		int mapY = Const.SPACING_TOP + detailsMapHeight + (Const.SPACING_MAP_HEIGHT / 2);
		int mapWidth = width / 3;
		int mapHeight = detailsMapHeight - (Const.SPACING_MAP_HEIGHT / 2);
		
		UnfoldingMap map = initSmallMap(pApplet, mapX, mapY, mapWidth, mapHeight);
		maps.add(map);
		return map;
	}

	public static UnfoldingMap initParkingMapB(PApplet pApplet, int width, int height, 
			List<UnfoldingMap> maps) {
		int detailsMapHeight = (height - Const.SPACING_TOP) / 2;
		int mapX = width / 3 * 2 + 2 * Const.SPACING_MAP_WIDTH;
		int mapY = Const.SPACING_TOP + detailsMapHeight + (Const.SPACING_MAP_HEIGHT / 2);
		int mapWidth = width / 3;
		int mapHeight = detailsMapHeight - (Const.SPACING_MAP_HEIGHT / 2);
		
		UnfoldingMap map = initSmallMap(pApplet, mapX, mapY, mapWidth, mapHeight);
		maps.add(map);
		return map;
	}
	
	private static UnfoldingMap initBigMap(PApplet pApplet, int x, int y, int width, int height) {
		UnfoldingMap map = initMap(pApplet, x, y, width, height);
		map.setZoomRange(11, 18);
		MapUtils.createDefaultEventDispatcher(pApplet, map);
		return map;
	}
	
	private static UnfoldingMap initSmallMap(PApplet pApplet, int x, int y, int width, int height) {
		UnfoldingMap map = initMap(pApplet, x, y, width, height);
		map.setZoomRange(12, 18);
		return map;
	}
	
	private static UnfoldingMap initMap(PApplet pApplet, int x, int y, int width, int height) {
		UnfoldingMap map = new UnfoldingMap(pApplet, x, y, width, height);
		map.zoomTo(Const.STANDARD_ZOOM_LEVEL);
		map.panTo(Const.ZURICH_LOCATION);
		map.setZoomRange(12, 18);
		map.setPanningRestriction(Const.ZURICH_LOCATION, Const.MAX_PANNING_DISTANCE);
		return map;
	}
	
}
