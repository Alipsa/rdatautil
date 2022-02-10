package test.rdatautils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.renjin.sexp.SEXP;
import se.alipsa.rdatautils.RDataUtil;
import se.alipsa.renjin.client.datautils.Table;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
}
