package nl.jemo85.oauth.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import nl.jemo85.oauth.listener.CustomSpringInitListenerForMenuAndRoutes.MappingAndRole;

@Controller
public class AngularController extends HttpServlet {

  private List<MappingAndRole> mappings;

  public AngularController(List<MappingAndRole> mappings) {
    this.mappings = mappings;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    ArrayList<String> roles = new ArrayList<>();
    if (authentication!=null)
    {
      var authorities = authentication.getAuthorities();
      authorities.forEach(authority->roles.add(authority.getAuthority()));
    }
    String uri = request.getRequestURI();
    Optional<MappingAndRole> optional = mappings.stream().filter(item->item.getMapping().equals(uri)).findAny();
    if (optional.isPresent())
    {
      MappingAndRole mapping = optional.get();
      if (!roles.contains(mapping.getRole()) && mapping.getRole()!=null)
      {
          response.sendError(403);
      }
      else
      {
        request.getRequestDispatcher("/template/index.jsp").forward(request, response);
      }
    }
    else
    {
      request.getRequestDispatcher("/template/index.jsp").forward(request, response);
    }
  }
}
