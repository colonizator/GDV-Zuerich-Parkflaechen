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
	
	private double totalNatureRatio;
	private double natureRatioA;
	private double natureRatioB;
	private double totalParkingRatio;
	private double parkingRatioA;
	private double parkingRatioB;
	
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
			this.natureVsParkingBarChart.setData(new float[] { 
					natureAreaTotal,
					parkingAreaTotal});
			this.natureVsParkingBarChart.setBarLabels(new String[] { 
				"Gr\u00FCnfl\u00E4chen",
				"Parkpl\u00E4tze"});
			
			this.natureVsParkingBarChart.setBarColour(
					new float[] {255, 0}, 
					cTable);
		} else if(this.mapASelected && !this.mapBSelected) {
			this.natureVsParkingBarChart.setData(new float[] {
					natureAreaTotal, parkingAreaTotal,
					natureAreaMapA, parkingAreaMapA});
			this.natureVsParkingBarChart.setBarLabels(new String[] {
					"Gr\u00FCnfl\u00E4chen", "Parkpl\u00E4tze",
					districtMapAName, districtMapAName});
			this.natureVsParkingBarChart.setBarColour(
					new float[] {255, 0, 255, 0}, 
					cTable);
		} else if(!this.mapASelected && this.mapBSelected) {
			// Tritt nicht ein
		} else if(this.mapASelected && this.mapBSelected) {
			this.natureVsParkingBarChart.setData(new float[] {
					natureAreaTotal, parkingAreaTotal, 
					natureAreaMapA, parkingAreaMapA, 
					natureAreaMapB, parkingAreaMapB});
			this.natureVsParkingBarChart.setBarLabels(new String[] {
					"Gr\u00FCnfl\u00E4chen", "Parkpl\u00E4tze", 
					districtMapAName, districtMapAName,  
					districtMapBName, districtMapBName});
			
			this.natureVsParkingBarChart.setBarColour(
					new float[] {255, 0, 255, 0, 255, 0}, 
					cTable);
			this.natureVsParkingBarChart.setAxisLabelColour(pApplet.color(51));
		}
		this.natureVsParkingBarChart.showValueAxis(true);
		this.natureVsParkingBarChart.setValueFormat("# m²");
		this.natureVsParkingBarChart.showCategoryAxis(true);
		this.natureVsParkingBarChart.setMinValue(0);
		
		this.totalNatureRatio = Math.round((natureAreaTotal*100 / parkingAreaTotal))/100.;
		this.totalParkingRatio = 1;
		this.natureRatioA = Math.round((natureAreaMapA*100 / parkingAreaMapA))/100.;
		this.parkingRatioA = 1;
		this.natureRatioB = Math.round((natureAreaMapB*100 / parkingAreaMapB))/100.;
		this.parkingRatioB = 1;
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
				diagramWidth-40, diagramHeight-140);
		
		int ratioX = diagramX+20+80;
		int ratioY = diagramY+diagramHeight-80;
		
		String doublePoint = " : ";
		String zurichTxt = "Z\u00FCrich: ";
		String zurichNatureRatioTxt = "" + this.totalNatureRatio;
		String zurichParkingRatioTxt = "" + this.totalParkingRatio;
		String districtNameATxt = this.districtMapAName + ": ";
		String natureRatioATxt = "" + this.natureRatioA;
		String parkingRatioATxt = "" + this.parkingRatioA;
		String districtNameBTxt = this.districtMapBName + ": ";
		String natureRatioBTxt = "" + this.natureRatioB;
		String parkingRatioBTxt = "" + this.parkingRatioB;
		
		this.pApplet.fill(this.pApplet.color(51));
		this.pApplet.text("Ratios: ", diagramX+20, ratioY);
		this.pApplet.text(zurichTxt, ratioX, ratioY);
		this.pApplet.fill(this.pApplet.color(0,153,76));
		this.pApplet.text(zurichNatureRatioTxt, ratioX + 120, ratioY);
		this.pApplet.fill(this.pApplet.color(51));
		this.pApplet.text(doublePoint, ratioX + 120 + 60, ratioY);
		this.pApplet.fill(this.pApplet.color(0,76,153));
		this.pApplet.text(zurichParkingRatioTxt, ratioX + 120 + 50 + 50, ratioY);
		
		if(this.mapASelected) {
			this.pApplet.fill(this.pApplet.color(51));
			this.pApplet.text(districtNameATxt, ratioX, ratioY+30);
			this.pApplet.fill(this.pApplet.color(0,153,76));
			this.pApplet.text(natureRatioATxt, ratioX + 120, ratioY+30);
			this.pApplet.fill(this.pApplet.color(51));
			this.pApplet.text(doublePoint, ratioX + 120 + 60, ratioY+30);
			this.pApplet.fill(this.pApplet.color(0,76,153));
			this.pApplet.text(parkingRatioATxt, ratioX + 120 + 50 + 50, ratioY+30);
			if(this.mapBSelected) {
				this.pApplet.fill(this.pApplet.color(51));
				this.pApplet.text(districtNameBTxt, ratioX, ratioY+60);
				this.pApplet.fill(this.pApplet.color(0,153,76));
				this.pApplet.text(natureRatioBTxt, ratioX + 120, ratioY+60);
				this.pApplet.fill(this.pApplet.color(51));
				this.pApplet.text(doublePoint, ratioX + 120 + 60, ratioY+60);
				this.pApplet.fill(this.pApplet.color(0,76,153));
				this.pApplet.text(parkingRatioBTxt, ratioX + 120 + 50 + 50, ratioY+60);
			}
		}
		this.pApplet.fill(this.pApplet.color(255));
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
