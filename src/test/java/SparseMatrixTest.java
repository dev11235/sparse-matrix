import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SparseMatrixTest {

  private static final int ROW_COUNT = 3;
  private static final int COLUMN_COUNT = 5;
  private static final double DENSITY = 0.3;
  private static final int MATRIX_SIZE = ROW_COUNT * COLUMN_COUNT;
  private static final int MAX_VAL = 100;

  private static int[] values;

  private static SparseMatrix fromArray(SparseMatrixSupportImpl matrixSupport, int[] array) {
    return matrixSupport.fromStream(Arrays.stream(array).boxed());
  }

  @BeforeAll
  static void setUp() {
    values = new int[MATRIX_SIZE + 2];
    values[0] = ROW_COUNT;
    values[1] = COLUMN_COUNT;

    ArrayList<Integer> list = new ArrayList<>();

    for (int i = 0; i < MATRIX_SIZE; i++) {
      list.add(i);
    }

    Collections.shuffle(list);

    for (int i = 2; i < values.length; i++) {
      if (list.subList(0, (int) (MATRIX_SIZE * DENSITY)).contains(i)) {
        values[i] = ((int) (Math.random() * (MAX_VAL - 1)) + 1);
      } else {
        values[i] = 0;
      }
    }
  }

  @Test
  void toArray() {
    SparseMatrix m = new SparseMatrixSupportImpl()
        .fromStream(Arrays.stream(values).boxed());
    assertArrayEquals(m.toArray(), values);
  }

  @Test
  void multiply() {
    SparseMatrixSupportImpl matrixSupport = new SparseMatrixSupportImpl();

    SparseMatrix first = fromArray(matrixSupport, new int[]{
        4, 8, 0, 0, 88, 0, 0, 55, 3, 0, 0, 17, 78, 0, 0, 0, 0, 0,
        35, 74, 0, 0, 0, 0, 0, 0, 0, 92, 0, 0, 0, 0, 0, 0
    });

    SparseMatrix second = fromArray(matrixSupport, new int[]{
        8, 4, 69, 92, 0, 0, 0, 0, 0, 0, 0, 0, 27, 0, 0, 9, 0, 87,
        59, 0, 0, 0, 14, 0, 0, 0, 38, 0, 0, 71, 0, 0, 0, 0
    });

    SparseMatrix result = first.multiply(second);

    SparseMatrix expected = fromArray(matrixSupport, new int[]{
        4, 4, 884, 0, 2376, 213, 0, 0, 2106, 0, 2415, 3220, 0, 0, 0, 0, 0, 0
    });

    assertEquals(expected, result);
  }

  @Test
  void toStream() {
    int[] arrayOfMatrixValues = {3, 5, 98, 29, 0, 0, 35, 0, 0, 0, 63, 0, 0, 0, 0, 0, 0};

    SparseMatrix matrix = fromArray(new SparseMatrixSupportImpl(), arrayOfMatrixValues);

    assertEquals((Arrays.stream(arrayOfMatrixValues).boxed().collect(Collectors.toList())),
        matrix.toStream().collect(Collectors.toList()));
  }

}