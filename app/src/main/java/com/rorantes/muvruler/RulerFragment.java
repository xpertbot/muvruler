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
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class RulerFragment extends Fragment {
    static final String DEVICE_WIDTH = "device_width";
    private static final float dp = 18f;
    DisplayMetrics metrics;
    int rulerLineWidth = 5;

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
            float fractionOfInch = inch / 8;

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
            //how tall the line
            int height = metrics.heightPixels;
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
    }
}
