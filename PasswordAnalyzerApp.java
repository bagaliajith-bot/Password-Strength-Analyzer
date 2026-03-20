import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PasswordAnalyzerApp {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Password Analyzer");
        frame.setSize(520, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(20, 20, 20));
        panel.setLayout(null);

        JLabel title = new JLabel("🔐 Password Analyzer");
        title.setBounds(120, 20, 300, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JTextField passwordField = new JTextField();
        passwordField.setBounds(100, 80, 300, 35);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton button = new JButton("Analyze");
        button.setBounds(180, 130, 140, 35);
        button.setBackground(new Color(0, 153, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel strengthLabel = new JLabel("");
        strengthLabel.setBounds(100, 190, 300, 25);
        strengthLabel.setForeground(Color.WHITE);

        JLabel crackLabel = new JLabel("");
        crackLabel.setBounds(100, 220, 300, 25);
        crackLabel.setForeground(Color.WHITE);

        JLabel suggestLabel = new JLabel("");
        suggestLabel.setBounds(100, 250, 350, 25);
        suggestLabel.setForeground(Color.WHITE);

        JProgressBar bar = new JProgressBar(0, 10);
        bar.setBounds(100, 290, 300, 20);
        bar.setStringPainted(true);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String password = passwordField.getText();

                int score = calculateScore(password);
                String strength = getStrength(score);
                String crackTime = estimateCrackTime(password);
                String suggestion = suggestPassword(password);

                strengthLabel.setText("Strength: " + strength);
                crackLabel.setText("Crack Time: " + crackTime);
                suggestLabel.setText("Suggestion: " + suggestion);

                bar.setValue(score);

                if (strength.equals("Very Weak") || strength.equals("Weak")) {
                    strengthLabel.setForeground(Color.RED);
                } else if (strength.equals("Moderate")) {
                    strengthLabel.setForeground(Color.ORANGE);
                } else {
                    strengthLabel.setForeground(Color.GREEN);
                }
            }
        });

        panel.add(title);
        panel.add(passwordField);
        panel.add(button);
        panel.add(strengthLabel);
        panel.add(crackLabel);
        panel.add(suggestLabel);
        panel.add(bar);

        frame.add(panel);
        frame.setVisible(true);
    }

    // LOGIC

    public static int calculateScore(String password) {
        int score = 0;

        if (password.length() >= 12) score += 3;
        else if (password.length() >= 8) score += 2;

        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*[0-9].*")) score++;
        if (password.matches(".*[@#$%^&+=!].*")) score++;

        if (password.toLowerCase().contains("1234")) score -= 2;
        if (password.toLowerCase().contains("password")) score -= 2;
        if (password.matches("(.)\\1{2,}")) score -= 2;

        if (score < 0) score = 0;

        return score;
    }

    public static String getStrength(int score) {
        if (score <= 2) return "Very Weak";
        else if (score <= 3) return "Weak";
        else if (score <= 5) return "Moderate";
        else if (score <= 7) return "Strong";
        else return "Very Strong";
    }

    // REALISTIC CRACK TIME

    public static String estimateCrackTime(String password) {
        int length = password.length();

        double combinations = Math.pow(70, length);
        double seconds = combinations / 1_000_000_000;

        if (seconds < 60)
            return (int) seconds + " seconds";
        else if (seconds < 3600)
            return (int)(seconds / 60) + " minutes";
        else if (seconds < 86400)
            return (int)(seconds / 3600) + " hours";
        else if (seconds < 2592000)
            return (int)(seconds / 86400) + " days";
        else if (seconds < 31536000)
            return (int)(seconds / 2592000) + " months";
        else {
            double years = seconds / 31536000;
            if (years > 100)
                return "100+ years";
            else
                return (int) years + " years";
        }
    }

    public static String suggestPassword(String password) {
        String suggestion = password;

        if (!suggestion.matches(".*[A-Z].*"))
            suggestion += "A";

        if (!suggestion.matches(".*[0-9].*"))
            suggestion += "1";

        if (!suggestion.matches(".*[@#$%^&+=!].*"))
            suggestion += "@";

        suggestion = suggestion.replace("a", "@");
        suggestion = suggestion.replace("i", "1");

        return suggestion;
    }
}