package nl.jemo85.oauth.controller.Rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

  @Autowired
  OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

  public class URLPermission
  {
    boolean isAllowed;

    public URLPermission(boolean isAllowed) {
      this.isAllowed = isAllowed;
    }

    public boolean isAllowed() {
      return isAllowed;
    }

    public void setAllowed(boolean isAllowed) {
      this.isAllowed = isAllowed;
    }

  }

  @Autowired
  WebInvocationPrivilegeEvaluator privilegeEvaluator;

  @RequestMapping("/security/isAllowed")
  public URLPermission isAllowed(@RequestParam("url") String url)
  {
    return new URLPermission(privilegeEvaluator.isAllowed(url, SecurityContextHolder.getContext().getAuthentication()));

  }

}
