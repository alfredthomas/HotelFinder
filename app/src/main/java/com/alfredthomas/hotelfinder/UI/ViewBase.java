package com.alfredthomas.hotelfinder.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ViewBase extends ViewGroup {
    public ViewBase(Context context) {
        super(context);
    }

    public ViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //just layout children
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChildren();
    }
    //measure views with positioning, so just layout sequentially
    public void layoutChildren()
    {
        int childCount = this.getChildCount();
        for(int i = 0; i<childCount;i++)
        {
            View view = getChildAt(i);
            //we keep track of left,top,right,bottom in our new LayoutParams
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            view.layout(layoutParams.left,layoutParams.top,layoutParams.right,layoutParams.bottom);
        }
    }

    //new LayoutParams including left,top,right,and bottom
    public class LayoutParams extends ViewGroup.LayoutParams
    {
        public int left, top, right, bottom;

        public LayoutParams(int width, int height)
        {
            super(width,height);
        }
        public LayoutParams()
        {
            super(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void updateLayoutParams(View view,int left,int top, int width, int height)
    {
        ViewBase.LayoutParams params = (ViewBase.LayoutParams)view.getLayoutParams();

        params.left = left;
        params.top = top;
        params.width = width;
        params.height = height;
        params.bottom = top+height;
        params.right = left+width;

    }

    //measure views individually, updating the properties of the LayoutParams
    public void measureView(View view, int left, int top, int width, int height)
    {
        this.updateLayoutParams(view,left,top,width,height);
        this.measureView(view);
    }

    //measure view with exact MeasureSpec
    public void measureView(View view)
    {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        view.measure(MeasureSpec.makeMeasureSpec(layoutParams.width,MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(layoutParams.height,MeasureSpec.EXACTLY));
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ViewBase.LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width,p.height);
    }
}
