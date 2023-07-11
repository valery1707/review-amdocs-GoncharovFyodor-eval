import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

public class Eval {

  public static int evaluate(String expression) {
    char[] tokens = expression.toCharArray();

    // Stack for numbers: 'values'
    Deque<Integer> values = new ArrayDeque<>();
    // Stack for Operators: 'ops'
    Deque<Character> ops = new ArrayDeque<>();

    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i] >= '0' && tokens[i] <= '9') {
        // Current token is a number, push it to stack for numbers
        i += readValue(expression, tokens, values, i);
      } else if (tokens[i] == '(') {
        // Current token is an opening brace, push it to 'ops'
        ops.push(tokens[i]);
      } else if (tokens[i] == ')') {
        // Closing brace encountered, solve entire brace
        applyOps(ops, values, op -> op != '(');
        ops.pop();
      } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
        // Current token is an operator.
        applyOps(ops, values, Eval::hasPrecedence);
        ops.push(tokens[i]);
      }
    }

    // Entire expression has been parsed at this point, apply remaining ops to remaining values
    applyOps(ops, values, __ -> true);

    return values.pop();
  }

  private static int readValue(String expression, char[] tokens, Deque<Integer> values, int start) {
    int end = start;
    do {
      end++;
      // There may be more than one digit in a number
    } while (end < tokens.length && tokens[end] >= '0' && tokens[end] <= '9');
    values.push(Integer.valueOf(expression.substring(start, end)));
    return end - start - 1;
  }

  // Returns true if 'op' can be calculated now.
  public static boolean hasPrecedence(char op) {
    return op == '*' || op == '/';
  }

  private static void applyOps(Deque<Character> ops, Deque<Integer> values, Predicate<Character> opTest) {
    while (!ops.isEmpty() && opTest.test(ops.peek())) {
      values.push(applyOp(ops.pop(), values.pop(), values.pop()));
    }
  }

  // A utility method to apply an operator 'op' on operands 'a' and 'b'. Return the result.
  public static int applyOp(char op, int b, int a) {
    switch (op) {
      case '-':
        return a - b;
      case '+':
        return a + b;
      case '*':
        return a * b;
      case '/':
        if (b == 0) {
          throw new UnsupportedOperationException("Cannot divide by zero");
        }
        return a / b;
    }
    return 0;
  }

}
