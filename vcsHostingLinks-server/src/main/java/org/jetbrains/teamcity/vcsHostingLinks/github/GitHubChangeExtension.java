package org.jetbrains.teamcity.vcsHostingLinks.github;

import jetbrains.buildServer.vcs.SVcsModification;
import jetbrains.buildServer.vcs.VcsRootInstance;
import jetbrains.buildServer.web.openapi.ChangeDetailsExtension;
import jetbrains.buildServer.web.openapi.PagePlaces;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class GitHubChangeExtension extends ChangeDetailsExtension {
  private static final String GIT_PLUGIN_NAME = "jetbrains.git";
  private static final String URL_PROP_NAME = "url";
  private static final String GITHUB_COM = "github.com";

  public GitHubChangeExtension(@NotNull PagePlaces pagePlaces) {
    super(pagePlaces);
    setIncludeUrl("/github/changeLink.jsp");
    register();
  }

  @Override
  public boolean isAvailable(@NotNull HttpServletRequest request) {
    SVcsModification mod = findVcsModification(request);
    if (mod != null) {
      VcsRootInstance vcsRoot = mod.getVcsRoot();
      if (GIT_PLUGIN_NAME.equals(vcsRoot.getName())) {
        String url = vcsRoot.getProperty(URL_PROP_NAME);
        if (url == null || !url.contains(GITHUB_COM)) return false;
      }
    }
    return super.isAvailable(request);
  }

  @Override
  public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {
    super.fillModel(model, request);
  }
}
