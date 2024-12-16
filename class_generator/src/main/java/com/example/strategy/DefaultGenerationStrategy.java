package com.example.strategy;

import com.example.config.XsdGeneratorConfig;
import com.example.exception.XsdGenerationException;
import com.example.transformer.ClassTransformer;
import com.sun.tools.xjc.Driver;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DefaultGenerationStrategy implements GenerationStrategy {
  private static final Logger LOGGER = Logger.getLogger(DefaultGenerationStrategy.class.getName());
  private final ClassTransformer transformer;

  /**
   * Constructor
   * 
   * @param transformer Class transformer
   * @version 1.0.0
   */
  public DefaultGenerationStrategy(ClassTransformer transformer) {
    this.transformer = transformer;
  }

  /**
   * Generates Java classes from an XSD file using the XJC tool
   * 
   * @param transformer Class transformer
   * @throws XsdGenerationException If an error occurs during generation
   * @throws XsdGenerationException If XJC driver generation failed
   * @version 1.0.0
   */
  @Override
  public void generate(XsdGeneratorConfig config) throws Exception {
    if (!validateConfig(config)) {
      throw new XsdGenerationException("Invalid generation configuration",
          XsdGenerationException.ErrorCode.INVALID_CONFIG);
    }

    List<String> xjcArgs = prepareXjcArguments(config);
    int result = Driver.run(
        xjcArgs.toArray(new String[0]),
        System.out,
        System.err);

    if (result != 0) {
      throw new XsdGenerationException("XJC generation failed", XsdGenerationException.ErrorCode.UNKNOWN_ERROR);
    }

    if (config.isGenerateSingletons()) {
      transformer.transformToSingletons(config);
    }

    LOGGER.info("XSD to Java generation completed successfully");
  }

  /**
   * Validates the generation configuration by checking if the source XSD file
   * and output directory exist
   * 
   * @param config Generation configuration
   * @return True if the configuration is valid
   * @version 1.0.0
   */
  @Override
  public boolean validateConfig(XsdGeneratorConfig config) {
    return config != null
        && config.getXsdSourcePath() != null
        && Files.exists(config.getXsdSourcePath())
        && config.getOutputDirectory() != null
        && Files.exists(config.getOutputDirectory());
  }

  /**
   * Prepares the arguments for the XJC tool based on the generation
   * configuration
   * 
   * @param config Generation configuration
   * @return List of arguments for the XJC tool
   * @version 1.0.0
   */
  private List<String> prepareXjcArguments(XsdGeneratorConfig config) {
    List<String> args = new ArrayList<>();

    args.add(config.getXsdSourcePath().toString());
    args.add("-d");
    args.add(config.getOutputDirectory().toString());
    args.add("-p");
    args.add(config.getBasePackage());

    return args;
  }
}
