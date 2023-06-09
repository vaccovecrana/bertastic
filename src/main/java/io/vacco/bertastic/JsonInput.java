package io.vacco.bertastic;

import java.io.*;
import java.lang.reflect.Type;

public interface JsonInput {

  <T> T fromJson(Reader r, Type knownType);

  default <T> T fromJson(InputStream in, Type knownType) {
    return fromJson(new BufferedReader(new InputStreamReader(in)), knownType);
  }

}