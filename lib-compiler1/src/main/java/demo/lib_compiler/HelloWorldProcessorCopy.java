package demo.lib_compiler;

import com.google.auto.service.AutoService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import demo.lib_annotation.HelloWorldGen;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes("demo.lib_annotation.HelloWorldGen")
public class HelloWorldProcessorCopy extends AbstractProcessor {

    // 可用@SupportedAnnotationTypes("demo.lib_annotation.HelloWorldGen")替代
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(HelloWorldGen.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(HelloWorldGen.class);
        System.out.println("HelloWorldProcessorCopy -> this=" + this);
        System.out.println("HelloWorldProcessorCopy -> process elements.size()=" + elements.size());
        System.out.println("HelloWorldProcessorCopy -> roundEnv.processingOver()=" + roundEnv.processingOver());
        if(elements.size() > 0) {
            // HelloWorld.java
            try {
                JavaFileObject helloWorld = processingEnv.getFiler()
                        .createSourceFile("demo.HelloWorldCopy");

                Writer writer = helloWorld.openWriter();

                writer.write("package demo;\n" +
                        "\n" +
                        "public final class HelloWorldCopy {\n" +
                        "  public static void main(String[] args) {\n" +
                        "    System.out.println(\"Hello jsr269!\");\n" +
                        "  }\n" +
                        "}");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.out.println("HelloWorldProcessorCopy -> gen file error, " + e);
            }
        }
        return false;
    }
}