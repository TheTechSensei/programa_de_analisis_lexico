import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer();
            lexer.openFile("input.txt"); // Aquí colocas el archivo de entrada con el texto

            int token;
            while ((token = lexer.yylex()) != -1) {
                System.out.println("Token: " + token + " Text: " + lexer.yytext());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
