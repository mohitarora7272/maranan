package com.maranan.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.maranan.ApplicationSingleton;
import com.maranan.ListOfAlerts;
import com.maranan.R;
import com.maranan.utils.ConnectionDetector;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class AlertsListAdapter extends BaseAdapter implements
        OnCompletionListener, OnPreparedListener, OnErrorListener {
    private Context context;
    private static AlertsListAdapter mContext;
    private ArrayList<GetterSetter> listalert;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private Boolean isClickEnable = true;
    private Boolean isFirsttime = true;
    private int pos;
    private View view;
    private int currentPosition = 0;

    // RadioLectureAdapter Instance
    public static AlertsListAdapter getInstance() {
        return mContext;
    }

    @SuppressWarnings("deprecation")
    public AlertsListAdapter(Context context, ArrayList<GetterSetter> listalert) {
        this.context = context;
        this.listalert = listalert;
        cd = new ConnectionDetector(context);
        isInternetPresent = cd.isConnectingToInternet();
        mContext = AlertsListAdapter.this;

        if (((ApplicationSingleton) context.getApplicationContext()).player2 == null) {
            ((ApplicationSingleton) context.getApplicationContext()).player2 = new MediaPlayer();
        }
        //mediaPlayer = new MediaPlayer();
    }

    @Override
    public int getCount() {
        return listalert.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //List<ViewHolder> dd = new ArrayList<AlertsListAdapter.ViewHolder>();
    Integer topPosition = null;

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.layout_alert_list_items, null, false);
            holder.img_alert = (ImageView) convertView.findViewById(R.id.img_alert);
            holder.tv_title_alerts = (TextView) convertView.findViewById(R.id.tv_title_alerts);
            holder.tv_alerts_des = (TextView) convertView.findViewById(R.id.tv_alerts_des);
            holder.tv_alerts_date_time = (TextView) convertView.findViewById(R.id.tv_alerts_date_time);
            holder.btn_loaction = (Button) convertView.findViewById(R.id.btn_loaction);
            holder.btn_phone_list = (Button) convertView.findViewById(R.id.btn_phone_list);
            holder.btn_swipe_left = (Button) convertView.findViewById(R.id.btn_swipe_left);
            holder.btn_play_pause = (Button) convertView.findViewById(R.id.btn_play_pause);
            holder.btn_play_pause.setBackgroundResource(R.drawable.play_icon_list);
            holder.progress_loading_list = (ProgressBar) convertView.findViewById(R.id.progress_loading_list);
            holder.seek_bar_list = (SeekBar) convertView.findViewById(R.id.seek_bar_list);
            holder.seek_bar_list.setOnSeekBarChangeListener(seekBarChangeListener);
            holder.seek_bar_list.setProgress(currentPosition);
            holder.seek_bar_list.setThumb(null);
            holder.seek_bar_list.setPadding(-1, 0, -1, 0);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title_alerts.setTag(position);
        holder.tv_alerts_des.setTag(position);
        holder.tv_alerts_date_time.setTag(position);
        holder.tv_title_alerts.setTag(position);
        holder.btn_loaction.setTag(position);
        holder.btn_phone_list.setTag(position);
        holder.btn_swipe_left.setTag(position);
        holder.btn_play_pause.setTag(position);
        holder.seek_bar_list.setTag(position);
        holder.progress_loading_list.setTag(position);

        if (listalert.get(position).getPhone() != null) {
            if (!listalert.get(position).getPhone().equals("")) {

                holder.btn_phone_list.setVisibility(View.VISIBLE);

            } else {

                holder.btn_phone_list.setVisibility(View.INVISIBLE);

            }

        } else {
            holder.btn_phone_list.setVisibility(View.INVISIBLE);
        }

        if (listalert.get(position).getImageResource() != -1) {
            holder.btn_play_pause.setVisibility(View.VISIBLE);
            holder.seek_bar_list.setProgress(0);

        } else {
            holder.btn_play_pause.setVisibility(View.INVISIBLE);
            holder.seek_bar_list.setProgress(0);
        }

        listalert.get(position).setSeek(holder.seek_bar_list);
        listalert.get(position).setPbar(holder.progress_loading_list);

        holder.tv_title_alerts.setText(Utilities.decodeImoString(listalert.get(position).getTitle()));
        holder.tv_alerts_des.setText(Utilities.decodeImoString(listalert.get(position).getDescriptions()));
        holder.tv_alerts_date_time.setText(listalert.get(position).getDate() + " " + listalert.get(position).getTime());

        Picasso.with(context)
                .load(listalert.get(position).getThumbnailHigh())
                .placeholder(R.drawable.below_thubnails)
                .error(R.drawable.below_thubnails).into(holder.img_alert);

        holder.btn_loaction.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });

        if (((ApplicationSingleton) context.getApplicationContext()).player2 != null) {
            if (((ApplicationSingleton) context.getApplicationContext()).player2.isPlaying() && !ListOfAlerts.getInstance().getAlertId().equals("")) {

                for (int i = 0; i < listalert.size(); i++) {
                    if (ListOfAlerts.getInstance().getAlertId().equals(listalert.get(i).getAlert_id())) {

                        //topPosition = i;
                        isClickEnable = true;
                        listalert.get(i).getPbar().setVisibility(View.INVISIBLE);
                        listalert.get(i).setImageResource(R.drawable.pause_icon_list);
                        ListOfAlerts.getInstance().setCompareId(listalert.get(i).getAlert_id());
                        notifyDataSetChanged();
                        listalert.get(i).getSeek().setOnSeekBarChangeListener(seekBarChangeListener);
                        updateSeekBar(((ApplicationSingleton) context.getApplicationContext()).player2, listalert.get(i).getSeek());

                        break;
                    }
                }
            }
        }

        holder.btn_phone_list.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ListOfAlerts.getInstance().getIntentValue() != null)
                    if (ListOfAlerts.getInstance().getIntentValue().equals(true)) {
                        if (((ApplicationSingleton) context.getApplicationContext()).player != null) {
                            if (((ApplicationSingleton) context.getApplicationContext()).player.isPlaying()) {
                                ((ApplicationSingleton) context.getApplicationContext()).player.pause();
                            }
                        }
                    }

                if (((ApplicationSingleton) context.getApplicationContext()).player2 != null && ((ApplicationSingleton) context.getApplicationContext()).player2.isPlaying()) {
                    ((ApplicationSingleton) context.getApplicationContext()).player2.pause();
                }
                Utilities.getCallIntent(context, listalert.get(position).getPhone());
            }
        });

        holder.btn_play_pause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isInternetPresent) {
                    pos = position;

                    if (listalert.get(position).getImageResource() == R.drawable.play_icon_list) {

                        if (ListOfAlerts.getInstance().getIntentValue() != null)
                            if (ListOfAlerts.getInstance().getIntentValue().equals(true)) {
                                if (((ApplicationSingleton) context.getApplicationContext()).player != null) {
                                    if (((ApplicationSingleton) context.getApplicationContext()).player.isPlaying()) {
                                        ((ApplicationSingleton) context.getApplicationContext()).player.pause();
                                    }
                                }
                            }

                        if (topPosition != null) {
                            if (topPosition == position) {
                                isFirsttime = false;
                            } else {
                                isFirsttime = true;
                            }
                        }

                        enableRadioProgram(arg0, position);

                    } else if (listalert.get(position).getImageResource() == R.drawable.pause_icon_list) {

                        if (isClickEnable) {
                            isFirsttime = false;

                            listalert.get(position).getPbar().setVisibility(View.GONE);
                            listalert.get(position).setImageResource(R.drawable.play_icon_list);

                            notifyDataSetChanged();

                            if (((ApplicationSingleton) context.getApplicationContext()).player2 != null && ((ApplicationSingleton) context.getApplicationContext()).player2.isPlaying()) {
                                ((ApplicationSingleton) context.getApplicationContext()).player2.pause();

                                if (ListOfAlerts.getInstance().getIntentValue() != null)
                                    if (ListOfAlerts.getInstance().getIntentValue().equals(true)) {
                                        if (((ApplicationSingleton) context.getApplicationContext()).player != null) {
                                            if (!((ApplicationSingleton) context.getApplicationContext()).player.isPlaying()) {
                                                ((ApplicationSingleton) context.getApplicationContext()).player.start();
                                            }
                                        }
                                    }
                            }

                        } else {
                            Utilities.showSnackbar(context,"Program Loading Please Wait...");
                        }
                    }

                } else {
                    Utilities.showAlertDialog(context, "Internet Connection Error", "Please connect to working Internet connection",
                            false);
                }
            }
        });

        if (listalert.get(position).getImageResource() != -1) {
            holder.btn_play_pause.setBackgroundResource(listalert.get(position).getImageResource());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_alert;
        TextView tv_title_alerts, tv_alerts_des, tv_alerts_date_time;
        Button btn_loaction, btn_phone_list, btn_swipe_left, btn_play_pause;
        ProgressBar progress_loading_list;
        SeekBar seek_bar_list;
    }

    // Enable Radio Program to play...
    private void enableRadioProgram(View arg0, int position) {

        if (!((ApplicationSingleton) context.getApplicationContext()).player2.isPlaying()) {

            if (isClickEnable) {

                //vie = dd.get(position);

                if (isFirsttime) {
                    if (!listalert.get(position).getRadio_alert().equals("")) {
                        view = arg0;

                        if (topPosition != null) {
                            if (listalert.get(topPosition).getSeek() != null) {
                                listalert.get(topPosition).getPbar().setVisibility(View.GONE);
                                listalert.get(topPosition).setImageResource(R.drawable.play_icon_list);
                                notifyDataSetChanged();
                            }
                        }

                        listalert.get(position).getPbar().setVisibility(View.VISIBLE);
                        isClickEnable = false;
                        arg0.setVisibility(View.VISIBLE);

                        startFromUrl(listalert.get(position).getRadio_alert());

                    } else {
                        Utilities.showSnackbar(context, "No file selected");
                    }

                } else {

                    listalert.get(position).setImageResource(R.drawable.pause_icon_list);
                    ((ApplicationSingleton) context.getApplicationContext()).player2.start();
                    isFirsttime = false;
                    notifyDataSetChanged();

                }

            } else {
                Utilities.showSnackbar(context, "Loading Please Wait...");
            }

        } else {

            isClickEnable = true;

            if (topPosition != null)
                if (topPosition == position) {
                    isFirsttime = false;
                    listalert.get(position).setImageResource(R.drawable.play_icon_list);
                    listalert.get(position).getPbar().setVisibility(View.GONE);

                    if (((ApplicationSingleton) context.getApplicationContext()).player2 != null && ((ApplicationSingleton) context.getApplicationContext()).player2.isPlaying()) {
                        ((ApplicationSingleton) context.getApplicationContext()).player2.pause();
                    }

                } else {
                    isFirsttime = true;
                    if (((ApplicationSingleton) context.getApplicationContext()).player2 != null && ((ApplicationSingleton) context.getApplicationContext()).player2.isPlaying()) {
                        ((ApplicationSingleton) context.getApplicationContext()).player2.stop();
                        enableRadioProgram(arg0, position);
                    }
                }


        }

    }

    // Here is the method to start mp3 from url...
    public void startFromUrl(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // String radioUrl = getStreamingURL(URL);
                try {
                    ((ApplicationSingleton) context.getApplicationContext()).player2.reset();
                    ((ApplicationSingleton) context.getApplicationContext()).player2 = new MediaPlayer();
                    ((ApplicationSingleton) context.getApplicationContext()).player2.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    ((ApplicationSingleton) context.getApplicationContext()).player2.setOnPreparedListener(AlertsListAdapter.this);
                    ((ApplicationSingleton) context.getApplicationContext()).player2.setOnCompletionListener(AlertsListAdapter.this);
                    ((ApplicationSingleton) context.getApplicationContext()).player2.setOnErrorListener(AlertsListAdapter.this);
                    ((ApplicationSingleton) context.getApplicationContext()).player2.setDataSource(context, Uri.parse(url));
                    ((ApplicationSingleton) context.getApplicationContext()).player2.prepareAsync();

                } catch (IllegalArgumentException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();

                } catch (IllegalStateException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();

                } catch (IOException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }

        }).start();
    }

    // Prepare Media player to play...
    @Override
    public void onPrepared(MediaPlayer mp) {
        topPosition = pos;
        isClickEnable = true;
        listalert.get(pos).getPbar().setVisibility(View.INVISIBLE);
        view.setVisibility(View.VISIBLE);
        listalert.get(pos).setImageResource(R.drawable.pause_icon_list);
        ListOfAlerts.getInstance().setCompareId(listalert.get(pos).getAlert_id());
        notifyDataSetChanged();

        if (!mp.isPlaying()) {
            mp.start();
            listalert.get(pos).getSeek().setOnSeekBarChangeListener(seekBarChangeListener);
            updateSeekBar(((ApplicationSingleton) context.getApplicationContext()).player2, listalert.get(pos).getSeek());

        }
    }

    // Oncompletion Listener To Listen Media Player
    @Override
    public void onCompletion(MediaPlayer mp) {
        isClickEnable = true;
//		listalert.get(pos).getSeek().setProgress(0);
//		//view.setBackgroundResource(R.drawable.play_icon_list);
//		listalert.get(pos).setImageResource(R.drawable.play_icon_list);
//		listalert.get(pos).getPbar().setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e("error??", "?? " + what);
        return true;
    }

    // SeekBar Change Listener Call Here...
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (((ApplicationSingleton) context.getApplicationContext()).player2 != null && fromUser) {
                ((ApplicationSingleton) context.getApplicationContext()).player2.seekTo(progress);
                seekBar.setProgress(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    // Update Seek Bar
    public void updateSeekBar(final MediaPlayer mediaPlayer, final SeekBar seekBar2) {

        // Worker thread that will update the seekbar as each song is playing
        Thread t = new Thread() {
            @Override
            public void run() {

                currentPosition = 0;
                int total = (int) mediaPlayer.getDuration();

                seekBar2.setMax(total);
                while (mediaPlayer.getCurrentPosition() < total) {
                    try {

                        Thread.sleep(1000);
                        currentPosition = mediaPlayer.getCurrentPosition();
                    } catch (InterruptedException e) {
                        return;
                    } catch (Exception e) {
                        return;
                    }
                    seekBar2.setProgress(mediaPlayer.getCurrentPosition());
                }

            }

        };
        t.start();
    }

    // get Media Player...
    public MediaPlayer getMdPlayer() {
        return ((ApplicationSingleton) context.getApplicationContext()).player2;
    }

    public void getAdapterNotify() {
        notifyDataSetChanged();
    }

    public void playNotificationAlert() {
        if (ListOfAlerts.getInstance().getIntentValue() != null)
            if (ListOfAlerts.getInstance().getIntentValue().equals(true)) {
                if (((ApplicationSingleton) context.getApplicationContext()).player != null) {
                    if (((ApplicationSingleton) context.getApplicationContext()).player.isPlaying()) {
                        ((ApplicationSingleton) context.getApplicationContext()).player.pause();
                    }
                }
            }

        // Here to check is Coming From Notification Or Not...
        if (Utilities.isComingFromNoti(context)) {
            ListOfAlerts.getInstance().setCompareId(Utilities.getAlertId(context));
            for (int i = 0; i < listalert.size(); i++) {
                if (Utilities.getAlertId(context).equals(listalert.get(i).getAlert_id())) {
                    topPosition = i;

                    if (((ApplicationSingleton) context.getApplicationContext()).player2.isPlaying()) {
                        listalert.get(i).setImageResource(R.drawable.pause_icon_list);
                        listalert.get(i).getSeek().setProgress(currentPosition);
                        updateSeekBar(((ApplicationSingleton) context.getApplicationContext()).player2, listalert.get(i).getSeek());
                        notifyDataSetChanged();
                    }
                    isClickEnable = false;
                    Utilities.setSharedPrefForNoti(context, false, Utilities.getAlertId(context), Utilities.getAlertURL(context));
                }
            }
        }
    }

    public void NotiUrl(String url) {
        startFromUrl(url);
    }

    public Boolean setClickEnable(Boolean isClickEnable) {
        return this.isClickEnable = isClickEnable;
    }
}
