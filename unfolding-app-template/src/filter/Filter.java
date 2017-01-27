package filter;

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
	
	private Chart chart;
	
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
		this.markersLists = markersLists;
		this.filterAttribute = filterAttribute;
		this.chart = chart;
		
		ControlP5 cP5 = new ControlP5(pApplet);
		this.checkbox = cP5.addCheckBox("checkbox")
				.setSize(10, 10)
				.setItemsPerRow(1)
				.setSpacingColumn(0)
				.setColorBackground(pApplet.color(224))
				.setColorForeground(pApplet.color(224))
				.setColorLabel(pApplet.color(51))
				.setSpacingRow(5);
		if(this.markersLists.length <= 1) {
			this.checkbox.setPosition(x-70, y+height+15);
		} else {
			this.checkbox.setPosition(x-80, y+height+15);
		}
		for(int i = 0; i < this.options.size(); i++) {
			String option = options.get(i);
			this.checkbox.addItem(option, 0);
			switch(option) {
				case "Blaue":
					this.checkbox.getItem(i).setColorActive(pApplet.color(0,160,227));
					break;
				case "Weisse":
					this.checkbox.getItem(i).setColorActive(pApplet.color(23,174,148));
					break;
				case "Behinderten":
					this.checkbox.getItem(i).setColorActive(pApplet.color(47,60,132));
					break;
				case "Gueterumschlag":
					this.checkbox.getItem(i).setColorActive(pApplet.color(102,157,210));
					break;
				case "Car":
					this.checkbox.getItem(i).setColorActive(pApplet.color(167,158,205));
					break;
				case "Elektro":
					this.checkbox.getItem(i).setColorActive(pApplet.color(14,119,0));
					break;
				case "Frauen":
					this.checkbox.getItem(i).setColorActive(pApplet.color(235,134,181));
					break;
				case "Gueter oder Taxi":
					this.checkbox.getItem(i).setColorActive(pApplet.color(142,0,193));
					break;
				case "Taxi":
					this.checkbox.getItem(i).setColorActive(pApplet.color(254,191,12));
					break;
				case "Gras":
					this.checkbox.getItem(i).setColorActive(pApplet.color(0, 220, 0));
					break;
				case "Wald":
					this.checkbox.getItem(i).setColorActive(pApplet.color(0, 70, 15));
					break;
				case "Park":
					this.checkbox.getItem(i).setColorActive(pApplet.color(0, 170, 0));
					break;
				default:
					break;
			}
		}
		this.checkbox.activateAll();
	}
	
	public void draw() {
		this.pApplet.stroke(102);
		if(this.markersLists.length <= 1) {
			this.pApplet.fill(249);
			this.pApplet.rect(this.x-75, this.y-2, 71, 20);
			this.pApplet.fill(51);
			this.pApplet.text("Parkpl\u00E4tze", this.x-70, this.y+13);
		} else {
			this.pApplet.fill(249);
			this.pApplet.rect(this.x-85, this.y-2, 82, 20);
			this.pApplet.fill(51);
			this.pApplet.text("Gr\u00FCnfl\u00E4chen", this.x-80, this.y+13);
		}
		this.pApplet.image(this.icon, this.x, this.y, this.width, this.height);
		if(this.showMenu) {
			this.pApplet.fill(255);
			if(this.markersLists.length <= 1) {
				this.pApplet.rect(this.menuX, this.menuY, this.menuWidth, this.menuHeight);
			} else {
				this.pApplet.rect(this.menuX-10, this.menuY, this.menuWidth+10, this.menuHeight);
			}
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
			this.chart.init();
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
	
	public boolean isShowMenu() {
		return showMenu;
	}

	public void setShowMenu(boolean showMenu) {
		this.showMenu = showMenu;
	}
	
}
