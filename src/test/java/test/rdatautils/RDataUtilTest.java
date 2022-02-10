package test.rdatautils;

import org.junit.jupiter.api.Test;
import org.renjin.primitives.sequence.IntSequence;
import org.renjin.sexp.*;
import se.alipsa.rdatautils.RDataUtil;
import se.alipsa.renjin.client.datautils.DataType;
import se.alipsa.renjin.client.datautils.Table;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RDataUtilTest {

  @Test
  public void testImport() throws IOException {
    URL url = getClass().getResource("/mtcars-3.2.3.rds");
    assertNotNull(url, "Failed to find mtcars-3.2.3.rds");
    try(InputStream is = url.openStream()) {
      SEXP sexp = RDataUtil.read(is);
      assertNotNull(sexp);
      Table table = Table.createTable(sexp);
      assertEquals(32,table.getRowList().size(), "Number of rows");
      assertEquals(21, table.getValueAsInteger(0,0), "First column in first row");
      assertEquals(2, table.getValueAsInteger(31,10), "Last column in last row");
      assertEquals(3.215, table.getColumnForName("wt").get(3), "4:th row of wt column");
    }
  }

  @Test
  public void testExport() throws IOException {
    ListVector.NamedBuilder df = new ListVector.NamedBuilder();
    df.add("names", new StringArrayVector("Kurt", "Susan", "Zoe"));
    df.add("income", new IntArrayVector(120000, 220202, 303030));
    df.setAttribute("row.names", new IntSequence(1, 1, 3));
    df.setAttribute("class", StringVector.valueOf("data.frame"));
    SEXP sexp = df.build();
    File outFile = new File("employeeSalaries.rds");
    RDataUtil.write(sexp, outFile);

    assertTrue(outFile.exists());
    assertTrue(outFile.length() > 100);

    // Same thing from a table
    List<String> headerList = new ArrayList<String>() {{
      add("names");
      add("income");
    }};
    List<List<Object>> rowList = new ArrayList<>();
    rowList.add(new ArrayList<Object>() {{ add("Kurt"); add(120000); }});
    rowList.add(new ArrayList<Object>() {{ add("Susan"); add(220202); }});
    rowList.add(new ArrayList<Object>() {{ add("Zoe"); add(303030); }});
    List<DataType> dataTypes = new ArrayList<DataType>() {{
      add(DataType.STRING);
      add(DataType.INTEGER);
    }};

    Table table = new Table(headerList, rowList, dataTypes);
    File outFile2 = new File("employeeSalaries2.rds");
    RDataUtil.write(table.asDataframe(), outFile2);

    assertTrue(outFile2.exists());
    assertTrue(outFile2.length() > 100);

    assertEquals(outFile.length(), outFile2.length(), "Length should be equal");
  }
}
