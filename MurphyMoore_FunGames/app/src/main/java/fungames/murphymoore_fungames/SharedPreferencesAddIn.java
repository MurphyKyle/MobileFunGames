package fungames.murphymoore_fungames;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SharedPreferencesAddIn extends AppCompatActivity {
	
	private static final String THE_PREFERENCE = "calcDb";
	private SharedPreferences sharedPreferences;
	private static String webpageData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// transfer line 9-10, 18-19, and 22-26
		sharedPreferences = getSharedPreferences(THE_PREFERENCE, MODE_PRIVATE);
		checkMem();
	}
	
	public void checkMem() {
		if (sharedPreferences.contains("webpage")) {
			String info = sharedPreferences.getString("webpage", null);
		}
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

}
