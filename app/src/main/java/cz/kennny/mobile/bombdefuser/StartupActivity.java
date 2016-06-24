package cz.kennny.mobile.bombdefuser;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class StartupActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
    }

    /**
     * Generic method for showing error message
     * @param title error dialog title
     * @param text  error message text
     */
    public void showErrorDialog(String title, String text)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(StartupActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void onStartButtonPressed(View v)
    {
        EditText et;

        // build intent, put all data and start new activity (defuse)

        Intent intent = new Intent(this, DefuseActivity.class);

        et = (EditText)findViewById(R.id.titleText);
        intent.putExtra("titletext", et.getText().toString());
        et = (EditText)findViewById(R.id.codeText);

        String ct = et.getText().toString();
        // check for allowed characters in code input field
        for (int i = 0; i < ct.length(); i++)
        {
            if ((ct.charAt(i) >= '0' && ct.charAt(i) <= '9')
                    || ct.charAt(i) == 'W')
                continue;

            showErrorDialog("Chybný kód", "Kód smí obsahovat pouze čísla 0-9 a písmeno W");
            return;
        }
        intent.putExtra("codetext", ct);

        et = (EditText)findViewById(R.id.timeMinText);
        intent.putExtra("timemintext", et.getText().toString());
        et = (EditText)findViewById(R.id.timeSecText);
        intent.putExtra("timesectext", et.getText().toString());
        et = (EditText)findViewById(R.id.finishText);
        intent.putExtra("finishtext", et.getText().toString());
        et.setText("");
        et = (EditText)findViewById(R.id.penaltyField);
        intent.putExtra("penaltysecs", et.getText().toString());

        // Let's go!
        startActivity(intent);
    }
}
