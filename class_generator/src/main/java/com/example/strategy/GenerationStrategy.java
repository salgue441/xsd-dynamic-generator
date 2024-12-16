package com.example.strategy;

import com.example.config.XsdGeneratorConfig;

/**
 * Strategy for XSD to Java class generation
 */
public interface GenerationStrategy {
  /**
   * Execute the generation process
   * 
   * @param config Generation configuration
   * @throws Exception If an error occurs during generation
   * @since 1.0.0
   */
  void generate(XsdGeneratorConfig config) throws Exception;

  /**
   * Validates the generation configuration
   * 
   * @param config Generation configuration
   * @throws Exception If the configuration is invalid
   * @return True if the configuration is valid
   * @since 1.0.0
   */
  boolean validateConfig(XsdGeneratorConfig config) throws Exception;
}
