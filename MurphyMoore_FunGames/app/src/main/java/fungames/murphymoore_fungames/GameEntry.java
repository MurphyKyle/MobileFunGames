package fungames.murphymoore_fungames;

import java.util.ArrayList;

public class GameEntry {
	
	private boolean correctlyGuessed = false;
	private String imgUrl = null;
	private String correctAnswer = null;
	private ArrayList<String> answerOptions = new ArrayList<>();
	
	
	public GameEntry(String imgUrl, String correctAnswer, ArrayList<String> answerOptions) {
		setImgUrl(imgUrl);
		setCorrectAnswer(correctAnswer);
		setAnswerOptions(answerOptions);
	}
	
	public boolean isCorrectlyGuessed() {
		return correctlyGuessed;
	}
	
	public void setCorrectlyGuessed(boolean correctlyGuessed) {
		this.correctlyGuessed = correctlyGuessed;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
	public ArrayList<String> getAnswerOptions() {
		return answerOptions;
	}
	
	public void setAnswerOptions(ArrayList<String> answerOptions) {
		this.answerOptions = answerOptions;
	}
}
