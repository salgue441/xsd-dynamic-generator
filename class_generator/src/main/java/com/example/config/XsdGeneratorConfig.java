package com.example.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuration for the XSD generator.
 * 
 * @version 1.0.0
 */
public class XsdGeneratorConfig {
  private final Path xsdSourcePath;
  private final Path outputDirectory;
  private final String basePackage;
  private final boolean generateSingletons;

  /**
   * Creates a new XSD generator configuration.
   * 
   * @param builder Configuration builder instance
   * @version 1.0.0
   */
  private XsdGeneratorConfig(Builder builder) {
    this.xsdSourcePath = builder.xsdSourcePath;
    this.outputDirectory = builder.outputDirectory;
    this.basePackage = builder.basePackage;
    this.generateSingletons = builder.generateSingletons;
  }

  /**
   * Gets the path to the XSD source file.
   * 
   * @return Path to the XSD source file
   * @version 1.0.0
   */
  public Path getXsdSourcePath() {
    return xsdSourcePath;
  }

  /**
   * Gets the output directory for the generated classes.
   * 
   * @return Output directory for the generated classes
   * @version 1.0.0
   */
  public Path getOutputDirectory() {
    return outputDirectory;
  }

  /**
   * Gets the base package for the generated classes.
   * 
   * @return Base package for the generated classes
   * @version 1.0.0
   */
  public String getBasePackage() {
    return basePackage;
  }

  /**
   * Gets whether to generate singleton classes for elements with maxOccurs=1.
   * 
   * @return Whether to generate singletons
   * @version 1.0.0
   */
  public boolean isGenerateSingletons() {
    return generateSingletons;
  }

  /**
   * Configuration builder for the XSD generator.
   */
  public static class Builder {
    private Path xsdSourcePath;
    private Path outputDirectory;
    private String basePackage = "com.example.generated.model";
    private boolean generateSingletons = true;

    /**
     * Sets the path to the XSD source file.
     * 
     * @param xsdSourcePath Path to the XSD source file
     * @return Builder instance
     * @version 1.0.0
     */
    public Builder xsdSourcePath(Path xsdSourcePath) {
      this.xsdSourcePath = xsdSourcePath;
      return this;
    }

    /**
     * Sets the path to the XSD source file.
     * 
     * @param xsdSourcePath Path to the XSD source file
     * @return Builder instance
     * @version 1.0.0
     */
    public Builder outputDirectory(Path outputDirectory) {
      this.outputDirectory = outputDirectory;
      return this;
    }

    /**
     * Sets the base package for the generated classes.
     * 
     * @param basePackage Base package name
     * @return Builder instance
     * @version 1.0.0
     */
    public Builder basePackage(String basePackage) {
      this.basePackage = basePackage;
      return this;
    }

    /**
     * Sets whether to generate singleton classes for elements with maxOccurs=1.
     * 
     * @param singleton Whether to generate singletons
     * @return Builder instance
     * @version 1.0.0w
     */
    public Builder generateSingletons(boolean singleton) {
      this.generateSingletons = singleton;
      return this;
    }

    /**
     * Builds the XSD generator configuration.
     * 
     * @return XsdGeneratorConfig instance
     * @throws IllegalArgumentException if any of the configurations are invalid
     * @version 1.0.0
     */
    public XsdGeneratorConfig build() {
      validate();
      return new XsdGeneratorConfig(this);
    }

    /**
     * Validates the provided configurations to ensure they are valid.
     * 
     * @throws IllegalArgumentException if any of the configurations are invalid
     * @version 1.0.0
     */
    private void validate() {
      validatePath(xsdSourcePath, "XSD source path", true);
      validatePath(outputDirectory, "Output directory", false);
      validateBasePackage(basePackage);
    }

    /**
     * Validates a file path
     * 
     * @param path        Path to validate
     * @param pathName    Name of the path for error messaging
     * @param isXsdSource Whether the path is the XSD source file
     * @throws IllegalArgumentException if path is invalid
     * @version 1.0.0
     */
    private void validatePath(Path path, String pathName, boolean isXsdSource) {
      if (path == null) {
        throw new IllegalArgumentException(pathName + " must be specified");
      }

      if (!Files.exists(path)) {
        throw new IllegalArgumentException(pathName + " does not exist");
      }

      if (!Files.isReadable(path)) {
        throw new IllegalArgumentException(pathName + " is not readable");
      }

      if (isXsdSource) {
        if (!Files.isRegularFile(path)) {
          throw new IllegalArgumentException("XSD source path must be a file");
        }

        String fileName = path.getFileName().toString().toLowerCase();
        if (!fileName.endsWith(".xsd")) {
          throw new IllegalArgumentException("XSD source must have a .xsd file extension");
        }
      } else {
        if (!Files.isDirectory(path)) {
          try {
            Files.createDirectories(path);
          } catch (Exception e) {
            throw new IllegalArgumentException("Output directory does not exist and could not be created");
          }
        }

        if (!Files.isWritable(path)) {
          throw new IllegalArgumentException("Output directory must be writable");
        }
      }
    }

    /**
     * Validates the base package name according to Java naming conventions
     * 
     * @param packageName Package name to validate
     * @throws IllegalArgumentException if package name is invalid
     * @version 1.0.0
     */
    private void validateBasePackage(String packageName) {
      if (packageName == null || packageName.trim().isEmpty()) {
        throw new IllegalArgumentException("Base package must be specified");
      }

      String packageNameRegex = "^[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)*$";
      if (!packageName.matches(packageNameRegex)) {
        throw new IllegalArgumentException(
            "Invalid package name. Must start with a lowercase letter and contain only lowercase letters, numbers, and underscores separated by dots");
      }

      if (packageName.length() > 256) {
        throw new IllegalArgumentException("Package name is too long. Must be 256 characters or less");
      }

      Set<String> reservedKeywords = new HashSet<>(Arrays.asList(
          "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
          "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
          "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
          "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
          "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
          "volatile", "while"));

      for (String part : packageName.split("\\.")) {
        if (reservedKeywords.contains(part)) {
          throw new IllegalArgumentException("Package name contains a reserved keyword: " + part);
        }
      }
    }
  }
}
