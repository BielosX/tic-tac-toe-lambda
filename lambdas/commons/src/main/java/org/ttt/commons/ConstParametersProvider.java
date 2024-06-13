package org.ttt.commons;

import java.util.HashMap;
import java.util.Map;

public class ConstParametersProvider implements ParametersProvider {
  private final Map<String, String> parameters = new HashMap<>();

  @Override
  public String getParameter(String key) {
    return parameters.get(key);
  }

  public void setParameter(String key, String value) {
    parameters.put(key, value);
  }
}
