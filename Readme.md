
# Análisis Léxico en Java

Este proyecto implementa un **analizador léxico** (lexer) en Java para un lenguaje simple que reconoce los siguientes componentes:
- **Identificadores** formados por letras.
- **Números enteros** y **números decimales**.
- **Espacios en blanco**.
- **Operadores de asignación (`::=`)** y **suma (`+`)**.

## Archivos en el Proyecto

- `Lexer.java`: Implementación del lexer que se encarga de analizar la entrada y generar tokens.
- `Main.java`: Clase principal que inicializa el lexer y procesa el archivo de entrada.
- `Token.java`: Clase que define los tipos de tokens (identificadores, enteros, reales, operadores, etc.).
- `input.txt`: Archivo de entrada con el texto a analizar léxicamente.
- `docs/reporte_DFA.md`: Documento que describe la construcción del autómata finito determinista (DFA) basado en las expresiones regulares del lenguaje.


## Pasos para Compilar y Ejecutar

1. **Compilar los archivos Java**:

   Para compilar los archivos `Lexer.java`, `Main.java`, y `Token.java`, ejecuta:

   ```bash
   javac Lexer.java Main.java Token.java
   ```

   Esto generará archivos `.class` para cada uno de los archivos fuente.

2. **Ejecutar el programa**:

   Luego, ejecuta el programa con el siguiente comando:

   ```bash
   java Main
   ```

   El programa leerá el archivo `input.txt` y generará tokens a partir del texto que contiene.

---

## Ejemplo de Ejecución

**Entrada**: Si el archivo `input.txt` contiene el siguiente código:

```txt
x := 10 + 3.14
```

**Salida**: El programa generará la siguiente salida:

```bash
Token: 1 Text: x
Token: 4 Text: 
Token: 5 Text: :=
Token: 4 Text: 
Token: 2 Text: 10
Token: 4 Text: 
Token: 5 Text: +
Token: 4 Text: 
Token: 3 Text: 3.14
```

Donde:
- `Token: 1` representa un **identificador**.
- `Token: 2` representa un **número entero**.
- `Token: 3` representa un **número real**.
- `Token: 5` representa un **operador**.

---

## Detalles Técnicos

- El lexer implementa un **autómata finito determinista (DFA)** para reconocer identificadores, números enteros, números reales, espacios en blanco, y operadores.
- Las expresiones regulares utilizadas en el DFA son:
  - **Identificadores (`id`)**: `[a-z]+`
  - **Números enteros (`ent`)**: `[1-9][0-9]*|0`
  - **Números reales (`real`)**: `ent(\.[0-9]+)?`
  - **Espacios en blanco (`esp`)**: `[\t\n\r\v]+`
  - **Operadores (`op`)**: `::= | +`

Para más detalles sobre la construcción del DFA y las reglas implementadas en el lexer, consulta el documento `docs/reporte_DFA.md`.

---

## Licencia

Este proyecto está bajo la licencia MIT.
