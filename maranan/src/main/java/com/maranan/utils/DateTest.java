package com.maranan.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.Log;

import com.maranan.R;
import com.maranan.adapters.PrayerAdapter;
import com.maranan.adapters.VideoListAdapter;

public class DateTest {

	static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
	Context ctx;
	String currentDateTime;
	String compareDateTime;

	@SuppressWarnings("deprecation")
	public DateTest(Context ctx, String currentDateTime, String compareDateTime, ArrayList<GetterSetter> list, int position, String activityName) {
		this.ctx = ctx;
		this.currentDateTime = currentDateTime;
		this.compareDateTime = compareDateTime;
		
		//Log.e("currentDateTime", "currentDateTime?? " + currentDateTime);
		//Log.e("compareDateTime", "compareDateTime?? " + compareDateTime);
		
		//Log.e("Position", "Position?? " + position);

		// TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jerusalem"));

		// diff between these 2 dates should be 1
		try {
			Date d1 = new Date(compareDateTime);
			Date d2 = new Date(currentDateTime);
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(d1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(d2);			
			//Log.e("Days", "Days?? "+daysBetween(cal1, cal2));
			// printOutput("Manual   ", d1, d2, calculateDays(d1, d2));
			// printOutput("Calendar ", d1, d2, daysBetween(cal1, cal2));
			setDateTimetext(cal1, cal2, list, position, activityName);
		    } catch (Exception e) {
			Log.e("Exception", "Exception?? "+e.getMessage());
		}
		
	}
    
	// Set date and time on the text view...
	private void setDateTimetext(Calendar cal1, Calendar cal2, ArrayList<GetterSetter> list, int position, String activityName) {
		if (daysBetween(cal1, cal2) == 0) {
			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.today_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.today_hebrew));
			}
			
			setDateTimeText(list, position, activityName);

		}else if (daysBetween(cal1, cal2) == 1) {
			
			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(
						String.valueOf(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" "+daysBetween(cal1, cal2) + " "
								+ ctx.getString(R.string.day_hebrew)));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(
						String.valueOf(ctx.getString(R.string.before_hebrew)+" "+daysBetween(cal1, cal2) + " "
								+ ctx.getString(R.string.day_hebrew)));
			}
			setDateTimeText(list, position, activityName);

		}else if (daysBetween(cal1, cal2) >= 1 && daysBetween(cal1, cal2) <= 7) { 

			if (daysBetween(cal1, cal2) == 7) {

				
				if (activityName.equals("PrayerActivity")) {
					PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.a_week_ago_hebrew));
				}else{
					VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.a_week_ago_hebrew));
				}
				setDateTimeText(list, position, activityName);

			} else {

				
				if (activityName.equals("PrayerActivity")) {
					PrayerAdapter.getInstance().getDateTimeTv().setText(
							String.valueOf(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" "+daysBetween(cal1, cal2) + " "
									+ ctx.getString(R.string.days_hebrew)));
				}else{
					VideoListAdapter.getInstance().getDateTimeTv().setText(
							String.valueOf(ctx.getString(R.string.before_hebrew)+" "+daysBetween(cal1, cal2) + " "
									+ ctx.getString(R.string.days_hebrew)));
				}
				setDateTimeText(list, position, activityName);

			}

		} else if (daysBetween(cal1, cal2) > 7 && daysBetween(cal1, cal2) < 30) {

			if(daysBetween(cal1, cal2) == 14){
				
				
				
				if (activityName.equals("PrayerActivity")) {
					PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.two_weeks_ago_hebrew));
				}else{
					VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.two_weeks_ago_hebrew));
				}
				setDateTimeText(list, position, activityName);
			
			}else if(daysBetween(cal1, cal2) == 21){
				
				
				if (activityName.equals("PrayerActivity")) {
					PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.three_weeks_ago_hebrew));
				}else{
					VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.three_weeks_ago_hebrew));
				}
				setDateTimeText(list, position, activityName);
			
			}else if(daysBetween(cal1, cal2) == 30){
				
				if (activityName.equals("PrayerActivity")) {
					PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.a_month_ago_hebrew));
				}else{
					VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.a_month_ago_hebrew));
				}
				setDateTimeText(list, position, activityName);
			
			}else {

				
				if (activityName.equals("PrayerActivity")) {
					PrayerAdapter.getInstance().getDateTimeTv().setText(
							String.valueOf(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" "+daysBetween(cal1, cal2) + " "
									+ ctx.getString(R.string.days_hebrew)));
				}else{
					VideoListAdapter.getInstance().getDateTimeTv().setText(
							String.valueOf(ctx.getString(R.string.before_hebrew)+" "+daysBetween(cal1, cal2) + " "
									+ ctx.getString(R.string.days_hebrew)));
				}
				setDateTimeText(list, position, activityName);

			}

		} else if (daysBetween(cal1, cal2) >= 31
				&& daysBetween(cal1, cal2) <= 60) {

			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.a_month_ago_hebrew));

			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.a_month_ago_hebrew));

			}
			setDateTimeText(list, position, activityName);

		} else if (daysBetween(cal1, cal2) >= 61
				&& daysBetween(cal1, cal2) <= 90) {

			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" " + "2" + " "
						+ ctx.getString(R.string.months_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.before_hebrew)+" " + "2" + " "
						+ ctx.getString(R.string.months_hebrew));
			}
			setDateTimeText(list, position, activityName);

		} else if (daysBetween(cal1, cal2) >= 91
				&& daysBetween(cal1, cal2) <= 120) {

			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" " + "3" + " "
						+ ctx.getString(R.string.months_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.before_hebrew)+" " + "3" + " "
						+ ctx.getString(R.string.months_hebrew));
			}
			setDateTimeText(list, position, activityName);

		} else if (daysBetween(cal1, cal2) >= 121
				&& daysBetween(cal1, cal2) <= 150) {

			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" " + "4" + " "
						+ ctx.getString(R.string.months_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.before_hebrew)+" " + "4" + " "
						+ ctx.getString(R.string.months_hebrew));
			}
			setDateTimeText(list, position, activityName);

		} else if (daysBetween(cal1, cal2) >= 151
				&& daysBetween(cal1, cal2) <= 180) {

			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" " + "5" + " "
						+ ctx.getString(R.string.months_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.before_hebrew)+" " + "5" + " "
						+ ctx.getString(R.string.months_hebrew));
			}
			setDateTimeText(list, position, activityName);

		} else if (daysBetween(cal1, cal2) >= 181
				&& daysBetween(cal1, cal2) <= 210) {

			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" " + "6" + " "
						+ ctx.getString(R.string.months_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.before_hebrew)+" " + "6" + " "
						+ ctx.getString(R.string.months_hebrew));
			}
			setDateTimeText(list, position, activityName);

		} else if (daysBetween(cal1, cal2) >= 211
				&& daysBetween(cal1, cal2) <= 240) {

			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" " + "7" + " "
						+ ctx.getString(R.string.months_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.before_hebrew)+" " + "7" + " "
						+ ctx.getString(R.string.months_hebrew));
			}
			setDateTimeText(list, position, activityName);

		} else if (daysBetween(cal1, cal2) >= 241
				&& daysBetween(cal1, cal2) <= 270) {

			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" " + "8" + " "
						+ ctx.getString(R.string.months_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.before_hebrew)+" " + "8" + " "
						+ ctx.getString(R.string.months_hebrew));
			}
			setDateTimeText(list, position, activityName);

		} else if (daysBetween(cal1, cal2) >= 271
				&& daysBetween(cal1, cal2) <= 300) {

			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" " + "9" + " "
						+ ctx.getString(R.string.months_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.before_hebrew)+" " + "9" + " "
						+ ctx.getString(R.string.months_hebrew));
			}
			setDateTimeText(list, position, activityName);

		} else if (daysBetween(cal1, cal2) >= 301
				&& daysBetween(cal1, cal2) <= 330) {

			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" " + "10" + " "
						+ ctx.getString(R.string.months_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.before_hebrew)+" " + "10" + " "
						+ ctx.getString(R.string.months_hebrew));
			}
			setDateTimeText(list, position, activityName);

		} else if (daysBetween(cal1, cal2) >= 331
				&& daysBetween(cal1, cal2) < 365) {

			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.before_hebrew)+" " + "11" + " "
						+ ctx.getString(R.string.months_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.before_hebrew)+" " + "11" + " "
						+ ctx.getString(R.string.months_hebrew));
			}
			setDateTimeText(list, position, activityName);

		} else if (daysBetween(cal1, cal2) == 365) {

			
			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.a_year_ago_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.a_year_ago_hebrew));
			}
			setDateTimeText(list, position, activityName);

		} else if (daysBetween(cal1, cal2) > 365) {

			

			if (activityName.equals("PrayerActivity")) {
				PrayerAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.last_visit)+" "+ctx.getString(R.string.a_year_ago_hebrew));
			}else{
				VideoListAdapter.getInstance().getDateTimeTv().setText(ctx.getString(R.string.a_year_ago_hebrew));
			}
			setDateTimeText(list, position, activityName);

		}

	}
    
	// Set date Time Text On Text View When Click On Item...
	private void setDateTimeText(ArrayList<GetterSetter> list, int position, String activityName) {
		if (activityName.equals("PrayerActivity")) {
			list.get(position).setDateTimeText(PrayerAdapter.getInstance().getDateTimeTv().getText().toString());
		}else{
			list.get(position).setDateTimeText(VideoListAdapter.getInstance().getDateTimeTv().getText().toString());
		}
	}

	@SuppressWarnings("unused")
	private static void printOutput(String type, Date d1, Date d2, long result) {
		System.out.println(type + "- Days between: " + sdf.format(d1) + " and "
				+ sdf.format(d2) + " is: " + result);
	}

	/** Manual Method - YIELDS INCORRECT RESULTS - DO NOT USE **/
	/* This method is used to find the no of days between the given dates */
	public static long calculateDays(Date dateEarly, Date dateLater) {
		return (dateLater.getTime() - dateEarly.getTime())
				/ (24 * 60 * 60 * 1000);
	}

	/** Using Calendar - THE CORRECT WAY **/
	public static long daysBetween(Calendar startDate, Calendar endDate) {
		Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}
}
