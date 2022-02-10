# rdatautil
Utility to read and write RData (rds) files in java.

RDS files are created in R:
```R
# Save a data.frame object to a file
saveRDS(mtcars, "mtcars.rds")
```

...and they can be read in R like this:
```R
# Restore it 
my_data <- readRDS("mtcars.rds")
```

This utility allows you to save and read such rsd files in java. It relies on the Renjin project for RData functionality.

## Usage

Add the dependency to your pom (or equivalent):
```xml
<dependency>
    <groupId>se.alipsa</groupId>
    <artifactId>rdatautil</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Read a rds file
the read method takes either a File or an InputStream as a parameter:

```groovy
import se.alipsa.rdatautils.RDataUtil;
import org.renjin.sexp.SEXP;

SEXP data = RDataUtil.read(new File("mtcars.rds"));
```

```groovy
import se.alipsa.rdatautils.RDataUtil;
import org.renjin.sexp.SEXP;

SEXP data = RDataUtil.read(getClass().getResourceAsStream("mtcars.rds"));
```

### Write to a rds file
```groovy
import se.alipsa.rdatautils.RDataUtil;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.PairList;

PairList.Builder file = new PairList.Builder();
file.add("names", new StringArrayVector("Kurt", "Susan", "Zoe", StringVector.NA));
file.add("income", new IntArrayVector(120000, 220202, 303030, IntVector.NA));
SEXP sexp = file.build();

// You can write to a file
RDataUtil.write(sexp, new File("employeeSalaries.rds"));

// or an OutputStream
RDataUtil.write(sexp, System.out);
```

### Handling complex data
The [se.alipsa:renjin-client-data-utils](https://github.com/perNyfelt/renjin-client-data-utils) project enables you to deal with tabular data and combines very well with rdatautl:

Reading data:
```groovy
import se.alipsa.rdatautils.RDataUtil;
import org.renjin.sexp.SEXP;
import se.alipsa.renjin.client.datautils.Table;

SEXP data = RDataUtil.read(getClass().getResourceAsStream("employeeSalaries.rds"));
Table table = Table.createTable(data);
// Get the data.frame as a List of rows suitable for how data is usually handled in java:
List<List<Object>> rows = table.getRowList();

// You can also get an individual value
String employee1 = table.getValueAsString(0,0);
```

Writing data:

```java
import se.alipsa.rdatautils.RDataUtil;
import org.renjin.sexp.SEXP;
import se.alipsa.renjin.client.datautils.Table;
import java.sql.ResultSet;
import java.sql.SQLException;

class RdataSaver {
  
    public static void saveToRds(ResultSet rs, OutputStream os) throws SQLException {
        Table table = new Table(rs);
        RDataUtil.write(table.asDataframe(), os);
    }
}
```