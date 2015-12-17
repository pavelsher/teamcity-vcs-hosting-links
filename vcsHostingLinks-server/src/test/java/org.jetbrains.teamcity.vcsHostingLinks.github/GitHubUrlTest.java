package org.jetbrains.teamcity.vcsHostingLinks.github;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class GitHubUrlTest {
  public void test_parse_https() {
    GitHubUrl parsed = GitHubUrl.parse("https://github.com/owner/repo.git");
    Assert.assertNotNull(parsed);
    Assert.assertEquals(parsed.getOwner(), "owner");
    Assert.assertEquals(parsed.getRepositoryName(), "repo");
  }

  public void test_parse_ssh() {
    GitHubUrl parsed = GitHubUrl.parse("git@github.com:JetBrains/teamcity-achievements.git");
    Assert.assertNotNull(parsed);
    Assert.assertEquals(parsed.getOwner(), "JetBrains");
    Assert.assertEquals(parsed.getRepositoryName(), "teamcity-achievements");
  }
}
