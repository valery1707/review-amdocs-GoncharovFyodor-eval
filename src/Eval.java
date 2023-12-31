import java.util.ArrayDeque;
import java.util.Deque;

public class Eval {
  public static void main(String[] args) {
    test(" 3 + 4", 7);
    test(" 5 + 2 * 6", 17);
    test(" 10 * 7 + 5", 75);
    test(" 111 * ( 2 + 3 )", 555);
    test(" 112 * ( 2 + 3 )", 560);
    test(" 234 - 3", 231);
    test(" 222 * ( 2 + 5 ) / 14", 111);
    test(" 222 * ( 12 + ( 1 - 3 ) * 2 ) / 8", 222);
    test("4+2*(5-2)", 10);
    test("144-2*(5-2)", 138);
  }

  public static int evaluate(String expression) {
    char[] tokens = expression.toCharArray();

    // Stack for numbers: 'values'
    Deque<Integer> values = new ArrayDeque<>();
    // Stack for Operators: 'ops'
    Deque<Character> ops = new ArrayDeque<>();

    for (int i = 0; i < tokens.length; i++) {
      // Current token is a whitespace, skip it
      if (tokens[i] == ' ')
        continue;

      // Current token is a number, push it to stack for numbers
      if (tokens[i] >= '0' && tokens[i] <= '9') {
        StringBuilder sbuf = new StringBuilder();

        // There may be more than one digit in a number
        while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
          sbuf.append(tokens[i++]);
        }
        i--;
        values.push(Integer.parseInt(sbuf.toString()));
      }

      // Current token is an opening brace, push it to 'ops'
      else if (tokens[i] == '(')
        ops.push(tokens[i]);

        // Closing brace encountered, solve entire brace
      else if (tokens[i] == ')') {
        while (ops.peek() != '(')
          values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        ops.pop();
      }

      // Current token is an operator.
      else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
        while (!ops.isEmpty() && hasPrecedence(tokens[i], ops.peek()) && values.size()>1)
          values.push(applyOp(ops.peek(), values.pop(), values.pop()));

        ops.push(tokens[i]);
      }
    }

    // Entire expression has been parsed at this point, apply remaining ops to remaining values
    while (!ops.isEmpty() && values.size() > 1) {
      values.push(applyOp(ops.pop(), values.pop(), values.pop()));
    }

    return values.pop();
  }

  // Returns true if 'op2' has higher or same precedence as 'op1', otherwise returns false.
  public static boolean hasPrecedence(char op1, char op2) {
    if (op2 == '(' || op2 == ')')
      return false;
    return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
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
        if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
        return a / b;
    }
    return 0;
  }

  private static void test(String str, Integer expect) {
    int result = evaluate(str);
    if (result == expect)
      System.out.println("CORRECT!");
    else
      System.out.println(str + " should be evaluated to " + expect + ", but was " + result);
  }
}
