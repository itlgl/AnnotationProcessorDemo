package demo.lib_compiler2;

import com.google.auto.service.AutoService;

import java.io.IOException;
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
public class HelloWorldProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(HelloWorldGen.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(HelloWorldGen.class);
        System.out.println("HelloWorldProcessor -> process elements.size()=" + elements.size());
        if(elements.size() > 0) {
            try {
                JavaFileObject helloWorld = processingEnv.getFiler()
                        .createSourceFile("demo.HelloWorld");

                Writer writer = helloWorld.openWriter();

                writer.write("package demo;\n" +
                        "\n" +
                        "public final class HelloWorld {\n" +
                        "  public static void main(String[] args) {\n" +
                        "    System.out.println(\"Hello, JavaPoet!\");\n" +
                        "  }\n" +
                        "}");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.out.println("HelloWorldProcessor -> gen file error, " + e);
            }

            try {
                String resourceFile = "META-INF/services/test";
                FileObject fileObject = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
                Writer writer = fileObject.openWriter();

                writer.write("test string");

                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.out.println("HelloWorldProcessor -> create resource error, " + e);
            }
        }
        return true;
    }
}