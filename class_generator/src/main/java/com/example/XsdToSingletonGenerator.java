package com.example;

import com.example.config.XsdGeneratorConfig;
import com.example.strategy.DefaultGenerationStrategy;
import com.example.strategy.GenerationStrategy;
import com.example.transformer.ClassTransformer;

import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Main entry point for XSD to Singleton Java class generator.
 */
public class XsdToSingletonGenerator {
    private static final Logger LOGGER = Logger.getLogger(XsdToSingletonGenerator.class.getName());

    public static void main(String[] args) {
        try {
            XsdGeneratorConfig config = new XsdGeneratorConfig.Builder()
                    .xsdSourcePath(Paths.get("src/main/resources/sample.xsd"))
                    .outputDirectory(Paths.get("src/main/java/com/example/generated"))
                    .basePackage("model")
                    .generateSingletons(true)
                    .build();

            ClassTransformer transformer = new ClassTransformer();
            GenerationStrategy strategy = new DefaultGenerationStrategy(transformer);

            strategy.generate(config);
        } catch (Exception e) {
            LOGGER.severe("XSD to singleton generation failed: " + e.getMessage());
            e.printStackTrace();
            ;
        }
    }
}
