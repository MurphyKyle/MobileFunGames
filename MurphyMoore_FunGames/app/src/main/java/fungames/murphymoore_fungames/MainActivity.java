package fungames.murphymoore_fungames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.jsoup.Jsoup;


// Load the web content and images from:
// https://www.pcauthority.com.au/News/170181,top-10-computer-games-of-all-time.aspx            20pts
// Search the downloaded content for the top 10 games                                           20pts
// Put the names and images in a list                                                           10pts

// The user will have 4 options on each screen to guess the right answer                        20pts
// Show a Toast if the answer is correct or incorrect                                           5pt
// Stay on the current image if the answer is incorrect, otherwise go to the next question.     25pt


public class MainActivity extends AppCompatActivity {
	
	private Jsoup theSoup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
}
