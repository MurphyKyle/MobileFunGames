package fungames.murphymoore_fungames;

import android.media.TimedText;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

// main gameplay activity
public class GuessImgActivity extends AppCompatActivity {
	private static ArrayList<GameEntry> gameEntries = new ArrayList<>();

	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_img);
        ArrayList<String> imgs =  getIntent().getStringArrayListExtra("imgs");
        ArrayList<String> titles = getIntent().getStringArrayListExtra("titles");
        makeGameEntries(imgs, titles);
        TextView t = (TextView) findViewById(R.id.random);
        t.setText(getGameEntries().get(0).getCorrectAnswer());
    }

	public static ArrayList<GameEntry> getGameEntries() {
		return gameEntries;
	}
//
//	public static void setGameEntries(ArrayList<GameEntry> gameEntries) {
//		if (gameEntries != null ) {
//			GuessImgActivity.gameEntries = gameEntries;
//		}
//	}



	private void makeGameEntries(ArrayList<String> imgs, ArrayList<String> titles){
        for(int i = 0; i < imgs.size(); i++){
            ArrayList<String> answerOptions = makeRandomTitleList(titles.get(i));
            GameEntry newEntry = new GameEntry(imgs.get(i), titles.get(i), answerOptions);
            gameEntries.add(newEntry);
        }
    }

    private ArrayList<String> makeRandomTitleList(String correctAnswer){
        String[] randomTitles = new String[]{"Halo", "Star Craft", "Online Poker", "Solitaire", "Go Fish"};
        ArrayList<String> result = new ArrayList<>();
        int i = 0;
        Random rand = new Random();
        while(i < 4){
            int index = rand.nextInt(randomTitles.length);
            if(result.contains(randomTitles[index])){
                result.remove(result.size()-1);
            }else{
                result.add(randomTitles[index]);
                i++;
            }
        }
        result.add(rand.nextInt(4), correctAnswer);
        return result;
    }
}
