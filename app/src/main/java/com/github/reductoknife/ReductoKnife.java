package com.github.reductoknife;

import android.app.Activity;

/**
 * =====================================
 * author   : hetao
 * version  ：V1.0
 * time     ：2019/4/16 19:04
 * desc     ：
 * revise   :
 * =====================================
 */
class ReductoKnife {

  private ReductoKnife() {
  }

  /**
   * 反射获取生成的 Java文件
   */
  public static void bind(Activity activity) {
    String activityName = activity.getClass().getName() + "_ViewBinding";
    try {
      Class<?>  cls =  Class.forName(activityName);
      ViewBinder viewBinder  = (ViewBinder) cls.newInstance();
      viewBinder.bind(activity);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    }
  }
}
