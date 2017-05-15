package github.lgyjg.compositionlines;

/*
 * Copyright 2017 Jianguo Yang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class FibonacciView extends View {
    private static final String TAG = "FibonacciView";

    Paint mPaint = new Paint();

    public FibonacciView(Context context) {
        super(context);
        setPaintColor(Color.WHITE);
    }

    public FibonacciView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPaintColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long start = System.currentTimeMillis();
        Trace.beginSection("drawFibonacci");
        drawFibonacci(canvas);
        Trace.endSection();
        Log.d(TAG, "onDraw: spend :" + (System.currentTimeMillis() - start));
    }

    /**
     * draw Fibonacci view on canvas
     *
     * @param canvas canvas to draw
     */
    private void drawFibonacci(Canvas canvas) {
        final int DEEP = 15; // only support 15 now!
        float startAngle = 0;
        float endAngle = startAngle + 90;
        int flag = 1;
        float length = 0;
        float nextLength;
        float lastLength = 0;
        float translateX = 0;
        float translateY = 0;

        // [0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765]
        ArrayList<Integer> fib = new ArrayList<>();
        fib.add(0);
        fib.add(1);
        int len = fib.size();
        for (int i = len; i <= DEEP; i++) {
            fib.add((fib.get(i - 1) + fib.get(i - 2)));
        }

        canvas.save();
        // the position of fibonacci's original point
        int wd = this.getWidth();
        // 15: deep -1 deep
        float scale = (float) wd / fib.get(DEEP - 1);
        Log.d(TAG, "scale: " + scale);
        // goto start position
        canvas.clipRect(1, 0, wd - 3, getHeight() - 2);
        canvas.translate((int) (wd * 0.278), (int) (this.getHeight() - (fib.get(DEEP) * scale) * 0.278));
        canvas.scale(scale, scale);

        // set mPaint
        mPaint.setAlpha(255);
        mPaint.setStrokeWidth(1.5f);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        for (int i = 5; i < DEEP; i++) {
            length = fib.get(i);
            nextLength = fib.get(i + 1);
            lastLength = fib.get(i - 1);
            // if draw Rect open it
            RectF rectfRect = new RectF(0f, 0f, length, length);
            canvas.drawRect(rectfRect, mPaint);
            // draw Arc
            RectF rectf = new RectF(0f, 0f, length * 2, length * 2);
            if (i % 2 != 0) {
                if (flag <= 2) {
                    Log.d(TAG, "drawFibonacci: 1");
                    startAngle = 0;
                    rectf.offset(-length, -length);
                    translateX = length - nextLength;
                    translateY = -fib.get(i + 1);
                } else {
                    Log.d(TAG, "drawFibonacci: 3");
                    startAngle = 180;
                    rectf.offset(0, 0);
                    translateX = 0;
                    translateY = length;
                }
            } else {
                if (flag <= 2) {
                    Log.d(TAG, "drawFibonacci: 2");
                    startAngle = 270;
                    rectf.offset(-length, 0);
                    translateX = -nextLength;
                    translateY = 0;
                } else {
                    Log.d(TAG, "drawFibonacci: 4");
                    startAngle = 90;
                    rectf.offset(0, -length);
                    translateX = length;
                    translateY = length - nextLength;
                }
            }
            canvas.drawArc(rectf, startAngle, endAngle, false, mPaint);
            canvas.translate(translateX, translateY);
            flag++;
            if (flag > 4) {
                flag = 1;
            }
        }
        canvas.translate(-translateX, -translateY);
        canvas.save();
//        mPaint.setColor(Color.RED);
//        mPaint.setStrokeWidth(1.5f);
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
//        canvas.drawRect(0, 0, length, length + lastLength, mPaint);
        canvas.clipRect(0, 0, length, length + lastLength);
        canvas.restore();
    }

    /**
     * @return return drawed bitmap
     */
    private Bitmap getFibonacciBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawFibonacci(canvas);
        return bitmap;
    }

    /**
     * set line's color
     *
     * @param color The new color (including alpha) to set in the mPaint.
     */
    public void setPaintColor(int color) {
        mPaint.setColor(color);
    }
}
