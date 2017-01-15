package marker;

import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import filter.Filterable;
import processing.core.PGraphics;

public class ParkMarker extends SimplePolygonMarker implements Filterable {

private boolean filtered;
	
	public ParkMarker() {
		super();
	}

	public ParkMarker(List<Location> locations) {
		super(locations);
	}
	
	public ParkMarker(List<Location> arg0, HashMap<String, Object> properties) {
		super(arg0, properties);
	}
	
	@Override
	public void draw(PGraphics pg, List<MapPosition> arg1) {
		if(!this.filtered) {
			super.draw(pg, arg1);
		}
	}

	@Override
	public void draw(PGraphics arg0, List<MapPosition> arg1, List<List<MapPosition>> arg2) {
		if(!this.filtered) {
			super.draw(arg0, arg1, arg2);
		}
	}

	public boolean isFiltered() {
		return filtered;
	}

	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}
	
}
