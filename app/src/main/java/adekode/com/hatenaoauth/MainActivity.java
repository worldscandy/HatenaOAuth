package adekode.com.hatenaoauth;

import android.app.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public static TextView main_text;
    private static Button OAuthStart;
    private static Button getUserJsonDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_text = (TextView) findViewById(R.id.main_text);
        OAuthStart = (Button) findViewById(R.id.OAuthStart);
        getUserJsonDate = (Button) findViewById(R.id.getUserJsonDate);


        main_text.setText("はてなOAuth認証をしてください！");

        OAuthStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent OAuth = new Intent(getApplication(), HatenaOAuthActivity.class);
                startActivity(OAuth);
            }
        });



        getUserJsonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getJson = new Intent(getApplication(), UserJsonDateActivity.class);
                startActivity(getJson);
            }
        });

    }


    public static void AddText(String addtext) {

        String previous_text = (String) main_text.getText();
        main_text.setText(previous_text + "\n" + addtext);
    }
}