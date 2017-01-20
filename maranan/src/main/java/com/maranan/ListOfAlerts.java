package com.maranan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.maranan.adapters.AlertsListAdapter;
import com.maranan.database.NotificationDB;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.MediaPlayerNoti;
import com.maranan.utils.Utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ListOfAlerts extends Activity implements OnClickListener {
    private static ListOfAlerts mContext;
    private Button btn_back_action, btn_setting_action;
    private SwipeMenuListView list_swipe;
    private AlertsListAdapter adapter;
    private NotificationDB db;
    private ArrayList<GetterSetter> listAlert;
    private int REQUEST_CODE = 100;
    public String compare_ID = "";
    public String alertId = "";
    private MediaPlayerAction actionMediaPlayer;

    // ListOfAlerts Instance
    public static ListOfAlerts getInstance() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.activity_open_translate,
                R.anim.activity_close_scale);
        // Handle UnCaughtException Exception Handler
//		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(
//				this));

        mContext = ListOfAlerts.this;
        db = new NotificationDB(this);

        if (Utilities.isComingFromNoti(mContext) && db.isAlertIdExists(Utilities.getAlertId(mContext))) {

            getDateFromDB();

            if (listAlert != null && listAlert.size() > 0) {
                for (int i = 0; i < listAlert.size(); i++) {
                    if (Utilities.getAlertId(mContext).equals(listAlert.get(i).getAlert_id())) {
                        passIntentToListDeatail(i);
                        break;
                    }
                }

            }
            // Play Audio ALert When It Comes From Notification And Also Exists in
            // Database...
            //playAudioAlert();

        } else {

            getDateFromDB();
        }

        setContentView(R.layout.layout_alert_list);
        initializeView();
    }

    /* Get data from database according to notification received */
    private void getDateFromDB() {

        if (db.getAllRecords().size() > 0) {

            listAlert = new ArrayList<GetterSetter>();
            listAlert = db.getAllRecords();
            Collections.reverse(listAlert);

        } else {
            listAlert = new ArrayList<GetterSetter>();
            db = new NotificationDB(this);
        }
    }

    // Play Audio Alert When Alert Notification Comes With Sound Alert...
    private void playAudioAlert() {
        if (Utilities.isComingFromNoti(this) && db.isAlertIdExists(Utilities.getAlertId(mContext))) {
            if (actionMediaPlayer != null) {
                actionMediaPlayer = null;
            }
            actionMediaPlayer = new MediaPlayerAction(this, Utilities.getAlertURL(this));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listAlert.size() > 0) {
            setAdapter();
        }

        setSwipeMenuItem();
    }

    // Initialize Views here...
    private void initializeView() {
        list_swipe = (SwipeMenuListView) findViewById(R.id.list_swipe);
        btn_back_action = (Button) findViewById(R.id.btn_back_action);
        btn_back_action.setOnClickListener(this);
        btn_setting_action = (Button) findViewById(R.id.btn_setting_action);
        btn_setting_action.setOnClickListener(this);
    }

    private void setSwipeMenuItem() {
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                // deleteItem.setBackground(new ColorDrawable(Color.rgb(244, 35,
                // 69)));
                // set item background
                deleteItem.setBackground(R.drawable.delete_red);
                // set item width
                deleteItem.setWidth(dp2px(120));
                // set a icon
                deleteItem.setIcon(R.drawable.delete_icon_list);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        list_swipe.setMenuCreator(creator);

        // step 2. listener item click event
        list_swipe
                .setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position,
                                                   SwipeMenu menu, int index) {
                        switch (index) {
                            case 0:
                                if (listAlert.size() > 0) {
                                    if (AlertsListAdapter.getInstance()
                                            .getMdPlayer() != null) {
                                        if (AlertsListAdapter.getInstance()
                                                .getMdPlayer().isPlaying()) {
                                            AlertsListAdapter.getInstance()
                                                    .getMdPlayer().stop();
                                            AlertsListAdapter.getInstance()
                                                    .getMdPlayer().release();
                                        }
                                    }
                                    db.deleteSingleRecord(listAlert.get(position)
                                            .getAlert_id());
                                    listAlert.remove(position);
                                }
                                adapter.notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });

        list_swipe.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (AlertsListAdapter.getInstance().getMdPlayer() != null) {
                    if (AlertsListAdapter.getInstance().getMdPlayer()
                            .isPlaying()) {
                        if (compare_ID.equals(listAlert.get(position).getAlert_id())) {
                            passIntentToListDeatail(position);
                        } else {

                            Utilities.showSnackbar(mContext, "Please Stop Playing Alert OR You Have To Select Playing Alert");

                        }
                    } else {
                        passIntentToListDeatail(position);
                    }
                }

            }
        });
    }

    private void passIntentToListDeatail(int position) {
        alertId = "";
        Intent fullView = new Intent(this, ListDetailed.class);
        fullView.putExtra("alert_id", listAlert.get(position).getAlert_id());
        fullView.putExtra("title", listAlert.get(position).getTitle());
        fullView.putExtra("description", listAlert.get(position).getDescriptions());
        fullView.putExtra("date", listAlert.get(position).getDate());
        fullView.putExtra("time", listAlert.get(position).getTime());
        fullView.putExtra("phone", listAlert.get(position).getPhone());
        fullView.putExtra("radio_alert", listAlert.get(position).getRadio_alert());
        fullView.putExtra("image", listAlert.get(position).getThumbnailHigh());
        startActivityForResult(fullView, REQUEST_CODE);
    }

    // Set List Adapter Here...
    private void setAdapter() {
        adapter = new AlertsListAdapter(this, listAlert);
        list_swipe.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // Onclick Listeners...
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_action:
                if (listAlert.size() > 0) {
                    if (AlertsListAdapter.getInstance().getMdPlayer() != null) {
                        if (AlertsListAdapter.getInstance().getMdPlayer()
                                .isPlaying()) {
                            AlertsListAdapter.getInstance().getMdPlayer().stop();
                            AlertsListAdapter.getInstance().getMdPlayer().release();
                            ((ApplicationSingleton) getApplicationContext()).player2 = null;
                        }
                    }
                }

                Intent mintent = new Intent();
                mintent.putExtra("isPlayerPlaying", getIntent()
                        .getSerializableExtra("isPlayerPlaying"));
                setResult(RESULT_OK, mintent);
                finish();
                overridePendingTransition(R.anim.activity_open_scale,
                        R.anim.activity_close_translate);
                break;

            case R.id.btn_setting_action:

                break;

            default:
                break;
        }

    }

    // Convert dp To pixel format...
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    // Get Intent Values To Stop An Start Player in the other activity...
    public Serializable getIntentValue() {
        return getIntent().getSerializableExtra("isPlayerPlaying");
    }

    @Override
    public void onBackPressed() {
        if (listAlert.size() > 0) {
            if (((ApplicationSingleton) getApplicationContext()).player2 != null) {
                if (((ApplicationSingleton) getApplicationContext()).player2.isPlaying()) {
                    ((ApplicationSingleton) getApplicationContext()).player2.stop();
                    ((ApplicationSingleton) getApplicationContext()).player2.release();
                    ((ApplicationSingleton) getApplicationContext()).player2 = null;
                }
            }
        }

        Intent mintent = new Intent();
        mintent.putExtra("isPlayerPlaying",
                getIntent().getSerializableExtra("isPlayerPlaying"));
        setResult(RESULT_OK, mintent);
        finish();
        overridePendingTransition(R.anim.activity_open_scale,
                R.anim.activity_close_translate);
        super.onBackPressed();
    }

    // OnActivity Result Call...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE){

                alertId = data.getStringExtra("alert_id");
            }
        }

    }

    // Get Compare Id...
    public String getCompareId() {
        return compare_ID;
    }

    // Set Compare Id...
    public String setCompareId(String id) {
        return compare_ID = id;
    }

    // Get Alert Id...
    public String getAlertId() {
        return alertId;
    }

    // Here Is The Class To Handle Media Player Action During Notification...
    class MediaPlayerAction extends MediaPlayerNoti {

        public MediaPlayerAction(Context context, String url) {
            super(context, url);
        }
    }
}
