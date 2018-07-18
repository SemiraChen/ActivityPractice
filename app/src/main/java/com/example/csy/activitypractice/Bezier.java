package com.example.csy.activitypractice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author CSY
 * Created by CSY on 2018/7/17.
 * 二阶贝塞尔曲线
 */
public class Bezier extends View {

    private Paint paint;
    private PointF start, end, control;

    public Bezier(Context context) {
        super(context);
    }

    public Bezier(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        start = new PointF();
        end = new PointF();
        control = new PointF();
    }

    public Bezier(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.v("CSYLLL", String.valueOf(w));
        Log.v("CSYLLL", String.valueOf(h));
        start.x = w / 2 - 200;
        start.y = h / 2 - 200;
        end.x = w / 2 + 200;
        end.y = h / 2 + 200;
        control.x = start.x + 500;
        control.y = start.y + 300;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStrokeWidth(20);
        //先画三个辅助点
        canvas.drawPoint(start.x, start.y, paint);
        canvas.drawPoint(end.x, end.y, paint);
        canvas.drawPoint(control.x, control.y, paint);

        paint.setStrokeWidth(10);
        //然后画两条辅助线
        canvas.drawLine(start.x, start.y, control.x, control.y, paint);
        canvas.drawLine(end.x, end.y, control.x, control.y, paint);
        //然后话曲线
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.quadTo(control.x, control.y, end.x, end.y);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        control.x = event.getX();
        control.y = event.getY();
        invalidate();
        return super.onTouchEvent(event);
    }
}
