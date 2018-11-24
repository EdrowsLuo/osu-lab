package com.edlplan.edjavaext_apt;

import com.edplan.framework.utils.apt.LoadOnStart;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class LoadOnStartProcessor extends AbstractProcessor {

    private Messager messager;

    private Filer filer;

    private ArrayList<String> names = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!names.isEmpty()) {
            names.clear();
            messager.printMessage(Diagnostic.Kind.NOTE, "clear names");
        }
        for (Element element : roundEnvironment.getElementsAnnotatedWith(LoadOnStart.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> names = new HashSet<>();
        names.add(LoadOnStart.class.getCanonicalName());
        return names;
    }

}
