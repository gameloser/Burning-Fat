package cs.dal.a4176_project;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tedzhao on 2017/11/19.
 */

public class DownloadUrl {

    public String readUrl(String myUrl) throws IOException
    {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            // create new url
            URL url = new URL(myUrl);
            // open http url connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            // get input stream
            inputStream = urlConnection.getInputStream();
            // store stream into buffer
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            // create new stream buffer
            StringBuffer sb = new StringBuffer();

            String line = "";
            // read all lines
            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());

            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(inputStream != null)
                inputStream.close();
            // http url disconnect
            urlConnection.disconnect();
        }

        Log.d("data downlaod",data);
        return data;

    }
}
