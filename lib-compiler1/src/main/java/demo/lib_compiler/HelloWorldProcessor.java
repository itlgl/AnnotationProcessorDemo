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
public class HelloWorldProcessor extends AbstractProcessor {

    // 可用@SupportedAnnotationTypes("demo.lib_annotation.HelloWorldGen")替代
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(HelloWorldGen.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(HelloWorldGen.class);
        System.out.println("HelloWorldProcessor -> this=" + this);
        System.out.println("HelloWorldProcessor -> process elements.size()=" + elements.size());
        System.out.println("HelloWorldProcessor -> roundEnv.processingOver()=" + roundEnv.processingOver());
        if(elements.size() > 0) {
            // HelloWorld.java
            for (Element element : elements) {
                try {
                    String className = "HelloWorld" + element.getSimpleName().toString();
                    JavaFileObject helloWorld = processingEnv.getFiler()
                            .createSourceFile("demo." + className, element);

                    Writer writer = helloWorld.openWriter();

                    writer.write("package demo;\n" +
                            "\n" +
                            "public final class " + className + " {\n" +
                            "  public static void main(String[] args) {\n" +
                            "    System.out.println(\"Hello jsr269!\");\n" +
                            "  }\n" +
                            "}");
                    writer.flush();
                    writer.close();
                    System.out.println("HelloWorldProcessor -> gen file:" + className);
                } catch (IOException e) {
                    System.out.println("HelloWorldProcessor -> gen file error, " + e);
                }

                // META-INF/services/test
                try {
                    String resourceFile = "META-INF/services/test_" + element.getSimpleName().toString();
                    FileObject fileObject = processingEnv.getFiler().createResource(
                            StandardLocation.CLASS_OUTPUT, "", resourceFile, element);
                    Writer writer = fileObject.openWriter();

                    writer.write("test string");

                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    System.out.println("HelloWorldProcessor -> create resource error, " + e);
                }
            }
        }
        return false;
    }
}