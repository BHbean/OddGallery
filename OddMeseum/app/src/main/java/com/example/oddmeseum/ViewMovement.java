package com.example.oddmeseum;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class ViewMovement {
    private WeakReference<View> viewReference;
    private int id;
    private int MODE;//当前状态
    public static final int MODE_NONE = 0;//无操作
    public static final int MODE_DRAG = 1;//单指操作
    public static final int MODE_SCALE = 2;//双指操作

    private double tolerenceDistance = 5.0;
    private int[] startMatrix = new int[4]; // L R T B
    private int[] endMatrix = new int[4];
    //    private Matrix startMatrix = new Matrix(); // L R T B
//    private Matrix endMatrix = new Matrix();
    private PointF startPointF = new PointF();

    private final String LOG_TAG = getClass().getSimpleName();

    public ViewMovement(View view, int id) {
        this.id = id;
        viewReference = new WeakReference<>(view);
    }

    public int handleTouch(View v, MotionEvent event) {
        int result = 0;
        if (v.getId() != id) return result;
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN://单指触碰
                //起始矩阵先获取ImageView的当前状态
                startMatrix[0] = viewReference.get().getLeft();
                startMatrix[1] = viewReference.get().getRight();
                startMatrix[2] = viewReference.get().getTop();
                startMatrix[3] = viewReference.get().getBottom();
//                startMatrix.set(viewReference.get().getMatrix());
                //获取起始坐标
                startPointF.set(event.getRawX(), event.getRawY());
                //此时状态是单指操作
                MODE = MODE_DRAG;
                Log.d(LOG_TAG, "ACTION_DOWN in " + LOG_TAG + " " +
                        Arrays.toString(startMatrix));
                break;
            case MotionEvent.ACTION_POINTER_DOWN://双指触碰
                break;
            case MotionEvent.ACTION_MOVE://滑动（单+双）

                if (MODE == MODE_DRAG) {//单指滑动时
                    //先把初始位置传给变化后位置,向矩阵传入位移距离
                    int width = viewReference.get().getWidth();
                    int height = viewReference.get().getHeight();
                    int parentWidth = ((View)viewReference.get().getParent()).getWidth();
                    int parentHeight = ((View)viewReference.get().getParent()).getHeight();
                    int deltaX = (int)(event.getRawX() - startPointF.x);
                    int deltaY = (int)(event.getRawY() - startPointF.y);
                    endMatrix[0] = startMatrix[0] + deltaX;
                    endMatrix[1] = startMatrix[1] + deltaX;
                    endMatrix[2] = startMatrix[2] + deltaY;
                    endMatrix[3] = startMatrix[3] + deltaY;
                    if (endMatrix[0] < 0) {
                        endMatrix[0] = 0;
                        endMatrix[1] = width;
                    }
                    if (endMatrix[2] < 0) {
                        endMatrix[2] = 0;
                        endMatrix[3] = height;
                    }
                    if (endMatrix[1] >= parentWidth) {
                        endMatrix[0] = parentWidth - width;
                        endMatrix[1] = parentWidth;
                    }
                    if (endMatrix[3] >= parentHeight) {
                        endMatrix[2] = parentHeight - height;
                        endMatrix[3] = parentHeight;
                    }
//                    float deltaX = (event.getRawX() - startPointF.x);
//                    float deltaY = (event.getRawY() - startPointF.y);
//                    endMatrix.set(startMatrix);
//                    endMatrix.postTranslate(deltaX, deltaY);
                    //向矩阵传入位移距离
                    Log.d(LOG_TAG, "ACTION_MOVE in " + LOG_TAG);
                }


                break;
            case MotionEvent.ACTION_UP://单指离开
                if (getDistance(event) > tolerenceDistance) {
                    result = 1; // 拖動
                } else {
                    result = 2; // 點擊
                }
            case MotionEvent.ACTION_POINTER_UP://双指离开
                //手指离开后，重置状态
                MODE = MODE_NONE;
                break;
        }
        //事件结束后，把矩阵的变化同步到ImageView上
        viewReference.get().layout(endMatrix[0], endMatrix[2], endMatrix[1], endMatrix[3]);
//        viewReference.get().setAnimationMatrix(endMatrix);
        return result;
    }

    double getDistance(MotionEvent event) {
        double deltaX = event.getRawX() - startPointF.x;
        double deltaY = event.getRawY() - startPointF.y;
        return Math.sqrt(
                deltaX * deltaX + deltaY * deltaY
        );
    }
}
