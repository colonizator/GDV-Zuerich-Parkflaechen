package charts;
import java.util.List;

import org.gicentre.utils.colour.ColourTable;
import org.gicentre.utils.stat.BarChart;

import de.fhpotsdam.unfolding.marker.Marker;
import filter.Filterable;
import marker.Property;
import processing.core.PApplet;
import zp.Const;

public class NatureVsParkingChart implements Chart {

	private PApplet pApplet;
	private BarChart natureVsParkingBarChart;
	private List<Marker> parkingMarkerA;
	private List<Marker> parkingMarkerB;
	private List<Marker> parkMarkerA;
	private List<Marker> parkMarkerB;
	private List<Marker> forestMarkerA;
	private List<Marker> forestMarkerB;
	private List<Marker> grassMarkerA;
	private List<Marker> grassMarkerB;
	private boolean mapASelected;
	private boolean mapBSelected;
	private String districtMapAName;
	private String districtMapBName;
	
	public NatureVsParkingChart(PApplet pApplet, List<Marker> parkingMarkerA,
			List<Marker> parkingMarkerB, List<Marker> parkMarkerA,
			List<Marker> parkMarkerB, List<Marker> forestMarkerA,
			List<Marker> forestMarkerB, List<Marker> grassMarkerA,
			List<Marker> grassMarkerB) {
		this.pApplet = pApplet;
		this.parkingMarkerA = parkingMarkerA;
		this.parkingMarkerB = parkingMarkerB;
		this.parkMarkerA = parkMarkerA;
		this.parkMarkerB = parkMarkerB;
		this.forestMarkerA = forestMarkerA;
		this.forestMarkerB = forestMarkerB;
		this.grassMarkerA = grassMarkerA;
		this.grassMarkerB = grassMarkerB;
		
		init();
	}
	
	public void init() {
		this.natureVsParkingBarChart = new BarChart(this.pApplet);
		float parkingAreaTotal = getTotalArea(parkingMarkerB, false);
		float parkingAreaMapA = getTotalArea(parkingMarkerA, true);
		float parkingAreaMapB = getTotalArea(parkingMarkerB, true);
		float forestAreaTotal = getTotalArea(forestMarkerB, false);
		float forestAreaMapA = getTotalArea(forestMarkerA, true);
		float forestAreaMapB = getTotalArea(forestMarkerB, true);
		float parkAreaTotal = getTotalArea(parkMarkerB, false);
		float parkAreaMapA = getTotalArea(parkMarkerA, true);
		float parkAreaMapB = getTotalArea(parkMarkerB, true);
		float grassAreaTotal = getTotalArea(grassMarkerB, false);
		float grassAreaMapA = getTotalArea(grassMarkerA, true);
		float grassAreaMapB = getTotalArea(grassMarkerB, true);
		float natureAreaTotal = forestAreaTotal + parkAreaTotal + grassAreaTotal;
		float natureAreaMapA = forestAreaMapA + parkAreaMapA + grassAreaMapA;
		float natureAreaMapB = forestAreaMapB + parkAreaMapB + grassAreaMapB;
		ColourTable cTable = new ColourTable();
		cTable.addContinuousColourRule(0.5f/6, 0,76,153);
		cTable.addContinuousColourRule(5.5f/6, 0,153,76);
		
		if(!this.mapASelected && !this.mapBSelected) {
			this.natureVsParkingBarChart.setData(new float[] {parkingAreaTotal, 
					natureAreaTotal});
			this.natureVsParkingBarChart.setBarLabels(new String[] {"Parkpl\u00E4tze", 
			"Gr\u00FCnfl\u00E4chen"});
			
			this.natureVsParkingBarChart.setBarColour(
					new float[] {0, 255}, 
					cTable);
		} else if(this.mapASelected && !this.mapBSelected) {
			this.natureVsParkingBarChart.setData(new float[] {
					parkingAreaTotal, parkingAreaMapA,
					natureAreaTotal, natureAreaMapA});
			this.natureVsParkingBarChart.setBarLabels(new String[] {
					"Parkpl\u00E4tze", districtMapAName,
					"Gr\u00FCnfl\u00E4chen", districtMapAName});
			this.natureVsParkingBarChart.setBarColour(
					new float[] {0, 0, 255, 255}, 
					cTable);
		} else if(!this.mapASelected && this.mapBSelected) {
			// Tritt nicht ein
		} else if(this.mapASelected && this.mapBSelected) {
			this.natureVsParkingBarChart.setData(new float[] {
					parkingAreaTotal, parkingAreaMapA, parkingAreaMapB,
					natureAreaTotal, natureAreaMapA, natureAreaMapB});
			this.natureVsParkingBarChart.setBarLabels(new String[] {
					"Parkpl\u00E4tze", districtMapAName, districtMapBName,
					"Gr\u00FCnfl\u00E4chen", districtMapAName,  districtMapBName});
			
			this.natureVsParkingBarChart.setBarColour(
					new float[] {0, 0, 0, 255, 255, 255}, 
					cTable);
			this.natureVsParkingBarChart.setAxisLabelColour(pApplet.color(51));
		}
		this.natureVsParkingBarChart.showValueAxis(true);
		this.natureVsParkingBarChart.setValueFormat("# m�");
		this.natureVsParkingBarChart.showCategoryAxis(true);
		this.natureVsParkingBarChart.setMinValue(0);
	}
	
	private float getTotalArea(List<Marker> markers, boolean onlyShown) {
		float total = 0;
		for(Marker marker : markers) {
			if(!onlyShown) {
				if(marker instanceof Filterable) {
					Filterable filterMarker = (Filterable) marker;
					if(!filterMarker.isFiltered()) {
						total += (float) marker.getProperty(Property.AREA.toString());
					}
				} else {
					total += (float) marker.getProperty(Property.AREA.toString());
				}
			} else {
				if(!marker.isHidden()) {
					if(marker instanceof Filterable) {
						Filterable filterMarker = (Filterable) marker;
						if(!filterMarker.isFiltered()) {
							total += (float) marker.getProperty(Property.AREA.toString());
						}
					} else {
						total += (float) marker.getProperty(Property.AREA.toString());
					}
				}
			}
		}
		return total;
	}
	
	public void draw(int width, int height) {
		int detailsMapHeight = (height - Const.SPACING_TOP) / 2;
		int diagramX = 0;
		int diagramY = Const.SPACING_TOP + detailsMapHeight + (Const.SPACING_MAP_HEIGHT / 2);
		int diagramWidth = width / 3;
		int diagramHeight = detailsMapHeight - (Const.SPACING_MAP_HEIGHT / 2);
		
		this.natureVsParkingBarChart.draw(diagramX+20, diagramY+20, 
				diagramWidth-40, diagramHeight-40);
	}
	
	public void setMapASelected(boolean mapASelected) {
		this.mapASelected = mapASelected;
	}
	
	public void setMapBSelected(boolean mapBSelected) {
		this.mapBSelected = mapBSelected;
	}
	
	public void setDistrictMapAName(String districtMapAName) {
		this.districtMapAName = districtMapAName;
	}
	
	public void setDistrictMapBName(String districtMapBName) {
		this.districtMapBName = districtMapBName;
	}
	
}
