package org.jetbrains.teamcity.vcsHostingLinks.bitbucket;

import jetbrains.buildServer.vcs.SVcsModification;
import jetbrains.buildServer.vcs.VcsRootInstance;
import jetbrains.buildServer.vcs.VcsRootNotFoundException;
import jetbrains.buildServer.web.openapi.ChangeDetailsExtension;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BitbucketChangeExtension extends ChangeDetailsExtension {
  private static final String GIT_PLUGIN_NAME = "jetbrains.git";
  private static final String HG_PLUGIN_NAME = "mercurial";
  private static final String GIT_URL_PROP_NAME = "url";
  private static final String HG_URL_PROP_NAME = "repositoryPath";

  private final Map<String, String> myVcsName2UrlMap = new HashMap<String, String>();

  private static final String BITBUCKET_ORG = "bitbucket.org/";

  public BitbucketChangeExtension(@NotNull PagePlaces pagePlaces, @NotNull PluginDescriptor pluginDescriptor) {
    super(pagePlaces);
    setPlaceId(PlaceId.CHANGE_DETAILS_BLOCK);
    setPluginName("bitbucketVcsHostingLinks");
    setIncludeUrl(pluginDescriptor.getPluginResourcesPath("/bitbucket/changeLink.jsp"));
    register();

    myVcsName2UrlMap.put(GIT_PLUGIN_NAME, GIT_URL_PROP_NAME);
    myVcsName2UrlMap.put(HG_PLUGIN_NAME, HG_URL_PROP_NAME);
  }

  @Override
  public boolean isAvailable(@NotNull HttpServletRequest request) {
    SVcsModification mod = findVcsModification(request);
    return mod != null && !mod.isPersonal() && getBitbucketUrl(mod) != null;
  }

  @Override
  public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {
    super.fillModel(model, request);
    SVcsModification mod = findVcsModification(request);
    if (mod != null) {
      String revision = mod.getVersion();
      String url = getBitbucketUrl(mod);
      if (url != null) {
        BitbucketUrl parsed = BitbucketUrl.parse(url);
        if (parsed != null) {
          String commitUrl = "https://bitbucket.org/" + parsed.getOwner() + "/" + parsed.getRepositoryName() + "/commits/" + revision;
          model.put("commitUrl", commitUrl);
          model.put("compactMode", !isChangePage(request));
        }
      }
    }
  }

  @Nullable
  private String getBitbucketUrl(@NotNull SVcsModification mod) {
    if (!mod.isPersonal()) {
      VcsRootInstance vcsRoot;
      try {
        vcsRoot = mod.getVcsRoot();
      } catch (VcsRootNotFoundException e) {
        return null;
      }

      String propName = myVcsName2UrlMap.get(vcsRoot.getVcsName());
      if (propName == null) return null;

      String url = vcsRoot.getProperty(propName);
      if (url != null && url.contains(BITBUCKET_ORG)) return url;
    }

    return null;
  }
}
