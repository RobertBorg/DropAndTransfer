package com.rauban.dropandtransfer.model;

import java.util.LinkedList;

import com.rauban.dropandtransfer.model.speaker.Speaker;

public class SpeakerBaseImpl<T extends Speaker> {
	protected LinkedList<T> listeners;
	public SpeakerBaseImpl(){
		listeners = new LinkedList<T>();
	}
	public void addListener(T listener){
		listeners.add(listener);
	}
}
