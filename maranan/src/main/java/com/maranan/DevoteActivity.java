package com.maranan;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.maranan.adapters.DevoteAdapter;
import com.maranan.database.MarananDB;
import com.maranan.utils.Config;
import com.maranan.utils.ConnectionDetector;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.ObjectSerializer;
import com.maranan.utils.Utilities;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class DevoteActivity extends Activity implements OnClickListener {
    private static DevoteActivity mContext;
    private Spinner spnr_one_blessing, spnr_two_nature;
    private AutoCompleteTextView edt_name, edt_there_is, edt_name_optional;
    private Button btn_woman, btn_men, btn_save, btn_microphone_firstname,
            btn_microphone_nameoptional, btn_microphone_thereIs;
    private CheckBox chk_box;
    private boolean isCheck = true;
    private ImageView arrow_action;
    private DynamicListView lv_list_register;
    private DevoteAdapter madapter;
    private String blessing_str = null;
    private String nature_str = null;
    private String sex_str, id;
    private ArrayList<GetterSetter> listDevote;
    private ArrayList<String> listFN;
    private ArrayList<String> listTI;
    private ArrayList<String> listLN;
    private InputFilter filter;
    private List<String> list_Bless, list_choice1, list_choice2, list_choice3,
            list_choice4;
    private boolean clickedMen = true;
    private boolean clickedWoman = false;
    private LinearLayout linear_devote;
    private MarananDB db;
    private SQLiteDatabase sqlitedb;
    private GetterSetter getset;
    private SharedPreferences pref;
    private SharedPreferences prefList;
    private DrawerLayout drawer_layout_devote;
    private RelativeLayout realtive_devote;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView tv_devote_side;
    private ImageView img_devote;
    private static int REQUEST_CODE_LOGIN = 500;
    private final int REQ_CODE_SPEECH_INPUT_ONE = 100;
    private final int REQ_CODE_SPEECH_INPUT_TWO = 200;
    private final int REQ_CODE_SPEECH_INPUT_THREE = 300;
    private int REQUEST_CODE_LIST_OF_ALERTS_ITEM = 600;
    private ProgressBar pDialog;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private String str;
    private String part1;

    // DevoteActivity Instance
    public static DevoteActivity getInstance() {
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
//        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(
//                this));
        setContentView(R.layout.activity_devote);
        mContext = DevoteActivity.this;
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();
        db = new MarananDB(this);
        initializeView();
        getPrefrenceValue();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_microphone_firstname = (Button) findViewById(R.id.btn_microphone_firstname);
        btn_microphone_firstname.setOnClickListener(this);
        btn_microphone_firstname.setTag(R.drawable.mike_icon_gray);
        btn_microphone_firstname
                .setBackgroundResource(R.drawable.mike_icon_gray);
        btn_microphone_nameoptional = (Button) findViewById(R.id.btn_microphone_nameoptional);
        btn_microphone_nameoptional.setOnClickListener(this);
        btn_microphone_nameoptional.setTag(R.drawable.mike_icon_gray);
        btn_microphone_nameoptional
                .setBackgroundResource(R.drawable.mike_icon_gray);
        btn_microphone_thereIs = (Button) findViewById(R.id.btn_microphone_thereIs);
        btn_microphone_thereIs.setOnClickListener(this);
        btn_microphone_thereIs.setTag(R.drawable.mike_icon_gray);
        btn_microphone_thereIs.setBackgroundResource(R.drawable.mike_icon_gray);
    }

    // Get Preference Value for getting email id
    @SuppressWarnings("unchecked")
    private void getPrefrenceValue() {
        pref = getApplicationContext().getSharedPreferences("RegisterPref",
                MODE_PRIVATE);
        if (pref.getString("email", null) != null)
            if (isInternetPresent) {

                new GetDedication().execute(pref.getString("email", null),
                        pref.getString("signinwith", null));

            } else {
                Utilities.showAlertDialog(DevoteActivity.this,
                        "Internet Connection Error",
                        "Please connect to working Internet connection", false);
            }

        // Check Array List For Names Add in the Shareprefrences for
        // Autocomplete TextView
        prefList = getApplicationContext().getSharedPreferences("ListPref",
                MODE_PRIVATE);

        try {
            listFN = new ArrayList<String>();
            listFN = (ArrayList<String>) ObjectSerializer
                    .deserialize(prefList
                            .getString("FirstNameList", ObjectSerializer
                                    .serialize(new ArrayList<String>())));
            setFirstNameAdapter(listFN);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            listTI = new ArrayList<String>();
            listTI = (ArrayList<String>) ObjectSerializer
                    .deserialize(prefList
                            .getString("ThereIsList", ObjectSerializer
                                    .serialize(new ArrayList<String>())));
            setThereIsAdapter(listTI);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            listLN = new ArrayList<String>();
            listLN = (ArrayList<String>) ObjectSerializer
                    .deserialize(prefList
                            .getString("LastNameList", ObjectSerializer
                                    .serialize(new ArrayList<String>())));
            setLastNameAdapter(listLN);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Set First Name Adapter Here...
    private void setLastNameAdapter(ArrayList<String> listLN) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, listLN);
        edt_name_optional.setAdapter(adapter);
    }

    // Set There Is Adapter Here..
    private void setThereIsAdapter(ArrayList<String> listTI) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, listTI);
        edt_there_is.setAdapter(adapter);
    }

    // Set Last Name Adapter Here..
    private void setFirstNameAdapter(ArrayList<String> listFN) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, listFN);
        edt_name.setAdapter(adapter);
    }

    // Get SharedPreferences Using Instance..
    public SharedPreferences getPref() {
        pref = getApplicationContext().getSharedPreferences("RegisterPref",
                MODE_PRIVATE);
        return pref;
    }

    // initializeView
    private void initializeView() {
        pDialog = (ProgressBar) findViewById(R.id.progressBar);
        pDialog.setVisibility(View.GONE);
        drawer_layout_devote = (DrawerLayout) findViewById(R.id.drawer_layout_devote);
        drawer_layout_devote.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        img_devote = (ImageView) findViewById(R.id.img_devote);
        img_devote.setOnClickListener(this);
        tv_devote_side = (TextView) findViewById(R.id.tv_devote_side);
        tv_devote_side.setOnClickListener(this);
        realtive_devote = (RelativeLayout) findViewById(R.id.realtive_devote);
        listDevote = new ArrayList<GetterSetter>();
        spnr_one_blessing = (Spinner) findViewById(R.id.spnr_one);
        spnr_two_nature = (Spinner) findViewById(R.id.spnr_two);
        setValuesOnSpinner();
        btn_woman = (Button) findViewById(R.id.btn_woman);
        btn_woman.setOnClickListener(this);
        btn_men = (Button) findViewById(R.id.btn_men);
        btn_men.setOnClickListener(this);
        sex_str = btn_men.getText().toString();
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        edt_name = (AutoCompleteTextView) findViewById(R.id.edt_name);
        edt_name.addTextChangedListener(mTextEditorWatcher);
        edt_there_is = (AutoCompleteTextView) findViewById(R.id.edt_there_is);
        edt_there_is.addTextChangedListener(mTextEditorWatcher);
        edt_name_optional = (AutoCompleteTextView) findViewById(R.id.edt_name_optional);
        edt_name_optional.addTextChangedListener(mTextEditorWatcher);
        arrow_action = (ImageView) findViewById(R.id.arrow_action);
        linear_devote = (LinearLayout) findViewById(R.id.linear_devote1);
        chk_box = (CheckBox) findViewById(R.id.chk_box);
        chk_box.setChecked(true);
        lv_list_register = (DynamicListView) findViewById(R.id.lv_list_register);
        Utilities.setListViewHeightBasedOnChildren(lv_list_register);
        lv_list_register.setOnTouchListener(new OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside
            // ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of
                // child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        // Swipe to dismiss list view item
        lv_list_register.enableSwipeToDismiss(new OnDismissCallback() {
            @Override
            public void onDismiss(final ViewGroup listView,
                                  final int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {

                    if (listDevote.size() > 0)
                        new DeleteDedication().execute(listDevote.get(position)
                                .getId());
                    db.deleteSingleRecord(listDevote.get(position).getId());
                    listDevote.remove(String.valueOf(position));
                    listDevote.remove(position);
                    DevoteAdapter.getInstance().getTimeList()
                            .remove(String.valueOf(position));
                    DevoteAdapter.getInstance().getTimeList().remove(position);
                    madapter.notifyDataSetChanged();
                }
            }
        });

        // check listener on check Box
        chk_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    isCheck = isChecked;
                    // chk_box.setButtonDrawable(R.drawable.chk_black);

                } else {

                    // chk_box.setButtonDrawable(R.drawable.chk_white);
                    isCheck = isChecked;

                }
            }
        });

        filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; ++i) {
                    if (!Pattern
                            .compile(
                                    "[a-z A-Z -_₪ \" 'אףשנבגקכעיןחלךצמ�?פרדוהסטזתץם]*")
                            .matcher(String.valueOf(source.charAt(i)))
                            .matches()) {
                        return "";
                    }
                }

                return null;
            }
        };

        arrow_action.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                linear_devote
                        .setVisibility(linear_devote.getVisibility() == View.VISIBLE ? View.GONE
                                : View.VISIBLE);

                if (linear_devote.getVisibility() == View.VISIBLE) {
                    arrow_action.setBackgroundResource(R.drawable.arrow_bottom);

                } else {
                    arrow_action.setBackgroundResource(R.drawable.arrow_top);

                }

            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout_devote,
                R.drawable.maranan_icon, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {
            public void onDrawerClosed(View view) {
            }

            public void onDrawerOpened(View drawerView) {
            }
        };
        drawer_layout_devote.setDrawerListener(mDrawerToggle);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_woman:
                clickedMen = false;
                clickedWoman = true;
                btn_woman.setBackgroundResource(R.drawable.blue_box);
                btn_men.setBackgroundResource(R.drawable.input);
                sex_str = btn_woman.getText().toString();
                checkItemSelection();
                break;

            case R.id.btn_men:
                clickedMen = true;
                clickedWoman = false;
                btn_woman.setBackgroundResource(R.drawable.input);
                btn_men.setBackgroundResource(R.drawable.blue_box);
                sex_str = btn_men.getText().toString();
                checkItemSelection();
                break;

            case R.id.btn_save:
                btn_microphone_firstname
                        .setBackgroundResource(R.drawable.mike_icon_gray);
                btn_microphone_firstname.setTag(R.drawable.mike_icon_gray);
                btn_microphone_nameoptional
                        .setBackgroundResource(R.drawable.mike_icon_gray);
                btn_microphone_nameoptional.setTag(R.drawable.mike_icon_gray);
                btn_microphone_thereIs
                        .setBackgroundResource(R.drawable.mike_icon_gray);
                btn_microphone_thereIs.setTag(R.drawable.mike_icon_gray);

                if (edt_name.length() == 0) {

                    edt_name.setBackgroundResource(R.drawable.input_red);
                    Utilities.showSnackbar(this, "This field is mandatory");

                } else if (edt_there_is.length() == 0) {

                    edt_there_is.setBackgroundResource(R.drawable.input_red);
                    Utilities.showSnackbar(this, "This field is mandatory");


                } else if (edt_name.getText().toString()
                        .equals(edt_there_is.getText().toString())) {

                    Utilities.showSnackbar(this, "Same Name Does Not Exist");

                    edt_name.setBackgroundResource(R.drawable.input_red);
                    edt_there_is.setBackgroundResource(R.drawable.input_red);

                } else if (isCheck == false) {

                    Utilities.showSnackbar(this, "This field is mandatory");

                } else if (pref.getString("email", null) == null) {

                    Intent registerInt = new Intent(DevoteActivity.this,
                            LogInActivity.class);
                    startActivityForResult(registerInt, REQUEST_CODE_LOGIN);

                } else if (getComparisonString(edt_name.getText().toString()) == true) {

                    edt_name.setBackgroundResource(R.drawable.input_red);
                    Utilities.showSnackbar(this, "The system has detected an illegal word in name");

                } else if (getComparisonString(edt_there_is.getText().toString()) == true) {

                    edt_there_is.setBackgroundResource(R.drawable.input_red);
                    Utilities.showSnackbar(this, "The system has detected an illegal word in there is");


                } else if (getComparisonString(edt_name_optional.getText().toString()) == true) {
                    edt_name_optional.setBackgroundResource(R.drawable.input_red);
                    Utilities.showSnackbar(this, "The system has detected an illegal word in name optional");


                } else if (!db.isFirstNameExist(edt_name.getText().toString()
                        .replace("'", "*").trim())
                        || !db.isThereIsExist(edt_there_is.getText().toString()
                        .replace("'", "*").trim())) {

                    setDedicationValues();

                    if (!edt_name.getText().equals("")) {
                        for (int i = 0; i < listFN.size(); i++) {
                            if (!edt_name.getText().toString()
                                    .equals(listFN.get(i))) {
                                listFN.add(edt_name.getText().toString());
                            }
                        }
                        setFirstNameAdapter(listFN);
                        Editor editor = prefList.edit();
                        try {
                            editor.putString("FirstNameList",
                                    ObjectSerializer.serialize(listFN));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                    }

                    if (!edt_there_is.getText().equals("")) {
                        for (int i = 0; i < listTI.size(); i++) {
                            if (!edt_there_is.getText().toString()
                                    .equals(listTI.get(i))) {
                                listTI.add(edt_there_is.getText().toString());
                            }
                        }
                        setThereIsAdapter(listTI);
                        Editor editor = prefList.edit();
                        try {
                            editor.putString("ThereIsList",
                                    ObjectSerializer.serialize(listTI));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                    }

                    if (!edt_name_optional.getText().equals("")) {
                        for (int i = 0; i < listLN.size(); i++) {
                            if (!edt_name_optional.getText().toString()
                                    .equals(listLN.get(i))) {
                                listLN.add(edt_name_optional.getText().toString());
                            }
                        }
                        setLastNameAdapter(listLN);
                        Editor editor = prefList.edit();
                        try {
                            editor.putString("LastNameList",
                                    ObjectSerializer.serialize(listLN));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                    }

                } else if (db.isFirstNameExist(edt_name.getText().toString()
                        .replace("'", "*").trim())
                        && db.isThereIsExist(edt_there_is.getText().toString()
                        .replace("'", "*").trim())
                        && !db.isNameOptExist(edt_name_optional.getText()
                        .toString().replace("'", "*").trim())) {

                    setDedicationValues();
                    if (!edt_name.getText().equals("")) {
                        listFN.add(edt_name.getText().toString());
                        setFirstNameAdapter(listFN);
                        Editor editor = prefList.edit();
                        try {
                            editor.putString("FirstNameList",
                                    ObjectSerializer.serialize(listFN));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                    }

                    if (!edt_there_is.getText().equals("")) {
                        listTI.add(edt_there_is.getText().toString());
                        setThereIsAdapter(listTI);
                        Editor editor = prefList.edit();
                        try {
                            editor.putString("ThereIsList",
                                    ObjectSerializer.serialize(listTI));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                    }

                    if (!edt_name_optional.getText().equals("")) {
                        listLN.add(edt_name_optional.getText().toString());
                        setLastNameAdapter(listLN);
                        Editor editor = prefList.edit();
                        try {
                            editor.putString("LastNameList",
                                    ObjectSerializer.serialize(listLN));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                    }

                } else {

                    edt_there_is.setBackgroundResource(R.drawable.input_red);
                    edt_name.setBackgroundResource(R.drawable.input_red);
                    Utilities.showSnackbar(this, "You can't enter same words again");
                }

                break;

            case R.id.img_devote:

                Utilities.logout(mContext);
                drawer_layout_devote.closeDrawer(realtive_devote);
                break;

            case R.id.tv_devote_side:

                Utilities.logout(mContext);
                drawer_layout_devote.closeDrawer(realtive_devote);
                break;

            case R.id.btn_microphone_firstname:
                btn_microphone_nameoptional
                        .setBackgroundResource(R.drawable.mike_icon_gray);
                btn_microphone_nameoptional.setTag(R.drawable.mike_icon_gray);
                btn_microphone_thereIs
                        .setBackgroundResource(R.drawable.mike_icon_gray);
                btn_microphone_thereIs.setTag(R.drawable.mike_icon_gray);

                if (Integer.parseInt(btn_microphone_firstname.getTag().toString()) == R.drawable.mike_icon_gray) {

                    btn_microphone_firstname
                            .setBackgroundResource(R.drawable.mike_icon_black);
                    btn_microphone_firstname.setTag(R.drawable.mike_icon_black);
                    promptSpeechInput("first_name");

                } else if (Integer.parseInt(btn_microphone_firstname.getTag()
                        .toString()) == R.drawable.mike_icon_black) {

                    btn_microphone_firstname
                            .setBackgroundResource(R.drawable.mike_icon_gray);
                    btn_microphone_firstname.setTag(R.drawable.mike_icon_gray);

                }
                break;

            case R.id.btn_microphone_thereIs:
                btn_microphone_firstname
                        .setBackgroundResource(R.drawable.mike_icon_gray);
                btn_microphone_firstname.setTag(R.drawable.mike_icon_gray);
                btn_microphone_nameoptional
                        .setBackgroundResource(R.drawable.mike_icon_gray);
                btn_microphone_nameoptional.setTag(R.drawable.mike_icon_gray);

                if (Integer.parseInt(btn_microphone_thereIs.getTag().toString()) == R.drawable.mike_icon_gray) {

                    btn_microphone_thereIs
                            .setBackgroundResource(R.drawable.mike_icon_black);
                    btn_microphone_thereIs.setTag(R.drawable.mike_icon_black);
                    promptSpeechInput("there_is");

                } else if (Integer.parseInt(btn_microphone_firstname.getTag()
                        .toString()) == R.drawable.mike_icon_black) {

                    btn_microphone_thereIs
                            .setBackgroundResource(R.drawable.mike_icon_gray);
                    btn_microphone_thereIs.setTag(R.drawable.mike_icon_gray);

                }
                break;

            case R.id.btn_microphone_nameoptional:
                btn_microphone_firstname
                        .setBackgroundResource(R.drawable.mike_icon_gray);
                btn_microphone_firstname.setTag(R.drawable.mike_icon_gray);
                btn_microphone_thereIs
                        .setBackgroundResource(R.drawable.mike_icon_gray);
                btn_microphone_thereIs.setTag(R.drawable.mike_icon_gray);

                if (Integer.parseInt(btn_microphone_nameoptional.getTag()
                        .toString()) == R.drawable.mike_icon_gray) {

                    btn_microphone_nameoptional
                            .setBackgroundResource(R.drawable.mike_icon_black);
                    btn_microphone_nameoptional.setTag(R.drawable.mike_icon_black);
                    promptSpeechInput("name_optional");

                } else if (Integer.parseInt(btn_microphone_nameoptional.getTag()
                        .toString()) == R.drawable.mike_icon_black) {

                    btn_microphone_nameoptional
                            .setBackgroundResource(R.drawable.mike_icon_gray);
                    btn_microphone_nameoptional.setTag(R.drawable.mike_icon_gray);

                }
                break;
        }
    }

    // Get Comparison String Here..
    public boolean getComparisonString(String newStr) {
        boolean find = false;
        if (newStr.length() > 0) {
            String[] parts = newStr.split(" ");
            for (int i = 0; i < parts.length; i++) {
                if (!parts[0].equals("")) {
                    part1 = parts[i];
                }
            }

            str = part1.replace(" ", "");
            str = str.replace("'", "");
            if (!str.equals("")) {
                if (Locale.getDefault().getDisplayLanguage()
                        .equals(getResources().getString(R.string.hebrew_lang))) {
                    str = str.substring(0, str.length() - 1);

                } else {
                    str = str.substring(0, str.length() - 1);
                }
            }
        }

        char[] str1 = null;
        if (!str.equals("")) {
            str1 = str.toCharArray();
            if (str1 != null) {
                for (int i = 0; i < str1.length; i++) {
                    if (Character.toString(str1[i]).equals(
                            getResources().getString(R.string.one_str).trim())) {
                        find = true;
                        break;
                    } else if (Character.toString(str1[i]).equals(
                            getResources().getString(R.string.two_str).trim())) {
                        find = true;
                        break;
                    } else if (Character.toString(str1[i])
                            .equals(getResources()
                                    .getString(R.string.three_str).trim())) {
                        find = true;
                        break;
                    } else if (Character.toString(str1[i]).equals(
                            getResources().getString(R.string.four_str).trim())) {
                        find = true;
                        break;
                    } else if (Character.toString(str1[i]).equals(
                            getResources().getString(R.string.five_str).trim())) {
                        find = true;
                        break;
                    } else {
                        find = false;
                    }
                }
            }
        } else {
            find = false;
        }

        return find;
    }

    // Find Target To Check Character With String(Only English Not Hebrew) ...
    public static boolean findTarget(String target, String source) {

        int target_len = target.length();
        int source_len = source.length();

        boolean found = false;

        for (int i = 0; (i < source_len && !found); i++) {

            int j = 0;

            while (!found) {

                if (j >= target_len) {
                    break;
                }

                /**
                 * Learning Concept:
                 *
                 * String target = "for"; String source =
                 * "Searching for a string within a string the hard way.";
                 *
                 * 1 - target.charAt( j ) : The character at position 0 > The
                 * first character in 'Target' > character 'f', index 0.
                 *
                 * 2 - source.charAt( i + j) :
                 *
                 * The source strings' array index is searched to determine if a
                 * match is found for the target strings' character index
                 * position. The position for each character in the target
                 * string is then compared to the position of the character in
                 * the source string.
                 *
                 * If the condition is true, the target loop continues for the
                 * length of the target string.nm h
                 *
                 * If all of the source strings' character array element
                 * position matches the target strings' character array element
                 * position Then the condition succeeds ..
                 */

                else if (target.charAt(j) != source.charAt(i + j)) {
                    break;
                } else {
                    ++j;
                    if (j == target_len) {
                        found = true;
                    }
                }
            }

        }

        return found;

    }

    // Here set dedication values after checking database values.
    private void setDedicationValues() {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            setDedicationRecord();

            // Dedication class execute
            new Dedication().execute(
                    nature_str,
                    Utilities.encodeImoString(edt_name.getText().toString())
                            .replace("'", "*").trim(),
                    sex_str,
                    Utilities.encodeImoString(edt_there_is.getText().toString()
                            .replace("'", "*").trim()),
                    Utilities.encodeImoString(edt_name_optional.getText()
                            .toString().replace("'", "*").trim()),
                    blessing_str, pref.getString("email", null),
                    pref.getString("signinwith", null),
                    Utilities.getIsraelTime(), Utilities.getIsraelDate());

        } else {
            Utilities.showAlertDialog(DevoteActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        }

    }

    // Set dedication complete records here to store in local database and send
    // to server...
    private void setDedicationRecord() {
        getset = new GetterSetter();
        getset.setNature(nature_str);
        getset.setName(edt_name.getText().toString().trim());
        getset.setSex(sex_str);
        getset.setThere_Is(edt_there_is.getText().toString().trim());
        getset.setName_Optional(edt_name_optional.getText().toString().trim());
        getset.setBlessing(blessing_str);
        getset.setEmail(pref.getString("email", null));
        getset.setSigninwith(pref.getString("signinwith", null));
        getset.setPublish("pending");
        getset.setAdmin("");
        getset.setTime(getResources().getString(R.string.pending_approval));
        // getset.setDate(Utilities.getIsraelDate());
        listDevote.add(0, getset);
        // db.insertRecords(sqlitedb, getset);
        setAdapterDedicate(listDevote);
        // spnrOne_str = null;
        // spnrTwo_str = null;
        // sex_str = null;
        setValuesOnSpinner();
        /*
         * btn_woman.setBackground(getResources().getDrawable(
		 * R.drawable.input)); btn_men.setBackground(getResources().getDrawable(
		 * R.drawable.blue_box));
		 */

    }

    // Set List Adapter
    private void setAdapterDedicate(ArrayList<GetterSetter> listDevote2) {
        // Collections.reverse(listDevote2);
        madapter = new DevoteAdapter(mContext, listDevote2);
        lv_list_register.setAdapter(madapter);
        madapter.notifyDataSetChanged();
    }

    // Set ValuesOnSpinner
    private void setValuesOnSpinner() {
        list_Bless = new ArrayList<String>();
        list_Bless.add(getResources().getString(R.string.success_hebrew));
        list_Bless.add(getResources().getString(R.string.medicine_hebrew));
        list_Bless.add(getResources().getString(R.string.InMemoryof_hebrew));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                DevoteActivity.this, R.layout.custom_register_spinner,
                list_Bless);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_two_nature.setAdapter(dataAdapter);
        spnr_two_nature
                .setOnItemSelectedListener(new CustomOnItemSelectedListenerTwo());

        // Add List Items For Spinner One Item
        list_choice1 = new ArrayList<String>();
        list_choice1.add(getResources().getString(R.string.frost_hebrew));
        list_choice1.add(getResources().getString(R.string.shlit_hebrew));
        list_choice1.add(getResources().getString(R.string.shlita_hebrew));

        list_choice2 = new ArrayList<String>();
        list_choice2.add(getResources().getString(R.string.whichLive_hebrew));
        list_choice2.add(getResources().getString(R.string.tlit_hebrew));

        list_choice3 = new ArrayList<String>();
        list_choice3.add(getResources().getString(R.string.h_hebrew));
        list_choice3.add(getResources().getString(R.string.memorial_hebrew));

        list_choice4 = new ArrayList<String>();
        list_choice4.add(getResources().getString(R.string.memorial_hebrew));
        list_choice4.add(getResources().getString(R.string.l_hebrew));
        list_choice4.add(getResources().getString(R.string.fourteenth_hebrew));
    }

    // Set list values according to user selection
    private void setListOnSpinner(List<String> list_Choice) {
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(
                DevoteActivity.this, R.layout.custom_register_spinner,
                list_Choice);
        dataAdapter2
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_one_blessing.setAdapter(dataAdapter2);
        dataAdapter2.notifyDataSetChanged();
        spnr_one_blessing
                .setOnItemSelectedListener(new CustomOnItemSelectedListenerOne());

    }

    // CustomOnItemSelectedListenerOne
    public class CustomOnItemSelectedListenerOne implements
            OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                   long id) {
            blessing_str = spnr_one_blessing.getSelectedItem().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }

    // CustomOnItemSelectedListenerTwo
    public class CustomOnItemSelectedListenerTwo implements
            OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                   long id) {

            nature_str = spnr_two_nature.getSelectedItem().toString();
            checkItemSelection();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }

    // Add text watcher on edit text to check text up to 13 character or not
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            btn_microphone_firstname
                    .setBackgroundResource(R.drawable.mike_icon_gray);
            btn_microphone_firstname.setTag(R.drawable.mike_icon_gray);
            btn_microphone_nameoptional
                    .setBackgroundResource(R.drawable.mike_icon_gray);
            btn_microphone_nameoptional.setTag(R.drawable.mike_icon_gray);
            btn_microphone_thereIs
                    .setBackgroundResource(R.drawable.mike_icon_gray);
            btn_microphone_thereIs.setTag(R.drawable.mike_icon_gray);
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            if (edt_name.getText().length() > 13) {

                edt_name.setFilters(new InputFilter[]{filter,
                        new InputFilter.LengthFilter(13)});
                edt_name.setBackgroundResource(R.drawable.input_red);
                // if(autoAdapter != null)
                // autoAdapter.getFilter().filter(edt_name.getText().toString());

            } else if (edt_name_optional.getText().length() > 13) {

                edt_name_optional.setFilters(new InputFilter[]{filter,
                        new InputFilter.LengthFilter(13)});
                edt_name_optional.setBackgroundResource(R.drawable.input_red);
                // if(autoAdapter != null)
                // autoAdapter.getFilter().filter(edt_name_optional.getText().toString());

            } else if (edt_there_is.getText().length() > 13) {

                edt_there_is.setFilters(new InputFilter[]{filter,
                        new InputFilter.LengthFilter(13)});
                edt_there_is.setBackgroundResource(R.drawable.input_red);
                // if(autoAdapter != null)
                // autoAdapter.getFilter().filter(edt_there_is.getText().toString());

            } else {
                edt_name.setBackgroundResource(R.drawable.input);
                edt_name_optional.setBackgroundResource(R.drawable.input);
                edt_there_is.setBackgroundResource(R.drawable.input);
                edt_name.setFilters(new InputFilter[]{filter,
                        new InputFilter.LengthFilter(14)});
                edt_name_optional.setFilters(new InputFilter[]{filter,
                        new InputFilter.LengthFilter(14)});
                edt_there_is.setFilters(new InputFilter[]{filter,
                        new InputFilter.LengthFilter(14)});
            }

            btn_microphone_firstname
                    .setBackgroundResource(R.drawable.mike_icon_gray);
            btn_microphone_firstname.setTag(R.drawable.mike_icon_gray);
            btn_microphone_nameoptional
                    .setBackgroundResource(R.drawable.mike_icon_gray);
            btn_microphone_nameoptional.setTag(R.drawable.mike_icon_gray);
            btn_microphone_thereIs
                    .setBackgroundResource(R.drawable.mike_icon_gray);
            btn_microphone_thereIs.setTag(R.drawable.mike_icon_gray);

        }

        public void afterTextChanged(Editable s) {
            btn_microphone_firstname
                    .setBackgroundResource(R.drawable.mike_icon_gray);
            btn_microphone_firstname.setTag(R.drawable.mike_icon_gray);
            btn_microphone_nameoptional
                    .setBackgroundResource(R.drawable.mike_icon_gray);
            btn_microphone_nameoptional.setTag(R.drawable.mike_icon_gray);
            btn_microphone_thereIs
                    .setBackgroundResource(R.drawable.mike_icon_gray);
            btn_microphone_thereIs.setTag(R.drawable.mike_icon_gray);
        }
    };

    // Check Item Selection On Spinner Item
    private void checkItemSelection() {
        if (clickedMen == true
                && clickedWoman == false
                && getResources().getString(R.string.success_hebrew).equals(
                nature_str)) {

            setListOnSpinner(list_choice1);

        } else if (clickedMen == true
                && clickedWoman == false
                && getResources().getString(R.string.medicine_hebrew).equals(
                nature_str)) {

            setListOnSpinner(list_choice1);

        } else if (clickedMen == false
                && clickedWoman == true
                && getResources().getString(R.string.medicine_hebrew).equals(
                nature_str)) {

            setListOnSpinner(list_choice2);

        } else if (clickedMen == false
                && clickedWoman == true
                && getResources().getString(R.string.success_hebrew).equals(
                nature_str)) {

            setListOnSpinner(list_choice2);

        } else if (clickedMen == false
                && clickedWoman == true
                && getResources().getString(R.string.InMemoryof_hebrew).equals(
                nature_str)) {

            setListOnSpinner(list_choice3);

        } else if (clickedMen == true
                && clickedWoman == false
                && getResources().getString(R.string.InMemoryof_hebrew).equals(
                nature_str)) {

            setListOnSpinner(list_choice4);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_LOGIN) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    getPrefrenceValue();

                } else {
                    Utilities.showAlertDialog(this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection",
                            false);
                }
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_LIST_OF_ALERTS_ITEM) {
                Intent mintent = new Intent();
                setResult(RESULT_OK, mintent);
                finish();
                overridePendingTransition(R.anim.activity_open_scale,
                        R.anim.activity_close_translate);
            }
        }

        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == REQ_CODE_SPEECH_INPUT_ONE) {

                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                edt_name.setText(result.get(0));

            } else if (requestCode == REQ_CODE_SPEECH_INPUT_TWO) {
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                edt_there_is.setText(result.get(0));

            } else if (requestCode == REQ_CODE_SPEECH_INPUT_THREE) {
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                edt_name_optional.setText(result.get(0));
            }
        }
    }

    // Dedication Task Execute Here..
    public class Dedication extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jObj = null;
            HttpParams params2 = new BasicHttpParams();
            params2.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                    HttpVersion.HTTP_1_1);
            DefaultHttpClient mHttpClient = new DefaultHttpClient(params2);
            String url = Config.ROOT_SERVER_CLIENT + Config.DEDICATE;

            HttpPost httppost = new HttpPost(url);
            MultipartEntity multipartEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            try {
                if (!params[0].equals(""))
                    multipartEntity.addPart("nature", new StringBody(params[0],
                            Charset.forName("UTF-8")));
                else
                    multipartEntity.addPart("nature", new StringBody(""));

                if (!params[1].equals(""))
                    multipartEntity.addPart("name", new StringBody(params[1],
                            Charset.forName("UTF-8")));
                else
                    multipartEntity.addPart("name", new StringBody(""));

                if (!params[2].equals(""))
                    multipartEntity.addPart("sex", new StringBody(params[2],
                            Charset.forName("UTF-8")));
                else
                    multipartEntity.addPart("sex", new StringBody(""));

                if (!params[3].equals(""))
                    multipartEntity.addPart("thereis", new StringBody(
                            params[3], Charset.forName("UTF-8")));
                else
                    multipartEntity.addPart("thereis", new StringBody(""));

                if (!params[4].equals(""))
                    multipartEntity.addPart("nameoptional", new StringBody(
                            params[4], Charset.forName("UTF-8")));
                else
                    multipartEntity.addPart("nameoptional", new StringBody(""));

                if (!params[5].equals(""))
                    multipartEntity.addPart("blessing", new StringBody(
                            params[5], Charset.forName("UTF-8")));
                else
                    multipartEntity.addPart("blessing", new StringBody(""));

                if (!params[6].equals(""))
                    multipartEntity.addPart("email", new StringBody(params[6],
                            Charset.forName("UTF-8")));
                else
                    multipartEntity.addPart("email", new StringBody(""));

                if (!params[7].equals(""))
                    multipartEntity.addPart("signinwith", new StringBody(
                            params[7], Charset.forName("UTF-8")));
                else
                    multipartEntity.addPart("signinwith", new StringBody(""));

                if (!params[8].equals(""))
                    multipartEntity.addPart("time", new StringBody(params[8]));
                else
                    multipartEntity.addPart("time", new StringBody(""));

                if (!params[9].equals(""))
                    multipartEntity.addPart("date", new StringBody(params[9]));
                else
                    multipartEntity.addPart("date", new StringBody(""));

                httppost.setEntity(multipartEntity);
                HttpResponse response = mHttpClient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                String strResponse = EntityUtils.toString(r_entity);
                jObj = new JSONObject(strResponse);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            pDialog.setVisibility(View.GONE);
            // setValuesOnSpinner();
            if (result != null) {
                try {

                    if (result.get("success").equals(true)) {

                        // edt_name.setText("");
                        // edt_there_is.setText("");
                        // edt_name_optional.setText("");
                        new GetDedication().execute(
                                pref.getString("email", null),
                                pref.getString("signinwith", null));

                    } else if (result.get("success").equals(false)) {

                        Utilities.showSnackbar(mContext, mContext.getResources().getString(
                                R.string.forbiden_word_message));


                        if (result.get("value").equals(
                                "forbidden word found in name!")) {
                            edt_name.setBackgroundResource(R.drawable.input_red);

                        } else if (result.get("value").equals(
                                "forbidden word found in there is!")) {
                            edt_there_is
                                    .setBackgroundResource(R.drawable.input_red);

                        } else if (result.get("value").equals(
                                "forbidden word found in name optional!")) {
                            edt_name_optional
                                    .setBackgroundResource(R.drawable.input_red);

                        } else if (result.get("value").equals(
                                "forbidden word found in name and thereis!")) {
                            edt_name.setBackgroundResource(R.drawable.input_red);
                            edt_there_is
                                    .setBackgroundResource(R.drawable.input_red);

                        } else if (result
                                .get("value")
                                .equals("forbidden word found in name and nameoptional!")) {
                            edt_name.setBackgroundResource(R.drawable.input_red);
                            edt_name_optional
                                    .setBackgroundResource(R.drawable.input_red);

                        } else if (result
                                .get("value")
                                .equals("forbidden word found in thereis and nameoptional!")) {
                            edt_there_is
                                    .setBackgroundResource(R.drawable.input_red);
                            edt_name_optional
                                    .setBackgroundResource(R.drawable.input_red);

                        } else if (result.get("value").equals(
                                "forbidden word found!")) {
                            edt_name.setBackgroundResource(R.drawable.input_red);
                            edt_there_is
                                    .setBackgroundResource(R.drawable.input_red);
                            edt_name_optional
                                    .setBackgroundResource(R.drawable.input_red);
                        }

                        new GetDedication().execute(
                                pref.getString("email", null),
                                pref.getString("signinwith", null));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    // Declare Get Dedication...
    class GetDedication extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jObj = null;
            HttpParams params2 = new BasicHttpParams();
            params2.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                    HttpVersion.HTTP_1_1);
            DefaultHttpClient mHttpClient = new DefaultHttpClient(params2);
            String url = Config.ROOT_SERVER_CLIENT + Config.GETDEDICATE;
            listDevote = new ArrayList<GetterSetter>();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity multipartEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            try {
                multipartEntity.addPart("emailid", new StringBody(params[0]));
                multipartEntity
                        .addPart("signinwith", new StringBody(params[1]));
                httppost.setEntity(multipartEntity);
                HttpResponse response = mHttpClient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                String strResponse = EntityUtils.toString(r_entity);
                jObj = new JSONObject(strResponse);
                JSONArray jArray = jObj.getJSONArray("details");

                // First of All To delete All Records from Database
                if (Utilities.doesDatabaseExist(DevoteActivity.this, "Maranan"))
                    db.deleteAllRecord();

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    getset = new GetterSetter();
                    getset.setId(jObject.getString("id"));
                    getset.setNature(jObject.getString("nature"));
                    getset.setName(jObject.getString("name"));
                    getset.setSex(jObject.getString("sex"));
                    getset.setName_Optional(jObject.getString("name_optional"));
                    getset.setThere_Is(jObject.getString("there_is"));
                    getset.setBlessing(jObject.getString("status_blessing"));
                    getset.setEmail(jObject.getString("email"));
                    getset.setSigninwith(jObject.getString("sign_with"));
                    getset.setDate(jObject.getString("date"));
                    getset.setPublish(jObject.getString("publish"));
                    getset.setAdmin(jObject.getString("admin"));
                    getset.setF_status(jObject.getString("name_status"));
                    getset.setL_status(jObject.getString("nameopt_status"));
                    getset.setM_status(jObject.getString("thereis_status"));

                    if (jObject.getString("publish").equals("accept")) {
                        getset.setImageResource(R.drawable.right);
                        getset.setTime(jObject.getString("time"));
                        Utilities.setUserIdDedicated(mContext, jObject.getString("email"));

                    } else if (jObject.getString("publish").equals("pending")) {
                        getset.setImageResource(R.drawable.right);
                        getset.setTime(getResources().getString(
                                R.string.pending_approval));

                    } else if (jObject.getString("publish").equals("reject")) {
                        getset.setImageResource(R.drawable.icon_failed);
                        getset.setTime(getResources().getString(
                                R.string.failed_hebrew));

                    } else if (jObject.getString("publish").equals("pause")) {
                        getset.setImageResource(R.drawable.circle_white);
                        getset.setTime(jObject.getString("time"));
                    }

                    if (Utilities.getUserIdDedicated(mContext).equals("")) {
                        Utilities.setUserIdDedicated(mContext, "");
                    }

                    listDevote.add(getset);

                    if (Utilities.doesDatabaseExist(DevoteActivity.this,
                            "Maranan")) {

                        db = new MarananDB(DevoteActivity.this);
                        db.insertRecords(sqlitedb, getset);

                    } else {

                        db = new MarananDB(DevoteActivity.this);
                        db.insertRecords(sqlitedb, getset);
                    }
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            pDialog.setVisibility(View.GONE);
            if (listDevote.size() > 0) {
                setAdapterDedicate(listDevote);
            } else {
                setAdapterDedicate(listDevote);
            }
        }

    }

    // Declare Delete Dedication...
    class DeleteDedication extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jObj = null;
            HttpParams params2 = new BasicHttpParams();
            params2.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                    HttpVersion.HTTP_1_1);
            DefaultHttpClient mHttpClient = new DefaultHttpClient(params2);
            String url = Config.ROOT_SERVER_CLIENT + Config.DELETEDEDICATION;

            HttpPost httppost = new HttpPost(url);

            MultipartEntity multipartEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            try {
                multipartEntity.addPart("dedicate_id",
                        new StringBody(params[0]));
                httppost.setEntity(multipartEntity);
                HttpResponse response = mHttpClient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                String strResponse = EntityUtils.toString(r_entity);
                jObj = new JSONObject(strResponse);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
			/*
			 * new GetDedication().execute(pref.getString("email", null),
			 * pref.getString("signinwith", null));
			 */
        }
    }

    // Declare Update Payment Dedication...
    class UpdatePaymentDedication extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jObj = null;
            HttpParams params2 = new BasicHttpParams();
            params2.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                    HttpVersion.HTTP_1_1);
            DefaultHttpClient mHttpClient = new DefaultHttpClient(params2);
            String url = Config.ROOT_SERVER_CLIENT + Config.PAYMENT_STATUS;

            HttpPost httppost = new HttpPost(url);

            MultipartEntity multipartEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            try {
                multipartEntity.addPart("user_id", new StringBody(params[0]));
                multipartEntity.addPart("payment_status", new StringBody(
                        params[1]));
                httppost.setEntity(multipartEntity);
                HttpResponse response = mHttpClient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                String strResponse = EntityUtils.toString(r_entity);
                jObj = new JSONObject(strResponse);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            pDialog.setVisibility(View.GONE);
            if (result != null) {
                try {
                    if (result.get("success").equals(true)) {
                        new GetDedication().execute(
                                pref.getString("email", null),
                                pref.getString("signinwith", null));
                    } else {
                        new GetDedication().execute(
                                pref.getString("email", null),
                                pref.getString("signinwith", null));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    // Show Payment Alert Dialog
    @SuppressWarnings("unused")
    private void showPaymentAlertDialog(final String record_id) {
        final Dialog dialog = new Dialog(DevoteActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.payment_dialog);
        dialog.setTitle(null);
        RadioGroup radioPaymentGroup = (RadioGroup) dialog
                .findViewById(R.id.radioPayment);
        radioPaymentGroup
                .setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.radioOneTime) {
                            id = record_id;
                            dialog.dismiss();

                        } else if (checkedId == R.id.radioWeekly) {
                            id = record_id;
                            dialog.dismiss();
                        }

                    }
                });
        dialog.show();
    }

    /***************************************
     * Keyboard hide
     ***************************************************/
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
                    .getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
        return ret;
    }

    // onBackPressed()
    public void onBackPressed() {
        if (Utilities.getUserIdDedicated(mContext) != null
                && !Utilities.getUserIdDedicated(mContext).equals("")) {
            Utilities.setComingFromDedication(this, true);
        } else {
            Utilities.setComingFromDedication(this, false);
        }

        if (pref.getString("email", null) != null && !pref.getString("email", null).equals("")) {
            Intent prayerIntent = new Intent(this, PrayerScreen.class);
            prayerIntent.putExtra("email", pref.getString("email", null));
            prayerIntent.putExtra("video_Id", "");
            startActivityForResult(prayerIntent, REQUEST_CODE_LIST_OF_ALERTS_ITEM);
        } else {
            Intent registerInt = new Intent(DevoteActivity.this,
                    LogInActivity.class);
            startActivityForResult(registerInt, REQUEST_CODE_LOGIN);
        }

    }


    // Get Local Data base using Instance
    public MarananDB getLocalDataBase() {
        return db;
    }

    // // On Key Down Fire...
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // if (keyCode == KeyEvent.KEYCODE_MEDIA_RECORD) {
    // // ur code to start handling voice
    // return true;
    // }else if(keyCode == KeyEvent.KEYCODE_HEADSETHOOK){
    // // ur code to start handling voice
    // return true;
    // } else {
    // super.onKeyDown(keyCode, event);
    // }
    // return true;
    //
    // }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput(String values) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        if (values.equals("first_name")) {
            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_ONE);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (values.equals("there_is")) {
            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_TWO);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_THREE);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onDestroy() {
        // stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

}
