package demo.lib_compiler;

import com.google.auto.service.AutoService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;

import demo.lib_annotation.Range;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RangeProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Range.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Range.class);
        for (Element element : elements) {
            System.out.println("RangeProcessor -> " + element.toString());
            if (element instanceof TypeElement) {
                TypeElement te = (TypeElement) element;
                List<? extends TypeParameterElement> typeParameters = te.getTypeParameters();
                for (TypeParameterElement tpe : typeParameters) {
                    System.out.println("RangeProcessor -> TypeElement tpe:" + tpe.toString());
                }
            }
            if(element instanceof ExecutableElement) {
                ExecutableElement ee = (ExecutableElement) element;
                List<? extends TypeParameterElement> typeParameters = ee.getTypeParameters();
                for (TypeParameterElement tpe : typeParameters) {
                    System.out.println("RangeProcessor -> ExecutableElement tpe:" + tpe.toString());
                }
            }
        }
        return true;
    }
}
