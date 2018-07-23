package fungames.murphymoore_fungames;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;


// Load the web content and images from:
// https://www.pcauthority.com.au/News/170181,top-10-computer-games-of-all-time.aspx            20pts
// Search the downloaded content for the top 10 games                                           20pts
// Put the names and images in a list                                                           10pts

// The user will have 4 options on each screen to guess the right answer                        20pts
// Show a Toast if the answer is correct or incorrect                                           5pt
// Stay on the current image if the answer is incorrect, otherwise go to the next question.     25pt


public class MainActivity extends AppCompatActivity {

    private static final String THE_PREFERENCE = "calcDb";
    private SharedPreferences sharedPreferences;
    private static String webpageData;
	private static ArrayList<String> imageRefs = new ArrayList<>();
	private static ArrayList<String> titleRefs = new ArrayList<>();
	DownloadTask myDownload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		URL url = null;
        sharedPreferences = getSharedPreferences(THE_PREFERENCE, MODE_PRIVATE);

        if (!checkMem()) {
            try {
                url = new URL("https://www.pcauthority.com.au/news/top-10-computer-games-of-all-time-170181");
                myDownload = new DownloadTask();
                myDownload.execute(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            parsingResults(getWebpageData());
        }
	}


    public boolean checkMem() {
        if (sharedPreferences.contains("webpage")) {
            String info = sharedPreferences.getString("webpage", null);

            if (info != null) {
                setWebpageData(info);
                return true;
            }

            return false;
        }
        return false;
    }

    public boolean saveWebpageData(String pageData) {
        try {
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putString("webpage", pageData);
            ed.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setWebpageData(String text) {
        webpageData = text;
    }

    public String getWebpageData() {
        return webpageData;
    }

    //Goes to the GuessImgActivity
	public void startGameClick(View v) {
		Intent i = new Intent(getApplicationContext(), GuessImgActivity.class);
		Bundle b = new Bundle();
		b.putStringArrayList("imgs", imageRefs);
		b.putStringArrayList("titles", titleRefs);
		i.putExtras(b);
		startActivity(i);
	}


	private String downloadUrl(URL url) throws IOException {
		InputStream stream = null;
		HttpURLConnection connection = null;
		String result = null;
		try {
			connection = (HttpsURLConnection) url.openConnection();
			connection.setReadTimeout(3000);
			connection.setConnectTimeout(3000);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.connect();
			int responseCode = connection.getResponseCode();
			if (responseCode != HttpsURLConnection.HTTP_OK) {
				throw new IOException("HTTP error code: " + responseCode);
			}
			// Retrieve the response body as an InputStream.
		 	stream = connection.getInputStream();
			if (stream != null) {
				// Converts Stream to String with max length of 500.
				result = readStream(stream, 120000);
			}
		}catch (Exception e) {
			System.out.println(e.toString());
	} finally {
		// Close Stream and disconnect HTTPS connection.
		if (stream != null) {
			stream.close();
		}if (connection != null) {
			connection.disconnect();
		}
		}
		return result;
	}


	public String readStream(InputStream stream, int maxReadSize)throws IOException, UnsupportedEncodingException {
		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] rawBuffer = new char[maxReadSize];
		int readSize = maxReadSize;
		StringBuffer buffer = new StringBuffer();
		while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
			if (readSize > maxReadSize) {
				readSize = maxReadSize;
			}
				buffer.append(rawBuffer, 0, readSize);
				maxReadSize -= readSize;
			}
			return buffer.toString();
	}

	private void parsingResults(String results){

		int i = 0;

		Document doc =  Jsoup.parse(results);
        Element element =  doc.getElementById("article-primary");
        Element thePTag = (element.getElementsByTag("p")).get(0);
        Elements titleHtmls = thePTag.getElementsByTag("b");
        Elements imgs = thePTag.getElementsByTag("a");

        for(Element e : titleHtmls){
        	if(i >=2){
        		String[] splitTitile = e.text().split(" ");
        		String title = "";
        		for(int j = 1; j < splitTitile.length; j++){
					title += splitTitile[j] + " ";
				}
				titleRefs.add(title.trim());

			}
			i++;
		}
		i = 0;
        for(Element e : imgs){
        	if(i >=6 && e.hasAttr("title")){
        		imageRefs.add(e.attr("href"));
			}
			i++;
		}

    }

	private class DownloadTask extends AsyncTask<URL, Void, String> {

		@Override
		protected String doInBackground(URL... urls) {
			String result = "";
			try {
				result = downloadUrl(urls[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String results){
            saveWebpageData(results);
            parsingResults(results);
		}

	}
}


