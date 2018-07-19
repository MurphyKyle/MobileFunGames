package fungames.murphymoore_fungames;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

// main gameplay activity
public class GuessImgActivity extends AppCompatActivity {
	private static ArrayList<GameEntry> gameEntries = new ArrayList<>();
	
	
	public static ArrayList<GameEntry> getGameEntries() {
		return gameEntries;
	}
	
	public static void setGameEntries(ArrayList<GameEntry> gameEntries) {
		if (gameEntries != null ) {
			GuessImgActivity.gameEntries = gameEntries;
		}
	}
}
