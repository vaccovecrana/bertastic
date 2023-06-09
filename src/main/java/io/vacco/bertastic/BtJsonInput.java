package io.vacco.bertastic;

import java.io.*;
import java.lang.reflect.Type;

public interface BtJsonInput {

  <T> T fromJson(Reader r, Type knownType);

}