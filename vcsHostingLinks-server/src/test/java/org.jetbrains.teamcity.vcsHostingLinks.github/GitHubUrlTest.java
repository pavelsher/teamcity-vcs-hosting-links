package org.jetbrains.teamcity.vcsHostingLinks.github;

import jetbrains.buildServer.BaseTestCase;
import org.testng.annotations.Test;

@Test
public class GitHubUrlTest extends BaseTestCase {
  public void test_parse_https() {
    GitHubUrl parsed = GitHubUrl.parse("https://github.com/owner/repo.git");
    assertNotNull(parsed);
    assertEquals("owner", parsed.getOwner());
    assertEquals("repo", parsed.getRepositoryName());
  }

  public void test_parse_https_no_git_suffix() {
    GitHubUrl parsed = GitHubUrl.parse("https://github.com/apache/commons-math");
    assertNotNull(parsed);
    assertEquals("apache", parsed.getOwner());
    assertEquals("commons-math", parsed.getRepositoryName());
  }

  public void test_parse_ssh() {
    GitHubUrl parsed = GitHubUrl.parse("git@github.com:JetBrains/teamcity-achievements.git");
    assertNotNull(parsed);
    assertEquals("JetBrains", parsed.getOwner());
    assertEquals("teamcity-achievements", parsed.getRepositoryName());
  }
}
