package com.maranan.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataContainer {

	public int count;

	public List<GetterSetter> list_youtube = new ArrayList<GetterSetter>();
	public List<GetterSetter> list_playlist = new ArrayList<GetterSetter>();

	public List<GetterSetter> list_AcceptedDedication = new ArrayList<GetterSetter>();
	public List<String> list_Count = new ArrayList<String>();
	public List<String> list_dates = new ArrayList<String>();
	public List<GetterSetter> list_NewsletterUp = new ArrayList<GetterSetter>();

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<GetterSetter> getList_NewsletterUp() {
		return list_NewsletterUp;
	}

	public void setList_NewsletterUp(List<GetterSetter> list_NewsletterUp) {
		this.list_NewsletterUp = list_NewsletterUp;
	}

	public HashMap<String, ArrayList<GetterSetter>> hashListNewsLetter = new HashMap<String, ArrayList<GetterSetter>>();

	public HashMap<String, ArrayList<GetterSetter>> getHashListNewsLetter() {
		return hashListNewsLetter;
	}

	public void setHashListNewsLetter(
			HashMap<String, ArrayList<GetterSetter>> hashListNewsLetter) {
		this.hashListNewsLetter = hashListNewsLetter;
	}

	public List<String> getList_dates() {
		return list_dates;
	}

	public void setList_dates(List<String> list_dates) {
		this.list_dates = list_dates;
	}

	public List<GetterSetter> getList_youtube() {
		return list_youtube;
	}

	public void setList_youtube(List<GetterSetter> list_youtube) {
		this.list_youtube = list_youtube;
	}

	public List<GetterSetter> getList_playlist() {
		return list_playlist;
	}

	public void setList_playlist(List<GetterSetter> list_playlist) {
		this.list_playlist = list_playlist;
	}

	public List<GetterSetter> getList_AcceptedDedication() {
		return list_AcceptedDedication;
	}

	public void setList_AcceptedDedication(
			List<GetterSetter> list_AcceptedDedication) {
		this.list_AcceptedDedication = list_AcceptedDedication;
	}

	public List<String> getList_Count() {
		return list_Count;
	}

	public void setList_Count(List<String> list_Count) {
		this.list_Count = list_Count;
	}

	public void setYoutubeList(List<GetterSetter> list_youtubes) {
		list_youtube = list_youtubes;

	}

	public List<GetterSetter> getYoutubeList() {
		return list_youtube;
	}

	public void setPlaylist(List<GetterSetter> list_playlists) {
		list_playlist = list_playlists;
	}

	public List<GetterSetter> getPlaylist() {
		return list_playlist;
	}

}
