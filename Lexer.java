import java.io.*;
import java.util.*;

public class Lexer {
    private PushbackReader in;
    private String yytext = "";
    private int q = 0;
    private char c;

    // Definir la tabla de transición para el autómata léxico
    private int[][] table = new int[8][128]; // Asumiendo un rango de caracteres ASCII (0-127)

    // Constructor para inicializar la tabla de transiciones
    public Lexer() {
        // Inicializar todas las transiciones a -1 (sin transición)
        for (int i = 0; i < table.length; i++) {
            Arrays.fill(table[i], -1);
        }

        // Estado q0 (estado inicial)
        for (int i = 97; i <= 122; i++) { // a-z
            table[0][i] = 1; // Transita a q1 para identificadores
        }
        for (int i = 48; i <= 57; i++) { // 0-9
            table[0][i] = 2; // Transita a q2 para números enteros
        }
        table[0][43] = 5;  // '+' operador
        table[0][58] = 6;  // ':' parte del operador de asignación
        table[0][32] = 4;  // Espacio
        table[0][9] = 4;   // Tab
        table[0][10] = 4;  // Newline
        table[0][13] = 4;  // Carriage return

        // Estado q1 (identificador)
        for (int i = 97; i <= 122; i++) { // a-z
            table[1][i] = 1; // Se queda en q1 para más letras en identificadores
        }

        // Estado q2 (número entero)
        for (int i = 48; i <= 57; i++) { // 0-9
            table[2][i] = 2; // Se queda en q2 para más dígitos en números enteros
        }
        table[2][46] = 3; // '.' para transitar a número real

        // Estado q3 (número real)
        for (int i = 48; i <= 57; i++) { // 0-9
            table[3][i] = 3; // Se queda en q3 para más dígitos en números reales
        }

        // Estado q4 (espacios)
        table[4][32] = 4;  // Espacio
        table[4][9] = 4;   // Tab
        table[4][10] = 4;  // Newline
        table[4][13] = 4;  // Carriage return

        // Estado q5 ('+')
        // No necesita transiciones adicionales

        // Estado q6 (operador ':')
        table[6][61] = 7;  // '=' para completar el operador ':='

        // Estado q7 (operador ':=')
        // No requiere más transiciones, es un estado de aceptación
    }

    // Definición de estados de aceptación
    private boolean[] acc = { 
        false, // q0 - no aceptado 
        true,  // q1 - identificador aceptado 
        true,  // q2 - número entero aceptado 
        true,  // q3 - número real aceptado 
        true,  // q4 - espacio aceptado 
        true,  // q5 - operador + aceptado
        false, // q6 - estado intermedio de :=
        true   // q7 - operador ':=' aceptado
    };

    // Método para abrir el archivo de entrada
    public void openFile(String fileName) throws IOException {
        in = new PushbackReader(new FileReader(fileName));
        yytext = "";
        q = 0;
    }

    // Obtener el siguiente carácter
    private char getChar() throws IOException {
        int character = in.read(); // Lee el siguiente carácter
        if (character == -1) {     // Verifica si llegamos al final del archivo
            return (char) -1;      // Devuelve un valor especial indicando EOF
        }
        return (char) character;
    }

    // Devolver un carácter al flujo de entrada
    private void ungetChar(char c) throws IOException {
        in.unread(c);
    }

    // Función principal del lexer, se encarga de leer y clasificar tokens
    public int yylex() throws IOException {
        yytext = "";
        q = 0;

        while (true) {
            c = getChar();

            // Verifica si llegamos al final del archivo
            if (c == (char) -1) {
                break;
            }

            int charIndex = (int) c;

            if (charIndex >= table[q].length || charIndex < 0 || table[q][charIndex] == -1) {
                // No hay transición válida
                if (acc[q]) {
                    if (c != (char) -1) {
                        ungetChar(c); // Devolver el carácter no consumido
                    }
                    return getToken(q);
                } else {
                    // Manejo de errores
                    System.out.println("Error léxico en el estado: " + q + ", carácter no reconocido: " + c + " (ASCII: " + charIndex + ")");
                    return Token.ERROR;
                }
            } else {
                yytext += c;
                q = table[q][charIndex];
            }
        }

        // Manejo de fin de archivo
        if (acc[q]) {
            return getToken(q);
        } else {
            return -1; // Fin del archivo sin tokens adicionales
        }
    }

    // Devuelve el token correspondiente al estado final
    private int getToken(int q) {
        switch (q) {
            case 1: return Token.ID;
            case 2: return Token.NUM_ENT;
            case 3: return Token.NUM_REAL;
            case 4: return Token.ESPACIO;
            case 5: return Token.OPERADOR;
            case 7: return Token.OPERADOR;  // Para el operador ':='
            default: return Token.ERROR;
        }
    }

    // Devolver el texto procesado
    public String yytext() {
        return yytext;
    }
}
