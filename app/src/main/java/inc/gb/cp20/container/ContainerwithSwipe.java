package inc.gb.cp20.container;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import cn.pedant.SweetAlert.SweetAlertDialog;
import inc.gb.cp20.AlphaList.AlphaListActivity;
import inc.gb.cp20.AlphaList.AlphabetsList;
import inc.gb.cp20.AlphaList.DrList_POJO;
import inc.gb.cp20.AlphaList.UserService;
import inc.gb.cp20.ContentLib.ContentLibrary;
import inc.gb.cp20.DB.DBHandler;
import inc.gb.cp20.Landing.LandingPage;
import inc.gb.cp20.R;
import inc.gb.cp20.Util.CmsInter;
import inc.gb.cp20.Util.Utility;
import inc.gb.cp20.sync.Sync;
import inc.gb.cp20.tracker.GPSTracker;
import inc.gb.cp20.widget.ColorPickerDialog;

/**
 * Created by Shubham on 2/8/16.
 */

@SuppressWarnings("deprecation")
@SuppressLint("SetJavaScriptEnabled")
public class ContainerwithSwipe extends AlphaListActivity implements View.OnClickListener {


    int stepcount = 1;
    private LinearLayout iconsBar;
    Button open = null;
    ImageView annot1, annot2, prevBrand, nextBrand, reference, close, backtoplaylist;
    private boolean flagForPlaylist = false;
    LinearLayout mylinear, content2, content3;
    GestureOverlayView gesturesView, gesturesView2;
    RelativeLayout myscroll2;
    SeekBar seek;
    ImageView colorw;
    // WebView webView;
    private String playstData[][];
    private String brandData[][];
    int count = 0;
    int actualPlayIndex = 0;
    private int savingCount = 0;
    private long startTime = 0;
    private String startTimeString = "";
    private long endTime = 0;
    private long duration = 0;
    static int refEmailPos = -1;
    DBHandler handler;
    String brandcode = "";
    private long startTimeForReference = 0;
    private String startTimeStringForRef = "";
    //, {"103", "Annotation", "icon7.png", "3"}
    String[][] iconsData = {{"101", "Back to playlist", "icon1.png", "1"},
            {"102", "email", "icon6.png", "2"},
            {"103", "Annotation", "icon7.png", "3"},
            {"104", "Like", "icon8.png", "3"},
            {"105", "Dislike", "icon9.png", "3"},
            {"106", "Search", "icon10.png", "4"},
            {"107", "Close", "icon11.png", "4"}};
    int groupId = 0;
    private Typeface font;
    int playIndex = 0;
    int previousIndex = 0;
    String currentDate;
    private String emailFlag = "0";
    private String likedislikeFlag = "0";
    private static Dialog refDialog, dialog;
    private int position = 0;
    String refData[][];
    String[] cgDataDTG = null;
    String[] cgCodeDTG = null;
    String[] cgDataDPL = null;
    String[] cgCodeDPL = null;
    String[] cgDataDSP = null;
    String[] cgCodeDSP = null;
    String[] cgDataDDC = null;
    String[] cgCodeDDC = null;

    String randomNumber = "";
    private String customer_id = "";
    private String customer_name = "";
    private String category_code = "";
    private String category_name = "";
    private String thumbnail_category = "";

    private ListView leftList, rightList;
    AlphabetsList list, list2;

    Vector<DrList_POJO> userCopyVector;

    TextView done;
    String index = "0";

    int PLAYLISTINDEX = 101;
    int REFERENCEINDEX = 102;
    TextView preview;

    private String BU = "";
    private String TERRITORY = "";
    private String patch = "";

    private GPSTracker tracker;
    EditText searchView;
    String[] cvrData;

    int mycountcount = 0;

    int swipeAdap = 0;

    //Page =false or barad =true

    boolean isBrand = true;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_containerwith_swipe);
        mSectionsPagerAdapter = new SectionsPagerAdapter();
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        randomNumber = Utility.getUniqueNo();
        font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        Bundle extras = getIntent().getExtras();

        customer_id = extras.getString("customer_id");
        customer_name = extras.getString("customer_name");
        category_code = extras.getString("category_code");
        category_name = extras.getString("category_name");
        thumbnail_category = extras.getString("thumbnail_category");
        index = extras.getString("index");
        patch = extras.getString("patch");


        if (customer_id == null)
            customer_id = "";
        if (customer_name == null)
            customer_name = "";
        if (index == null)
            index = "0";
        if (patch == null)
            patch = "";

        handler = DBHandler.getInstance(this);

        String data[][] = handler.genericSelect("Select VAL FROM TBUPW", 1);
        String[] upwData = data[0][0].split("\\^");
        BU = upwData[9];

        String data2[][] = handler.genericSelect("Select COL2 FROM TBCVR", 1);
        cvrData = data2[0][0].split("\\^");
        TERRITORY = cvrData[11];

        SQLiteDatabase db = handler.getWritableDatabase();
        db.delete("TBNAME", null, null);

        if (!customer_id.equals("")) {
            String query = "insert into TBNAME SELECT COL0,COL1,COL2,COL3,COL4,COL5,COL6,COL7,COL8,COL9,COL10,COL11,COL12,COL13,COL14,COL15,COL16,COL17 FROM TBPARTY where COL0 = '" + customer_id + "'";
            db.execSQL(query);
        }

        userCopyVector = UserService.getUserList(this, "TBPARTY", "", "", true);

        Date date = new Date();
        currentDate = new SimpleDateFormat("dd-MM-yyyy").format(date);

        fillPlaystData();

        String brandQuery = "SELECT COL0, COL1, COL2, COL3, COL4, COL5, COL6, COL7, COL8 FROM TBBRAND b where b.COL0 = '" + thumbnail_category + "' and exists (select 1 from TBDPS s, TBDPG t where s.col5 = t.col0 and s.col9= b.col0 and s.col1 =  b.col3 and t.col7 = '1' )";

        brandData = handler.genericSelect(brandQuery, 9);

        tracker = new GPSTracker(ContainerwithSwipe.this);

        backtoplaylist = (ImageView) findViewById(R.id.backtoplaylist);
        backtoplaylist.setOnClickListener(this);
        backtoplaylist.setTag("1");
        prevBrand = (ImageView) findViewById(R.id.previousbrand);
        nextBrand = (ImageView) findViewById(R.id.nextbrand);
        prevBrand.setOnClickListener(prevNextLsitener);
        nextBrand.setOnClickListener(prevNextLsitener);

        preview = (TextView) findViewById(R.id.preview);
        if (index.equals("3"))
            preview.setVisibility(View.VISIBLE);

        close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(this);

        annot1 = (ImageView) findViewById(R.id.annot1);
        annot1.setOnClickListener(this);
        annot1.setTag("1");

        reference = (ImageView) findViewById(R.id.refrence);
        reference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content_id = view.getTag().toString();
                refData = handler.genericSelect("Select COL1, COL15, COL3, COL2 from TBDRG where COL0 = '" + content_id + "'", 4);
                if (refData != null) {
                    refDialog = new Dialog(ContainerwithSwipe.this);
                    refDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    refDialog.setContentView(R.layout.listnil);
                    ListView lv = (ListView) refDialog.findViewById(R.id.listView);
                    Custom_grid adaptor = new Custom_grid(ContainerwithSwipe.this,
                            refData);
                    lv.setAdapter(adaptor);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            ContainerwithSwipe.this.position = position;
                            startTimeForReference = System.currentTimeMillis();
                            Date date = new Date();
                            DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
                            startTimeStringForRef = formatter.format(date);
                            if (refData[position][2].toLowerCase().equals("mp4")) {
                                Intent intent = new Intent(ContainerwithSwipe.this,
                                        VideoPlay.class);
                                intent.putExtra("fileName", refData[position][0]);
                                startActivityForResult(intent, 1001);
                            } else if (refData[position][2].toLowerCase().equals("pdf")) {
                                String path1 = ContainerwithSwipe.this.getFilesDir().getAbsolutePath() + "/"
                                        + refData[position][0];
                                File file1 = new File(path1);
                                if (file1.exists()) {
                                    file1.setReadable(true, false);
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                                    try {
                                        startActivityForResult(intent, 1001);
                                    } catch (Exception e) {
                                        System.out.println("PDF Exception = = = >" + e.toString());
                                    }
                                } else {
                                    Toast.makeText(ContainerwithSwipe.this,
                                            "Please wait PDF being downloaded.....", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }

                        }
                    });

                    WindowManager.LayoutParams wmlp = refDialog.getWindow().getAttributes();

                    wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
                    wmlp.x = 140;
                    wmlp.y = 20;
                    refDialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                            Color.TRANSPARENT));
                    refDialog.getWindow().setLayout(240, 300);
                    refDialog.getWindow().clearFlags(
                            WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    refDialog.show();
                }
            }
        });

        myscroll2 = (RelativeLayout) findViewById(R.id.scroll2);

        mylinear = (LinearLayout) findViewById(R.id.mainid);
//        webView = (WebView) findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setSupportZoom(true);
//        //noinspection deprecation,deprecation,deprecation,deprecation,deprecation,deprecation,deprecation,deprecation,deprecation
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setAllowFileAccess(true);
//        int SDK_INT = android.os.Build.VERSION.SDK_INT;
//        if (SDK_INT > 16) {
//            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
//        }
//
//        // webView.addJavascriptInterface(this, "cpjs");
//
//        webView.addJavascriptInterface(new JavaScriptInterface(this),
//                "cpjs");


        gesturesView = (GestureOverlayView) findViewById(R.id.gestures);
        gesturesView2 = (GestureOverlayView) findViewById(R.id.gestures2);

        gesturesView.setVisibility(View.GONE);
        gesturesView2.setVisibility(View.GONE);

        gesturesView.addOnGesturePerformedListener(listener);
        gesturesView2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (gesturesView2.getVisibility() == View.VISIBLE) {
                    gesturesView2.setVisibility(View.GONE);
                    Animation animation = AnimationUtils.loadAnimation(
                            ContainerwithSwipe.this, R.anim.top_to_bottom);
                    animation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // TODO Auto-generated method stub
                            mylinear.setVisibility(View.GONE);
                            myscroll2.setVisibility(View.GONE);
                        }
                    });
                    mylinear.startAnimation(animation);
                    if (!customer_id.equals("") || category_name.equalsIgnoreCase("S")) {
                        if (flagForPlaylist)
                            backtoplaylist.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        content2 = (LinearLayout) findViewById(R.id.content2);
        content3 = (LinearLayout) findViewById(R.id.content3);

        annot2 = (ImageView) findViewById(R.id.annot2);
        seek = (SeekBar) findViewById(R.id.seek);
        colorw = (ImageView) findViewById(R.id.colorw);
        colorw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(
                        ContainerwithSwipe.this, Color.parseColor("#00ff09"),
                        new ColorPickerDialog.OnColorSelectedListener() {

                            @Override
                            public void onColorSelected(int color) {
                                gesturesView.setGestureColor(color);
                            }

                        });
                colorPickerDialog.show();
            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                gesturesView.setGestureStrokeWidth(progress);
            }
        });

        iconsBar = (LinearLayout) findViewById(R.id.icons_bar);

        for (int i = 1; i < iconsData.length - 1; i++) {
            ImageView icons = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 10, 20, 0);
            icons.setLayoutParams(params);
            icons.setId(Integer.parseInt(iconsData[i][0]));
            String filePath = new File(getFilesDir(), iconsData[i][2]).getAbsolutePath();
            //String filePath = getFilesDir() + "/" + iconsData[i][2];
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            icons.setTag("1");
            icons.setImageBitmap(bitmap);
            icons.setOnClickListener(this);
            iconsBar.addView(icons);
            groupId = Integer.parseInt(iconsData[i][3]);
            if (i < iconsData.length - 1)
                if (groupId != Integer.parseInt(iconsData[i + 1][3])) {
                    View view = new View(this);
                    LinearLayout.LayoutParams viewparam = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
                    viewparam.setMargins(0, 10, 0, 5);
                    view.setLayoutParams(viewparam);
                    view.setBackgroundColor(Color.WHITE);
                    iconsBar.addView(view);
                }
        }

        open = (Button) findViewById(R.id.handle);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mylinear.getVisibility() == View.GONE) {
                    gesturesView2.setVisibility(View.VISIBLE);
                    open.setVisibility(View.INVISIBLE);
                    // webView.setClickable(false);
                    mylinear.setVisibility(View.VISIBLE);
                    myscroll2.setVisibility(View.VISIBLE);
                    myscroll2.startAnimation(AnimationUtils.loadAnimation(
                            ContainerwithSwipe.this, R.anim.bottom_to_top));
                    Animation animation = AnimationUtils.loadAnimation(
                            ContainerwithSwipe.this, R.anim.bottom_to_top);
                    animation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // TODO Auto-generated method stub
                            //open.setBackgroundResource(R.drawable.navigated);
                            open.setVisibility(View.VISIBLE);
                        }
                    });
                    mylinear.startAnimation(animation);
                    //annot2.setVisibility(View.GONE);
                    backtoplaylist.setVisibility(View.GONE);
                } else {
                    gesturesView2.setVisibility(View.GONE);
                    open.setVisibility(View.INVISIBLE);
                    // webView.setClickable(true);
                    Animation animation = AnimationUtils.loadAnimation(
                            ContainerwithSwipe.this, R.anim.top_to_bottom);
                    animation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // TODO Auto-generated method stub
                            //open.setBackgroundResource(R.drawable.navigateup);
                            open.setVisibility(View.VISIBLE);
                            mylinear.setVisibility(View.GONE);
                            myscroll2.setVisibility(View.GONE);
                        }
                    });
                    mylinear.startAnimation(animation);
                    if (!customer_id.equals("") || category_name.equalsIgnoreCase("S")) {
                        if (flagForPlaylist)
                            backtoplaylist.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        if (playIndex != 0)
            fillPlayList(1);
        else
            fillPlayList(0);
        fillBrandList();

//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                gesturesView.cancelClearAnimation();
//                gesturesView.clear(true);
//                gesturesView.setVisibility(View.GONE);
//                seek.setVisibility(View.GONE);
//                colorw.setVisibility(View.GONE);
//                ImageView image = (ImageView) findViewById(R.id.annot1);
//                image.setTag("1");
//                previousIndex = playIndex;
//
//
//                if (playIndex < playstData.length - 1) {
//                    playIndex++;
//                    ImageView imageView = (ImageView) ((RelativeLayout) content2.getChildAt(playIndex)).getChildAt(0);
//                    for (int i = 0; i < content2.getChildCount(); i++) {
//                        ImageView child = (ImageView) ((RelativeLayout) content2.getChildAt(i)).getChildAt(0);
//                        child.setScaleX(1.0f);
//                        child.setScaleY(1.0f);
//                    }
//                    imageView.setScaleX(1.4f);
//                    imageView.setScaleY(1.4f);
//                    displayFocussedBrands(playIndex);
//                } else if (playIndex < playstData.length && !customer_id.equals("")) {
//                    playIndex++;
//                    for (int i = 0; i < content2.getChildCount(); i++) {
//                        ImageView child = (ImageView) ((RelativeLayout) content2.getChildAt(i)).getChildAt(0);
//                        child.setScaleX(1.0f);
//                        child.setScaleY(1.0f);
//                    }
//                    displayFocussedBrands(playIndex);
//                }
////                if (1 == R.id.previousbrand) {
////                    if (playIndex > 0) {
////                        playIndex--;
////                        ImageView imageView = (ImageView) ((RelativeLayout) content2.getChildAt(playIndex)).getChildAt(0);
////                        for (int i = 0; i < content2.getChildCount(); i++) {
////                            ImageView child = (ImageView) ((RelativeLayout) content2.getChildAt(i)).getChildAt(0);
////                            child.setScaleX(1.0f);
////                            child.setScaleY(1.0f);
////                        }
////                        imageView.setScaleX(1.4f);
////                        imageView.setScaleY(1.4f);
////                        displayFocussedBrands(playIndex);
////                    }
////                } else if (1 == R.id.nextbrand) {
////                    if (playIndex < playstData.length - 1) {
////                        playIndex++;
////                        ImageView imageView = (ImageView) ((RelativeLayout) content2.getChildAt(playIndex)).getChildAt(0);
////                        for (int i = 0; i < content2.getChildCount(); i++) {
////                            ImageView child = (ImageView) ((RelativeLayout) content2.getChildAt(i)).getChildAt(0);
////                            child.setScaleX(1.0f);
////                            child.setScaleY(1.0f);
////                        }
////                        imageView.setScaleX(1.4f);
////                        imageView.setScaleY(1.4f);
////                        displayFocussedBrands(playIndex);
////                    } else if (playIndex < playstData.length && !customer_id.equals("")) {
////                        playIndex++;
////                        for (int i = 0; i < content2.getChildCount(); i++) {
////                            ImageView child = (ImageView) ((RelativeLayout) content2.getChildAt(i)).getChildAt(0);
////                            child.setScaleX(1.0f);
////                            child.setScaleY(1.0f);
////                        }
////                        displayFocussedBrands(playIndex);
////                    }
////                }
//                hideDrawer();
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

    }

    public void disMiss(int pos) {
        refEmailPos = pos;
        refDialog.dismiss();
        handler = DBHandler.getInstance(ContainerwithSwipe.this);
        SQLiteDatabase db = handler.getWritableDatabase();

        double latitude = tracker.getLatitude();
        double longitude = tracker.getLongitude();

        ContentValues values = new ContentValues();
        values.put("COL0", BU); //BU
        values.put("COL1", TERRITORY); //Territory
        values.put("COL2", patch); //Patch
        values.put("COL3", currentDate); //Date
        values.put("COL4", category_code);
        values.put("COL5", category_name);
        values.put("COL6", brandcode); //brandcode
        values.put("COL7", refData[refEmailPos][3]); //referenceid
        values.put("COL8", ""); //duration
        values.put("COL9", latitude); //latitude
        values.put("COL10", longitude); //longitude
        values.put("COL11", "0"); //Syncflag
        values.put("COL12", "1"); //hardcode
        values.put("COL13", "1"); //Email
        values.put("COL14", ""); //start time
        if (index.equals("1"))
            values.put("COL15", "D"); //Doctor
        else if (index.equals("2"))
            values.put("COL15", "R"); //RightSide
        else if (index.equals("3"))
            values.put("COL15", "P"); //playList
        else if (index.equals("4"))
            values.put("COL15", "L"); //Library

        values.put("COL16", ""); //like/dislike
        values.put("COL17", playstData[playIndex][0]); //reference pageid
        values.put("COL18", randomNumber); //randrom number
        values.put("COL19", Utility.getUniqueNo());
        values.put("COL20", "0");
        values.put("COL21", ""); //playlistid

        db.insert("TXN102", null, values);
        db.close();

    }


    private void fillPlayList(final int index) {

        ContainerwithSwipe.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (playstData != null) {
                    Bitmap bitmap = null;
                    content2.removeAllViews();
                    for (int i = 0; i < playstData.length; i++) {
                        LayoutInflater inflater = (LayoutInflater) ContainerwithSwipe.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.playlist_groups, null);
                        view.setId(i);
                        ImageView img = (ImageView) view.findViewById(R.id.img);
                        try {
                            String filePath = new File(getFilesDir() + "/" + FilenameUtils.removeExtension(playstData[i][2]) + "/", FilenameUtils.removeExtension(playstData[i][2]) + ".png").getAbsolutePath();
                            bitmap = BitmapFactory.decodeFile(filePath);
                            if (bitmap != null)
                                img.setImageBitmap(bitmap);
                            else
                                img.setImageResource(R.drawable.page);
                        } catch (Exception e) {
                            img.setImageResource(R.drawable.page);
                        }

                        TextView name = (TextView) view.findViewById(R.id.name);
                        name.setText(playstData[i][3]);
                        view.setOnClickListener(pagesListener);
                        if (index == 0 && i == 0) {
                            img.setScaleX(1.4f);
                            img.setScaleY(1.4f);
                            displayFocussedBrands(0);
                        } else if (index == 1 && i == playIndex) {
                            img.setScaleX(1.4f);
                            img.setScaleY(1.4f);
                            displayFocussedBrands(playIndex);
                        }
                        content2.addView(view);
                    }
                }
            }
        });

    }

    private void fillBrandList() {
        content3.removeAllViews();
        if (brandData != null)
            for (int i = 0; i < brandData.length; i++) {
                LayoutInflater inflater = (LayoutInflater) ContainerwithSwipe.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View childView = inflater.inflate(R.layout.subgroups, null);


                ImageView sub_img = (ImageView) childView.findViewById(R.id.sub_img);
                try {
                    String filePath = new File(getFilesDir() + "/", brandData[i][4] + ".png").getAbsolutePath();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    if (bitmap != null)
                        sub_img.setImageBitmap(bitmap);
                    else
                        sub_img.setImageResource(R.drawable.brand);
                } catch (Exception e) {
                    sub_img.setImageResource(R.drawable.brand);
                }


                TextView name = (TextView) childView.findViewById(R.id.namesub);
                name.setText(brandData[i][2]);
                childView.setId(Integer.parseInt(brandData[i][3]));
                childView.setOnClickListener(brandListener);
                content3.addView(childView);
            }
        else
            Utility.showSweetAlert(ContainerwithSwipe.this, "No Brands Available", CmsInter.ERROR_TYPE);
    }

    View.OnClickListener brandListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            brandcode = "" + view.getId();
            if (count == 0)
                actualPlayIndex = playIndex;
            count++;

            System.out.println("Step" + stepcount + "------------Ac-" + actualPlayIndex + "-------Play>" + playIndex);
            ImageView imageView = (ImageView) ((RelativeLayout) view).getChildAt(0);
            for (int i = 0; i < content3.getChildCount(); i++) {
                ImageView child = (ImageView) ((RelativeLayout) content3.getChildAt(i)).getChildAt(0);
                child.setScaleX(1.0f);
                child.setScaleY(1.0f);
            }
            imageView.setScaleX(1.4f);
            imageView.setScaleY(1.4f);
            previousIndex = playIndex;
            playIndex = 0;

            playstData = handler.genericSelect("select a.COL5, a.COL2, b.COL1, b.COL2, b.COL3, b.COL5, b.COL16, a.COL11 from TBDPS a , TBDPG b\n" +
                    "        where a.col5 = b.col0\n" +
                    "        and a.COL3 = '" + brandcode + "' and a.COL9 = '" + thumbnail_category + "' and a.COL10 = 'IPL' and b.COL7 = '1' order by  CAST (a.col2 AS INTEGER) ASC ", 8);
            isBrand = true;
            fillPlayList(0);
            if (!customer_id.equals("") || category_name.equalsIgnoreCase("S")) {
                backtoplaylist.setVisibility(View.VISIBLE);
                flagForPlaylist = true;
            }
            hideDrawer();
        }
    };

    View.OnClickListener pagesListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageView imageView = (ImageView) ((RelativeLayout) view).getChildAt(0);
            for (int i = 0; i < content2.getChildCount(); i++) {
                ImageView child = (ImageView) ((RelativeLayout) content2.getChildAt(i)).getChildAt(0);
                child.setScaleX(1.0f);
                child.setScaleY(1.0f);
            }
            imageView.setScaleX(1.4f);
            imageView.setScaleY(1.4f);
            previousIndex = playIndex;
            playIndex = view.getId();
            isBrand = false;
            displayFocussedBrands(playIndex);
            hideDrawer();
        }
    };

    View.OnClickListener prevNextLsitener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            gesturesView.cancelClearAnimation();
            gesturesView.clear(true);
            gesturesView.setVisibility(View.GONE);
            seek.setVisibility(View.GONE);
            colorw.setVisibility(View.GONE);
            ImageView image = (ImageView) findViewById(R.id.annot1);
            image.setTag("1");
            previousIndex = playIndex;
            if (id == R.id.previousbrand) {
                if (playIndex > 0) {
                    playIndex--;
                    ImageView imageView = (ImageView) ((RelativeLayout) content2.getChildAt(playIndex)).getChildAt(0);
                    for (int i = 0; i < content2.getChildCount(); i++) {
                        ImageView child = (ImageView) ((RelativeLayout) content2.getChildAt(i)).getChildAt(0);
                        child.setScaleX(1.0f);
                        child.setScaleY(1.0f);
                    }
                    imageView.setScaleX(1.4f);
                    imageView.setScaleY(1.4f);
                    displayFocussedBrands(playIndex);
                }
            } else if (id == R.id.nextbrand) {
                if (playIndex < playstData.length - 1) {
                    playIndex++;
                    ImageView imageView = (ImageView) ((RelativeLayout) content2.getChildAt(playIndex)).getChildAt(0);
                    for (int i = 0; i < content2.getChildCount(); i++) {
                        ImageView child = (ImageView) ((RelativeLayout) content2.getChildAt(i)).getChildAt(0);
                        child.setScaleX(1.0f);
                        child.setScaleY(1.0f);
                    }
                    imageView.setScaleX(1.4f);
                    imageView.setScaleY(1.4f);
                    displayFocussedBrands(playIndex);
                } else if (playIndex < playstData.length && !customer_id.equals("")) {
                    playIndex++;
                    for (int i = 0; i < content2.getChildCount(); i++) {
                        ImageView child = (ImageView) ((RelativeLayout) content2.getChildAt(i)).getChildAt(0);
                        child.setScaleX(1.0f);
                        child.setScaleY(1.0f);
                    }
                    displayFocussedBrands(playIndex);
                }
            }
            hideDrawer();
        }
    };

    private void hideDrawer() {
        if (mylinear.getVisibility() == View.VISIBLE) {
            open.setVisibility(View.INVISIBLE);
            if (gesturesView.getVisibility() == View.VISIBLE) {
                gesturesView.cancelClearAnimation();
                gesturesView.clear(true);
                gesturesView.setVisibility(View.GONE);
                ImageView image = (ImageView) findViewById(R.id.annot1);
                image.setTag("1");
            }
            seek.setVisibility(View.GONE);
            colorw.setVisibility(View.GONE);
            gesturesView2.setVisibility(View.GONE);
            // webView.setClickable(true);
            Animation animation = AnimationUtils.loadAnimation(
                    ContainerwithSwipe.this, R.anim.top_to_bottom);
            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub
                    //open.setBackgroundResource(R.drawable.navigateup);
                    open.setVisibility(View.VISIBLE);
                    mylinear.setVisibility(View.GONE);
                    myscroll2.setVisibility(View.GONE);
                }
            });
            mylinear.startAnimation(animation);
            if (!customer_id.equals("") || category_name.equalsIgnoreCase("S")) {
                if (flagForPlaylist)
                    backtoplaylist.setVisibility(View.VISIBLE);
            }
        } else if (gesturesView.getVisibility() == View.VISIBLE) {
            gesturesView.cancelClearAnimation();
            gesturesView.clear(true);
            gesturesView.setVisibility(View.GONE);
            ImageView image = (ImageView) findViewById(R.id.annot1);
            image.setTag("1");
        }
    }

    public void displayFocussedBrands(int playIndex) {

        System.out.println("Step" + stepcount + "------------Ac-" + actualPlayIndex + "-------Play>" + playIndex);

        if (savingCount == 0) {
            startTime = System.currentTimeMillis();
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            startTimeString = formatter.format(date);
            savingCount = 1;
        } else {
            if (previousIndex != playstData.length)
                saveData(PLAYLISTINDEX);

            startTime = System.currentTimeMillis();
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            startTimeString = formatter.format(date);
            ContainerwithSwipe.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    emailFlag = "0";
                    likedislikeFlag = "0";
                    String filePath = "";
                    Bitmap bitmap = null;
                    filePath = new File(getFilesDir(), "icon6.png").getAbsolutePath();
                    bitmap = BitmapFactory.decodeFile(filePath);
                    iconsBar.getChildAt(0).setTag("1");
                    ((ImageView) iconsBar.getChildAt(0)).setImageBitmap(bitmap);

                    filePath = new File(getFilesDir(), "icon7.png").getAbsolutePath();
                    bitmap = BitmapFactory.decodeFile(filePath);
                    iconsBar.getChildAt(2).setTag("1");
                    ((ImageView) iconsBar.getChildAt(2)).setImageBitmap(bitmap);

                    filePath = new File(getFilesDir(), "icon8.png").getAbsolutePath();
                    bitmap = BitmapFactory.decodeFile(filePath);
                    iconsBar.getChildAt(3).setTag("1");
                    ((ImageView) iconsBar.getChildAt(3)).setImageBitmap(bitmap);

                    filePath = new File(getFilesDir(), "icon9.png").getAbsolutePath();
                    bitmap = BitmapFactory.decodeFile(filePath);
                    iconsBar.getChildAt(4).setTag("1");
                    ((ImageView) iconsBar.getChildAt(4)).setImageBitmap(bitmap);
                    bitmap = null;
                }
            });


        }

        if (playIndex == playstData.length) {
            if (backtoplaylist.getVisibility() != View.VISIBLE) {
                final String url = "file://" + getFilesDir().getAbsolutePath() + "/thank/thank.htm";

//
//                int SDK_INT = android.os.Build.VERSION.SDK_INT;
//                if (SDK_INT > 16) {
//                    webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
//                }

                iconsBar.getChildAt(0).setVisibility(View.GONE);
                iconsBar.getChildAt(1).setVisibility(View.GONE);
                reference.setVisibility(View.GONE);
//                webView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        webView.loadUrl(url);
//                    }
//                });
            }
        } else {

            final String url = "file://" + getFilesDir().getAbsolutePath() + "/" + FilenameUtils.removeExtension(playstData[playIndex][2]) + "/" + playstData[playIndex][2];

            if (playstData[playIndex][5].equals("1")) {//Emailable
                iconsBar.getChildAt(0).setVisibility(View.VISIBLE);
                iconsBar.getChildAt(1).setVisibility(View.VISIBLE);
            } else {
                iconsBar.getChildAt(0).setVisibility(View.GONE);
                iconsBar.getChildAt(1).setVisibility(View.GONE);
            }
            String refCount[][] = handler.genericSelect("Select count (1) from TBDRG where COL0 = '" + playstData[playIndex][0] + "'", 1);

            if (refCount != null && Integer.parseInt(refCount[0][0]) > 0) {//Reference
                reference.setVisibility(View.VISIBLE);
                reference.setTag(playstData[playIndex][0]);
            } else
                reference.setVisibility(View.GONE);


            // startTime = System.currentTimeMillis();
//            webView.post(new Runnable() {
//                @Override
//                public void run() {
//                    webView.loadUrl(url);
//                }
//            });


                mViewPager.setAdapter(mSectionsPagerAdapter);
                mSectionsPagerAdapter.notifyDataSetChanged();

            mViewPager.setCurrentItem(playIndex);


            handler.GenricUpdates("TBDPG", "COL13", "1", "COL0", playstData[playIndex][0]);
            handler.ExecuteQuery("update TBBRAND \n" +
                    "set col11 = (\n" +
                    "select count(1)\n" +
                    "from TBDPG h , TBDPS l\n" +
                    "where h.col0 = l.col5\n" +
                    "and  h.col13 = 0\n" +
                    "and l.col9 = TBBRAND.col0\n" +
                    "and l.col3 = TBBRAND.col3\n" +
                    ")");
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (view instanceof ImageView) {
            ImageView imgView = (ImageView) view;
            String imgName = "";
            String filePath = "";
            Bitmap bitmap = null;

            String imgName2 = "";
            String filePath2 = "";

            switch (id) {
                case R.id.backtoplaylist: // Back to Playlist
                    previousIndex = playIndex;
                    playIndex = actualPlayIndex;

                    System.out.println("Step" + stepcount + "------------Ac-" + actualPlayIndex + "-------Play>" + playIndex);


                    count = 0;
                    for (int i = 0; i < content3.getChildCount(); i++) {
                        content3.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                    fillPlaystData();
                    fillPlayList(1);
                    RelativeLayout rlView = (RelativeLayout) content2.getChildAt(playIndex);
//                    int x = rlView.getRight();
//                    int y = rlView.getTop();
                    backtoplaylist.setVisibility(View.GONE);
                    flagForPlaylist = false;
                    break;
                case 102: //Email
                    if (imgView.getTag().equals("1")) {
                        imgName = "icon66.png";//glow
                        view.setTag("2");
                        emailFlag = "1";
                    } else if (imgView.getTag().equals("2")) {
                        imgName = "icon6.png";
                        view.setTag("1");
                        emailFlag = "0";
                    }
                    filePath = new File(getFilesDir(), imgName).getAbsolutePath();
                    bitmap = BitmapFactory.decodeFile(filePath);
                    imgView.setImageBitmap(bitmap);
                    break;
                case R.id.annot1: //annotation
                    if (imgView.getTag().equals("2")) {
                        if (gesturesView.getVisibility() == View.VISIBLE) {
                            gesturesView.cancelClearAnimation();
                            gesturesView.clear(true);
                            gesturesView.setVisibility(View.GONE);
                        }
                        seek.setVisibility(View.GONE);
                        colorw.setVisibility(View.GONE);
                        view.setTag("1");
                    } else if (imgView.getTag().equals("1")) {
                        gesturesView.setGestureColor(Color.parseColor("#00ff09"));
                        gesturesView.setVisibility(View.VISIBLE);
                        colorw.setVisibility(View.VISIBLE);
                        seek.setVisibility(View.VISIBLE);
                        view.setTag("2");
                    }
                    break;
                case 103: //annotation
                    if (imgView.getTag().equals("2")) {
                        imgName = "icon7.png";
                        filePath = new File(getFilesDir(), imgName).getAbsolutePath();
                        bitmap = BitmapFactory.decodeFile(filePath);
                        imgView.setImageBitmap(bitmap);
                        if (gesturesView.getVisibility() == View.VISIBLE) {
                            gesturesView.cancelClearAnimation();
                            gesturesView.clear(true);
                            gesturesView.setVisibility(View.GONE);
                        }
                        seek.setVisibility(View.GONE);
                        colorw.setVisibility(View.GONE);
                        view.setTag("1");
                    } else if (imgView.getTag().equals("1")) {
                        imgName = "icon77.png";
                        filePath = new File(getFilesDir(), imgName).getAbsolutePath();
                        bitmap = BitmapFactory.decodeFile(filePath);
                        imgView.setImageBitmap(bitmap);
                        gesturesView.setGestureColor(Color.parseColor("#00ff09"));
                        gesturesView.setVisibility(View.VISIBLE);
                        colorw.setVisibility(View.VISIBLE);
                        seek.setVisibility(View.VISIBLE);
                        view.setTag("2");
                    }
                    break;
                case 104: //like
                    if (imgView.getTag().equals("1")) {
                        imgName = "icon88.png";//glow
                        view.setTag("2");

                        imgName2 = "icon9.png";
                        iconsBar.getChildAt(4).setTag("1");
                        filePath2 = new File(getFilesDir(), imgName2).getAbsolutePath();
                        bitmap = BitmapFactory.decodeFile(filePath2);
                        ((ImageView) iconsBar.getChildAt(4)).setImageBitmap(bitmap);

                    } else if (imgView.getTag().equals("2")) {
                        imgName = "icon8.png";
                        view.setTag("1");
                    }
                    filePath = new File(getFilesDir(), imgName).getAbsolutePath();
                    bitmap = BitmapFactory.decodeFile(filePath);
                    imgView.setImageBitmap(bitmap);
                    likedislikeFlag = "1";
                    break;
                case 105: //dislike
                    if (imgView.getTag().equals("1")) {
                        imgName = "icon99.png";//glow
                        view.setTag("2");

                        imgName2 = "icon8.png";
                        iconsBar.getChildAt(3).setTag("1");
                        filePath2 = new File(getFilesDir(), imgName2).getAbsolutePath();
                        bitmap = BitmapFactory.decodeFile(filePath2);
                        ((ImageView) iconsBar.getChildAt(3)).setImageBitmap(bitmap);
                    } else if (imgView.getTag().equals("2")) {
                        imgName = "icon9.png";
                        view.setTag("1");
                    }
                    filePath = new File(getFilesDir(), imgName).getAbsolutePath();
                    bitmap = BitmapFactory.decodeFile(filePath);
                    imgView.setImageBitmap(bitmap);
                    likedislikeFlag = "2";
                    break;
                case 106: //library

//                    if (!customer_id.equals("") || category_name.equalsIgnoreCase("S")) {
//                        backtoplaylist.setVisibility(View.VISIBLE);
//                        flagForPlaylist = true;
//                    }
                    if (actualPlayIndex == 0)
                        actualPlayIndex = playIndex;
                    if (count == 0)
                        actualPlayIndex = playIndex;
                    count++;
                    hideDrawer();
                    Intent intent1 = new Intent(this, ContentLibrary.class);
                    startActivityForResult(intent1, 1212);
                    break;
                case R.id.close: //close
                    if (!index.equals("3")) {
                        showAlertForDetailing();
                    } else {
                        previousIndex = playIndex;
                        if (previousIndex != playstData.length)
                            saveData(PLAYLISTINDEX);
                        finish();
                    }
                    break;
            }
        }
    }

    GestureOverlayView.OnGesturePerformedListener listener = new GestureOverlayView.OnGesturePerformedListener() {
        @Override
        public void onGesturePerformed(GestureOverlayView overlay,
                                       Gesture gesture) {

        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            previousIndex = playIndex;
            if (previousIndex != playstData.length)
                saveData(REFERENCEINDEX);
        } else if (requestCode == 1212 && resultCode == RESULT_OK) {
            String category_code = data.getStringExtra("category_code");
            String category_name = data.getStringExtra("category_name");
            String page_number = data.getStringExtra("page_number");
            index = data.getStringExtra("index_lib");
            previousIndex = playIndex;
            //
            playIndex = Integer.parseInt(page_number);
            playstData = handler.genericSelect("select a.COL5, a.COL2, b.COL1, b.COL2, b.COL3, b.COL5, b.COL16, a.COL11 from TBDPS a , TBDPG b\n" +
                    "        where a.col5 = b.col0\n" +
                    "        and a.COL3 = '" + category_code + "' and a.COL9 = '" + category_name + "' and a.COL10 = 'IPL' and b.COL7 = '1' order by  CAST (a.col2 AS INTEGER) ASC ", 8);
            fillPlayList(1);
            if (!customer_id.equals("") || category_name.equalsIgnoreCase("S")) {
                backtoplaylist.setVisibility(View.VISIBLE);
                flagForPlaylist = true;
            }
        }
    }

    @Override
    public void onItemListClick(DrList_POJO objDrListPOJO, View view) {
        super.onItemListClick(objDrListPOJO, view);
        ListView listView = (ListView) view.getParent().getParent();
        SQLiteDatabase db = handler.getWritableDatabase();
        if (listView == leftList) {
            String alreadyExist[][] = handler.genericSelect("Select count(1) from TBNAME where COL0 = '" + objDrListPOJO.getCOL0() + "'", 1);
            if (Integer.parseInt(alreadyExist[0][0]) == 0) {
                db = handler.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("COL0", objDrListPOJO.getCOL0());
                cv.put("COL1", objDrListPOJO.getCOL1());
                cv.put("COL2", objDrListPOJO.getCOL2());
                cv.put("COL3", objDrListPOJO.getCOL3());
                cv.put("COL4", objDrListPOJO.getCOL4());
                cv.put("COL5", objDrListPOJO.getCOL5());
                cv.put("COL6", objDrListPOJO.getCOL6());
                cv.put("COL7", objDrListPOJO.getCOL7());
                cv.put("COL8", objDrListPOJO.getCOL8());
                cv.put("COL9", objDrListPOJO.getCOL9());
                cv.put("COL10", objDrListPOJO.getCOL10());
                cv.put("COL11", objDrListPOJO.getCOL11());
                cv.put("COL12", objDrListPOJO.getCOL12());
                cv.put("COL13", objDrListPOJO.getCOL13());
                cv.put("COL14", objDrListPOJO.getCOL14());
                cv.put("COL15", objDrListPOJO.getCOL15());
                cv.put("COL16", objDrListPOJO.getCOL16());
                db.insert("TBNAME", null, cv);
                list.userVector.remove(objDrListPOJO);
            }
        } else if (listView == rightList) {
            if (!objDrListPOJO.getCOL0().equals(customer_id)) {
                String whereClause = "COL0=?";
                String[] whereArgs = new String[]{objDrListPOJO.getCOL0()};
                if (objDrListPOJO.getCOL0().startsWith("-")) {
                    db.delete("TBNAME", whereClause, whereArgs);
                } else {
//                    String[][] tbData = handler.genericSelect("SELECT * FROM TBNAME where COL0 > '0' order by COL14 ", 16);
//                    int count = 0;
//                    for (int j = 0; j < tbData.length; j++) {
//                        if (tbData[j][0].equals(objDrListPOJO.getCOL0())) {
//                            count = j;
//                            break;
//                        }
//                    }
//                    DrList_POJO newPojo;
//                    int newCount = 0;
//                    for (int m = 0; m < userCopyVector.size(); m++) {
//                        newPojo = userCopyVector.get(m);
//                        if (newPojo.getCOL0().equals(objDrListPOJO.getCOL0())) {
//                            newCount = m;
//                        }
//                    }
//                    int position = newCount - count;

                    //list.userVector.add(position, objDrListPOJO);
                    db = handler.getWritableDatabase();
                    db.delete("TBNAME", whereClause, whereArgs);
                    String notexistQuery = " not exists (SELECT 1 FROM TBNAME where COL0 = TBPARTY.COL0)";
                    Vector<DrList_POJO> userVector = UserService.getUserList(ContainerwithSwipe.this, "TBPARTY", searchView.getText().toString().trim(), notexistQuery, true);
                    list.userVector = userVector;

                }
            }
        }

        String sts[][] = handler.genericSelect("SELECT COUNT(1) FROM TBNAME", 1);
        if (sts[0][0].equals("0"))
            done.setVisibility(View.GONE);
        else
            done.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        list2.setAdapter(0, rightList, "TBNAME", false, false, false, CmsInter.LIST_TAG_DOC, false, CmsInter.TAG_DOC_RIGHT, customer_id);
                        list.setAdapter(1, leftList, "TBPARTY", false, false, true, CmsInter.LIST_TAG_DOC, true, CmsInter.TAG_DOC_LEFT, "");

                    }
                });
            }
        }, 400);
    }


    public void fillPlaystData() {
        if (customer_id.equals(""))
            playstData = handler.genericSelect("select a.COL5, a.COL2, b.COL1, b.COL2, b.COL3, b.COL5, b.COL16, a.COL11 from TBDPS a , TBDPG b\n" +
                    "        where a.col5 = b.col0\n" +
                    "        and a.COL3 = '" + category_code + "' and a.COL9 = '" + category_name + "' and a.COL10 = 'IPL' and b.COL7 = '1' order by  CAST (a.col2 AS INTEGER) ASC ", 8);
        else {
            String[][] countData = handler.genericSelect("Select count(1) from TBDPS2 where col10 = '" + customer_id + "'", 1);
            if (countData[0][0].equals("0")) {
                //select and insert
                SQLiteDatabase db = handler.getWritableDatabase();
                db.execSQL(" insert into TBDPS2 select A.COL0, A.COL1, A.COL2, A.COL3, A.COL4, A.COL5, A.COL6, A.COL7, A.COL8, A.COL9, '" + customer_id + "', '0', A.COL11 from TBDPS A WHERE COL3 = '" + category_code + "' and COL9 = '" + category_name + "'");
            }
            //initialize playlist
            playstData = handler.genericSelect("select a.COL5, a.COL2, b.COL1, b.COL2, b.COL3, b.COL5, b.COL16, a.COL12 from TBDPS2 a , TBDPG b\n" +
                    "        where a.col5 = b.col0\n" +
                    "        and a.COL10 = '" + customer_id + "' and b.COL7 = '1' order by  CAST (a.col2 AS INTEGER) ASC ", 8);

        }
    }

    private void showAlertForDetailing() {
        SweetAlertDialog sDialog = new SweetAlertDialog(ContainerwithSwipe.this, SweetAlertDialog.WARNING_TYPE);
        sDialog.setTitleText("Do you want to end this session?")
                .setCancelText("Cancel!")
                .setConfirmText("Save!")
                .setNeutralText("Don't Save!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        previousIndex = playIndex;
                        if (previousIndex != playstData.length)
                            saveData(PLAYLISTINDEX);
                        if (!customer_id.equals(""))
                            showAddDocDialog();
                        else
                            showAddDocScreen();
                    }
                })
                .setNeutralClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        previousIndex = playIndex;
                        if (previousIndex != playstData.length)
                            saveData(PLAYLISTINDEX);
                        SQLiteDatabase db = handler.getWritableDatabase();
                        db.execSQL("update  TXN102  set COL20 = '-1' where COL18 = '" + randomNumber + "'");
                        Intent intent = new Intent(ContainerwithSwipe.this, LandingPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void showAddDocDialog() {
        SweetAlertDialog dialog = new SweetAlertDialog(ContainerwithSwipe.this, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("Do you want to add more doctors?")
                .setCancelText("No !")
                .setConfirmText("Yes !")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if (!customer_id.equals("")) {
                            String sts[][] = handler.genericSelect("SELECT COL0, COL1, COL2, COL5, COL3, COL12 FROM TBNAME WHERE COL0 = '" + customer_id + "'", 6);
                            SQLiteDatabase db = handler.getWritableDatabase();
                            if (sts != null) {
                                ContentValues cv = new ContentValues();
                                cv.put("COL0", randomNumber); // A unique code from UPW
                                cv.put("COL1", sts[0][0]); // pcode
                                cv.put("COL2", sts[0][1]); //name
                                cv.put("COL3", sts[0][2]); //patchcode
                                cv.put("COL4", sts[0][3]); //speccode
                                cv.put("COL5", sts[0][4]); //classcode
                                cv.put("COL6", sts[0][5]); //hqcode
                                db.insert("TBPHTAG", null, cv);
                            }
                        }
                        sDialog.cancel();
                        SyncData();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        showAddDocScreen();
                    }
                })
                .show();
    }


    private void showAddDocScreen() {
        final SQLiteDatabase db = handler.getWritableDatabase();

        dialog = new Dialog(ContainerwithSwipe.this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_doc);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(
                        Color.TRANSPARENT));
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        // dialog.setCancelable(false);
        //noinspection deprecation,deprecation
        @SuppressWarnings("deprecation") int width = display.getWidth();
        //noinspection deprecation,deprecation
        @SuppressWarnings("deprecation") int height = display.getHeight();
        dialog.getWindow().setLayout((29 * width) / 30,
                (height * 9) / 10);
        LinearLayout first = (LinearLayout) dialog
                .findViewById(R.id.first);
        list = new AlphabetsList(ContainerwithSwipe.this);
        View view1 = list.getAlphabestListView("TBPARTY", false, false, true, 0, "", CmsInter.TAG_DOC_LEFT);
        first.addView(view1);
        leftList = (ListView) ((LinearLayout) ((RelativeLayout) view1).getChildAt(1)).getChildAt(0);
        FrameLayout frameLayout = (FrameLayout) ((RelativeLayout) view1).getChildAt(0);
        searchView = (EditText) (frameLayout.getChildAt(0));
        searchView.addTextChangedListener(new TextWatcher() {
                                              String notexistQuery = " not exists (SELECT 1 FROM TBNAME where COL0 = TBPARTY.COL0)";

                                              @Override
                                              public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                              }

                                              @Override
                                              public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                                                  Vector<DrList_POJO> userVector = UserService.getUserList(ContainerwithSwipe.this, "TBPARTY", s.toString(), notexistQuery, true);
                                                  list.userVector = userVector;
                                                  list.setAdapter(1, leftList, "TBPARTY", false, false, true, CmsInter.LIST_TAG_DOC, true, CmsInter.TAG_DOC_LEFT, "");
                                              }

                                              @Override
                                              public void afterTextChanged(Editable editable) {

                                              }
                                          }

        );

        LinearLayout second = (LinearLayout) dialog
                .findViewById(R.id.second);
        list2 = new AlphabetsList(ContainerwithSwipe.this);

        View view2 = list2.getAlphabestListView("TBNAME", false, false, false, 0, customer_id, CmsInter.TAG_DOC_RIGHT);
        view2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        second.addView(view2);
        list2.setSidepannel(View.GONE);
        list2.SerachViewVis(View.GONE);
        rightList = (ListView) ((LinearLayout) ((RelativeLayout) view2).getChildAt(1)).getChildAt(0);

        final Spinner hq = (Spinner) dialog.findViewById(R.id.hq);
        final Spinner patch = (Spinner) dialog.findViewById(R.id.patch);
        final Spinner speciality = (Spinner) dialog.findViewById(R.id.speciality);
        final Spinner clas = (Spinner) dialog.findViewById(R.id.clas);

        cgDataDPL = new String[1];
        cgCodeDPL = new String[1];
        cgDataDPL[0] = "Select Patch*";
        cgCodeDPL[0] = "0";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ContainerwithSwipe.this, android.R.layout.simple_spinner_item, cgDataDPL);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patch.setAdapter(adapter);


        String[][] strData1 = handler.genericSelect("SELECT COL0, COL1 FROM TBDTG order by COL1 ", 2);
        if (strData1 != null) {
            cgDataDTG = new String[strData1.length + 1];
            cgCodeDTG = new String[strData1.length + 1];
            cgDataDTG[0] = "Select HQ*";
            cgCodeDTG[0] = "0";
            for (int j = 0; j < strData1.length; j++) {
                cgDataDTG[j + 1] = strData1[j][1];
                cgCodeDTG[j + 1] = strData1[j][0];
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ContainerwithSwipe.this, android.R.layout.simple_spinner_item, cgDataDTG);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hq.setAdapter(spinnerArrayAdapter);
        }

        hq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                         @Override
                                         public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                                                                    long id) {
                                             String[][] strData = handler.genericSelect("SELECT COL0, COL1 FROM TBDPL where COL3 = '" + cgCodeDTG[position] + "' order by COL1 ", 2);
                                             if (strData != null) {
                                                 cgDataDPL = new String[strData.length + 1];
                                                 cgCodeDPL = new String[strData.length + 1];
                                                 cgDataDPL[0] = "Select Patch*";
                                                 cgCodeDPL[0] = "0";
                                                 for (int j = 0; j < strData.length; j++) {
                                                     cgDataDPL[j + 1] = strData[j][1];
                                                     cgCodeDPL[j + 1] = strData[j][0];
                                                 }
                                                 ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ContainerwithSwipe.this, android.R.layout.simple_spinner_item, cgDataDPL);
                                                 spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                 patch.setAdapter(spinnerArrayAdapter);
                                             } else {
                                                 cgDataDPL = new String[1];
                                                 cgCodeDPL = new String[1];
                                                 cgDataDPL[0] = "Select Patch*";
                                                 cgCodeDPL[0] = "0";
                                                 ArrayAdapter<String> adapter = new ArrayAdapter<String>(ContainerwithSwipe.this, android.R.layout.simple_spinner_item, cgDataDPL);
                                                 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                 patch.setAdapter(adapter);
                                             }
                                         }

                                         @Override
                                         public void onNothingSelected(AdapterView<?> adapterView) {

                                         }
                                     }

        );

        String[][] strData2 = handler.getTagData("TBDSP");
        if (strData2 != null)

        {
            cgDataDSP = new String[strData2.length + 1];
            cgCodeDSP = new String[strData2.length + 1];
            cgDataDSP[0] = "Select Speciality*";
            cgCodeDSP[0] = "0";
            for (int j = 0; j < strData2.length; j++) {
                cgDataDSP[j + 1] = strData2[j][1];
                cgCodeDSP[j + 1] = strData2[j][0];
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ContainerwithSwipe.this, android.R.layout.simple_spinner_item, cgDataDSP);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            speciality.setAdapter(spinnerArrayAdapter);
        }

        String[][] strData3 = handler.getTagData("TBDDC");
        if (strData3 != null)

        {
            cgDataDDC = new String[strData3.length + 1];
            cgCodeDDC = new String[strData3.length + 1];
            cgDataDDC[0] = "Select Class*";
            cgCodeDDC[0] = "0";
            for (int j = 0; j < strData3.length; j++) {
                cgDataDDC[j + 1] = strData3[j][1];
                cgCodeDDC[j + 1] = strData3[j][0];
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ContainerwithSwipe.this, android.R.layout.simple_spinner_item, cgDataDDC);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            clas.setAdapter(spinnerArrayAdapter);
        }

        done = (TextView) dialog.findViewById(R.id.done);
        if (!customer_id.equals("")) {
            done.setVisibility(View.VISIBLE);
            String notexistQuery = " not exists (SELECT 1 FROM TBNAME where COL0 = TBPARTY.COL0)";
            Vector<DrList_POJO> userVector = UserService.getUserList(ContainerwithSwipe.this, "TBPARTY", "", notexistQuery, true);
            list.userVector = userVector;
            list.setAdapter(1, leftList, "TBPARTY", false, false, true, CmsInter.LIST_TAG_DOC, true, CmsInter.TAG_DOC_LEFT, "");
        }

        done.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String sts[][] = handler.genericSelect("SELECT COL0, COL1, COL2, COL5, COL3, COL12 FROM TBNAME", 6);
                                        SQLiteDatabase db = handler.getWritableDatabase();
                                        if (sts != null) {
                                            for (int i = 0; i < sts.length; i++) {
                                                ContentValues cv = new ContentValues();
                                                cv.put("COL0", randomNumber); // A unique code from UPW
                                                cv.put("COL1", sts[i][0]); // pcode
                                                cv.put("COL2", sts[i][1]); //name
                                                cv.put("COL3", sts[i][2]); //patchcode
                                                cv.put("COL4", sts[i][3]); //speccode
                                                cv.put("COL5", sts[i][4]); //classcode
                                                cv.put("COL6", sts[i][5]); //hqcode
                                                db.insert("TBPHTAG", null, cv);
                                            }
                                        }
                                        dialog.dismiss();
                                        SyncData();
                                    }
                                }

        );
        final EditText phy_name = (EditText) dialog.findViewById(R.id.phy_name);
        TextView move = (TextView) dialog.findViewById(R.id.move);
        move.setText(getResources().getString(R.string.reply) + "  ADD TO LIST");
        move.setTypeface(font, Typeface.BOLD);
        move.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View view) {
                                        String str = phy_name.getText().toString();
                                        phy_name.setText("");
                                        if (!str.equals("") && patch.getSelectedItemPosition() != 0 && speciality.getSelectedItemPosition() != 0) {
                                            String patStr = cgDataDPL[patch.getSelectedItemPosition()];
                                            String specStr = cgDataDSP[speciality.getSelectedItemPosition()];
                                            //String clasStr = cgDataDDC[clas.getSelectedItemPosition()];
                                            String clasStr = "";
                                            String patCodeStr = cgCodeDPL[patch.getSelectedItemPosition()];
                                            String specCodeStr = cgCodeDSP[speciality.getSelectedItemPosition()];
                                            //String clasCodeStr = cgCodeDDC[clas.getSelectedItemPosition()];
                                            String clasCodeStr = "";
//                                            String hqStr = cgDataDTG[hq.getSelectedItemPosition()];
                                            String hqCodeStr = cgCodeDTG[hq.getSelectedItemPosition()];

                                            ContentValues cv = new ContentValues();

                                            SharedPreferences preferences = getSharedPreferences("CP20", MODE_PRIVATE);
                                            String value = preferences.getString("unlisted_key", null);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            int newUpdatedNumber = Integer.parseInt(value) - 1;
                                            editor.putString("unlisted_key", newUpdatedNumber + "");
                                            editor.commit();

                                            String territory_code = cvrData[11];

                                            SQLiteDatabase db = handler.getWritableDatabase();

                                            cv.put("COL0", value); // A unique code from UPW
                                            cv.put("COL1", str);
                                            cv.put("COL2", patCodeStr);
                                            cv.put("COL3", clasCodeStr);
                                            cv.put("COL4", "");
                                            cv.put("COL5", specCodeStr);
                                            cv.put("COL6", "");
                                            cv.put("COL7", patStr);
                                            cv.put("COL8", "");
                                            cv.put("COL9", "");
                                            cv.put("COL10", specStr);
                                            cv.put("COL11", clasStr);
                                            cv.put("COL12", hqCodeStr);
                                            cv.put("COL13", territory_code);
                                            cv.put("COL14", str);

                                            db.insert("TBNAME", null, cv);

                                            String sts[][] = handler.genericSelect("SELECT COUNT(1) FROM TBNAME", 1);
                                            if (sts[0][0].equals("0"))
                                                done.setVisibility(View.GONE);
                                            else
                                                done.setVisibility(View.VISIBLE);

                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    runOnUiThread(new Runnable() {

                                                        @Override
                                                        public void run() {
                                                            list2.setAdapter(0, rightList, "TBNAME", false, false, false, CmsInter.LIST_TAG_DOC, false, CmsInter.TAG_DOC_RIGHT, customer_id);
                                                            //rightList.invalidateViews();
                                                            // TODO Auto-generated method stub

                                                        }
                                                    });
                                                }
                                            }, 400);
                                            hq.setSelection(0);
                                            patch.setSelection(0);
                                            speciality.setSelection(0);
                                            clas.setSelection(0);
                                        } else {
                                            Utility.showSweetAlert(ContainerwithSwipe.this, CmsInter.AL_BLANK, CmsInter.ERROR_TYPE);
                                        }
                                    }
                                }

        );

        TextView close = (TextView) dialog
                .findViewById(R.id.close);
        close.setTypeface(font);
        close.setOnClickListener(new View.OnClickListener()

                                 {

                                     @Override
                                     public void onClick(View v) {
                                         // TODO Auto-generated method stub
                                         showAlertForTagDoc();
                                     }
                                 }

        );
        dialog.show();
    }

    private void showAlertForTagDoc() {
        SweetAlertDialog sDialog = new SweetAlertDialog(ContainerwithSwipe.this, SweetAlertDialog.WARNING_TYPE);
        sDialog.setTitleText("Save Data?")
                .setCancelText("Cancel !")
                .setConfirmText("Yes !")
                .setNeutralText("Discard !")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        String sts[][] = handler.genericSelect("SELECT COL0, COL1, COL2, COL5, COL3, COL12 FROM TBNAME", 6);
                        SQLiteDatabase db = handler.getWritableDatabase();
                        if (sts != null) {
                            for (int i = 0; i < sts.length; i++) {
                                ContentValues cv = new ContentValues();
                                cv.put("COL0", randomNumber); // A unique code from UPW
                                cv.put("COL1", sts[i][0]); // pcode
                                cv.put("COL2", sts[i][1]); //name
                                cv.put("COL3", sts[i][2]); //patchcode
                                cv.put("COL4", sts[i][3]); //speccode
                                cv.put("COL5", sts[i][4]); //classcode
                                cv.put("COL6", sts[0][5]); //hqcode
                                db.insert("TBPHTAG", null, cv);
                            }
                        }
                        dialog.dismiss();
                        SyncData();
                    }
                })
                .setNeutralClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        SQLiteDatabase db = handler.getWritableDatabase();
                        db.execSQL("update  TXN102  set COL20 = '-1' where COL18 = '" + randomNumber + "'");
                        Intent intent = new Intent(ContainerwithSwipe.this, LandingPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {


        if (index.equals("3")) {
            previousIndex = playIndex;
            if (previousIndex != playstData.length)
                saveData(PLAYLISTINDEX);

            super.onBackPressed();
        } else if (playstData != null) {
            showAlertForDetailing();
        } else {
            super.onBackPressed();
        }
    }

    private void saveData(int dataIndex) {

        stepcount++;
        int flag = 0;
        endTime = System.currentTimeMillis();
        if (dataIndex == PLAYLISTINDEX) {//backpress//displayfocussedbrand
            duration = endTime - startTime;
        } else if (dataIndex == REFERENCEINDEX) {//onactivityresult
            duration = endTime - startTimeForReference;
            if (position == refEmailPos)
                flag = 1;
        }
        SQLiteDatabase db = handler.getWritableDatabase();

        double latitude = tracker.getLatitude();
        double longitude = tracker.getLongitude();

        ContentValues values = new ContentValues();
        values.put("COL0", BU); //BU
        values.put("COL1", TERRITORY); //Territory
        values.put("COL2", patch); //Patch
        values.put("COL3", currentDate); //Date
        values.put("COL4", category_code);
        values.put("COL5", category_name);
        values.put("COL6", brandcode); //brandcode
        try {
            if (dataIndex == PLAYLISTINDEX) {
                values.put("COL7", playstData[previousIndex][0]); //pagecode
            } else if (dataIndex == REFERENCEINDEX) {
                values.put("COL7", refData[position][3]); //referenceid
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        values.put("COL8", duration); //duration
        values.put("COL9", latitude); //latitude
        values.put("COL10", longitude); //longitude
        values.put("COL11", "0"); //Syncflag
        values.put("COL12", "1"); //hardcode

        if (dataIndex == PLAYLISTINDEX) {
            values.put("COL13", emailFlag); //Email
            values.put("COL14", startTimeString); //start time
        } else if (dataIndex == REFERENCEINDEX) {
            values.put("COL13", flag); //Email
            values.put("COL14", startTimeStringForRef); //start time
        }

        if (index.equals("1"))
            values.put("COL15", "D"); //Doctor
        else if (index.equals("2"))
            values.put("COL15", "R"); //RightSide
        else if (index.equals("3"))
            values.put("COL15", "P"); //playList
        else if (index.equals("4"))
            values.put("COL15", "L"); //Library

        if (dataIndex == PLAYLISTINDEX) {
            values.put("COL16", likedislikeFlag); //like/dislike
            values.put("COL17", ""); //reference pageid
        } else if (dataIndex == REFERENCEINDEX) {
            values.put("COL16", ""); //like/dislike
            try {
                values.put("COL17", playstData[previousIndex][0]); //reference pageid
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        values.put("COL18", randomNumber); //randrom number
        values.put("COL19", Utility.getUniqueNo());
        values.put("COL20", "0");

        if (dataIndex == PLAYLISTINDEX) {
            try {
                values.put("COL21", playstData[previousIndex][7]);
            } catch (Exception e) {

            }//playlistid
        } else if (dataIndex == REFERENCEINDEX) {
            values.put("COL21", ""); //playlistid
        }
        db.insert("TXN102", null, values);
        db.close();
        //       }
    }

    private void SyncData() {

        Sync sync = new Sync(ContainerwithSwipe.this);
        sync.prepareRequest(1);
        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ContainerwithSwipe.this, CmsInter.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Data Saved Successfully")
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sweetAlertDialog.dismiss();
                        Intent intent = new Intent(ContainerwithSwipe.this, LandingPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .show();
    }

    public class JavaScriptInterface       {
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @android.webkit.JavascriptInterface
        public void sendToAndroid(String str) {
//            Toast.makeText(ContainerwithSwipe.this, "Hot Spot Called-->" + str, Toast.LENGTH_LONG).show();
            previousIndex = playIndex;
            String[][] Query = handler.genericSelect("select COL10,COL15 ,COL1 from TBDPG where Col1='" + str + ".htm'", 3);
            playstData = handler.genericSelect("select a.COL5, a.COL2, b.COL1, b.COL2, b.COL3, b.COL5, b.COL16, a.COL11 from TBDPS a , TBDPG b\n" +
                    "        where a.col5 = b.col0\n" +
                    "        and a.COL3 = '" + Query[0][1] + "' and a.COL9 = '" + Query[0][0] + "' and a.COL10 = 'IPL' and b.COL7 = '1' order by  CAST (a.col2 AS INTEGER) ASC ", 8);


            String temp = str + ".htm";


            for (int i = 0; i < playstData.length; ++i) {
                for (int j = 0; j < playstData[i].length; ++j) {
                    if (temp.equals(playstData[i][j])) {
                        if (count == 0)
                            actualPlayIndex = playIndex;
                        count++;
                        //  if (!index.equals("4"))
                        System.out.println("Step" + stepcount + "------------Ac-" + actualPlayIndex + "-------Play>" + playIndex);

                        playIndex = i;
                        ContainerwithSwipe.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!customer_id.equals("") || category_name.equalsIgnoreCase("S")) {
                                    backtoplaylist.setVisibility(View.VISIBLE);
                                    flagForPlaylist = true;
                                }
                            }
                        });

                        fillPlayList(1);
                        break;
                    }
                }
            }

        }

    }

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public class PlaceholderFragment extends Fragment {
//
//
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            int playIndex_viewPager = 0;
//            View rootView = inflater.inflate(R.layout.fragment_containerwith_swipe, container, false);
//            WebView webView = (WebView) rootView.findViewById(R.id.webView);
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.addJavascriptInterface(new JavaScriptInterface(getActivity()),
//                    "cpjs");
//
//            webView.getSettings().setBuiltInZoomControls(true);
//            webView.getSettings().setSupportZoom(true);
//            //noinspection deprecation,deprecation,deprecation,deprecation,deprecation,deprecation,deprecation,deprecation,deprecation
//            webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
//            webView.getSettings().setAllowFileAccess(true);
//            int SDK_INT = android.os.Build.VERSION.SDK_INT;
//            if (SDK_INT > 16) {
//                webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
//            }
//            playIndex_viewPager = getArguments().getInt(ARG_SECTION_NUMBER);
//
////            if (playIndex == -1)
////                playIndex = 0;
////        //    else if (playstData.length == playIndex_viewPager)
////
////            else
//            playIndex = playIndex_viewPager;
//            // playIndex = (playIndex_viewPager - 1);
//
//
//            String url = "file://" + getActivity().getFilesDir().getAbsolutePath() + "/" + FilenameUtils.removeExtension(playstData[playIndex_viewPager][2]) + "/" + playstData[playIndex_viewPager][2];
//
//            webView.loadUrl(url);
//            System.out.println("Step" + stepcount + "------------Ac-" + actualPlayIndex + "-------Play>" + playIndex);
//
//
//            return rootView;
//        }
//    }
//
//    /**
//     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
//     * one of the sections/tabs/pages.
//     */
//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            PlaceholderFragment p = new PlaceholderFragment();
//            return p.newInstance(position);
//        }
//
//        @Override
//        public int getCount() {
//            // Show  total pages.
//            return playstData.length;
//        }
//
//
//    }


    class SectionsPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            //Return total pages, here one for each data item
            return playstData.length;
        }

        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View rootView = inflater.inflate(R.layout.fragment_containerwith_swipe, null);


            int playIndex_viewPager;

            WebView webView = (WebView) rootView.findViewById(R.id.webView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new JavaScriptInterface(ContainerwithSwipe.this),
                    "cpjs");

            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSupportZoom(true);
            //noinspection deprecation,deprecation,deprecation,deprecation,deprecation,deprecation,deprecation,deprecation,deprecation
            webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
            webView.getSettings().setAllowFileAccess(true);
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 16) {
                webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
            }
            playIndex_viewPager = position;
            playIndex = playIndex_viewPager;
            String url = "file://" + ContainerwithSwipe.this.getFilesDir().getAbsolutePath() + "/" + FilenameUtils.removeExtension(playstData[playIndex_viewPager][2]) + "/" + playstData[playIndex_viewPager][2];
            webView.loadUrl(url);
            //Add the page to the front of the queue
            container.addView(rootView, 0);


            System.out.println("Step in Get" + stepcount + "------------Ac-" + actualPlayIndex + "-------Play>" + playIndex + "---dsf" + position);
            return rootView;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0 == (View) arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object = null;
        }
    }
}

