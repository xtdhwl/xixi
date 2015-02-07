package net.shenru.xixi.pocket;

import java.util.ArrayList;

import net.shenru.xixi.ActionListener;

public class XixiService {

	private static XixiService instace;
	private ArrayList<ActionListener> listeners;
	private XixiService() {
		listeners = new ArrayList<ActionListener>();
	}

	public static XixiService getInstace() {
		if (instace == null) {
			instace = new XixiService();
		}
		return instace;
	}

	public void startLook(String str) {
		for(ActionListener l : listeners){
			if(l != null){
				l.startLook(str);
			}
		}
	}

	public void stopLook(String str) {
		for(ActionListener l : listeners){
			if(l != null){
				l.stopLook(str);
			}
		}
	}
	
	public void registerActionListener(ActionListener filter){
		if(!listeners.contains(filter)){
			listeners.add(filter);
		}
	}
	
	public void unregisterActionListener(ActionListener filter){
		if(listeners.contains(filter)){
			listeners.remove(filter);
		}
	}
}
