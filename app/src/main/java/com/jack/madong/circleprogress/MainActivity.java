package com.jack.madong.circleprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    CircleView mCircleView;
    private int progress = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

          mCircleView = (CircleView) findViewById(R.id.progress);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress <= 99) {
                    progress +=1;

                    System.out.println(progress);

                    mCircleView.setProgress(progress);
//                    mRoundProgressBar2.setProgress(progress);
//                    mRoundProgressBar3.setProgress(progress);
//                    mRoundProgressBar4.setProgress(progress);
//                    mRoundProgressBar5.setProgress(progress);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                progress = 0;
            }
        }).start();
    }
}
