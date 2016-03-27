package inc.gb.cp20.AlphaList;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import inc.gb.cp20.R;


/**
 * Created by GB on 2/12/16.
 */
public class AlphabetsList {
    Context mContext;
    int mLastFirstVisibleItem = 0;


    protected GestureDetector mGestureDetector;

    public Vector<DrList_POJO> userVector;

    protected int totalListSize = 0;

    /**
     * list with items for side index
     */
    private ArrayList<Object[]> indexList = null;

    /**
     * list with row number for side index
     */
    protected List<Integer> dealList = new ArrayList<Integer>();

    /**
     * height of left side index
     */
    protected int sideIndexHeight;

    /**
     * number of items in the side index
     */
    private int indexListSize;

    /**
     * x and y coordinates within our side index
     */
    protected static float sideIndexX;
    protected static float sideIndexY;

    protected TextView selectedIndex;

    private List<String> alphaList = new ArrayList<String>();
    private ListView booksLV;
    EditText searchView;
    LinearLayout sidePannel;

    private UserListAdapter userListAdapter;

    public View mainView;

    public AlphabetsList(Context mContext) {
        this.mContext = mContext;


    }

    public View getAlphabestListView(final String TabelName, boolean play_icon, boolean tick_icon, final boolean header) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.mainindextable, null);
        booksLV = (ListView) mainView.findViewById(R.id.booksLV);


        searchView = (EditText) mainView.findViewById(R.id.searchView);
        sidePannel = (LinearLayout) mainView.findViewById(R.id.sideIndex);
        selectedIndex = (TextView) mainView.findViewById(R.id.selectedIndex);
        setAdapter(0, booksLV, TabelName, play_icon, tick_icon, header);

        LinearLayout sideIndex = (LinearLayout) mainView.findViewById(R.id.sideIndex);
        sideIndex.setOnClickListener(onClicked);
        sideIndexHeight = sideIndex.getHeight();
        mGestureDetector = new GestureDetector(mContext,
                new ListIndexGestureListener());
        if (userVector.size() > 0)
            getDisplayListOnChange(mainView);

        booksLV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                boolean enable = false;
                if (booksLV != null && booksLV.getChildCount() > 0 && header) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = booksLV.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = booksLV.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                    //   searchView.animate().alpha(1.0f);
                    //    searchView.setVisibility(View.VISIBLE);

                    TranslateAnimation animate = new TranslateAnimation(view.getHeight(), 0, 0, 0);
                    animate.setDuration(700);
                    animate.setFillAfter(true);


                    //  Toast.makeText(mContext, "st-->" + scrollState, Toast.LENGTH_SHORT).show();
                    //   searchView.startAnimation(animate);
                    //   searchView.setVisibility(View.VISIBLE);


                    //                   TExtWithCross("GSASA", searchView);
//                    TExtWithCross1("gb", searchView);


                    ///

                    // TODO Auto-generated method stub
                    final ListView lw = booksLV;
                    if (scrollState == 0) {
                        searchView.startAnimation(animate);
                        searchView.setVisibility(View.VISIBLE);
                        //  Log.i("a", "scrolling stopped...");
                    }


                    if (view.getId() == lw.getId()) {
                        final int currentFirstVisibleItem = lw.getFirstVisiblePosition();

                        if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                            // mIsScrollingUp = false;
                            searchView.startAnimation(animate);
                            searchView.setVisibility(View.VISIBLE);
                        } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                            //  mIsScrollingUp = true;
                            searchView.startAnimation(animate);
                            searchView.setVisibility(View.GONE);
                        }
                        mLastFirstVisibleItem = currentFirstVisibleItem;
                    }
                }


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                userVector = UserService.getUserList(mContext, TabelName, s.toString());

                Vector<DrList_POJO> subsidiesList = getIndexedBooks(userVector);
                totalListSize = subsidiesList.size();

                userListAdapter = new UserListAdapter(subsidiesList, mContext, true, false, false);
                booksLV.setAdapter(userListAdapter);
                userListAdapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable s) {
                //  TExtWithCross("dff adsfds dsagfvsdfg adgvads");
            }
        });


        return mainView;
    }


//    public void TExtWithCross(String name) {
//        final SpannableStringBuilder sb = new SpannableStringBuilder();
//        TextView tv = null;
//
//        String[] test = name.split(" ");
//        for (int i = 0; i < test.length; i++) {
//            tv = createContactTextView(test[i]);
//            BitmapDrawable bd = (BitmapDrawable) convertViewToDrawable(tv);
//            bd.setBounds(0, 0, bd.getIntrinsicWidth(), bd.getIntrinsicHeight());
//            sb.append(test[i] + " ");
//            sb.setSpan(new ImageSpan(bd), sb.length() - (test[i].length() + 1), sb.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        }
//        searchView.setText(sb);
//    }
//
//
//    public TextView createContactTextView(String text) {
//        //creating textview dynamically
//        TextView tv = new TextView(mContext);
//        tv.setText(text);
//        tv.setTextSize(20);
//        tv.setBackgroundResource(R.drawable.normalbg);
//        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_delete, 0);
//        return tv;
//    }
//
//    public static Object convertViewToDrawable(View view) {
//        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        view.measure(spec, spec);
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
//                Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(b);
//        c.translate(-view.getScrollX(), -view.getScrollY());
//        view.draw(c);
//        view.setDrawingCacheEnabled(true);
//        Bitmap cacheBmp = view.getDrawingCache();
//        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
//        view.destroyDrawingCache();
//        return new BitmapDrawable(viewBmp);
//
//    }


    private Vector<DrList_POJO> getIndexedBooks(Vector<DrList_POJO> booksVector) {

        // Retrieve it from DB in shorting order

        Vector<DrList_POJO> v = new Vector<DrList_POJO>();
        // Add default item

        String idx1 = null;
        String idx2 = null;
        for (int i = 0; i < booksVector.size(); i++) {
            DrList_POJO temp = booksVector.elementAt(i);
            // Insert the alphabets
            idx1 = (temp.getCOL14().substring(0, 1)).toLowerCase();
            if (!idx1.equalsIgnoreCase(idx2)) {
                v.add(new DrList_POJO(idx1.toUpperCase(), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                idx2 = idx1;
                dealList.add(i);
            }
            v.add(temp);
        }

        return v;
    }

    /**
     * ListIndexGestureListener method gets the list on scroll.
     */
    private class ListIndexGestureListener extends
            GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            /**
             * we know already coordinates of first touch we know as well a
             * scroll distance
             */
            sideIndexX = sideIndexX - distanceX;
            sideIndexY = sideIndexY - distanceY;

            /**
             * when the user scrolls within our side index, we can show for
             * every position in it a proper item in the list
             */
            if (sideIndexX >= 0 && sideIndexY >= 0) {
                displayListItem(mainView);
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//
//        getDisplayListOnChange(mainView);
//    }

    private int listLocation;

    /**
     * displayListItem is method used to display the row from the list on scrool
     * or touch.
     */
    public void displayListItem(View view) {

        // compute number of pixels for every side index item
        double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

        // compute the item index for given event position belongs to
        int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

        if (itemPosition < 0) {
            itemPosition = 0;
        } else if (itemPosition >= dealList.size()) {
            itemPosition = dealList.size() - 1;
        }

        selectedIndex.setText(alphaList.get(itemPosition));

        ListView listView = (ListView) view.findViewById(R.id.booksLV);

        listLocation = dealList.get(itemPosition) + itemPosition;

        if (listLocation > totalListSize) {
            listLocation = totalListSize;
        }

        listView.setSelection(listLocation);
    }

    /**
     * getListArrayIndex consists of index details, to navigate through out the
     * index.
     *
     * @param strArr , index values
     * @return ArrayList object
     */
    private ArrayList<Object[]> getListArrayIndex(String[] strArr) {
        ArrayList<Object[]> tmpIndexList = new ArrayList<Object[]>();
        Object[] tmpIndexItem = null;

        int tmpPos = 0;
        String tmpLetter = "";
        String currentLetter = null;

        for (int j = 0; j < strArr.length; j++) {
            currentLetter = strArr[j];

            // every time new letters comes
            // save it to index list
            if (!currentLetter.equalsIgnoreCase(tmpLetter)) {
                tmpIndexItem = new Object[3];
                tmpIndexItem[0] = tmpLetter;
                tmpIndexItem[1] = tmpPos - 1;
                tmpIndexItem[2] = j - 1;

                tmpLetter = currentLetter;
                tmpPos = j + 1;
                tmpIndexList.add(tmpIndexItem);
            }
        }

        // save also last letter
        tmpIndexItem = new Object[3];
        tmpIndexItem[0] = tmpLetter;
        tmpIndexItem[1] = tmpPos - 1;
        tmpIndexItem[2] = strArr.length - 1;
        tmpIndexList.add(tmpIndexItem);

        // and remove first temporary empty entry
        if (tmpIndexList != null && tmpIndexList.size() > 0) {
            tmpIndexList.remove(0);
        }

        return tmpIndexList;
    }

    /**
     * getDisplayListOnChange method display the list from the index.
     */
    public void getDisplayListOnChange(View view) {
        LinearLayout sideIndex = (LinearLayout) view.findViewById(R.id.sideIndex);
        sideIndexHeight = sideIndex.getHeight();

        if (sideIndexHeight == 0) {
            Display display = ((WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            sideIndexHeight = display.getHeight();
            // Navigation Bar + Tab space comes approximately 80dip
        }

        sideIndex.removeAllViews();

        /**
         * temporary TextView for every visible item
         */
        TextView tmpTV = null;

        /**
         * we will create the index list
         */
        String[] strArr = new String[userVector.size()];

        int j = 0;
        for (DrList_POJO user : userVector) {
            strArr[j++] = user.getCOL14().substring(0, 1);
        }

        indexList = getListArrayIndex(strArr);


        /**
         * number of items in the index List
         */
        indexListSize = indexList.size();

        /**
         * maximal number of item, which could be displayed
         */
        int indexMaxSize = (int) Math.floor(sideIndexHeight / 25);

        int tmpIndexListSize = indexListSize;

        /**
         * handling that case when indexListSize > indexMaxSize, if true display
         * the half of the side index otherwise full index.
         */
        while (tmpIndexListSize > indexMaxSize) {
            tmpIndexListSize = tmpIndexListSize / 2;
        }

        /**
         * computing delta (only a part of items will be displayed to save a
         * place, without compact
         */
        double delta = indexListSize / tmpIndexListSize;

        String tmpLetter = null;

        Object[] tmpIndexItem = null;

        for (double i = 1; i <= indexListSize; i = i + delta) {
            tmpIndexItem = indexList.get((int) i - 1);
            tmpLetter = tmpIndexItem[0].toString();
            tmpTV = new TextView(mContext);
            tmpTV.setText(tmpLetter);
            tmpTV.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            tmpTV.setLayoutParams(params);
            tmpTV.setTextColor(Color.parseColor("#FF6EB9F7"));
            alphaList.add(tmpLetter);
            sideIndex.addView(tmpTV);
        }

        sideIndex.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sideIndexX = event.getX();
                sideIndexY = event.getY();

                displayListItem(mainView);

                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    selectedIndex.setText("");
                }
                return false;
            }
        });
    }

    protected View.OnClickListener onClicked = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

//            Toast.makeText(mContext, "Clickewd", Toast.LENGTH_SHORT).show();


        }
    };

    public void setAdapter(int index, ListView booksLV, String tableName, boolean play_icon, boolean tick_icon, boolean header) {
        if (index == 0)
            userVector = UserService.getUserList(mContext, tableName, "");

        Vector<DrList_POJO> subsidiesList = getIndexedBooks(userVector);
        totalListSize = subsidiesList.size();

        userListAdapter = new UserListAdapter(subsidiesList, mContext, header, play_icon, tick_icon);
        booksLV.setAdapter(userListAdapter);
        userListAdapter.notifyDataSetChanged();
    }


    public void setSidepannel(int mode) {
        sidePannel.setVisibility(mode);
    }

    public void SerachViewVis(int mode) {
        searchView.setVisibility(mode);
    }


}
