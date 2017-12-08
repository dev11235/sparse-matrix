import java.util.stream.Stream;

public class SparseMatrixSupportImpl implements SparseMatrixSupport<SparseMatrix> {

  @Override
  public Stream<Integer> toStream(SparseMatrix matrix) {
    return matrix.toStream();
  }

  @Override
  public SparseMatrix fromStream(Stream<Integer> stream) {
    SparceMatrixBuilder matrixBuilder = new SparceMatrixBuilder();
    stream.forEach(matrixBuilder::putNextVal);
    return matrixBuilder.getMatrix();
  }

  @Override
  public SparseMatrix multiply(SparseMatrix first, SparseMatrix second) {
    return first.multiply(second);
  }

  private class SparceMatrixBuilder {

    private int rowCount;
    private int columnCount;

    private int count = 0;
    private int currentRow = 0;
    private int currentColumn = 0;

    private SparseMatrix matrix;

    void putNextVal(int val) {
      if (count == 0) {
        rowCount = val;
      } else if (count == 1) {
        columnCount = val;
        matrix = new SparseMatrix(rowCount, columnCount);
      } else {
        matrix.set(currentRow, currentColumn, val);

        currentColumn++;

        if (currentColumn == matrix.getColumnCount()) {
          currentColumn = 0;
          currentRow++;
        }
      }

      count++;
    }

    SparseMatrix getMatrix() {
      if (count == rowCount * columnCount + 2) {
        return matrix;
      } else {
        throw new IllegalStateException("matrix creation is not finished");
      }
    }
  }
}
