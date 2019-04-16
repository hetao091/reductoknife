package com.github.reductoprocessor;

import com.github.reductoannotations.BindView;
import com.google.auto.service.AutoService;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

/**
 * =====================================
 * author   : hetao
 * version  ：V1.0
 * time     ：2019/3/26 20:13
 * desc     ：定义注解处理器
 * revise   :
 * =====================================
 */
@AutoService(Processor.class)
public class ReductoKnife extends AbstractProcessor {

  // 拿到被注解的控件
  // 生成类文件去处理

  // 生成Java文件
  Filer mFiler;

  private Messager mErrorMessager;


  // 初始化
  @Override
  public synchronized void init(ProcessingEnvironment processingEnvironment) {
    super.init(processingEnvironment);
    mFiler = processingEnvironment.getFiler();
    mErrorMessager = processingEnvironment.getMessager();
  }


  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> mTypes = new HashSet<>();
    // 拿到 注解名称
    mTypes.add(BindView.class.getCanonicalName());
    return mTypes;
  }

  // 注解处理器的版本
  @Override
  public SourceVersion getSupportedSourceVersion() {
    return processingEnv.getSourceVersion();
  }

  /**
   * 事物处理
   */
  @Override
  public boolean process(
      Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

    //获取所有使用了BindView注解的成员变量也就是控件
    Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
    /*
     * 创建一个装载控件的节点容器并结构化
     * VariableElement
     */
    Map<String, List<VariableElement>> map = new HashMap<>();
    //
    for (Element element : elements) {
      //强转
      VariableElement variableElement = (VariableElement) elements;
      //
      String activityClassName = getClassName(variableElement);
      //  结构化类中的成员变量
      List<VariableElement> list = map.get(activityClassName);
      if (list == null) {
        list = new ArrayList<>();
        map.put(activityClassName, list);
      }
      list.add(variableElement);

    }

    // 生成文件
    //通过key获取值
    for (String name : map.keySet()) {

      //  获取类名

      List<VariableElement> list = map.get(name);
      //
      String packagesName = getPackageName(list.get(0));
      //生成新的文件名字关联
      String newClassName = name + "_ViewBinding";

      Writer writer = null;
      try {

        JavaFileObject sourceFile = mFiler.createSourceFile(newClassName);
        writer = sourceFile.openWriter();
        //开始写第一行
        writer.write("package " + packagesName + ";\n\n");
        //  第二行
        writer.write("import " + packagesName + ".ViewBinder;\n");
        // 生成类名
        writer.write(
            "public class  " + newClassName + " implements ViewBinder(" + packagesName + "." + name
                + "){\n");
        // 生成方法
        writer.write("public void bind(" + packagesName + "." + name + " target){\n");
        //findviewbyid
        for (VariableElement variableElement : list) {
          // 拿到控件类型
          TypeMirror typeMirror = variableElement.asType();
          // 拿到控件名称
          String variableName = variableElement.getSimpleName().toString();
          // 拿到控件ID->先拿到注解
          BindView bindView = variableElement.getAnnotation(BindView.class);
          int id = bindView.value();

          writer.write(
              "target." + variableName + " =(" + typeMirror + ")target.findViewById( " + id + "));\n");
        }
        writer.write("}\n}");
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (writer != null) {
          try {
            writer.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

    }

    return false;
  }


  /**
   * 通过控件节点获取当前类名
   */

  private String getClassName(VariableElement variableElement) {

    // 获取上一层节点
    TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();

    // 获取最外层控件类名
    String className = typeElement.getSimpleName().toString();
    //
    return className;
  }


  /**
   * 通过控件节点获取当前包名
   */
  private String getPackageName(VariableElement variableElement) {

    // 获取上一层节点
    TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
    // 通过类节点获取包名
    String packagesName = processingEnv.getElementUtils().getPackageOf(typeElement).toString();
    //
    return packagesName;
  }

//  /**
//   * 创建Java代码
//   */
//  public String generateJavaCode() {
//    StringBuilder builder = new StringBuilder();
//    builder.append("package ").append(mPackageName).append(";\n\n");
//    builder.append("import com.example.gavin.apt_library.*;\n");
//    builder.append('\n');
//    builder.append("public class ").append(mBindingClassName);
//    builder.append(" {\n");
//
//    generateMethods(builder);
//    builder.append('\n');
//    builder.append("}\n");
//    return builder.toString();
//  }
//
//  /**
//   * 加入Method
//   */
//  private void generateMethods(StringBuilder builder) {
//    builder.append("public void bind(" + mTypeElement.getQualifiedName() + " host ) {\n");
//    for (int id : mVariableElementMap.keySet()) {
//      VariableElement element = mVariableElementMap.get(id);
//      String name = element.getSimpleName().toString();
//      String type = element.asType().toString();
//      builder.append("host." + name).append(" = ");
//      builder.append("(" + type + ")(((android.app.Activity)host).findViewById( " + id + "));\n");
//    }
//    builder.append("  }\n");
//  }
}
