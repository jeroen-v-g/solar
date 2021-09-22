package nl.jemo85.oauth.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(value = { "role" })
public class MenuItem{
  private String description;
  private String uri;
  private Boolean isRoute;
  private String role;
  private List<MenuItem> children;

  public MenuItem()
  {}

  public MenuItem(String description, String uri, Boolean isRoute, List<MenuItem> children, String role) {
    this.description = description;
    this.uri = uri;
    this.isRoute = isRoute;
    this.children = children;
    this.role=role;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public Boolean isRoute() {
    return isRoute;
  }

  public void setRoute(Boolean isRoute) {
    this.isRoute = isRoute;
  }

  public List<MenuItem> getChildren() {
    return children;
  }

  public void setChildren(List<MenuItem> children) {
    this.children = children;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

}
