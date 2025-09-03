package cn.labzen.tool.meta;

import cn.labzen.meta.component.DeclaredComponent;

public class ToolMeta implements DeclaredComponent {

  @Override
  public String mark() {
    return "Labzen-Tool";
  }

  @Override
  public String packageBased() {
    return "cn.labzen.tool";
  }

  @Override
  public String description() {
    return "工具包，包含最基本的包依赖集合，并提供普适基础工具与底层功能";
  }
}
