package com.rorantes.muvruler;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class RulerFragment extends Fragment {
    private static final float dp = 18f;
    DisplayMetrics metrics;
    int inch;
    int rulerLineWidth = 5;
    float[] markerCoords;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RulerView(getActivity());
    }

    private class RulerView extends View{

        public RulerView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.WHITE);

            metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int totalWidth = metrics.widthPixels;
            float mXDpi = metrics.xdpi;
            inch = Math.round(mXDpi);
            float fractionOfInch = inch / 8;
            //how tall the line
            int height = metrics.heightPixels;

            //Draw red line to place the measurement
            Paint marker = new Paint();
            marker.setColor(Color.RED);
            marker.setStyle(Paint.Style.STROKE);
            marker.setStrokeWidth(rulerLineWidth);
            if(markerCoords == null){
                //first time on create
                markerCoords = new float[]{0, 0, 0, (metrics.heightPixels/2)};
            }
            canvas.drawLine(markerCoords[0], markerCoords[1], markerCoords[2], markerCoords[3], marker);

            //ruler line settings
            Paint rulerLinePaint = new Paint();
            rulerLinePaint.setColor(Color.BLACK);
            rulerLinePaint.setStyle(Paint.Style.STROKE);
            rulerLinePaint.setStrokeWidth(rulerLineWidth);

            Paint rulerText = new Paint();
            float fpixels = metrics.density * dp;
            int fontSize = (int) (fpixels + 0.5f);
            rulerText.setTextSize(fontSize);
            Typeface tf = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
            rulerText.setTypeface(tf);

            //Line Points
            float startX;
            float stopX;
            //stay on top of device for the lines
            float startY = 0;
            float stopY;
            int y = 1;
            int digits = 0;
            for(int i = 0; i < totalWidth; i += Math.round(fractionOfInch)){
                startX = stopX = (i + rulerLineWidth);
                if(y == 8){
                    stopY = height / 4;
                    canvas.drawText(Integer.toString(++digits), (startX - (fontSize/4)), (stopY + fontSize), rulerText);
                    y = 0;
                } else if(y % 2 == 0){
                    stopY = height / 8;
                } else {
                    stopY = height / 16;
                }
                canvas.drawLine(startX, startY, stopX, stopY, rulerLinePaint);
                y++;
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    markerCoords = new float[]{event.getX(),0,event.getX(), (metrics.heightPixels / 2)};
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    markerCoords = new float[]{event.getX(),0,event.getX(), (metrics.heightPixels / 2)};
                    invalidate();
                case MotionEvent.ACTION_CANCEL:
                    break;
                case MotionEvent.ACTION_UP:
                    float measuredDistance = event.getX()/ inch;
                    break;
            }
            return true;
        }
    }
}
