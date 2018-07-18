package com.example.csy.activitypractice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * @author CSY
 * Created by CSY on 2018/7/18.
 */
public class BezierCircle extends View {
    Paint mPaint;
    PointF leftStart, leftEnd, leftControlOne, leftControlTwo, leftCenter;
    PointF rightStart, rightEnd, rightControlOne, rightControlTwo, rightCenter;
    PointF topStart, topEnd, topControlOne, topControlTwo, topCenter;

    public BezierCircle(Context context) {
        super(context);
    }

    public BezierCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        leftStart = new PointF();
        leftEnd = new PointF();
        leftControlOne = new PointF();
        leftControlTwo = new PointF();
        leftCenter = new PointF();

        rightStart = new PointF();
        rightEnd = new PointF();
        rightControlOne = new PointF();
        rightControlTwo = new PointF();
        rightCenter = new PointF();

        topStart = new PointF();
        topEnd = new PointF();
        topControlOne = new PointF();
        topControlTwo = new PointF();
        topCenter = new PointF();
    }

    public BezierCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addChildrenForAccessibility(ArrayList<View> outChildren) {
        super.addChildrenForAccessibility(outChildren);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        leftCenter.x = w / 2;
        leftCenter.y = h / 2;
        leftStart.x = leftCenter.x + 100;
        leftStart.y = leftCenter.y + 100;
        leftEnd.x = leftCenter.x + 100;
        leftEnd.y = leftCenter.y - 100;
        leftControlOne.x = leftCenter.x + 100 + (float) (25 * Math.sqrt(2));
        leftControlOne.y = leftCenter.y + 100;
        leftControlTwo.x = leftCenter.x + 100 + (float) (25 * Math.sqrt(2));
        leftControlTwo.y = leftCenter.y - 100;

        rightStart.x = leftCenter.x - 100;
        rightStart.y = leftCenter.y + 100;
        rightEnd.x = leftCenter.x - 100;
        rightEnd.y = leftCenter.y - 100;
        rightControlOne.x = leftCenter.x - 100 - (float) (25 * Math.sqrt(2));
        rightControlOne.y = leftCenter.y + 100;
        rightControlTwo.x = leftCenter.x - 100 - (float) (25 * Math.sqrt(2));
        rightControlTwo.y = leftCenter.y - 100;

        topStart.x = leftCenter.x - 100;
        topStart.y = leftCenter.y + 100;
        topEnd.x = leftCenter.x + 100;
        topEnd.y = leftCenter.y + 100;

        topControlOne.x = leftCenter.x - 100;
        topControlOne.y = leftCenter.y + 100 + (float) (25 * Math.sqrt(2));
        topControlTwo.x = leftCenter.x + 100;
        topControlTwo.y = leftCenter.y + 100 + (float) (25 * Math.sqrt(2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setColor(Color.RED);

        canvas.drawPoint(leftCenter.x, leftCenter.y, mPaint);

        //画点，划线，画曲线
        canvas.drawPoint(leftStart.x, leftStart.y, mPaint);
        canvas.drawPoint(leftEnd.x, leftEnd.y, mPaint);
        canvas.drawPoint(leftControlOne.x, leftControlOne.y, mPaint);
        canvas.drawPoint(leftControlTwo.x, leftControlTwo.y, mPaint);

        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);
        canvas.drawLine(leftStart.x, leftStart.y, leftControlOne.x, leftControlOne.y, mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawLine(leftEnd.x, leftEnd.y, leftControlTwo.x, leftControlTwo.y, mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawLine(leftControlOne.x, leftControlOne.y, leftControlTwo.x, leftControlTwo.y, mPaint);
        mPaint.setColor(Color.RED);
        Path path = new Path();
        path.moveTo(leftStart.x, leftStart.y);
        path.cubicTo(leftControlOne.x, leftControlOne.y, leftControlTwo.x, leftControlTwo.y, leftEnd.x, leftEnd.y);
        canvas.drawPath(path, mPaint);


        //画点，划线，画曲线
        canvas.drawPoint(rightStart.x, rightStart.y, mPaint);
        canvas.drawPoint(rightEnd.x, rightEnd.y, mPaint);
        canvas.drawPoint(rightControlOne.x, rightControlOne.y, mPaint);
        canvas.drawPoint(rightControlTwo.x, rightControlTwo.y, mPaint);

        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);
        canvas.drawLine(rightStart.x, rightStart.y, rightControlOne.x, rightControlOne.y, mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawLine(rightEnd.x, rightEnd.y, rightControlTwo.x, rightControlTwo.y, mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawLine(rightControlOne.x, rightControlOne.y, rightControlTwo.x, rightControlTwo.y, mPaint);
        mPaint.setColor(Color.RED);
        path = new Path();
        path.moveTo(rightStart.x, rightStart.y);
        path.cubicTo(rightControlOne.x, rightControlOne.y, rightControlTwo.x, rightControlTwo.y, rightEnd.x, rightEnd.y);
        canvas.drawPath(path, mPaint);


        //画点，划线，画曲线
        canvas.drawPoint(topStart.x, topStart.y, mPaint);
        canvas.drawPoint(topEnd.x, topEnd.y, mPaint);
        canvas.drawPoint(topControlOne.x, topControlOne.y, mPaint);
        canvas.drawPoint(topControlTwo.x, topControlTwo.y, mPaint);

        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);
        canvas.drawLine(topStart.x, topStart.y, topControlOne.x, topControlOne.y, mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawLine(topEnd.x, topEnd.y, topControlTwo.x, topControlTwo.y, mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawLine(topControlOne.x, topControlOne.y, topControlTwo.x, topControlTwo.y, mPaint);
        mPaint.setColor(Color.RED);
        path = new Path();
        path.moveTo(topStart.x, topStart.y);
        path.cubicTo(topControlOne.x, topControlOne.y, topControlTwo.x, topControlTwo.y, topEnd.x, topEnd.y);
        canvas.drawPath(path, mPaint);
    }
}
