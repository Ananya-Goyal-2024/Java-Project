import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class QuizApp extends JFrame implements ActionListener {
    private JLabel questionLabel;
    private JRadioButton[] options = new JRadioButton[4];
    private ButtonGroup optionGroup;
    private JButton nextButton, submitButton;
    private int currentQuestionIndex = 0, score = 0;
    private String userName;
    private java.util.List<Question> questions;

    public QuizApp() {
        setTitle("Quiz Application");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        userName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (userName == null || userName.trim().isEmpty()) {
            userName = "Anonymous";
        }

        // Load questions
        questions = loadQuestions();

        // Top label for question
        questionLabel = new JLabel("", JLabel.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(questionLabel, BorderLayout.NORTH);

        // Center panel for options
        JPanel optionPanel = new JPanel(new GridLayout(4, 1));
        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            optionGroup.add(options[i]);
            optionPanel.add(options[i]);
        }
        add(optionPanel, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel();
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");
        nextButton.addActionListener(this);
        submitButton.addActionListener(this);
        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        displayQuestion(currentQuestionIndex);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (!isOptionSelected()) {
            JOptionPane.showMessageDialog(this, "Please select an answer!");
            return;
        }

        if (isAnswerCorrect(currentQuestionIndex)) {
            score++;
        }

        if (e.getSource() == nextButton) {
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                displayQuestion(currentQuestionIndex);
            } else {
                nextButton.setEnabled(false);
                submitButton.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Click Submit to finish.");
            }
        }

        if (e.getSource() == submitButton) {
            JOptionPane.showMessageDialog(this, "Quiz Over! " + userName + ", Your Score: " + score + "/" + questions.size());
            saveScore(userName, score);
            System.exit(0);
        }
    }

    private boolean isOptionSelected() {
        for (JRadioButton option : options) {
            if (option.isSelected()) return true;
        }
        return false;
    }

    private boolean isAnswerCorrect(int index) {
        int correct = questions.get(index).correctOptionIndex;
        return options[correct].isSelected();
    }

    private void displayQuestion(int index) {
        optionGroup.clearSelection();
        Question q = questions.get(index);
        questionLabel.setText("Q" + (index + 1) + ": " + q.question);
        for (int i = 0; i < 4; i++) {
            options[i].setText(q.options[i]);
        }
        if (index == questions.size() - 1) {
            nextButton.setEnabled(false);
            submitButton.setEnabled(true);
        }
    }

    private java.util.List<Question> loadQuestions() {
        java.util.List<Question> list = new ArrayList<>();
        list.add(new Question("What is the capital of India?", new String[]{"Delhi", "Mumbai", "Kolkata", "Chennai"}, 0));
        list.add(new Question("Java runs on which platform?", new String[]{"Browser", "JVM", "Microcontroller", "BIOS"}, 1));
        list.add(new Question("Which one is not an OOP concept?", new String[]{"Inheritance", "Encapsulation", "Polymorphism", "Compilation"}, 3));
        list.add(new Question("Which company developed Java?", new String[]{"Google", "Microsoft", "Sun Microsystems", "IBM"}, 2));
        list.add(new Question("Which keyword is used to inherit a class?", new String[]{"this", "super", "extends", "implements"}, 2));
        return list;
    }

    private void saveScore(String name, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt", true))) {
            writer.write(name + " - " + score + "/" + questions.size());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving score: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new QuizApp();
    }
}

class Question {
    String question;
    String[] options;
    int correctOptionIndex;

    public Question(String question, String[] options, int correctOptionIndex) {
        this.question = question;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }
}