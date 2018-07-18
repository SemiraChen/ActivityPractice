package com.example.csy.activitypractice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author CSY
 * Created by CSY on 2018/7/18.
 * 三阶贝塞尔曲线
 */
public class Bezier2 extends View {
    Paint paint;
    private PointF start, end, control, control2, center;
    private int mode = 0;

    public Bezier2(Context context) {
        super(context);
    }

    public Bezier2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint = new Paint();
        start = new PointF();
        end = new PointF();
        control = new PointF();
        control2 = new PointF();
        center = new PointF();
    }

    public Bezier2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mode == 0) {
            control.x = event.getX();
            control.y = event.getY();
        } else {
            control2.x = event.getX();
            control2.y = event.getY();
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        center.x = w / 2;
        center.y = h / 2;
        start.x = center.x - 200;
        start.y = center.y - 200;
        end.x = center.x + 200;
        end.y = center.y - 200;
        control.x = center.x + 50;
        control.x = center.y + 100;
        control2.x = center.x - 50;
        control2.y = center.y + 100;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(20);
        //先画三个辅助点
        canvas.drawPoint(start.x, start.y, paint);
        canvas.drawPoint(end.x, end.y, paint);
        canvas.drawPoint(control.x, control.y, paint);
        canvas.drawPoint(control2.x, control2.y, paint);

        paint.setStrokeWidth(10);
        //然后画两条辅助线
        canvas.drawLine(start.x, start.y, control2.x, control2.y, paint);
        canvas.drawLine(end.x, end.y, control.x, control.y, paint);
        canvas.drawLine(control.x, control.y, control2.x, control2.y, paint);
        //然后话曲线
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.cubicTo(control.x, control.y, control2.x, control2.y, end.x, end.y);
        canvas.drawPath(path, paint);
    }

}
