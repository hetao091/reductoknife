package com.github.reductoknife;

/**
 * =====================================
 * author   : hetao
 * version  ：V1.0
 * time     ：2019/4/16 18:29
 * desc     ：生成的Java文件都要实现这个接口
 * revise   :
 * =====================================
 */
public interface ViewBinder<T> {

  // 泛型支持
  void bind(T target);
}
