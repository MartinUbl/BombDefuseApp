package cz.kennny.mobile.bombdefuser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class FinishActivity extends AppCompatActivity
{
    private String finishText;
    private int remainingTime;

    private int numberResources[] = {R.drawable.n0, R.drawable.n1, R.drawable.n2, R.drawable.n3,
            R.drawable.n4,R.drawable.n5,R.drawable.n6,R.drawable.n7,R.drawable.n8,R.drawable.n9};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_finish);

        Intent in = getIntent();

        finishText = in.getStringExtra("finishtext");
        remainingTime = in.getIntExtra("remainingsecs", 0);

        ImageView digits[] = new ImageView[4];
        digits[0] = (ImageView)findViewById(R.id.digit1);
        digits[1] = (ImageView)findViewById(R.id.digit2);
        digits[2] = (ImageView)findViewById(R.id.digit3);
        digits[3] = (ImageView)findViewById(R.id.digit4);

        int mins = remainingTime / 60;
        int secs = remainingTime % 60;

        digits[0].setImageResource(numberResources[mins / 10]);
        digits[1].setImageResource(numberResources[mins % 10]);
        digits[2].setImageResource(numberResources[secs / 10]);
        digits[3].setImageResource(numberResources[secs % 10]);

        TextView tv = (TextView)findViewById(R.id.finishTextView);
        tv.setText(finishText);
    }
}
