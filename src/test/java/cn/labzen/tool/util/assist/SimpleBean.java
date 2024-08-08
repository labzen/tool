package cn.labzen.tool.util.assist;

public class SimpleBean {

  private String stringValue;
  private Integer intValue;
  private Boolean booleanValue;

  public SimpleBean(String stringValue, Integer intValue, Boolean booleanValue) {
    this.stringValue = stringValue;
    this.intValue = intValue;
    this.booleanValue = booleanValue;
  }

  public String getStringValue() {
    return stringValue;
  }

  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  public Integer getIntValue() {
    return intValue;
  }

  public void setIntValue(Integer intValue) {
    this.intValue = intValue;
  }

  public Boolean getBooleanValue() {
    return booleanValue;
  }

  public void setBooleanValue(Boolean booleanValue) {
    this.booleanValue = booleanValue;
  }
}
