package com.github.reductoannotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * =====================================
 * author   : hetao
 * version  ：V1.0
 * time     ：2019/3/26 20:04
 * desc     ：自定义注解
 * revise   :
 * =====================================
 */

@Target(ElementType.FIELD)  // 定义作用域为成员变量
@Retention(RetentionPolicy.SOURCE) //声明注解的生命周期
public @interface BindView {
   int value();
}
