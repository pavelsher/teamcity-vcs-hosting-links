package org.jetbrains.teamcity.vcsHostingLinks.github;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.vcs.SVcsModification;
import jetbrains.buildServer.vcs.VcsRootInstance;
import jetbrains.buildServer.vcs.VcsRootNotFoundException;
import jetbrains.buildServer.web.openapi.PagePlace;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jmock.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;

@Test
public class GitHubChangeExtensionTest extends BaseTestCase {
  private GitHubChangeExtension myExtension;

  @BeforeMethod
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    Mock pagePlacesMock = mock(PagePlaces.class);
    Mock placeMock = mock(PagePlace.class);
    pagePlacesMock.stubs().method("getPlaceById").will(returnValue(placeMock.proxy()));
    placeMock.stubs().method("addExtension");
    Mock pluginDescriptorMock = mock(PluginDescriptor.class);
    pluginDescriptorMock.stubs().method("getPluginResourcesPath").will(returnValue("path"));
    myExtension = new GitHubChangeExtension((PagePlaces)pagePlacesMock.proxy(), (PluginDescriptor)pluginDescriptorMock.proxy());
  }

  public void test_is_available() {
    Mock vcsRootMock = prepareVcsRoot("jetbrains.git", "https://some/repo");
    Mock modificationMock = prepareVcsModification(vcsRootMock, false);

    Mock requestMock = prepareRequest(modificationMock);

    assertFalse(myExtension.isAvailable((HttpServletRequest) requestMock.proxy()));

    vcsRootMock.stubs().method("getProperty").with(eq("url")).will(returnValue("https://github.com/owner/repo.git"));
    assertTrue(myExtension.isAvailable((HttpServletRequest) requestMock.proxy()));
  }

  public void test_is_available_no_vcs_root() {
    Mock modificationMock = prepareVcsModification(null, false);

    Mock requestMock = prepareRequest(modificationMock);

    assertFalse(myExtension.isAvailable((HttpServletRequest) requestMock.proxy()));
  }

  public void test_is_available_personal() {
    Mock modificationMock = prepareVcsModification(null, true);

    Mock requestMock = prepareRequest(modificationMock);

    assertFalse(myExtension.isAvailable((HttpServletRequest) requestMock.proxy()));
  }

  @NotNull
  private Mock prepareRequest(Mock modificationMock) {
    Mock requestMock = mock(HttpServletRequest.class);
    requestMock.stubs().method("getAttribute").with(eq("modification")).will(returnValue(modificationMock.proxy()));
    return requestMock;
  }

  private Mock prepareVcsModification(Mock vcsRootMock, boolean personal) {
    Mock modificationMock = mock(SVcsModification.class);
    if (vcsRootMock != null) {
      modificationMock.stubs().method("getVcsRoot").will(returnValue(vcsRootMock.proxy()));
    } else {
      modificationMock.stubs().method("getVcsRoot").will(throwException(new VcsRootNotFoundException("no vcs root")));
    }

    modificationMock.stubs().method("isPersonal").will(returnValue(personal));
    return modificationMock;
  }

  @NotNull
  private Mock prepareVcsRoot(String vcsName, String url) {
    Mock vcsRootMock = mock(VcsRootInstance.class);
    vcsRootMock.stubs().method("getVcsName").will(returnValue(vcsName));
    vcsRootMock.stubs().method("getProperty").with(eq("url")).will(returnValue(url));
    return vcsRootMock;
  }
}
