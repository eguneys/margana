package oyun.net.anagram.model;

public interface JsonAttributes {

    String ANSWER = "answer";
    String ID = "id";
    String NAME = "name";
    String MEANING = "meaning";
    String QUESTION = "question";
    String QUIZZES = "quizzes";
    String TIME = "time";
    String THEME = "theme";
    String TYPE = "type";
    String SCORES = "scores";
    String SOLVED = "solved";
    String WORD = "word";
    String WORD_LENGTH = "wordLength";
    String WORD_LIMIT = "wordLimit";
    String NB_LETTER = "length";

    interface QuizType {
        String ANAGRAM_QUIZ = "anagram-quiz";
    }
    
}
