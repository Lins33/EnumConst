package com.enumconst;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = {"com.enumconst.BosEnum"})
public class BosEnumProcessor extends AbstractProcessor {

    private Types types;
    private Elements elements;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        types=processingEnv.getTypeUtils();
        elements= processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        LinkedHashSet<String> annotations = new LinkedHashSet<String>();
        annotations.add(BosEnum.class.getCanonicalName());
        return annotations;
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE,"begin-----------------");
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(BosEnum.class);
        for (Element element : elementsAnnotatedWith) {
            if(element.getKind()== ElementKind.ENUM){
                PackageElement packageElement = elements.getPackageOf(element);
                String packagePath = packageElement.getQualifiedName().toString();
                String className =element.getSimpleName().toString();
                try {
                    JavaFileObject sourceFile = filer.createSourceFile(packagePath + "." + className + "Const", element);
                    Writer writer = sourceFile.openWriter();
                    writer.write("package "+packagePath+";\n");
                    writer.write("public class "+className+"Const"+"{\n");
                    writer.write("\n");
                    writer.append("     final static  int  test1=(int)Math.pow(2,0);\n");
                    String var=getItem(element);
                    writer.append(var);
                    writer.write("\n");
                    writer.append("}");
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        messager.printMessage(Diagnostic.Kind.NOTE,"end-----------------------");
        return true;
    }

    private String getItem(Element e){
        StringBuilder stringBuffer=new StringBuilder();
        List<? extends Element> enclosedElements = e.getEnclosedElements();
        for (Element element:enclosedElements){
            if (element.getKind()==ElementKind.ENUM_CONSTANT){
                String name=element.getSimpleName().toString();
                System.out.println(name);
                BosEnumItem bosEnumItem=element.getAnnotation(BosEnumItem.class);
                if (bosEnumItem!=null){
                    int value = bosEnumItem.value();
                    String workName = bosEnumItem.workName();
                    stringBuffer.append("     final static  int  ").append(name).append("=(int)Math.pow(").append(2).append(",").append(value).append(");\n");
                }

            }
        }
        return stringBuffer.toString();
    }
}
