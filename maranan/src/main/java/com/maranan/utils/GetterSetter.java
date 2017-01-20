package com.maranan.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

public class GetterSetter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String author;
	String auth_title;
	String vid_Id;
	String title;
	String descriptions;
	String channelTitle;
	String thumbnailHigh;
	String maxresThembUrl;
	String playListId;
	String total_videos;
	String time;
	String email;
	String signinwith;
	String id;
	String publish;
	String admin;
	String date;
	String status;
	String f_status;
	String l_status;
	String m_status;
	String radio_link;
	String duration;
	String nextPageToken;
	int pagePosition;
	int count;
	public int imageResource;
	public int colorResource;
	public int imagefavResource;
	String radio_programs;
	String phone;
	String location;
	String radio_alert;
	String alert_id;
	String seekBar;
	String progressBar;
	SeekBar seek;
	View seekBarView;
	String dateTimeText;
	String yourfav;
	String youtofav;
	String bothfav;
	String tag;
	String favEmail;
	String pdf;
	String year;
	String image;
	String publish_status;
	String pdf_pages;
	String total_size;
	String image_thumb;
	String unique_video_id;
	int countNew;

	public ArrayList<String> getListImage() {
		return listImage;
	}

	public void setListImage(ArrayList<String> listImage) {
		this.listImage = listImage;
	}

	ArrayList<String> listImage;
	
	public int imageResourceMan;
	public int imageResourceMap;
	
	public int getImageResourceMan() {
		return imageResourceMan;
	}

	public void setImageResourceMan(int imageResourceMan) {
		this.imageResourceMan = imageResourceMan;
	}

	public int getImageResourceMap() {
		return imageResourceMap;
	}

	public void setImageResourceMap(int imageResourceMap) {
		this.imageResourceMap = imageResourceMap;
	}

	public String getUnique_video_id() {
		return unique_video_id;
	}

	public void setUnique_video_id(String unique_video_id) {
		this.unique_video_id = unique_video_id;
	}
	
	public int getCountNew() {
		return countNew;
	}

	public void setCountNew(int countNew) {
		this.countNew = countNew;
	}

	public String getImage_thumb() {
		return image_thumb;
	}

	public void setImage_thumb(String image_thumb) {
		this.image_thumb = image_thumb;
	}

	public String getTotal_size() {
		return total_size;
	}

	public void setTotal_size(String total_size) {
		this.total_size = total_size;
	}

	public String getPdf_pages() {
		return pdf_pages;
	}

	public void setPdf_pages(String pdf_pages) {
		this.pdf_pages = pdf_pages;
	}

	public String getPublish_status() {
		return publish_status;
	}

	public void setPublish_status(String publish_status) {
		this.publish_status = publish_status;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getFavEmail() {
		return favEmail;
	}

	public void setFavEmail(String favEmail) {
		this.favEmail = favEmail;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getYourfav() {
		return yourfav;
	}

	public void setYourfav(String yourfav) {
		this.yourfav = yourfav;
	}

	public String getYoutofav() {
		return youtofav;
	}

	public void setYoutofav(String youtofav) {
		this.youtofav = youtofav;
	}

	public String getBothfav() {
		return bothfav;
	}

	public void setBothfav(String bothfav) {
		this.bothfav = bothfav;
	}

	public int getImagefavResource() {
		return imagefavResource;
	}

	public void setImagefavResource(int imagefavResource) {
		this.imagefavResource = imagefavResource;
	}
	
	public String getDateTimeText() {
		return dateTimeText;
	}

	public void setDateTimeText(String dateTimeText) {
		this.dateTimeText = dateTimeText;
	}

	public int getColorResource() {
		return colorResource;
	}

	public void setColorResource(int colorResource) {
		this.colorResource = colorResource;
	}
	
	public SeekBar getSeek() {
		return seek;
	}

	public View getSeekBarView() {
		return seekBarView;
	}

	public void setSeekBarView(View seekBarView) {
		this.seekBarView = seekBarView;
	}

	public void setSeek(SeekBar seek) {
		this.seek = seek;
	}

	ProgressBar pbar;
	
	public ProgressBar getPbar() {
		return pbar;
	}

	public void setPbar(ProgressBar pbar) {
		this.pbar = pbar;
	}

	public String getSeekBar() {
		return seekBar;
	}

	public void setSeekBar(String seekBar) {
		this.seekBar = seekBar;
	}

	public String getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(String progressBar) {
		this.progressBar = progressBar;
	}

	public String getAlert_id() {
		return alert_id;
	}

	public void setAlert_id(String alert_id) {
		this.alert_id = alert_id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRadio_alert() {
		return radio_alert;
	}

	public void setRadio_alert(String radio_alert) {
		this.radio_alert = radio_alert;
	}

	public String getRadio_programs() {
		return radio_programs;
	}

	public void setRadio_programs(String radio_programs) {
		this.radio_programs = radio_programs;
	}

	public String getNextPageToken() {
		return nextPageToken;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getRadio_link() {
		return radio_link;
	}

	public void setRadio_link(String radio_link) {
		this.radio_link = radio_link;
	}

	public int getImageResource() {
		return imageResource;
	}

	public void setImageResource(int imageResource) {
		this.imageResource = imageResource;
	}

	public String getF_status() {
		return f_status;
	}

	public void setF_status(String f_status) {
		this.f_status = f_status;
	}

	public String getL_status() {
		return l_status;
	}

	public void setL_status(String l_status) {
		this.l_status = l_status;
	}

	public String getM_status() {
		return m_status;
	}

	public void setM_status(String m_status) {
		this.m_status = m_status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getPublish() {
		return publish;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSigninwith() {
		return signinwith;
	}

	public void setSigninwith(String signinwith) {
		this.signinwith = signinwith;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	ArrayList<HashMap<String, String>> list;
	ArrayList<HashMap<String, Integer>> list_count;

	public ArrayList<HashMap<String, Integer>> getList_count() {
		return list_count;
	}

	public void setList_count(ArrayList<HashMap<String, Integer>> list_count) {
		this.list_count = list_count;
	}

	public String getTotal_videos() {
		return total_videos;
	}

	public void setTotal_videos(String total_videos) {
		this.total_videos = total_videos;
	}

	public int getPagePosition() {
		return pagePosition;
	}

	public void setPagePosition(int pagePosition) {
		this.pagePosition = pagePosition;
	}

	public ArrayList<HashMap<String, String>> getList() {
		return list;
	}

	public void setList(ArrayList<HashMap<String, String>> list) {
		this.list = list;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPlayListId() {
		return playListId;
	}

	public void setPlayListId(String playListId) {
		this.playListId = playListId;
	}

	public String getThumbnailHigh() {
		return thumbnailHigh;
	}

	public void setThumbnailHigh(String thumbnailHigh) {
		this.thumbnailHigh = thumbnailHigh;
	}

	public String getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuth_title() {
		return auth_title;
	}

	public void setAuth_title(String auth_title) {
		this.auth_title = auth_title;
	}

	public String getVid_Id() {
		return vid_Id;
	}

	public void setVid_Id(String vid_Id) {
		this.vid_Id = vid_Id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	// NEW GETTER SETTER

	public String getMaxresThembUrl() {
		return maxresThembUrl;
	}

	public void setMaxresThembUrl(String maxresThembUrl) {
		this.maxresThembUrl = maxresThembUrl;
	}

	// Getter Setter Values For Devote Screen
	String blessing;
	String nature;
	String name;
	String name_Optional;
	String there_Is;
	String sex;

	public String getBlessing() {
		return blessing;
	}

	public void setBlessing(String blessing) {
		this.blessing = blessing;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_Optional() {
		return name_Optional;
	}

	public void setName_Optional(String name_Optional) {
		this.name_Optional = name_Optional;
	}

	public String getThere_Is() {
		return there_Is;
	}

	public void setThere_Is(String there_Is) {
		this.there_Is = there_Is;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
