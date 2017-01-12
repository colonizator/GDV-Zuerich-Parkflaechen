package events;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.interactions.MouseHandler;
import processing.core.PApplet;

public class CustomEventDispatcher {

	private UnfoldingMap[] maps;
	private String[] mapIds;
	private EventDispatcher eventDispatcher;
	private MouseHandler mouseHandler;
	
	public CustomEventDispatcher(PApplet pApplet, UnfoldingMap... maps) {
		this.maps = maps;
		this.mapIds = mapIds();
		this.eventDispatcher = new EventDispatcher();
		this.mouseHandler = new MouseHandler(pApplet, maps);
	}
	
	private String[] mapIds() {
		List<String> ids = new ArrayList<String>();
		for(UnfoldingMap map : this.maps) {
			ids.add(map.getId());
		}
		return ids.toArray(new String[ids.size()]);
	}
	
	public void registerAllEvents() {
		registerPanEvent();
		registerZoomEvent();
		regiserMouseHandler();
	}
	
	public void registerPanEvent() {
		registerEvent("pan");
	}
	
	public void registerZoomEvent() {
		registerEvent("zoom");
	}
	
	private void registerEvent(String event) {
		for(UnfoldingMap map : this.maps) {
			this.eventDispatcher.register(map, event, this.mapIds);
		}
	}
	
	private void regiserMouseHandler() {
		this.eventDispatcher.addBroadcaster(this.mouseHandler);
	}
	
}
