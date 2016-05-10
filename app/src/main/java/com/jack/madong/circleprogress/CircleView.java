package com.jack.madong.circleprogress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by madong on 2016/4/12.
 */
public class CircleView extends View{

    private static String TAG = "QQHealthView";

    private Paint allPaint;//画总弧的画笔
    private Paint progressPaint;//画进度
    private Paint curSpeedPaint;


    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
//    private Paint curSpeedPaint;


    private Paint bitmapPaint;
    private Canvas mCanvas;



    private float curSpeedSize = dipToPx(13);
    private float textSize = dipToPx(30);
    private float hintSize = dipToPx(15);

    private int currceProgress;
    private  int maxProgress;
    private String hintColor = "#676767";





    private int mArcCenterX;
    private int mArcCenterY;
    private RectF mArcRect;
    private float mRatio;
    private int mWidth;//自定义View宽
    private int mHeight;//自定义View高
    private float mArcWidth;//画笔的宽度
    float xPos;
    float yPos;
    private boolean isDraw = true;


    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultWidth = Integer.MAX_VALUE;
        int width;
        int height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //  int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //  int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            width = widthSize;
        } else {
            width = defaultWidth;
        }
        int defaultHeight = (int) (width * 1.f / mRatio);//高取宽的一定比列
        height = defaultHeight;
        setMeasuredDimension(width, height);
        Log.i("TAG", "width:" + width + "| height:" + height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mArcCenterX = (int) (mWidth / 2.f);
        mArcCenterY = (int) (160.f / 525.f * mHeight);
        mArcRect = new RectF();//这里为外切矩形设置显示比列
        mArcRect.left = mArcCenterX - 125.f / 450.f * mWidth;
        mArcRect.top = mArcCenterY - 125.f / 525.f * mHeight;
        mArcRect.right = mArcCenterX + 125.f / 450.f * mWidth;
        mArcRect.bottom = mArcCenterY + 125.f / 525.f * mHeight;

        mArcWidth = 20.f / 450.f * mWidth;
//        mBarWidth = 16.f / 450.f * mWidth;

        //画笔的宽度一定要在这里设置才能自适应
        allPaint.setStrokeWidth(mArcWidth);
        progressPaint.setStrokeWidth(mArcWidth);
//        mBarPaint.setStrokeWidth(mBarWidth);
    }



    private void init() {
        //自定义View的宽高比例
        mRatio = 450.f / 525.f;
        allPaint = new Paint();
        allPaint.setAntiAlias(true);
        allPaint.setColor(Color.parseColor("#e7ebe5"));
        allPaint.setStrokeCap(Paint.Cap.ROUND);//设置圆角
        allPaint.setStyle(Paint.Style.STROKE);


        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);//设置圆角
//        progressPaint.setColor(Color.BLUE);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setDither(true);
        progressPaint.setStrokeJoin(Paint.Join.MITER);



        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(Color.parseColor(hintColor));
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);


        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.BLACK);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(Color.parseColor(hintColor));
        hintPaint.setTextAlign(Paint.Align.CENTER);

        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(Color.parseColor(hintColor));
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);


        //画小图
        bitmapPaint = new Paint();
        bitmapPaint.setStrokeCap(Paint.Cap.ROUND);//设置圆角
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));


        Bitmap bitmap=Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);
    }



    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        float right =  getWidth()/2;
        float left =  getWidth()/2;

        Shader mShader = new SweepGradient(right,left,new int[]{Color.parseColor("#ff2b00"), Color.parseColor("#ffd200"), Color.parseColor("#61f09a"), Color.parseColor("#61f09a")},null);
        Matrix matrix = new Matrix();
        matrix.setRotate(130, right, left);//很重要
        mShader.setLocalMatrix(matrix);
//        progressPaint.setShader(mShader);
        progressPaint.setShader(mShader);
//        final RectF rect = new RectF(10, 10, 250,250);//外切矩形

        //画整个圆弧
        canvas.drawArc(mArcRect,135,270,false,allPaint);
//        canvas.drawArc();

        if(isDraw){
            //画进度
            canvas.drawArc(mArcRect,135, 270-currceProgress,false,progressPaint);
        }


        float textWidth = curSpeedPaint.measureText("流量");
//        canvas.drawText("流量"+"\n80"+"\nhhhhhh", left-textWidth/2,left -textWidth/2,curSpeedPaint);
//        canvas.drawText("流量", 100, 100, curSpeedPaint);




//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
////        int bitmapWidth = bitmap.getWidth();
////        canvas.drawBitmap(bitmap,left-bitmapWidth,right-bitmapWidth,bitmapPaint);
//
////        Bitmap.createScaledBitmap(bitmap,(int)bitmap.getWidth()*mRatio,(int)bitmap.getHeight()*mRatio,false);
////        BitmapFactory.
//
//        Bitmap mBitmap = zoomImage(bitmap,mRatio);
//        Rect mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
//        Rect mDestRect = new Rect(
//                mArcCenterX-mBitmap.getWidth()/2,mArcCenterY-mBitmap.getHeight(),
//                mArcCenterX+mBitmap.getWidth()/2,mArcCenterY+mBitmap.getHeight());
//
//        canvas.drawBitmap(mBitmap, mSrcRect, mDestRect, bitmapPaint);

//        canvas.drawText("50", left-textWidth/2, right + textSize / 3, vTextPaint);
//        canvas.drawText("力量", left-textWidth/2, right + 2 * textSize / 3, hintPaint);
//        canvas.drawText("M", left-textWidth/2, right - 2 * textSize / 3, curSpeedPaint);


        /**
         * 测量后的
         */
        //4.绘制圆弧里面的文字
        xPos = mArcCenterX;
        yPos = (int) (mArcCenterY - 40.f / 525.f * mHeight);
        hintPaint.setTextAlign(Paint.Align.CENTER);
        hintPaint.setTextSize(15.f / 450.f * mWidth);
        canvas.drawText("截至22:50分已走", xPos, yPos, hintPaint);
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setTextSize(42.f / 450.f * mWidth);
//        vTextPaint.setColor(mThemeColor);
        canvas.drawText( "50", mArcCenterX, mArcCenterY, vTextPaint);
        yPos = (int) (mArcCenterY + 50.f / 525.f * mHeight);
        curSpeedPaint.setColor(Color.parseColor("#C1C1C1"));
        curSpeedPaint.setTextSize(13.f / 450.f * mWidth);
        canvas.drawText("好友平均5620步", mArcCenterX, yPos, curSpeedPaint);


    }

    /**
     * dip 转换成px
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }


    public void setProgress(int progress){
        if(progress==0){
            isDraw = false;
        }
        this.currceProgress= progress*270/100;
//        progress;
        postInvalidate();
    }


    /**
     *
     * @param bgimage
     * @param mRatio
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, float mRatio) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = mRatio* width;
        float scaleHeight = mRatio* height;
        // 缩放图片动作
//        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height,matrix, true);

//        Bitmap.Config;
//        Bitmap.createBitmap(width,height,new Bitmap.Config());
        return bitmap;
    }

}
