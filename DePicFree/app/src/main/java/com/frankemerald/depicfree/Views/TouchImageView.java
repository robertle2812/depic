package com.frankemerald.depicfree.Views;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.Scroller;

import com.frankemerald.depicfree.R;


public class TouchImageView extends ImageView{

    private static final String DEBUG = "DEBUG";

    //
    // SuperMin and SuperMax multipliers. Determine how much the image can be
    // zoomed below or above the zoom boundaries, before animating back to the
    // min/max zoom boundary.
    //
    private static final float SUPER_MIN_MULTIPLIER = .75f;
    private static final float SUPER_MAX_MULTIPLIER = 1.25f;

    //
    // Scale of image ranges from minScale to maxScale, where minScale == 1
    // when the image is stretched to fit view.
    //
    private float normalizedScale;

    //
    // Matrix applied to image. MSCALE_X and MSCALE_Y should always be equal.
    // MTRANS_X and MTRANS_Y are the other values used. prevMatrix is the matrix
    // saved prior to the screen rotating.
    //
    private Matrix matrix, prevMatrix;

    private static enum State { NONE, DRAG, ZOOM, FLING, ANIMATE_ZOOM };
    private State state;

    private float minScale;
    private float maxScale;
    private float superMinScale;
    private float superMaxScale;
    private float[] m;

    private Context context;
    private Fling fling;

    private ScaleType mScaleType;

    private boolean imageRenderedAtLeastOnce;
    private boolean onDrawReady;

    private ZoomVariables delayedZoomVariables;

    //
    // Size of view and previous view size (ie before rotation)
    //
    private int viewWidth, viewHeight, prevViewWidth, prevViewHeight;

    //
    // Size of image when it is stretched to fit view. Before and After rotation.
    //
    private float matchViewWidth, matchViewHeight, prevMatchViewWidth, prevMatchViewHeight;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    private GestureDetector.OnDoubleTapListener doubleTapListener = null;
    private OnTouchListener userTouchListener = null;
    private OnTouchImageViewListener touchImageViewListener = null;

    boolean measurable = false;


    //////////////////
    private final static float CORNER_RADIUS = 10.0f;

    private Bitmap maskBitmap;
    private Paint paint, maskPaint;
    private float cornerRadius;


    private void init(Context context) {
//        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CORNER_RADIUS, metrics);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        setWillNotDraw(false);
    }

    @Override
    public void draw(Canvas canvas) {
        if(getDrawable()!=null) {
            Bitmap offscreenBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas offscreenCanvas = new Canvas(offscreenBitmap);

            super.draw(offscreenCanvas);

            if (maskBitmap == null) {
                maskBitmap = createMask(canvas.getWidth(), canvas.getHeight());
            }

            offscreenCanvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint);
            canvas.drawBitmap(offscreenBitmap, 0f, 0f, paint);
        }
    }

    private Bitmap createMask(int width, int height) {
        Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(mask);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        canvas.drawRect(0, 0, width, height, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRoundRect(new RectF(0, 0, width, height), cornerRadius, cornerRadius, paint);

        return mask;
    }


    /////////////////////////////

    public TouchImageView(Context context) {

        super(context);
        Log.i("touchimageview","public TouchImageView(Context context) ");
        init(context);
        sharedConstructing(context);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i("touchimageview","public TouchImageView(Context context, AttributeSet attrs) ");
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.com_frankemerald_mark_depic_Views_TouchImageView);
        cornerRadius = a.getDimension(R.styleable.com_frankemerald_mark_depic_Views_TouchImageView_radius_corners, 0);
        a.recycle();
        init(context);
        sharedConstructing(context);
    }

    public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.i("touchimageview","TouchImageView(Context context, AttributeSet attrs, int defStyle) ");
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.com_frankemerald_mark_depic_Views_TouchImageView);
        cornerRadius = a.getDimension(R.styleable.com_frankemerald_mark_depic_Views_TouchImageView_radius_corners, 0);
        a.recycle();
        init(context);
        sharedConstructing(context);
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);
        Log.i("touchimageview","sharedConstructing");
        setDrawingCacheEnabled(true);
        setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);
        this.context = context;
        setScaleType(ScaleType.CENTER_CROP);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureDetector = new GestureDetector(context, new GestureListener());
        matrix = new Matrix();
        prevMatrix = new Matrix();
        m = new float[9];
        normalizedScale = 1;
        if (mScaleType == null) {
            mScaleType = ScaleType.FIT_CENTER;
        }
        minScale = 1;
        maxScale = 3;
        superMinScale = SUPER_MIN_MULTIPLIER * minScale;
        superMaxScale = SUPER_MAX_MULTIPLIER * maxScale;
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);
        setState(State.NONE);
        onDrawReady = false;
        super.setOnTouchListener(new PrivateOnTouchListener());
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        userTouchListener = l;
    }

    public void setOnTouchImageViewListener(OnTouchImageViewListener l) {
        touchImageViewListener = l;
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener l) {
        doubleTapListener = l;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        savePreviousImageValues();
        fitImageToView();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        Log.i("touchimageview","setImageBitmap");
        super.setImageBitmap(bm);
        measurable=true;
        sharedConstructing(context);
        setScaleType(ScaleType.CENTER_CROP);
//        imageRenderedAtLeastOnce = true;
//        savePreviousImageValues();
        fitImageToView();
        measurable=false;
    }

    public void setFilterBitmap(Bitmap bm){
        super.setImageBitmap(bm);
        measurable = true;
        fitImageToView();
        measurable = false;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        savePreviousImageValues();
        fitImageToView();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        savePreviousImageValues();
        fitImageToView();
    }

    @Override
    public void setScaleType(ScaleType type) {
        Log.i("touchimageview","setScaleType ");
        if (type == ScaleType.FIT_START || type == ScaleType.FIT_END) {
            throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
        }
        if (type == ScaleType.MATRIX) {
            super.setScaleType(ScaleType.MATRIX);

        } else {
            mScaleType = type;
            if (onDrawReady) {
                //
                // If the image is already rendered, scaleType has been called programmatically
                // and the TouchImageView should be updated with the new scaleType.
                //
                setZoom(this);
            }
        }
    }

    @Override
    public ScaleType getScaleType() {
        Log.i("touchimageview","getScaleType()");
        return mScaleType;
    }

    /**
     * Returns false if image is in initial, unzoomed state. False, otherwise.
     * @return true if image is zoomed
     */
    public boolean isZoomed() {
        Log.i("touchimageview","isZoomed");
        return normalizedScale != 1;
    }

    /**
     * Return a Rect representing the zoomed image.
     * @return rect representing zoomed image
     */
    public RectF getZoomedRect() {
        if (mScaleType == ScaleType.FIT_XY) {
            throw new UnsupportedOperationException("getZoomedRect() not supported with FIT_XY");
        }
        PointF topLeft = transformCoordTouchToBitmap(0, 0, true);
        PointF bottomRight = transformCoordTouchToBitmap(viewWidth, viewHeight, true);

        float w = getDrawable().getIntrinsicWidth();
        float h = getDrawable().getIntrinsicHeight();
        return new RectF(topLeft.x / w, topLeft.y / h, bottomRight.x / w, bottomRight.y / h);
    }

    /**
     * Save the current matrix and view dimensions
     * in the prevMatrix and prevView variables.
     */
    private void savePreviousImageValues() {
        Log.i("touchimageview","savePreviousImageValues");
        if (matrix != null && viewHeight != 0 && viewWidth != 0) {
            matrix.getValues(m);
            prevMatrix.setValues(m);
            prevMatchViewHeight = matchViewHeight;
            prevMatchViewWidth = matchViewWidth;
            prevViewHeight = viewHeight;
            prevViewWidth = viewWidth;
        }
    }

    private Bitmap getBitmap(){
        Bitmap bitmap = null;
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
//

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("touchimageview","onDraw");
        onDrawReady = true;
        imageRenderedAtLeastOnce = true;
        if (delayedZoomVariables != null) {
            setZoom(delayedZoomVariables.scale, delayedZoomVariables.focusX, delayedZoomVariables.focusY, delayedZoomVariables.scaleType);
            delayedZoomVariables = null;
        }

        //////////
//        if(getDrawable()!=null) {
//            Bitmap bm = getBitmap();
//            int width = bm.getWidth();
//            int height = bm.getHeight();
//            Paint paint = new Paint();
//            paint.setAntiAlias(true);
//            paint.setShader(new BitmapShader(getBitmap(), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
//            canvas.drawRoundRect(new RectF(0, 0, width, height), 10, 10,
//                    paint);
////            bm.recycle();
//        }
        /////////

        super.onDraw(canvas);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("touchimageview","onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        savePreviousImageValues();
    }

    /**
     * Get the max zoom multiplier.
     * @return max zoom multiplier.
     */
    @Override
    public Parcelable onSaveInstanceState() {
        Log.i("touchimageview","onSaveInstanceState");
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putFloat("saveScale", normalizedScale);
        bundle.putFloat("matchViewHeight", matchViewHeight);
        bundle.putFloat("matchViewWidth", matchViewWidth);
        bundle.putInt("viewWidth", viewWidth);
        bundle.putInt("viewHeight", viewHeight);
        matrix.getValues(m);
        bundle.putFloatArray("matrix", m);
        bundle.putBoolean("imageRendered", imageRenderedAtLeastOnce);
        return bundle;
    }
//
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        Log.i("touchimageview","onRestoreInstanceState");
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            normalizedScale = bundle.getFloat("saveScale");
            m = bundle.getFloatArray("matrix");
            prevMatrix.setValues(m);
            prevMatchViewHeight = bundle.getFloat("matchViewHeight");
            prevMatchViewWidth = bundle.getFloat("matchViewWidth");
            prevViewHeight = bundle.getInt("viewHeight");
            prevViewWidth = bundle.getInt("viewWidth");
            imageRenderedAtLeastOnce = bundle.getBoolean("imageRendered");
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
            return;
        }

        super.onRestoreInstanceState(state);
    }

    public float getMaxZoom() {
        return maxScale;
    }

    /**
     * Set the max zoom multiplier. Default value: 3.
     * @param max max zoom multiplier.
     */
    public void setMaxZoom(float max) {
        maxScale = max;
        superMaxScale = SUPER_MAX_MULTIPLIER * maxScale;
    }

    /**
     * Get the min zoom multiplier.
     * @return min zoom multiplier.
     */
    public float getMinZoom() {
        return minScale;
    }

    /**
     * Get the current zoom. This is the zoom relative to the initial
     * scale, not the original resource.
     * @return current zoom multiplier.
     */
    public float getCurrentZoom() {
        Log.i("touchimageview","getCurrentZoom");
        return normalizedScale;
    }

    /**
     * Set the min zoom multiplier. Default value: 1.
     * @param min min zoom multiplier.
     */
    public void setMinZoom(float min) {
        minScale = min;
        superMinScale = SUPER_MIN_MULTIPLIER * minScale;
    }

    /**
     * Reset zoom and translation to initial state.
     */
    public void resetZoom() {
        Log.i("touchimageview","resetZoom");
        normalizedScale = 1;
        fitImageToView();
    }

    /**
     * Set zoom to the specified scale. Image will be centered by default.
     * @param scale
     */
    public void setZoom(float scale) {
        Log.i("touchimageview","setZoom(float scale)");
        setZoom(scale, 0.5f, 0.5f);
    }

    /**
     * Set zoom to the specified scale. Image will be centered around the point
     * (focusX, focusY). These floats range from 0 to 1 and denote the focus point
     * as a fraction from the left and top of the view. For example, the top left
     * corner of the image would be (0, 0). And the bottom right corner would be (1, 1).
     * @param scale
     * @param focusX
     * @param focusY
     */
    public void setZoom(float scale, float focusX, float focusY) {
        Log.i("touchimageview","setZoom(float scale, float focusX, float focusY)");
        setZoom(scale, focusX, focusY, mScaleType);
    }

    /**
     * Set zoom to the specified scale. Image will be centered around the point
     * (focusX, focusY). These floats range from 0 to 1 and denote the focus point
     * as a fraction from the left and top of the view. For example, the top left
     * corner of the image would be (0, 0). And the bottom right corner would be (1, 1).
     * @param scale
     * @param focusX
     * @param focusY
     * @param scaleType
     */
    public void setZoom(float scale, float focusX, float focusY, ScaleType scaleType) {
        Log.i("touchimageview","setZoom(float scale, float focusX, float focusY, ScaleType scaleType)");
        //
        // setZoom can be called before the image is on the screen, but at this point,
        // image and view sizes have not yet been calculated in onMeasure. Thus, we should
        // delay calling setZoom until the view has been measured.
        //
        if (!onDrawReady) {
            delayedZoomVariables = new ZoomVariables(scale, focusX, focusY, scaleType);
            return;
        }

        if (scaleType != mScaleType) {
            setScaleType(scaleType);
        }
        resetZoom();
        scaleImage(scale, viewWidth / 2, viewHeight / 2, true);
        matrix.getValues(m);
        m[Matrix.MTRANS_X] = -((focusX * getImageWidth()) - (viewWidth * 0.5f));
        m[Matrix.MTRANS_Y] = -((focusY * getImageHeight()) - (viewHeight * 0.5f));
        matrix.setValues(m);
        fixTrans();
        setImageMatrix(matrix);
    }

    /**
     * Set zoom parameters equal to another TouchImageView. Including scale, position,
     * and ScaleType.
     */
    public void setZoom(TouchImageView img) {
        Log.i("touchimageview","setZoom(TouchImageView img)");
        PointF center = img.getScrollPosition();
        setZoom(img.getCurrentZoom(), center.x, center.y, img.getScaleType());
    }

    /**
     * Return the point at the center of the zoomed image. The PointF coordinates range
     * in value between 0 and 1 and the focus point is denoted as a fraction from the left
     * and top of the view. For example, the top left corner of the image would be (0, 0).
     * And the bottom right corner would be (1, 1).
     * @return PointF representing the scroll position of the zoomed image.
     */
    public PointF getScrollPosition() {
        Log.i("touchimageview","getScrollPosition");
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null;
        }
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();

        PointF point = transformCoordTouchToBitmap(viewWidth / 2, viewHeight / 2, true);
        point.x /= drawableWidth;
        point.y /= drawableHeight;
        return point;
    }

    /**
     * Set the focus point of the zoomed image. The focus points are denoted as a fraction from the
     * left and top of the view. The focus points can range in value between 0 and 1.
     * @param focusX
     * @param focusY
     */
    public void setScrollPosition(float focusX, float focusY) {
        setZoom(normalizedScale, focusX, focusY);
    }

    /**
     * Performs boundary checking and fixes the image matrix if it
     * is out of bounds.
     */
    private void fixTrans() {
        if(!measurable) return;
        Log.i("touchimageview","fixTrans");
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];

        float fixTransX = getFixTrans(transX, viewWidth, getImageWidth());
        float fixTransY = getFixTrans(transY, viewHeight, getImageHeight());

        if (fixTransX != 0 || fixTransY != 0) {
            matrix.postTranslate(fixTransX, fixTransY);
        }
    }

    /**
     * When transitioning from zooming from focus to zoom from center (or vice versa)
     * the image can become unaligned within the view. This is apparent when zooming
     * quickly. When the content size is less than the view size, the content will often
     * be centered incorrectly within the view. fixScaleTrans first calls fixTrans() and
     * then makes sure the image is centered correctly within the view.
     */
    private void fixScaleTrans() {
        Log.i("touchimageview","fixScaleTrans");
        fixTrans();
        matrix.getValues(m);
        if (getImageWidth() < viewWidth) {
            m[Matrix.MTRANS_X] = (viewWidth - getImageWidth()) / 2;
        }

        if (getImageHeight() < viewHeight) {
            m[Matrix.MTRANS_Y] = (viewHeight - getImageHeight()) / 2;
        }
        matrix.setValues(m);
    }

    private float getFixTrans(float trans, float viewSize, float contentSize) {
        Log.i("touchimageview","getFixTrans(float trans, float viewSize, float contentSize)");
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;

        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }

    private float getFixDragTrans(float delta, float viewSize, float contentSize) {
        Log.i("touchimageview","getFixDragTrans(float delta, float viewSize, float contentSize)");
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    private float getImageWidth() {
        Log.i("touchimageview","getImageWidth");
        return matchViewWidth * normalizedScale;
    }

    private float getImageHeight() {
        Log.i("touchimageview","getImageHeight");
        return matchViewHeight * normalizedScale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        if(measurable) {

            Log.i("touchimageview", "onMeasure");
            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
                setMeasuredDimension(0, 0);
                return;
            }

            int drawableWidth = drawable.getIntrinsicWidth();
            int drawableHeight = drawable.getIntrinsicHeight();
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            viewWidth = setViewSize(widthMode, widthSize, drawableWidth);
            viewHeight = setViewSize(heightMode, heightSize, drawableHeight);

            //
            // Set view dimensions
            //
            setMeasuredDimension(viewWidth, viewHeight);

            //
            // Fit content within view
            //
//        if(measurable)
            fitImageToView();
//        }
//        else{
//            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//
//        }
    }

    /**
     * If the normalizedScale is equal to 1, then the image is made to fit the screen. Otherwise,
     * it is made to fit the screen according to the dimensions of the previous image matrix. This
     * allows the image to maintain its zoom after rotation.
     */
    private void fitImageToView() {
        Log.i("touchimageview","fitImageToView");
        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            return;
        }
        if (matrix == null || prevMatrix == null) {
            return;
        }

        Log.i("fitimage","intro");
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();

        //
        // Scale image for view
        //
        float scaleX = (float) viewWidth / drawableWidth;
        float scaleY = (float) viewHeight / drawableHeight;

        switch (mScaleType) {
            case CENTER:
                scaleX = scaleY = 1;
                break;

            case CENTER_CROP:
                scaleX = scaleY = Math.max(scaleX, scaleY);
                break;

            case CENTER_INSIDE:
                scaleX = scaleY = Math.min(1, Math.min(scaleX, scaleY));

            case FIT_CENTER:
                scaleX = scaleY = Math.min(scaleX, scaleY);
                break;

            case FIT_XY:
                break;

            default:
                //
                // FIT_START and FIT_END not supported
                //
                throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");

        }

        //
        // Center the image
        //
        float redundantXSpace = viewWidth - (scaleX * drawableWidth);
        float redundantYSpace = viewHeight - (scaleY * drawableHeight);
        matchViewWidth = viewWidth - redundantXSpace;
        matchViewHeight = viewHeight - redundantYSpace;
        if (!isZoomed() && !imageRenderedAtLeastOnce) {


            Log.i("fitimage","?!isZoomed&&!imageRenderedAtLeastOnce");
            //
            // Stretch and center image to fit view
            //
            matrix.setScale(scaleX, scaleY);
            matrix.postTranslate(redundantXSpace / 2, redundantYSpace / 2);
            normalizedScale = 1;


        } else if(measurable){


            Log.i("fitimage",":!isZoomed&&!imageRenderedAtLeastOnce");
            //
            // These values should never be 0 or we will set viewWidth and viewHeight
            // to NaN in translateMatrixAfterRotate. To avoid this, call savePreviousImageValues
            // to set them equal to the current values.
            //
            if (prevMatchViewWidth == 0 || prevMatchViewHeight == 0) {
                Log.i("fitimage","?prevMatchViewWidth==0||prevMatchViewHeight==0");

                savePreviousImageValues();
            }

            prevMatrix.getValues(m);

            //
            // Rescale Matrix after rotation
            //
            m[Matrix.MSCALE_X] = matchViewWidth / drawableWidth * normalizedScale;
            m[Matrix.MSCALE_Y] = matchViewHeight / drawableHeight * normalizedScale;

            //
            // TransX and TransY from previous matrix
            //
            float transX = m[Matrix.MTRANS_X];
            float transY = m[Matrix.MTRANS_Y];

            //
            // Width
            //
            float prevActualWidth = prevMatchViewWidth * normalizedScale;
            float actualWidth = getImageWidth();
            translateMatrixAfterRotate(Matrix.MTRANS_X, transX, prevActualWidth, actualWidth, prevViewWidth, viewWidth, drawableWidth);

            //
            // Height
            //
            float prevActualHeight = prevMatchViewHeight * normalizedScale;
            float actualHeight = getImageHeight();
            translateMatrixAfterRotate(Matrix.MTRANS_Y, transY, prevActualHeight, actualHeight, prevViewHeight, viewHeight, drawableHeight);

            //
            // Set the matrix to the adjusted scale and translate values.
            //
            matrix.setValues(m);
        }


        fixTrans();
        setImageMatrix(matrix);
    }

    /**
     * Set view dimensions based on layout params
     *
     * @param mode
     * @param size
     * @param drawableWidth
     * @return
     */
    private int setViewSize(int mode, int size, int drawableWidth) {
        Log.i("touchimageview","setViewSize");
        int viewSize;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                viewSize = size;
                break;

            case MeasureSpec.AT_MOST:
                viewSize = Math.min(drawableWidth, size);
                break;

            case MeasureSpec.UNSPECIFIED:
                viewSize = drawableWidth;
                break;

            default:
                viewSize = size;
                break;
        }
        return viewSize;
    }

    /**
     * After rotating, the matrix needs to be translated. This function finds the area of image
     * which was previously centered and adjusts translations so that is again the center, post-rotation.
     *
     * @param axis Matrix.MTRANS_X or Matrix.MTRANS_Y
     * @param trans the value of trans in that axis before the rotation
     * @param prevImageSize the width/height of the image before the rotation
     * @param imageSize width/height of the image after rotation
     * @param prevViewSize width/height of view before rotation
     * @param viewSize width/height of view after rotation
     * @param drawableSize width/height of drawable
     */
    private void translateMatrixAfterRotate(int axis, float trans, float prevImageSize, float imageSize, int prevViewSize, int viewSize, int drawableSize) {
        if (imageSize < viewSize) {
            Log.i("touchimageview","translateMatrixAfterRotate");
            //
            // The width/height of image is less than the view's width/height. Center it.
            //
            m[axis] = (viewSize - (drawableSize * m[Matrix.MSCALE_X])) * 0.5f;

        } else if (trans > 0) {
            //
            // The image is larger than the view, but was not before rotation. Center it.
            //
            m[axis] = -((imageSize - viewSize) * 0.5f);

        } else {
            //
            // Find the area of the image which was previously centered in the view. Determine its distance
            // from the left/top side of the view as a fraction of the entire image's width/height. Use that percentage
            // to calculate the trans in the new view width/height.
            //
            float percentage = (Math.abs(trans) + (0.5f * prevViewSize)) / prevImageSize;
            m[axis] = -((percentage * imageSize) - (viewSize * 0.5f));
        }
    }

    private void setState(State state) {
        Log.i("touchimageview","setState");
        this.state = state;
    }

    public boolean canScrollHorizontallyFroyo(int direction) {
        return canScrollHorizontally(direction);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        Log.i("touchimageview","canScrollHorizontally");
        matrix.getValues(m);
        float x = m[Matrix.MTRANS_X];

        if (getImageWidth() < viewWidth) {
            return false;

        } else if (x >= -1 && direction < 0) {
            return false;

        } else if (Math.abs(x) + viewWidth + 1 >= getImageWidth() && direction > 0) {
            return false;
        }

        return true;
    }

    /**
     * Gesture Listener detects a single click or long click and passes that on
     * to the view's listener.
     * @author Ortiz
     *
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            Log.i("touchimageview","onSingleTapConfirmed");
//            if(doubleTapListener != null) {
//                return doubleTapListener.onSingleTapConfirmed(e);
//            }
            return performClick();
//            return true;
        }

        @Override
        public void onLongPress(MotionEvent e)
        {
            Log.i("touchimageview","onLongPress");
            performLongClick();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            Log.i("touchimageview","onFling");
            if (fling != null) {
                //
                // If a previous fling is still active, it should be cancelled so that two flings
                // are not run simultaenously.
                //
                fling.cancelFling();
            }
            fling = new Fling((int) velocityX, (int) velocityY);
            compatPostOnAnimation(fling);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("touchimageview","onDoubleTap");
            boolean consumed = false;
            measurable = true;
            if(doubleTapListener != null) {
                consumed = doubleTapListener.onDoubleTap(e);
            }
//            if (state == State.NONE) {
//                float targetZoom = (normalizedScale == minScale) ? maxScale : minScale;
//                DoubleTapZoom doubleTap = new DoubleTapZoom(targetZoom, e.getX(), e.getY(), false);
//                compatPostOnAnimation(doubleTap);
//                consumed = true;
//            }
            measurable = false;
            return consumed;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.i("touchimageview","onDoubleTapEvent");
            measurable = true;
            if(doubleTapListener != null) {
                return doubleTapListener.onDoubleTapEvent(e);
            }
            measurable = false;
            return false;
        }
    }

    public interface OnTouchImageViewListener {
        public void onMove();
    }

    /**
     * Responsible for all touch events. Handles the heavy lifting of drag and also sends
     * touch events to Scale Detector and Gesture Detector.
     * @author Ortiz
     *
     */
    private class PrivateOnTouchListener implements OnTouchListener {

        //
        // Remember last point position for dragging
        //
        private PointF last = new PointF();

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.i("touchimageview","onTouch");
            mScaleDetector.onTouchEvent(event);
            mGestureDetector.onTouchEvent(event);
            PointF curr = new PointF(event.getX(), event.getY());

            if (state == State.NONE || state == State.DRAG || state == State.FLING) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        measurable=true;
                        last.set(curr);
                        if (fling != null)
                            fling.cancelFling();
                        setState(State.DRAG);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (state == State.DRAG) {
                            float deltaX = curr.x - last.x;
                            float deltaY = curr.y - last.y;
                            float fixTransX = getFixDragTrans(deltaX, viewWidth, getImageWidth());
                            float fixTransY = getFixDragTrans(deltaY, viewHeight, getImageHeight());
                            matrix.postTranslate(fixTransX, fixTransY);
                            fixTrans();
                            last.set(curr.x, curr.y);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        measurable = false;
                    case MotionEvent.ACTION_POINTER_UP:
                        setState(State.NONE);
                        break;
                }
            }

            setImageMatrix(matrix);

            //
            // User-defined OnTouchListener
            //
            if(userTouchListener != null) {
                userTouchListener.onTouch(v, event);
            }

            //
            // OnTouchImageViewListener is set: TouchImageView dragged by user.
            //
            if (touchImageViewListener != null) {
                touchImageViewListener.onMove();
            }

            //
            // indicate event was handled
            //
            return false;
        }
    }

    /**
     * ScaleListener detects user two finger scaling and scales image.
     * @author Ortiz
     *
     */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.i("touchimageview","onScaleBegin");
            measurable = true;
            setState(State.ZOOM);
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.i("touchimageview","onScale");
            scaleImage(detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY(), true);

            //
            // OnTouchImageViewListener is set: TouchImageView pinch zoomed by user.
            //
            if (touchImageViewListener != null) {
                touchImageViewListener.onMove();
            }
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            Log.i("touchimageview","onScaleEnd");
            super.onScaleEnd(detector);
            setState(State.NONE);
            boolean animateToZoomBoundary = false;
            float targetZoom = normalizedScale;
            if (normalizedScale > maxScale) {
                targetZoom = maxScale;
                animateToZoomBoundary = true;

            } else if (normalizedScale < minScale) {
                targetZoom = minScale;
                animateToZoomBoundary = true;
            }

            if (animateToZoomBoundary) {
                DoubleTapZoom doubleTap = new DoubleTapZoom(targetZoom, viewWidth / 2, viewHeight / 2, true);
                compatPostOnAnimation(doubleTap);
            }
            measurable = false;
        }
    }

    private void scaleImage(double deltaScale, float focusX, float focusY, boolean stretchImageToSuper) {
        Log.i("touchimageview","scaleImage");

        float lowerScale, upperScale;
        if (stretchImageToSuper) {
            lowerScale = superMinScale;
            upperScale = superMaxScale;

        } else {
            lowerScale = minScale;
            upperScale = maxScale;
        }

        float origScale = normalizedScale;
        normalizedScale *= deltaScale;
        if (normalizedScale > upperScale) {
            normalizedScale = upperScale;
            deltaScale = upperScale / origScale;
        } else if (normalizedScale < lowerScale) {
            normalizedScale = lowerScale;
            deltaScale = lowerScale / origScale;
        }

        matrix.postScale((float) deltaScale, (float) deltaScale, focusX, focusY);
        fixScaleTrans();
    }

    /**
     * DoubleTapZoom calls a series of runnables which apply
     * an animated zoom in/out graphic to the image.
     * @author Ortiz
     *
     */
    private class DoubleTapZoom implements Runnable {

        private long startTime;
        private static final float ZOOM_TIME = 500;
        private float startZoom, targetZoom;
        private float bitmapX, bitmapY;
        private boolean stretchImageToSuper;
        private AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        private PointF startTouch;
        private PointF endTouch;

        DoubleTapZoom(float targetZoom, float focusX, float focusY, boolean stretchImageToSuper) {
            Log.i("touchimageview","DoubleTapZoom(float targetZoom, float focusX, float focusY, boolean stretchImageToSuper)");
            setState(State.ANIMATE_ZOOM);
            startTime = System.currentTimeMillis();
            this.startZoom = normalizedScale;
            this.targetZoom = targetZoom;
            this.stretchImageToSuper = stretchImageToSuper;
            PointF bitmapPoint = transformCoordTouchToBitmap(focusX, focusY, false);
            this.bitmapX = bitmapPoint.x;
            this.bitmapY = bitmapPoint.y;

            //
            // Used for translating image during scaling
            //
            startTouch = transformCoordBitmapToTouch(bitmapX, bitmapY);
            endTouch = new PointF(viewWidth / 2, viewHeight / 2);
        }

        @Override
        public void run() {
            Log.i("touchimageview","DoubleTapZoom.run");
            measurable = true;
            float t = interpolate();
            double deltaScale = calculateDeltaScale(t);
            scaleImage(deltaScale, bitmapX, bitmapY, stretchImageToSuper);
            translateImageToCenterTouchPosition(t);
            fixScaleTrans();
            setImageMatrix(matrix);

            //
            // OnTouchImageViewListener is set: double tap runnable updates listener
            // with every frame.
            //
            if (touchImageViewListener != null) {
                touchImageViewListener.onMove();
            }

            if (t < 1f) {
                //
                // We haven't finished zooming
                //
                compatPostOnAnimation(this);

            } else {
                //
                // Finished zooming
                //
                setState(State.NONE);
            }
            measurable = false;
        }

        /**
         * Interpolate between where the image should start and end in order to translate
         * the image so that the point that is touched is what ends up centered at the end
         * of the zoom.
         * @param t
         */
        private void translateImageToCenterTouchPosition(float t) {
            Log.i("touchimageview","DoubleTapZoom.translateImageToCenterTouchPosition");
            float targetX = startTouch.x + t * (endTouch.x - startTouch.x);
            float targetY = startTouch.y + t * (endTouch.y - startTouch.y);
            PointF curr = transformCoordBitmapToTouch(bitmapX, bitmapY);
            matrix.postTranslate(targetX - curr.x, targetY - curr.y);
        }

        /**
         * Use interpolator to get t
         * @return
         */
        private float interpolate() {
            Log.i("touchimageview","DoubleTapZoom.interpolate");
            long currTime = System.currentTimeMillis();
            float elapsed = (currTime - startTime) / ZOOM_TIME;
            elapsed = Math.min(1f, elapsed);
            return interpolator.getInterpolation(elapsed);
        }

        /**
         * Interpolate the current targeted zoom and get the delta
         * from the current zoom.
         * @param t
         * @return
         */
        private double calculateDeltaScale(float t) {
            Log.i("touchimageview","DoubleTapZoom.calculateDeltaScale");
            double zoom = startZoom + t * (targetZoom - startZoom);
            return zoom / normalizedScale;
        }
    }

    /**
     * This function will transform the coordinates in the touch event to the coordinate
     * system of the drawable that the imageview contain
     * @param x x-coordinate of touch event
     * @param y y-coordinate of touch event
     * @param clipToBitmap Touch event may occur within view, but outside image content. True, to clip return value
     * 			to the bounds of the bitmap size.
     * @return Coordinates of the point touched, in the coordinate system of the original drawable.
     */
    private PointF transformCoordTouchToBitmap(float x, float y, boolean clipToBitmap) {
        Log.i("touchimageview","transformCoordTouchToBitmap");
        matrix.getValues(m);
        float origW = getDrawable().getIntrinsicWidth();
        float origH = getDrawable().getIntrinsicHeight();
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];
        float finalX = ((x - transX) * origW) / getImageWidth();
        float finalY = ((y - transY) * origH) / getImageHeight();

        if (clipToBitmap) {
            finalX = Math.min(Math.max(finalX, 0), origW);
            finalY = Math.min(Math.max(finalY, 0), origH);
        }

        return new PointF(finalX , finalY);
    }

    /**
     * Inverse of transformCoordTouchToBitmap. This function will transform the coordinates in the
     * drawable's coordinate system to the view's coordinate system.
     * @param bx x-coordinate in original bitmap coordinate system
     * @param by y-coordinate in original bitmap coordinate system
     * @return Coordinates of the point in the view's coordinate system.
     */
    private PointF transformCoordBitmapToTouch(float bx, float by) {
        Log.i("touchimageview","transformCoordBitmapToTouch");
        matrix.getValues(m);
        float origW = getDrawable().getIntrinsicWidth();
        float origH = getDrawable().getIntrinsicHeight();
        float px = bx / origW;
        float py = by / origH;
        float finalX = m[Matrix.MTRANS_X] + getImageWidth() * px;
        float finalY = m[Matrix.MTRANS_Y] + getImageHeight() * py;
        return new PointF(finalX , finalY);
    }

    /**
     * Fling launches sequential runnables which apply
     * the fling graphic to the image. The values for the translation
     * are interpolated by the Scroller.
     * @author Ortiz
     *
     */
    private class Fling implements Runnable {

        CompatScroller scroller;
        int currX, currY;

        Fling(int velocityX, int velocityY) {
            Log.i("touchimageview","Fling(int velocityX, int velocityY)");
            setState(State.FLING);
            scroller = new CompatScroller(context);
            matrix.getValues(m);

            int startX = (int) m[Matrix.MTRANS_X];
            int startY = (int) m[Matrix.MTRANS_Y];
            int minX, maxX, minY, maxY;

            if (getImageWidth() > viewWidth) {
                minX = viewWidth - (int) getImageWidth();
                maxX = 0;

            } else {
                minX = maxX = startX;
            }

            if (getImageHeight() > viewHeight) {
                minY = viewHeight - (int) getImageHeight();
                maxY = 0;

            } else {
                minY = maxY = startY;
            }

            scroller.fling(startX, startY, (int) velocityX, (int) velocityY, minX,
                    maxX, minY, maxY);
            currX = startX;
            currY = startY;
        }

        public void cancelFling() {
            Log.i("touchimageview","Fling.cancelFling");
            if (scroller != null) {
                setState(State.NONE);
                scroller.forceFinished(true);
            }
        }

        @Override
        public void run() {
            Log.i("touchimageview","Fling.run");

            //
            // OnTouchImageViewListener is set: TouchImageView listener has been flung by user.
            // Listener runnable updated with each frame of fling animation.
            //
            if (touchImageViewListener != null) {
                touchImageViewListener.onMove();
            }

            if (scroller.isFinished()) {
                scroller = null;
                return;
            }

            if (scroller.computeScrollOffset()) {
                int newX = scroller.getCurrX();
                int newY = scroller.getCurrY();
                int transX = newX - currX;
                int transY = newY - currY;
                currX = newX;
                currY = newY;
                matrix.postTranslate(transX, transY);
                fixTrans();
                setImageMatrix(matrix);
                compatPostOnAnimation(this);
            }
        }
    }

    @TargetApi(VERSION_CODES.GINGERBREAD)
    private class CompatScroller {
        Scroller scroller;
        OverScroller overScroller;
        boolean isPreGingerbread;

        public CompatScroller(Context context) {
            Log.i("touchimageview","CompatScroller(Context context)");
            if (VERSION.SDK_INT < VERSION_CODES.GINGERBREAD) {
                isPreGingerbread = true;
                scroller = new Scroller(context);

            } else {
                isPreGingerbread = false;
                overScroller = new OverScroller(context);
            }
        }

        public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
            Log.i("touchimageview","CompatScroller.fling");
            if (isPreGingerbread) {
                scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
            } else {
                overScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
            }
        }

        public void forceFinished(boolean finished) {
            Log.i("touchimageview","CompatScroller.forceFinished");
            if (isPreGingerbread) {
                scroller.forceFinished(finished);
            } else {
                overScroller.forceFinished(finished);
            }
        }

        public boolean isFinished() {
            Log.i("touchimageview","CompatScroller.isFinished");
            if (isPreGingerbread) {
                return scroller.isFinished();
            } else {
                return overScroller.isFinished();
            }
        }

        public boolean computeScrollOffset() {
            Log.i("touchimageview","CompatScroller.computeScrollOffset");
            if (isPreGingerbread) {
                return scroller.computeScrollOffset();
            } else {
                overScroller.computeScrollOffset();
                return overScroller.computeScrollOffset();
            }
        }

        public int getCurrX() {
            Log.i("touchimageview","CompatScroller.getCurrX");
            if (isPreGingerbread) {
                return scroller.getCurrX();
            } else {
                return overScroller.getCurrX();
            }
        }

        public int getCurrY() {
            Log.i("touchimageview","CompatScroller.getCurrY");
            if (isPreGingerbread) {
                return scroller.getCurrY();
            } else {
                return overScroller.getCurrY();
            }
        }
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    private void compatPostOnAnimation(Runnable runnable) {
        Log.i("touchimageview","compatPostOnAnimation");
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            postOnAnimation(runnable);

        } else {
            postDelayed(runnable, 1000/60);
        }
    }

    private class ZoomVariables {
        public float scale;
        public float focusX;
        public float focusY;
        public ScaleType scaleType;

        public ZoomVariables(float scale, float focusX, float focusY, ScaleType scaleType) {
            Log.i("touchimageview","ZoomVariables(float scale, float focusX, float focusY, ScaleType scaleType)");
            this.scale = scale;
            this.focusX = focusX;
            this.focusY = focusY;
            this.scaleType = scaleType;
        }
    }

    private void printMatrixInfo() {
        float[] n = new float[9];
        matrix.getValues(n);
        Log.d(DEBUG, "Scale: " + n[Matrix.MSCALE_X] + " TransX: " + n[Matrix.MTRANS_X] + " TransY: " + n[Matrix.MTRANS_Y]);
    }

    public float[] getMatrixValues(){
        return m;
//        return matrix;
//        return prevMatrix;
    }

    public Matrix getTestMatrix(){
        return matrix;
    }
}
