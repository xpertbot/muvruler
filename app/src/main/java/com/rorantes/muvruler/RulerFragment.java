package com.rorantes.muvruler;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class RulerFragment extends Fragment {
    static final String DEVICE_WIDTH = "device_width";
    DisplayMetrics metrics;
    int rulerLineWidth = 20;

    @Override
    public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        if(savedInstaceState != null){
            metrics = (DisplayMetrics) savedInstaceState.getSerializable(DEVICE_WIDTH);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RulerView(getActivity());
    }

    private class RulerView extends View {

        public RulerView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.WHITE);

            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int totalWidth = metrics.widthPixels;
            float mXDpi = metrics.xdpi;
            int inch = Math.round(mXDpi);

            //ruler line settings
            Paint rulerLinePaint = new Paint();
            rulerLinePaint.setColor(Color.BLACK);
            rulerLinePaint.setStyle(Paint.Style.STROKE);
            rulerLinePaint.setStrokeWidth(rulerLineWidth);
            //Line Points
            float startX;
            float stopX;
            //stay on top of device for the lines
            float startY = 0;
            //how tall the line
            float stopY = metrics.heightPixels / 2;

            for(int i = inch; i < totalWidth; i = (i + rulerLineWidth)){
                startX = stopX = i + rulerLineWidth;
                Log.d("MyDebug", Float.toString(startX));
                Log.d("MyDebug", Float.toString(startY));
                Log.d("MyDebug", Float.toString(stopX));
                Log.d("MyDebug", Float.toString(stopY));
                canvas.drawLine(startX, startY, stopX, stopY, rulerLinePaint);
            }
        }
    }
}
