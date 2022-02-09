package se.alipsa.rdatautils;

import org.renjin.eval.Context;
import org.renjin.serialization.RDataReader;
import org.renjin.serialization.RDataWriter;
import org.renjin.sexp.SEXP;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;

public final class RDataUtil {

  private RDataUtil() {
    // Static only
  }

  public static SEXP read(File rdsFile) throws IOException {
    try (InputStream is = Files.newInputStream(rdsFile.toPath())) {
      return read(is);
    }
  }

  public static SEXP read(InputStream is) throws IOException {
    try (GZIPInputStream gzipIn = new GZIPInputStream(is);
         RDataReader reader = new RDataReader(Context.newTopLevelContext(), gzipIn)) {
      return reader.readFile();
    }
  }

  public static void write(SEXP sexp, File toFile) throws IOException {
    try(OutputStream fos = Files.newOutputStream(toFile.toPath());
        RDataWriter writer = new RDataWriter(Context.newTopLevelContext(), fos)) {
      writer.save(sexp);
    }
  }
}
