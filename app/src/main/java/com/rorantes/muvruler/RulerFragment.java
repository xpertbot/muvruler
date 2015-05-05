package com.rorantes.muvruler;


import android.app.Activity;
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
    OnSavingMeasurement callback;
    private static final float dp = 18f;
    private float measuredDistance = 0f;
    DisplayMetrics metrics;
    float inch;
    float rulerLineWidth = 5f;
    float[] markerCoords;
    boolean gpsOn = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (OnSavingMeasurement) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnsavingMeasurement");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RulerView(getActivity());
    }

    public void setGPSFlag(){
        gpsOn = callback.onGPSAction();
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
            inch = metrics.xdpi;
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
                markerCoords = new float[]{0, 0, 0, (height/2)};
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

            //Current Measurement
            Paint currentMeasurementText = new Paint();
            currentMeasurementText.setTextSize(fontSize);
            currentMeasurementText.setTypeface(tf);
            canvas.drawText(Float.toString(measuredDistance), ((totalWidth / 2) - (fontSize / 4)), (height - (height / 4)), currentMeasurementText);

            //Line Points
            float startX;
            float stopX;
            //stay on top of device for the lines
            float startY = 0;
            float stopY;
            int y = 0;
            int digits = 0;
            for(float i = 0; i < totalWidth; i += fractionOfInch){
                startX = stopX = i;
                if(y == 8){
                    stopY = height / 4;
                    canvas.drawText(Integer.toString(++digits), (startX - (fontSize/4)), (stopY + fontSize), rulerText);
                    y = 0;
                } else if(y % 2 == 0){
                    stopY = height / 8;
                } else {
                    stopY = height / 16;
                }
                if(i != 0){
                    canvas.drawLine(startX, startY, stopX, stopY, rulerLinePaint);
                }
                y++;
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (gpsOn == true) {
                invalidate();
                return false;
            } else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        markerCoords = new float[]{event.getRawX(), 0, event.getRawX(), (metrics.heightPixels / 2)};
                        invalidate();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        measuredDistance = event.getRawX() / inch;
                        markerCoords = new float[]{event.getRawX(), 0, event.getRawX(), (metrics.heightPixels / 2)};
                        invalidate();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        measuredDistance = event.getRawX() / inch;
                        break;
                }
                callback.onSaveOption(measuredDistance);
                return true;
            }
        }
    }

    public interface OnSavingMeasurement{
        public void onSaveOption(float measuredDistance);
        public boolean onGPSAction();
    }
}
