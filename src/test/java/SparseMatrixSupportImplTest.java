import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.Test;

public class SparseMatrixSupportImplTest {

  @Test
  public void fromStream() {
    int[] arrayOfMatrixValues = {3, 5, 98, 29, 0, 0, 35, 0, 0, 0, 63, 0, 0, 0, 0, 0, 0};

    SparseMatrix matrix = new SparseMatrixSupportImpl()
        .fromStream(Arrays.stream(arrayOfMatrixValues).boxed());

    assertEquals((Arrays.stream(arrayOfMatrixValues).boxed().collect(Collectors.toList())),
        matrix.toStream().collect(Collectors.toList()));
  }

}