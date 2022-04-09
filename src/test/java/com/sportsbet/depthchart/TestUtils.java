package com.sportsbet.depthchart;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class TestUtils {

  public static ObjectMapper objectMapper = new ObjectMapper();

  public static <T> T loadFromFile(String filePath, Class<T> type) throws IOException {

    File file = new File(filePath);

    return objectMapper.readValue(file, type);
  }
}
