package adekode.com.hatenaoauth;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class HatenaOAuthActivity extends AppCompatActivity {

    private static final String CONSUMER_KEY = "NPYvJkaV72F8tA==";
    private static final String CONSUMER_SECRET = "gt4Oxpg2GNzw9XXz3dYjX5PqQRk=";

    private static final String REQUEST_TOKEN_ENDPOINT_URL =
            "https://www.hatena.com/oauth/initiate";
    private static final String ACCESS_TOKEN_ENDPOINT_URL =
            "https://www.hatena.com/oauth/token";
    private static final String AUTHORIZATION_WEBSITE_URL =
            "https://www.hatena.ne.jp/touch/oauth/authorize";

    public static String mAccessToken = null;
    public static String mAccessTokenSecret = null;

    public static String authUrl;

    public static OAuthConsumer mConsumer = new DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

    public static OAuthProvider provider =
            new DefaultOAuthProvider(REQUEST_TOKEN_ENDPOINT_URL + "?scope=read_public%2Cwrite_public%2Cread_private%2Cwrite_private",
                    ACCESS_TOKEN_ENDPOINT_URL,
                    AUTHORIZATION_WEBSITE_URL);

    public static TextView Result_field;
    public static EditText InputPin_field;
    private static Button OAuthButton;
    private static Button InputPinButton;
    private static Button returnHatenaOAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //edittextにフォーカスが当たり、アプリ起動時からIMEが開いている状態を回避するコード
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_hatenaoauth);

        InputPin_field = (EditText) findViewById(R.id.inputPIN_field);
        Result_field = (TextView) findViewById(R.id.OAuthResult);
        OAuthButton = (Button) findViewById(R.id.OAuthButton);
        InputPinButton = (Button) findViewById(R.id.PINinput);
        returnHatenaOAuth = (Button) findViewById(R.id.returnHatenaOAuth);


        Result_field.setText("はてなOAuth認証 Start\n");

        OAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinget();
            }
        });

        returnHatenaOAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void pinget() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            String pin_input = null;

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    authUrl = provider.retrieveRequestToken(mConsumer, OAuth.OUT_OF_BAND);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
                    startActivity(intent);

                } catch (OAuthMessageSignerException e) {
                    e.printStackTrace();
                } catch (OAuthNotAuthorizedException e) {
                    e.printStackTrace();
                } catch (OAuthExpectationFailedException e) {
                    e.printStackTrace();
                } catch (OAuthCommunicationException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                AddText("認証URL：" + authUrl + "\n");
                AddText("PINを入力して,右のボタンをタップ");
                InputPinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAcceseToken();

                    }
                });
            }
        };
        task.execute();
    }

    private static void getAcceseToken() {

        AddText("getAcceseToken start\n");

        final String Pin = InputPin_field.getText().toString();

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    provider.retrieveAccessToken(mConsumer, Pin);
                    mAccessTokenSecret = mConsumer.getTokenSecret();
                    mAccessToken = mConsumer.getToken();

                } catch (OAuthMessageSignerException e) {
                    e.printStackTrace();
                } catch (OAuthNotAuthorizedException e) {
                    e.printStackTrace();
                } catch (OAuthExpectationFailedException e) {
                    e.printStackTrace();
                } catch (OAuthCommunicationException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                AddText("Access token:" + mAccessToken + "\nToken secret:" + mAccessTokenSecret);
                AddText("\nはてなOAuth認証完了！\nトップに戻りましょう！");
                if(mAccessToken != null){
                    MainActivity.AddText("\nはてなOAuth認証に成功しました！\n\nユーザー情報を取得できます！\nがそれ以外のことはできません。\nごめんなさい( '﹃'⑉)");
                }

            }
        };
        task.execute();
    }



    private static void AddText(String addtext) {

        String previous_text = (String) Result_field.getText();
        Result_field.setText(previous_text + "\n" + addtext);
    }
}