package org.ttt;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("org.ttt")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "junit")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "org.ttt")
public class RunCucumberTest {}
