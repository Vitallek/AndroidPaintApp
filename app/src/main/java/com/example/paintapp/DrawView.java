package com.example.paintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

public class DrawView extends View {

    public static final float TOUCH_TOLERANCE = 10;

    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Paint paintScreen;
    private Paint paintLine;
    private HashMap<Integer, Path> pathMap;
    private HashMap<Integer, Point> previousPointMap;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        paintScreen = new Paint();

        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.BLACK);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(23);
        paintLine.setStrokeCap(Paint.Cap.ROUND);

        pathMap = new HashMap<>();
        previousPointMap = new HashMap<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap,0,0, paintScreen);

        for(Integer key: pathMap.keySet()) {
            canvas.drawPath(pathMap.get(key), paintLine);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked(); //event type
        int actionIndex = event.getActionIndex(); //pointer

        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_UP){
            //if finger is touching screen
            touchStarted(event.getX(actionIndex), event.getY(actionIndex), event.getPointerId(actionIndex));

        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
            touchEnded(event.getPointerId(actionIndex));
        } else {
            touchMoved(event);
        }

        invalidate(); //redraw screen
        return true; //super.onTouchEvent(event);
    }

    private void touchMoved (MotionEvent event){
        for(int i = 0; i < event.getPointerCount(); i++) {
            int pointerID = event.getPointerId(i);
            int pointerIndex = event.findPointerIndex(pointerID);

            if(pathMap.containsKey(pointerID)) {
                float newX = event.getX(pointerIndex);
                float newY = event.getY(pointerIndex);

                Path path = pathMap.get(pointerID);
                Point point = previousPointMap.get(pointerID);

                //calc how far user moved from the last update
                float deltaX = Math.abs(newX - point.x);
                float deltaY = Math.abs(newY - point.y);

                //check if it's a movement actually
                if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) {
                    path.quadTo(point.x, point.y, (newX + point.x) / 2, (newY + point.y) / 2);
                    point.x = (int) newX;
                    point.y = (int) newY;
                }
            }
        }
    }

    public void clear() {
        pathMap.clear(); //remove all paths
        previousPointMap.clear();
        bitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    private void touchEnded (int pointerID){
        Path path = pathMap.get(pointerID); //get the corresponding Path
        bitmapCanvas.drawPath(path, paintLine); //draw to bitmapCanvas
        path.reset();
    }

    private void touchStarted (float x, float y, int pointerID){
        Path path; //store path for given touch
        Point point; //store last point in path

        if(pathMap.containsKey(pointerID)) {
            path = pathMap.get(pointerID);
            point = previousPointMap.get(pointerID);
        } else {
            path = new Path();
            pathMap.put(pointerID, path);
            point = new Point();
            previousPointMap.put(pointerID, point);
        }

        //move to the coordinates of the touch
        path.moveTo(x,y);
        point.x = (int) x;
        point.y = (int) y;
    }
}
