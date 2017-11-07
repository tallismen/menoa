package nl.anwb.menoa.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import nl.anwb.menoa.R;

public class GameActivity extends AppCompatActivity {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private Float x, y;

    private Random r;
    private ImageView wegenwachtauto, pechauto;
    private TextView scoreview;
    private int directionX, directionY, speedX, speedY, opgelost, nietOpgelost;
    private boolean eersteKeer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log.info("onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        wegenwachtauto = (ImageView) findViewById(R.id.wegenwachtauto);
        pechauto = (ImageView) findViewById(R.id.pechauto);
        scoreview = (TextView) findViewById(R.id.score);
        r = new Random();
        updateWegenwachtLocatie((float) 200, (float) 200);
        updatePechautoRandom();
        upScore(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (x != null) {
            Float newX = event.getX() - 30;
            Float newY = event.getY() - 100;
            compareXY(x, y, newX, newY);
            log.info("X: " + x + "\nY: " + y);
            updateWegenwachtLocatie(x, y);
        } else {
            x = event.getX() - 30;
            y = event.getY() - 150;
        }
        updateWegenwachtLocatie(x, y);
        if ((wegenwachtauto.getX() > pechauto.getX() - 50 && wegenwachtauto.getX() < pechauto.getX() + 50) && (wegenwachtauto.getY() > pechauto.getY() - 50 && wegenwachtauto.getY() < pechauto.getY() + 50)) {
            upScore(1);
            updatePechautoRandom();
            eersteKeer = true;
        }
        if ((pechauto.getY() > 800 || pechauto.getY() < 0) || (pechauto.getX() > 400 || pechauto.getX() < 0)) {
            updatePechautoRandom();
            upScore(-1);
            eersteKeer = true;
        }
        if (opgelost > 100 && opgelost < 150) {
            scoreview.setText("WHOhooooo " + opgelost + " Pechgevallen opgelost!\n Je mag stoppen.");
        }
        if (opgelost >= 150) {
            String text = scoreview.getText() + " Stop maar!";
            scoreview.setText(text);
        }
        movePechauto();
        return true;
    }

    private void compareXY(float oudX, float oudY, float newX, float newY) {
        if ((newX > oudX - 50 && newX < oudX + 50) && (newY > oudY - 50 && newY < oudY + 50)) {
            x = newX;
            y = newY;
        }
    }

    private void updateWegenwachtLocatie(float xi, float yi) {
        log.info("updateWegenwachtLocatie()");
        wegenwachtauto.setX(xi);
        wegenwachtauto.setY(yi);
    }

    private void movePechauto() {
        if (eersteKeer) {
            directionX = r.nextInt(2);
            directionY = r.nextInt(2);
            speedX = r.nextInt(3) + 1;
            speedY = r.nextInt(3) + 1;
            eersteKeer = false;
        }
        log.info("directionX: " + directionX);
        log.info("updateWegenwachtLocatie()");
        switch (directionX) {
            case 0:
                pechauto.setX(pechauto.getX() + speedX);
                break;
            case 1:
                pechauto.setX(pechauto.getX() - speedX
                );
        }
        switch (directionY) {
            case 0:
                pechauto.setY(pechauto.getY() + speedY);
                break;
            case 1:
                pechauto.setY(pechauto.getY() - speedY);
                break;
        }
    }

    private void updatePechautoRandom() {
        pechauto.setX((float) r.nextInt(400));
        pechauto.setY((float) r.nextInt(500));
    }

    private void upScore(int up) {
        if (up > 0) {
            opgelost += up;
        } else {
            nietOpgelost += up;
        }
        scoreview.setText("Opgelost: " + opgelost + " Niet opgelost: " + nietOpgelost);
    }
}
