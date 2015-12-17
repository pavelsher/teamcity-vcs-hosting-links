package org.jetbrains.teamcity.vcsHostingLinks.github;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class GitHubUrlTest {
  public void test_parse() {
    GitHubUrl parsed = GitHubUrl.parse("https://github.com/owner/repo.git");
    Assert.assertNotNull(parsed);
  }
}
