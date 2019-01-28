package com.cmykui.framework.cmykui.layout;

import android.content.Context;
import android.graphics.Point;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cmykui.framework.cmykui.R;

public class InboxLayout extends ViewGroup {

    int deviceWidth;
    int deviceHeight;

    TextView TitleText;
    TextView SearchText;
    EditText SearchInput;

    private boolean SearchON = false;
    private float scale = getResources().getDisplayMetrics().density;

    public InboxLayout(Context context) {
        super(context);
    }

    public InboxLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public InboxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;
        deviceHeight = deviceDisplay.y;

        inflate(getContext(), R.layout.inbox_layout, this);

        TitleText = findViewById(R.id.TitleText);
        SearchText = findViewById(R.id.SearchText);
        SearchInput = findViewById(R.id.SearchInput);

        SearchInput.setVisibility(View.GONE);

        SearchText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleShowSearch();
            }
        });

        SearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchChild();
            }
        });
    }

    private void searchChild() {

        final int count = getChildCount();
        String seachText = SearchInput.getText().toString();

        for(int i = 0; i < count; i++){
            View temp = getChildAt(i);

            if(temp instanceof InboxViewItem){
                InboxViewItem child = (InboxViewItem) getChildAt(i);

                if(!child.getTitle().toLowerCase().contains(seachText.toLowerCase())){
                    child.setVisibility(View.GONE);
                }
                else{
                    child.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private void ToggleShowSearch() {
        if(SearchON){
            SearchON = false;
            SearchText.setPadding(0,0,0,(int) (10 * scale + 0.5f));
            SearchInput.setVisibility(View.GONE);
        }
        else {
            SearchON = true;
            SearchText.setPadding(0,0,0,0);
            SearchInput.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;
        //get the available size of child view
        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;
        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                return;
            //Get the maximum size of the child
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();
            //wrap is reach to the end
            if (curLeft + curWidth >= childRight) {
                curLeft = childLeft;
                curTop += maxHeight;
                maxHeight = 0;
            }
            //do the layout
            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
            //store the max height
            if (maxHeight < curHeight)
                maxHeight = curHeight;
            curLeft += curWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        // Measurement will ultimately be computing these values.
        int maxHeight = 50 * count;
        int maxWidth = LayoutParams.MATCH_PARENT;
        int childState = 0;
        int mLeftWidth = 0;
        int rowCount = 0;
        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;

            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }
        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
        resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }


}