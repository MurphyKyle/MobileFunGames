package fungames.murphymoore_fungames;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

// main gameplay activity
public class GuessImgActivity extends AppCompatActivity {
    private GameState currentGameState = GameState.START;
    private ArrayList<GameEntry> gameEntries = new ArrayList<>();
    private Iterator entryIterator = gameEntries.iterator();
    private MyDownloadTask myDownload;
    private int quizNumber = 1;
    private GameEntry currentGame = null;
    private RadioGroup radioGroup = null;
    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> imgs = getIntent().getStringArrayListExtra("imgs");
        ArrayList<String> titles = getIntent().getStringArrayListExtra("titles");
        makeGameEntries(imgs, titles);
        Collections.shuffle(gameEntries);

        setContentView(R.layout.activity_guess_img);
        entryIterator = gameEntries.iterator();
        // play first game
        radioGroup = (RadioGroup) findViewById(R.id.RGrp_AnsOptions);
        playNext();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isChecked = true;
            }
        });
    }

    private void playNext() {

        GameEntry e = null;
        if (entryIterator.hasNext()) {
            e = (GameEntry) entryIterator.next();

            setNextGame(e);
        } else {
            currentGameState = GameState.END_OF_GAME;
        }

    }

    private void setNextGame(GameEntry entry) {
        // reset the current game data
        currentGame = entry;

        // update quiz # header
        String quizText = "Quiz #" + quizNumber++;
        ((TextView) findViewById(R.id.Lbl_QNum)).setText(quizText);

        // set image to screen
//        ((ImageView) findViewById(R.id.Img_GameImg))
//                .setImageBitmap(IMAGE_LOADER.loadImageSync(entry.getImgUrl()));
        myDownload = new MyDownloadTask();
        myDownload.execute(entry.getImgUrl());

        setAnswerButtons(entry.getAnswerOptions());
        ((Button) findViewById(R.id.Btn_Submit)).setText(R.string.btn_submit);

        currentGameState = GameState.GUESSING;
    }


    public Bitmap loadImageFromURL(String fileUrl) {
        Bitmap bitmap = null;
        try {

            URL myFileUrl = new URL(fileUrl);
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            return bitmap;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public void btnSubmitClicked(View view) {

        if (currentGameState != GameState.END_OF_GAME) {
            RadioGroup group = findViewById(R.id.RGrp_AnsOptions);
            int id = group.getCheckedRadioButtonId();
            if (!isChecked) {
                Toast.makeText(this, "You messed up, try again!", Toast.LENGTH_SHORT);
                group.setBackgroundColor(Color.YELLOW);
                return;
            }
            int selectedId = group.getCheckedRadioButtonId();
            RadioButton selectedBtn = findViewById(selectedId);
            Button submitBtn = findViewById(R.id.Btn_Submit);
            TextView guessResultView = findViewById(R.id.Lbl_GuessResult);
            group.setBackgroundColor(Color.WHITE);
            switch (currentGameState) {
                case START:
                case GUESSING:
                case INCORRECT:
                    // check user's guess
                    if (selectedBtn.getText().equals(currentGame.getCorrectAnswer())) {
                        // they guessed correctly, notify and change button text
                        guessResultView.setText(R.string.good_guess);
                        submitBtn.setText(R.string.btn_next_img);
                        currentGameState = GameState.CORRECT;
                    } else {
                        // they guessed INcorrectly
                        guessResultView.setText(R.string.bad_guess);
                        // disable the wrong button?
                        selectedBtn.setEnabled(false);
                        // reset the radios so they can't re-submit a disabled button
                        group.clearCheck();
                        currentGameState = GameState.INCORRECT;
                    }
                    break;
                case CORRECT:
                    // moving to new image
                    guessResultView.setText(""); // that clear the box?

                    // reset any dead buttons
                    for (int i = 0; i < group.getChildCount(); i++) {
                        RadioButton rb = (RadioButton) group.getChildAt(i);
                        if (rb.isChecked()) {
                            rb.setChecked(false);
                        }
                        if (!rb.isEnabled()) {
                            rb.setEnabled(true);
                        }
                    }
                    isChecked = !isChecked;

                    // reset button text to default
                    submitBtn.setText(R.string.btn_submit);

                    playNext();
                    break;
            }
        } else {
            Toast.makeText(this, "Congratulations!!", Toast.LENGTH_LONG);
            finish();
        }

    }


    private void setAnswerButtons(ArrayList<String> options) {
        // i'm not sure of a more efficient way to do this:
        ((RadioButton) findViewById(R.id.Ans1)).setText(options.get(0));
        ((RadioButton) findViewById(R.id.Ans2)).setText(options.get(1));
        ((RadioButton) findViewById(R.id.Ans3)).setText(options.get(2));
        ((RadioButton) findViewById(R.id.Ans4)).setText(options.get(3));
    }

    private void makeGameEntries(ArrayList<String> imgs, ArrayList<String> titles) {
        for (int i = 0; i < imgs.size(); i++) {
            ArrayList<String> answerOptions = makeRandomTitleList(titles.get(i), titles);
            GameEntry newEntry = new GameEntry(imgs.get(i), titles.get(i), answerOptions);
            gameEntries.add(newEntry);
        }
    }

    private ArrayList<String> makeRandomTitleList(String correctAnswer, ArrayList<String> legitTitles) {
        Random rand = new Random();

        ArrayList<String> randomTitles = new ArrayList<>(legitTitles);
        randomTitles.addAll(Arrays.asList("Halo", "Star Craft", "Online Poker", "Solitaire", "Go Fish"));

        ArrayList<String> result = new ArrayList<>();
        result.add(correctAnswer);

        // try to add until we have 4 answers
        while (result.size() < 4) {
            // get a random title index
            int index = rand.nextInt(randomTitles.size());
            String nextTitle = randomTitles.get(index);

            // only add if the title is not already in our list
            if (!result.contains(nextTitle)) {
                result.add(randomTitles.get(index));
            }
        }

        // randomize the guesses so the correct answer isn't first
        Collections.shuffle(result);
        return result;
    }

    private class MyDownloadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            return loadImageFromURL(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView i = (ImageView) findViewById(R.id.Img_GameImg);
            i.setImageBitmap(result);
        }
    }
}

