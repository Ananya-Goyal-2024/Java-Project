// src/Question.java
public class Question {
    private String questionText;
    private String[] options;
    private char correctOption;

    public Question(String questionText, String[] options, char correctOption) {
        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public char getCorrectOption() {
        return correctOption;
    }

    public boolean isCorrect(char userAnswer) {
        return Character.toUpperCase(userAnswer) == Character.toUpperCase(correctOption);
    }
}
