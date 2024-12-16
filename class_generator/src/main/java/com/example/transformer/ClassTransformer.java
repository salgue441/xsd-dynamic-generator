package com.example.transformer;

import com.example.config.XsdGeneratorConfig;
import com.example.exception.XsdGenerationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Transforms generated Java classes to implement singleton pattern.
 */
public class ClassTransformer {
  private static final Logger LOGGER = Logger.getLogger(ClassTransformer.class.getName());

  /**
   * Transforms all Java classes in the given directory to singletons.
   * 
   * @param config the configuration for the XSD generator
   * @throws XsdGenerationException if an error occurs while reading or writing
   *                                files
   * @version 1.0.0
   */
  public void transformToSingletons(XsdGeneratorConfig config) throws XsdGenerationException {
    try {
      List<Path> javaFiles = Files.walk(config.getOutputDirectory())
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().endsWith(".java"))
          .collect(Collectors.toList());

      for (Path classFile : javaFiles) {
        transformClassToSingleton(classFile);
      }
    } catch (IOException e) {
      throw new XsdGenerationException("Error transforming Java classes to singletons",
          XsdGenerationException.ErrorCode.FILE_ACCESS_ERROR, e);
    }
  }

  /**
   * Transforms all Java classes in the given directory to singletons.
   * 
   * @param classFile the directory containing the Java classes
   * @throws IOException if an error occurs while reading or writing
   *                     files
   * @version 1.0.0
   */
  private void transformClassToSingleton(Path classFile) {
    try {
      List<String> lines = Files.readAllLines(classFile);
      String className = extractClassName(lines);

      if (className == null) {
        LOGGER.warning("Could not extract class name from " + classFile);
        return;
      }

      List<String> transformedLines = transformLines(lines, className);
      Files.write(classFile, transformedLines);

      LOGGER.info("Transformed " + classFile + " to singleton");
    } catch (IOException e) {
      LOGGER.warning("Could not transform " + classFile + " to singleton: " + e.getMessage());
    }
  }

  /**
   * Extracts the class name from the given lines of code.
   * 
   * @param lines the lines of code to search
   * @return the class name if found, otherwise null
   * @version 1.0.0
   */
  private String extractClassName(List<String> lines) {
    for (String line : lines) {
      if (line.trim().matches("^public\\s+class\\s+\\w+.*\\{?$")) {
        String[] parts = line.trim().split("\\s+");

        for (int i = 0; i < parts.length; i++) {
          if (parts[i].equals("class") && i + 1 < parts.length) {
            return parts[i + 1].replace("{", "").trim();
          }
        }
      }
    }

    return null;
  }

  /**
   * Transforms the given lines of code to implement the singleton pattern.
   * 
   * @param originalLines the original lines of code
   * @param className     the name of the class to transform
   * @return the transformed lines of code
   * @version 1.0.0
   */
  private List<String> transformLines(List<String> originalLines, String className) {
    return originalLines.stream()
        .map(line -> {
          if (line.trim().matches("^public\\s+" + className + "\\s*\\(.*\\)\\s*\\{.*$")) {
            return generateSingletonCode(className);
          }
          return line;
        })
        .collect(Collectors.toList());
  }

  /**
   * Generates the code for the singleton pattern.
   * 
   * @param className the name of the class to transform
   * @return the code for the singleton pattern
   * @version 1.0.0
   */
  private String generateSingletonCode(String className) {
    return String.join(System.lineSeparator(),
      "    private static final " + className + " INSTANCE = new " + className + "();",
      "",
      "    private " + className + "() {",
      "        // Private constructor to prevent instantiation",
      "    }",
      "",
      "    public static " + className + " getInstance() {",
      "        return INSTANCE;",
      "    }");
  }
}
