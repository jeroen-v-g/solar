package nl.jemo85.oauth.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import nl.jemo85.oauth.controller.AngularController;
import nl.jemo85.oauth.dto.MenuItem;

@WebListener
public class CustomSpringInitListenerForMenuAndRoutes implements ServletContextListener {

  public static class MappingAndRole
  {
    private String mapping;
    private String role;

    public MappingAndRole(String mapping, String role) {
      this.mapping = mapping;
      this.role = role;
    }

    public String getMapping() {
      return mapping;
    }

    public String getRole() {
      return role;
    }

  }

  private final MenuItem root;

  public CustomSpringInitListenerForMenuAndRoutes() throws JsonParseException, JsonMappingException, IOException
  {
    var mapper = new ObjectMapper(new YAMLFactory());
    var resourceStream = this.getClass().getClassLoader().getResourceAsStream("menu.yml");
    String file = new BufferedReader(new InputStreamReader(resourceStream)).lines().collect(Collectors.joining("\n"));
    root = mapper.readValue(file, MenuItem.class);

  }

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent)
  {
    var servletContext = servletContextEvent.getServletContext();
    var mappings = getServletMapppings();
    var servlet = servletContext.addServlet(AngularController.class.getSimpleName(),new AngularController(mappings));
    servlet.addMapping("");
    mappings.forEach(mapping->{
      servlet.addMapping(mapping.mapping);
   });
  }

  public List<MappingAndRole> getServletMapppings()
  {
    return recursiveGetServletMappings(root);
  }

  private List<MappingAndRole> recursiveGetServletMappings(MenuItem item)
  {
    List<MappingAndRole> routes = new ArrayList<>();


    if (item.getChildren()!=null)
    {
      for (MenuItem childItem: item.getChildren())
      {
        routes.addAll(recursiveGetServletMappings(childItem));
      }
    }
    if (item.isRoute()!=null && item.isRoute())
      routes.add(new MappingAndRole(item.getUri(),item.getRole()));

    return routes;
  }

  public MenuItem filterMenuForRoles(List<String> roles)
  {
    return recursiveFilterMenuForRoles(root, roles);
  }

  private MenuItem recursiveFilterMenuForRoles(MenuItem item, List<String> roles)
  {
    if (!roles.contains(item.getRole()) && item.getRole()!=null)
      return null;

    MenuItem filteredItem = new MenuItem(item.getDescription(), item.getUri(), item.isRoute(), null, null);
    if (item.getChildren()!=null)
    {
      ArrayList<MenuItem> children = new ArrayList<>();
      for (MenuItem childItem: item.getChildren())
      {
        var child = recursiveFilterMenuForRoles(childItem, roles);
        if (child!=null)
        children.add(child);
      }
      filteredItem.setChildren(children);
    }
    return filteredItem;
  }

}
