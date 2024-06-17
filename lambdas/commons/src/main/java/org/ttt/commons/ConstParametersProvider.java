package org.ttt.commons;

import java.util.Map;

public class ConstParametersProvider implements ParametersProvider {
  private final Map<String, String> parameters;

  public ConstParametersProvider(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  @Override
  public String getParameter(String key) {
    return parameters.get(key);
  }
}
