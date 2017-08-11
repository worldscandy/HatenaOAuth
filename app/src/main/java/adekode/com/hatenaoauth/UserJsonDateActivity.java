package adekode.com.hatenaoauth;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

/**
 * Created by kamadayuji on 2017/08/11.
 */

public class UserJsonDateActivity extends Activity {

    public static TextView url_name;
    public static TextView display_name;
    private static Button returnUserDate;
    private static ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userjsondate);

        url_name = (TextView) findViewById(R.id.url_name);
        display_name = (TextView) findViewById(R.id.display_name);
        returnUserDate = (Button) findViewById(R.id.returnUserDate);
        imageView = (ImageView) findViewById(R.id.result);

        getUserJsonDate();

        returnUserDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private static void getUserJsonDate() {
        //AddText("\ngetUserJsonDate start");
        AsyncTask<Void, Integer, Void> task = new AsyncTask<Void, Integer, Void>() {

            HttpURLConnection con;
            String buffer;
            int responsecode;
            String responsemessage,userJsonDate;

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    HatenaOAuthActivity.mConsumer.setTokenWithSecret(HatenaOAuthActivity.mAccessToken, HatenaOAuthActivity.mAccessTokenSecret);
                    URL url = new URL("http://n.hatena.com/applications/my.json");

                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET"); // HTTPメソッドはPOST
                    HatenaOAuthActivity.mConsumer.sign(con); // リクエストに署名

                    con.connect();

                    responsecode = con.getResponseCode();
                    responsemessage = con.getResponseMessage();

                    Log.d("debug_log", "Get Response" + responsecode + " " +
                            responsemessage );
                    //publishProgress(0);


                    BufferedReader reader = new BufferedReader(new InputStreamReader
                            (con.getInputStream(), "UTF-8"));

                    buffer = reader.readLine();

                    userJsonDate = buffer;

                    while (null != buffer) {
                      //  publishProgress(1);
                        Thread.sleep(100L);
                        Log.d("debug_log",buffer);
                        buffer = reader.readLine();
                    }

                    con.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (OAuthMessageSignerException e) {
                    e.printStackTrace();
                } catch (OAuthExpectationFailedException e) {
                    e.printStackTrace();
                } catch (OAuthCommunicationException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onProgressUpdate(Integer... values) {

              /*  if(values[0] == 0){
                    AddText("Get Response" + responsecode + " " +
                            responsemessage );
                } else if (values[0] == 1){
                    AddText("JSON :" + buffer);
                }
                */
            }

            @Override
            protected void onPostExecute(Void result) {
                parthJsonDate(userJsonDate);
            }
        };
        task.execute();
    }

    private static void parthJsonDate(String jsonDate) {
        String profileUrl = new String();
        try {
            JSONObject jsonObject = new JSONObject(jsonDate);
            url_name.setText("url_name：" + jsonObject.getString("url_name"));
            display_name.setText("display_name：" + jsonObject.getString("display_name"));
             profileUrl = jsonObject.getString("profile_image_url");
            //Log.d("debug_log",profileUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getprofile_image(profileUrl);

    }

    private static void getprofile_image(final String url){
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            Bitmap bmp;

            @Override
            protected Void doInBackground(Void... params) {
                bmp = downloadImage(url);
                return null;
            }

            @Override
            protected void onPostExecute(Void result){
                imageView.setImageBitmap(bmp);
            }
        };
        task.execute();
    }

    private static Bitmap downloadImage(String address) {
        Bitmap bmp = null;

        try {
            URL url = new URL( address );

            // HttpURLConnection インスタンス生成
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // タイムアウト設定
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(20000);

            // リクエストメソッド
            urlConnection.setRequestMethod("GET");

            // リダイレクトを自動で許可しない設定
            urlConnection.setInstanceFollowRedirects(false);

            // ヘッダーの設定(複数設定可能)
            urlConnection.setRequestProperty("Accept-Language", "jp");

            // 接続
            urlConnection.connect();

            //http応答メッセージ状態コードを取得
            int resp = urlConnection.getResponseCode();

            switch (resp){
                case HttpURLConnection.HTTP_OK:
                    InputStream is = urlConnection.getInputStream();
                    bmp = BitmapFactory.decodeStream(is);
                    is.close();
                    break;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.d("debug_log", "downloadImage error");
            e.printStackTrace();
        }

        return bmp;
    }
    }





