package org.jetbrains.teamcity.vcsHostingLinks.github;

import jetbrains.buildServer.vcs.SVcsModification;
import jetbrains.buildServer.vcs.VcsRootInstance;
import jetbrains.buildServer.web.openapi.ChangeDetailsExtension;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class GitHubChangeExtension extends ChangeDetailsExtension {
  private static final String GIT_PLUGIN_NAME = "jetbrains.git";
  private static final String URL_PROP_NAME = "url";
  private static final String GITHUB_COM = "github.com";

  public GitHubChangeExtension(@NotNull PagePlaces pagePlaces, @NotNull PluginDescriptor pluginDescriptor) {
    super(pagePlaces);
    setPlaceId(PlaceId.CHANGE_DETAILS_BLOCK);
    setPluginName("githubVcsHostingLinks");
    setIncludeUrl(pluginDescriptor.getPluginResourcesPath("/github/changeLink.jsp"));
    addCssFile(pluginDescriptor.getPluginResourcesPath("/github/github.css"));
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
    SVcsModification mod = findVcsModification(request);
    if (mod != null) {
      String revision = mod.getVersion();
      String url = mod.getVcsRoot().getProperty(URL_PROP_NAME);
      if (url != null) {
        GitHubUrl parsed = GitHubUrl.parse(url);
        if (parsed != null) {
          String commitUrl = "https://github.com/" + parsed.getOwner() + "/" + parsed.getRepositoryName() + "/commit/" + revision;
          model.put("commitUrl", commitUrl);
        }
      }
    }
  }
}
