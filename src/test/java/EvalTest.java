import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class EvalTest {

  @ParameterizedTest(name = "[{index}] Should evaluate `{0}` to {1}")
  @CsvSource(ignoreLeadingAndTrailingWhitespace = false, value = {
    " 3 + 4,7",
    " 5 + 2 * 6,17",
    " 10 * 7 + 5,75",
    " 111 * ( 2 + 3 ),555",
    " 112 * ( 2 + 3 ),560",
    " 234 - 3,231",
    " 222 * ( 12 + ( 1 - 3 ) * 2 ) / 8,222",
    "4+2*(5-2),10",
    "144-2*(5-2),138",
    " 1 + 2 + 3 + 4 + 5,15",
    "1a + 2b,3",
  })
  void testEval(String expression, int expected) {
    assertEquals(expected, Eval.evaluate(expression));
  }

}
