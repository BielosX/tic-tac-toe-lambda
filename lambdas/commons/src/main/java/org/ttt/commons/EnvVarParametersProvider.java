package org.ttt.commons;

public class EnvVarParametersProvider implements ParametersProvider {
  @Override
  public String getParameter(String key) {
    return System.getenv(key);
  }
}
