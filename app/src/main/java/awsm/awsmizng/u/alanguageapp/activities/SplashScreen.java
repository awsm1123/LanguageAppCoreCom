package awsm.awsmizng.u.alanguageapp.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.statics.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {


    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    private SpringAnimation xAnimation;
    private SpringAnimation yAnimation;

    private float dX, dY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        imageViewDragSpringAnimation();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Constants.DISPLAY_WIDTH= size.x;
        Constants.DISPLAY_HEIGHT = size.y;

        Thread timer = new Thread() {
            public void run() {
                try {
                    //Display for 3 seconds
                    sleep(500);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(getApplication(), Crossway.class));
                    finish();
                }
            }
        };
        timer.start();
    }

    private void imageViewDragSpringAnimation() {

        llTitle.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        llTitle.setOnTouchListener(touchListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            xAnimation = createSpringAnimation(llTitle, SpringAnimation.X, llTitle.getX(),
                    SpringForce.STIFFNESS_HIGH, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
            yAnimation = createSpringAnimation(llTitle, SpringAnimation.Y, llTitle.getY(),
                    SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        }
    };

    public SpringAnimation createSpringAnimation(View view,
                                                 DynamicAnimation.ViewProperty property,
                                                 float finalPosition,
                                                 float stiffness,
                                                 float dampingRatio) {
        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce springForce = new SpringForce(finalPosition);
        springForce.setStiffness(stiffness);
        springForce.setDampingRatio(dampingRatio);
        animation.setSpring(springForce);
        return animation;
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    // cancel animations
                    xAnimation.cancel();
                    yAnimation.cancel();
                    break;
                case MotionEvent.ACTION_MOVE:
                    llTitle.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    break;
                case MotionEvent.ACTION_UP:
                    xAnimation.start();
                    yAnimation.start();
                    break;
            }
            return true;
        }
    };
}
