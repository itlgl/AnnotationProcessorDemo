package demo.lib_compiler3;

import com.google.auto.service.AutoService;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import demo.lib_annotation.Getter;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class GetterProcessor extends AbstractProcessor {

    private JavacTrees mJavacTrees;
    private TreeMaker mTreeMaker;
    private Names mNames;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        mJavacTrees = JavacTrees.instance(processingEnv);
        mTreeMaker = TreeMaker.instance(context);
        mNames = Names.instance(context);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Getter.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Getter.class);
        for (Element element : elements) {
            if(!element.getKind().isClass()) {
                continue;
            }

            // visit fields & insert getter
            JCTree tree = mJavacTrees.getTree(element);
            tree.accept(new TreeTranslator() {
                 @Override
                 public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                     // List.nil不支持remove，用defsTem把defs里面的加进去
                     ArrayList<JCTree> defsTem = new ArrayList<>();
                     for (JCTree jcTree : jcClassDecl.defs) {
                         if(jcTree.getKind().equals(Tree.Kind.VARIABLE)) {
                             JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) jcTree;
                             // 删除map字段
                             if(jcVariableDecl.name.toString().equals("map")) {
                                 continue;
                             }

                             // 增加getter方法
                             JCTree.JCMethodDecl jcMethodDecl = makeGetterMethodDecl(jcVariableDecl);
                             defsTem.add(jcMethodDecl);
                             defsTem.add(jcTree);
                         } else {
                             defsTem.add(jcTree);
                         }
                     }
                     jcClassDecl.defs = List.from(defsTem);

                     super.visitClassDef(jcClassDecl);
                 }
             });
        }
        return true;
    }

    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {
        List<JCTree.JCStatement> jcStatements = new ListBuffer<JCTree.JCStatement>()
                .append(mTreeMaker.Return(mTreeMaker.Select(mTreeMaker.Ident(mNames._this), jcVariableDecl.getName())))
                .toList();
        JCTree.JCBlock body = mTreeMaker.Block(0, jcStatements);

        return mTreeMaker.MethodDef(
                mTreeMaker.Modifiers(Flags.PUBLIC),
                mNames.fromString(generateGetterMethodName(jcVariableDecl.sym.toString(), jcVariableDecl.vartype)),
                jcVariableDecl.vartype,
                List.nil(),
                List.nil(),
                List.nil(),
                body,
                null
        );
    }

    private static String generateGetterMethodName(String paramName, JCTree.JCExpression varType) {
        String get = "get";
        if(varType.toString().equals("boolean") || varType.toString().equals("Boolean")) {
            get = "is";
        }
        return get + paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
    }
}