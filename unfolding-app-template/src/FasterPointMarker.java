import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

public class FasterPointMarker extends SimplePointMarker {

	boolean showIcon;
	
	public FasterPointMarker() {
		super();
	}

	public FasterPointMarker(Location location) {
		super(location);
	}
	
	// ICONS BEI NAHEM ZOOM
	// LABEL BEI MOUSE MOVE OVER

	@Override
	public void draw(PGraphics pg, float x, float y) {
		pg.noStroke();
		
		
		String type = (String)properties.get("ART");
		
		if ("Blaue_PP".equals(type)) {
			pg.fill(200, 50, 50);
				
		}
		if ("Weisse_PP".equals(type))
			pg.fill(50, 50, 200);
			
		
		pg.rect(x, y, 3, 3);
		
		
	}
 
	
	
}
