package loading.geakw.com.loading;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by geakw on 2015/8/17.
 */
public class LoadingDrawable extends Drawable implements Animatable {
    private View mAnimView;
    private Animation mAnimation;
    private Ring mRing;
    private static final int DURATION = 1000 * 80 / 60;
    private static final float MAX_ARC = .8f;
    private StartArcInterpolator startArcInterpolator = new StartArcInterpolator();
    private EndArcInterpolator endArcInterpolator = new EndArcInterpolator();
    private float mRotation;
    private float mRotationCount;
    private static final float NUM_POINTS = 5f;
    public LoadingDrawable(View animView){
        mAnimView = animView;
        mRing = new Ring();
        setUpAnimation();
    }

    private void setUpAnimation() {
        mAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                float minArc = (float) Math.toRadians(mRing.getStokeWidth() / (2 * Math.PI * mRing.getOvalRadius()));
                float endMaxArc = MAX_ARC - minArc;
                float startArcNow = mRing.getBeginStartArc() + MAX_ARC * startArcInterpolator.getInterpolation(interpolatedTime);
                float endArcNow = mRing.getBeginEndArc() + endMaxArc * endArcInterpolator.getInterpolation(interpolatedTime);
                float sweepArc = endArcNow - startArcNow;
                if(Math.abs(sweepArc) >= 1.0f){
                    endArcNow = startArcNow + 0.5f;
                }
                mRing.setStartArc(startArcNow);
                mRing.setEndArc(endArcNow);
                mRotation = ((720.0f / NUM_POINTS) * interpolatedTime)
                        + (720.0f * (mRotationCount / NUM_POINTS));
            }
        };
        mAnimation.setDuration(DURATION);
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mRotationCount = 0;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                mRing.storeStatus();
                mRotationCount = (mRotationCount + 1) % (NUM_POINTS);
            }
        });
    }

    @Override
    public void start() {
       if(mAnimation != null){
           mAnimView.startAnimation(mAnimation);
       }
    }

    @Override
    public void stop() {
       mAnimView.clearAnimation();
    }

    @Override
    public boolean isRunning() {
        return mAnimation != null && !mAnimation.hasEnded();
    }

    @Override
    public void draw(Canvas canvas) {
        final Rect bounds = getBounds();
        final int saveCount = canvas.save();
        canvas.rotate(mRotation,bounds.centerX(),bounds.centerY());
        mRing.draw(canvas,bounds);
        canvas.restoreToCount(saveCount);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }


    private class Ring {
        private float startArc;
        private float endArc;
        private float beginStartArc;
        private float beginEndArc;
        private float stokeWidth;
        private double ovalRadius;
        private RectF tempRec = new RectF();
        private Paint paint = new Paint();

        public Ring(){
            paint.setStrokeCap(Paint.Cap.SQUARE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#566da9"));
            paint.setStrokeWidth(6);
            ovalRadius = 56;
            stokeWidth = 5;
        }

        public float getStartArc() {
            return startArc;
        }

        public void setStartArc(float startArc) {
            this.startArc = startArc;
            invalidateSelf();
        }

        public float getEndArc() {
            return endArc;
        }

        public void setEndArc(float endArc) {
            this.endArc = endArc;
        }

        public float getBeginStartArc() {
            return beginStartArc;
        }

        public void setBeginStartArc(float beginStartArc) {
            this.beginStartArc = beginStartArc;
        }

        public float getBeginEndArc() {
            return beginEndArc;
        }

        public void setBeginEndArc(float beginEndArc) {
            this.beginEndArc = beginEndArc;
            invalidateSelf();
        }

        public float getStokeWidth() {
            return stokeWidth;
        }

        public void setStokeWidth(float stokeWidth) {
            this.stokeWidth = stokeWidth;
        }

        public double getOvalRadius() {
            return ovalRadius;
        }

        public void setOvalRadius(double ovalRadius) {
            this.ovalRadius = ovalRadius;
        }

        public RectF getTempRec() {
            return tempRec;
        }

        public void setTempRec(RectF tempRec) {
            this.tempRec = tempRec;
        }

        public Paint getPaint() {
            return paint;
        }

        public void setPaint(Paint paint) {
            this.paint = paint;
        }

        public void draw(Canvas c,Rect bounds){
            RectF rectF = tempRec;
            rectF.set(bounds);
            rectF.inset(20,20);
            c.drawArc(rectF,startArc * 360,(endArc - startArc) * 360,false,paint);
        }

        public void storeStatus(){
            beginStartArc = startArc;
            beginEndArc = endArc;
            startArc = endArc;
            invalidateSelf();
        }
    }

    private class StartArcInterpolator extends AccelerateInterpolator{

        @Override
        public float getInterpolation(float input) {
            return super.getInterpolation(Math.max(0,(input - 0.5f) * 2.0f));
        }
    }

    private class EndArcInterpolator extends AccelerateInterpolator{

        @Override
        public float getInterpolation(float input) {
            return super.getInterpolation(Math.min(1,input * 2.0f));
        }
    }
}
