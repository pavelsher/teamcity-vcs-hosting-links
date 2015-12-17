package org.jetbrains.teamcity.vcsHostingLinks.github;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubUrl {
  private final static Pattern URL_PATTERN = Pattern.compile("(https://github\\.com/|git@github\\.com:)([^:/]+)/(.+)");
  private final String myOwner;
  private final String myRepositoryName;

  public GitHubUrl(@NotNull String githubUrl) {
    final Matcher matcher = URL_PATTERN.matcher(githubUrl);
    if (matcher.matches()) {
      myOwner = matcher.group(2);
      String repoName = matcher.group(3);
      if (repoName.endsWith(".git")) {
        repoName = repoName.substring(0, repoName.length()-4);
      }

      myRepositoryName = repoName;
    } else {
      myOwner = null;
      myRepositoryName = null;
    }
  }

  @NotNull
  public String getOwner() {
    return myOwner;
  }

  @NotNull
  public String getRepositoryName() {
    return myRepositoryName;
  }

  @Nullable
  public static GitHubUrl parse(@NotNull String url) {
    GitHubUrl parsed = new GitHubUrl(url);
    if (parsed.myOwner != null && parsed.myRepositoryName != null) return parsed;
    return null;
  }
}
