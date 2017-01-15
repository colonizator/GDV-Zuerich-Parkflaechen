package marker;

import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import processing.core.PGraphics;

public class DistrictMarker extends SimplePolygonMarker {

	private boolean showLabel;
	private int labelPosX;
	private int labelPosY;
	
	public DistrictMarker() {
		super();
	}

	public DistrictMarker(List<Location> arg0) {
		super(arg0);
	}
	
	public DistrictMarker(List<Location> arg0, HashMap<String, Object> properties) {
		super(arg0, properties);
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> arg1) {
		super.draw(pg, arg1);
		if(this.showLabel) {
			String name = (String) this.properties.get("name");
			pg.fill(0);
			pg.text(name, labelPosX, labelPosY);
		}
	}
	
	public void showLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	public void setLabelPosX(int labelPosX) {
		this.labelPosX = labelPosX;
	}

	public void setLabelPosY(int labelPosY) {
		this.labelPosY = labelPosY;
	}
	
	
	
}
