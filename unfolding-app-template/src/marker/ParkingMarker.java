package marker;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

public class ParkingMarker extends SimplePointMarker {

	boolean showIcon;
	
	public ParkingMarker() {
		super();
	}

	public ParkingMarker(Location location) {
		super(location);
	}
	
	// ICONS BEI NAHEM ZOOM
	// LABEL BEI MOUSE MOVE OVER

	@Override
	public void draw(PGraphics pg, float x, float y) {
		if(!this.hidden) {
			pg.noStroke();
			String type = (String) this.properties.get("ART");
			
			if ("Blaue_PP".equals(type)) {
				pg.fill(200, 50, 50);
					
			}
			if ("Weisse_PP".equals(type))
				pg.fill(50, 50, 200);
				
			
			pg.rect(x, y, 3, 3);
		
		}
	}
	
}
