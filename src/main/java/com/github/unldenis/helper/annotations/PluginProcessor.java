package com.github.unldenis.helper.annotations;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Set;

@SupportedAnnotationTypes("com.github.unldenis.helper.annotations.Plugin")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class PluginProcessor extends AbstractProcessor {
    Messager messager;

    @Override
    public void init(ProcessingEnvironment env) {
        messager = env.getMessager();
        super.init(env);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        FileObject file;
        for(Element element: roundEnv.getElementsAnnotatedWith(Plugin.class)) {
            messager.printMessage(Diagnostic.Kind.WARNING, "Printing: " + element.toString());
            Plugin pluginAnnotation = element.getAnnotation(Plugin.class);

            try {
                file = this.processingEnv.getFiler().createResource( StandardLocation.CLASS_OUTPUT, "", "plugin.yml" );
                try ( Writer w = file.openWriter() ) {
                    w.append("name: "+ pluginAnnotation.name());
                    w.append("\n");
                    w.append("version: " + pluginAnnotation.version());
                    w.append("\n");
                    w.append("description: " + pluginAnnotation.description());
                    w.append("\n");
                    w.append("api_version: " + pluginAnnotation.api_version());
                    w.append("\n");
                    w.append("author: " + pluginAnnotation.author());
                    w.append("\n");
                    w.append("depend: " + Arrays.toString(pluginAnnotation.depend()));
                    w.append("\n");
                    w.append("softdepend: " + Arrays.toString(pluginAnnotation.softdepend()));
                    w.append("\n");
                    w.append("loadbefore: " + Arrays.toString(pluginAnnotation.loadbefore()));
                    w.append("\n");
                    w.append("commands:");
                    for(String s: pluginAnnotation.commands())
                        w.append("\n  " + s + ":");
                    w.flush();
                }
                messager.printMessage(Diagnostic.Kind.WARNING, "File created at "+ file.toUri().getPath());

            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Error file at " + e.getMessage());
            }

        }
        return true;
    }
}
