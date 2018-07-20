package fungames.murphymoore_fungames;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


// Load the web content and images from:
// https://www.pcauthority.com.au/News/170181,top-10-computer-games-of-all-time.aspx            20pts
// Search the downloaded content for the top 10 games                                           20pts
// Put the names and images in a list                                                           10pts

// The user will have 4 options on each screen to guess the right answer                        20pts
// Show a Toast if the answer is correct or incorrect                                           5pt
// Stay on the current image if the answer is incorrect, otherwise go to the next question.     25pt


public class MainActivity extends AppCompatActivity {

	private Jsoup theSoup;
	DownloadTask myDownload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		URL url = null;
		try {
			url = new URL("https://www.pcauthority.com.au/news/top-10-computer-games-of-all-time-170181");
			myDownload = new DownloadTask();
			myDownload.execute(url);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void startGameClick(View v) {
		Intent i = new Intent(getApplicationContext(), GuessImgActivity.class);
		startActivity(i);
	}


	public Jsoup getTheSoup() {
		return theSoup;
	}

	public void setTheSoup(Jsoup theSoup) {
		if (theSoup != null) {
			theSoup = theSoup;
		}
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
				result = readStream(stream, 500);
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
		int readSize;
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

		}

	}
}


