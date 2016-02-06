package onl.deepspace.deepspaceteamspeak;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class Launcher extends AppCompatActivity implements OnTaskCompletedInterface{

    TextView users;
    String JSONusers;
    final String LOGTAG = "DeepSpace";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        users = (TextView) findViewById(R.id.users);

        Toast.makeText(Launcher.this, "Loading", Toast.LENGTH_LONG).show();

        new GetUserData(Launcher.this).execute(new Object());
    }

    @Override
    public void onTaskCompleted(String response) {
        users.setText(response);
    }

    public class GetUserData extends AsyncTask<Object, Void, String>{

        String result = "";

        private OnTaskCompletedInterface taskCompleted;

        @Override
        protected String doInBackground(Object... params) {
            return GetSomething();
        }

        public GetUserData(OnTaskCompletedInterface activityContext){
            this.taskCompleted = activityContext;
        }

        final String GetSomething()
        {
            String url = "https://deepspace.onl/scripts/sites/teamspeak.php";
            BufferedReader inStream = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpRequest = new HttpGet(url);
                HttpResponse response = httpClient.execute(httpRequest);
                inStream = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent()));

                StringBuffer buffer = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = inStream.readLine()) != null) {
                    buffer.append(line + NL);
                }
                inStream.close();

                result = buffer.toString();
                Log.d(LOGTAG, result);
            } catch (Exception e) {
                Log.e(LOGTAG, e.toString());
                e.printStackTrace();
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }

        protected void onPostExecute(String page)
        {
            taskCompleted.onTaskCompleted(page);
        }
    }

}
