package filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import charts.Chart;
import controlP5.CheckBox;
import controlP5.ControlP5;
import controlP5.Toggle;
import de.fhpotsdam.unfolding.marker.Marker;
import processing.core.PApplet;
import processing.core.PImage;

public class Filter {

	private PApplet pApplet;
	private int x;
	private int y;
	private int width;
	private int height;
	
	private int menuX;
	private int menuY;
	private int menuWidth;
	private int menuHeight;
	
	private PImage icon;
	private boolean showMenu;
	private CheckBox checkbox;
	private List<String> options;
	private List<Marker>[] markersLists;
	private String filterAttribute;
	
	private List<Chart> charts;
	
	@SafeVarargs
	public Filter(PApplet pApplet, int x, int y, int width, int height,
			List<String> options, String filterAttribute,
			Chart chart, List<Marker>... markersLists) {
		this.pApplet = pApplet;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.menuX = this.x-75;
		this.menuY = this.y + this.height + 10;
		this.menuWidth = 90;
		this.menuHeight = 140;
		
		this.icon = this.pApplet.loadImage("img/filter.png");
		this.options = options;
		ControlP5 cP5 = new ControlP5(pApplet);
		this.checkbox = cP5.addCheckBox("checkbox")
				.setPosition(x-70, y+height+15)
				.setSize(10, 10)
				.setItemsPerRow(1)
				.setSpacingColumn(0)
				.setSpacingRow(5);
		for(String option : this.options) {
			this.checkbox.addItem(option, 0);
		}
		this.checkbox.activateAll();
		
		this.markersLists = markersLists;
		this.filterAttribute = filterAttribute;
		this.charts = new ArrayList<>();
		this.charts.add(chart);
	}
	
	public void draw() {
		this.pApplet.fill(0);
		if(this.markersLists.length <= 1) {
			this.pApplet.text("Parkpl\u00E4tze", this.x-70, this.y+13);
		} else {
			this.pApplet.text("Gr\u00FCnfl\u00E4chen", this.x-80, this.y+13);
		}
		this.pApplet.image(this.icon, this.x, this.y, this.width, this.height);
		if(this.showMenu) {
			this.pApplet.fill(0);
			this.pApplet.rect(this.menuX, this.menuY, this.menuWidth, this.menuHeight);
			this.checkbox.setVisible(true);
		} else {
			this.checkbox.setVisible(false);
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY) {
		if(mouseX >= this.x && mouseX <= this.x + this.width
				&& mouseY >= this.y && mouseY <= this.y + this.height) {
			this.showMenu = !this.showMenu;
		} else if(mouseX >= this.menuX && mouseX <= this.menuX + this.menuWidth
				&& mouseY >= this.menuY && mouseY <= this.menuY + this.menuHeight) {
			filter();
			initCharts();
		} else {
			this.showMenu = false;
		}
	}
	
	private void filter() {
		Map<String, Boolean> filterMap = new HashMap<>();
		for(Toggle t : this.checkbox.getItems()) {
			filterMap.put(t.getLabel(), t.getState());
		}
		for(List<Marker> markersList : markersLists) {
			for(Marker marker : markersList) {
				if(marker instanceof Filterable) {
					Filterable m = (Filterable) marker;
					String attributeValue = 
							(String) marker.getProperty(filterAttribute);
					Boolean checked = filterMap.get(attributeValue);
					if(checked != null && checked) {
						m.setFiltered(false);
					} else {
						m.setFiltered(true);
					}
				} else {
					
				}
			}
		}
	}
	
	public void initCharts() {
		for(Chart chart : this.charts) {
			chart.init();
		}
	}
	
	public boolean isShowMenu() {
		return showMenu;
	}

	public void setShowMenu(boolean showMenu) {
		this.showMenu = showMenu;
	}
	
}
