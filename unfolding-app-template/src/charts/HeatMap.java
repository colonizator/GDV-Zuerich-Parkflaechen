package charts;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PApplet;

public class HeatMap {

	private PApplet pApplet;
	private UnfoldingMap map;
	private int gridWidth;
	private int gridHeight;
	private int mapX;
	private int mapY;
	
	public HeatMap(PApplet pApplet, UnfoldingMap map, int mapX, int mapY) {
		this.pApplet = pApplet;
		this.map = map;
		this.gridWidth = 50;
		this.gridHeight = 50;
		this.mapX = mapX;
		this.mapY = mapY;
	}
	
	public void draw() {
		for (int x = mapX; x < mapX+map.getWidth(); x += gridWidth) {
			for (int y = mapY; y < mapY+map.getHeight(); y += gridHeight) {
				int insideMarkerNumber = 0;
				MarkerManager<Marker> markerManager = map.getDefaultMarkerManager();
				for(int i = 0; i < markerManager.getMarkers().size(); i++) {
					Marker m = markerManager.getMarkers().get(i);
					ScreenPosition pos = map.getScreenPosition(m.getLocation());
					if (pos.x > x && pos.x < x + gridWidth && pos.y > y && pos.y < y + gridHeight) {
						if(!m.isHidden())
							insideMarkerNumber++;
					}
				}

				// Map number to color (NB: max value set to 10)
				float alpha = PApplet.map(insideMarkerNumber, 0, 10, 0, 255);
				
				// Draw current grid rectangle
				pApplet.fill(255, 0, 0, alpha);
				pApplet.noStroke();
				pApplet.rect(x, y, gridWidth, gridHeight);

			}
		}
	}
	
}
