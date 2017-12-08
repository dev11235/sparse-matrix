import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SparseMatrix {

  private final int rowCount;
  private int columnCount;

  private TMap<Key, Integer> values = new THashMap<>();


  public SparseMatrix(int rowCount, int columnCount) {
    this.rowCount = rowCount;
    this.columnCount = columnCount;
  }

  public int get(int row, int col) {
    Integer value = values.get(new Key(row, col));
    return value != null ? value : 0;
  }

  public void set(int row, int col, int value) {
    if (value != 0) {
      values.put(new Key(row, col), value);
    }
  }

  private void addValue(int row, int col, int value) {
    if (value != 0) {
      int currentValue = get(row, col);
      set(row, col, currentValue + value);
    }
  }

  public int getRowCount() {
    return rowCount;
  }

  public int getColumnCount() {
    return columnCount;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int currentRow = 0; currentRow < rowCount; currentRow++) {
      for (int currentColumn = 0; currentColumn < columnCount; currentColumn++) {
        sb.append(get(currentRow, currentColumn)).append(" ");
      }
      sb.append("\n");
    }

    return sb.toString();

  }

  public int[] toArray() {
    int[] result = new int[rowCount * columnCount + 2];

    result[0] = rowCount;
    result[1] = columnCount;

    int j = 2;

    for (int row = 0; row < rowCount; row++) {
      for (int col = 0; col < columnCount; col++) {
        Integer value = values.get(new Key(row, col));
        result[j++] = value != null ? value : 0;
      }
    }

    return result;
  }

  public SparseMatrix multiply(SparseMatrix second) {
    SparseMatrix result = new SparseMatrix(this.rowCount, second.getColumnCount());

    for (TMap.Entry<Key, Integer> first : this.values.entrySet()) {
      for (int col = 0; col < second.getColumnCount(); col++) {
        if (second.values.containsKey(new Key(first.getKey().col, col))) {
          result.addValue(first.getKey().row, col,
              first.getValue() * second.get(first.getKey().col, col));
        }
      }
    }

    return result;
  }

  public Stream<Integer> toStream() {
    Spliterator<Integer> spliterator = Spliterators
        .spliterator(new Itr(), rowCount * columnCount + 2, Spliterator.ORDERED);
    return StreamSupport.stream(spliterator, false);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SparseMatrix that = (SparseMatrix) o;

    if (rowCount != that.rowCount) {
      return false;
    }
    if (columnCount != that.columnCount) {
      return false;
    }
    return values.equals(that.values);
  }

  @Override
  public int hashCode() {
    int result = rowCount;
    result = 31 * result + columnCount;
    result = 31 * result + values.hashCode();
    return result;
  }

  private static class Key {

    int row;
    int col;

    Key(int row, int col) {
      this.row = row;
      this.col = col;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Key key = (Key) o;

      return row == key.row && col == key.col;
    }

    @Override
    public int hashCode() {
      int result = row;
      result = 31 * result + col;
      return result;
    }

    @Override
    public String toString() {
      return "{" + row + ", " + col + '}';
    }
  }

  private class Itr implements Iterator<Integer> {

    int cursor = 0;

    int row = 0;
    int col = 0;

    @Override
    public boolean hasNext() {
      return cursor < rowCount * columnCount + 2;
    }

    @Override
    public Integer next() {
      if (cursor == 0) {
        cursor++;
        return rowCount;
      }

      if (cursor == 1) {
        cursor++;
        return columnCount;
      }

      Integer value = values.get(new Key(row, col));

      if (col == columnCount - 1) {
        row++;
        col = 0;
      } else {
        col++;
      }

      cursor++;

      return value != null ? value : 0;
    }
  }
}
