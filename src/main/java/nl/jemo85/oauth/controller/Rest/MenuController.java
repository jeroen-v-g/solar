package nl.jemo85.oauth.controller.Rest;

import java.util.ArrayList;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import nl.jemo85.oauth.dto.MenuItem;
import nl.jemo85.oauth.listener.CustomSpringInitListenerForMenuAndRoutes;

@RestController

public class MenuController {

  @RequestMapping({"/menu/get"})

  public @ResponseBody MenuItem getMenu(CustomSpringInitListenerForMenuAndRoutes customSpringInitListener)
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    ArrayList<String> roles = new ArrayList<>();
    if (authentication!=null)
    {
      var authorities = authentication.getAuthorities();
      authorities.forEach(authority->roles.add(authority.getAuthority()));
    }
    return customSpringInitListener.filterMenuForRoles(roles);
  }
}
