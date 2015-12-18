package org.jetbrains.teamcity.vcsHostingLinks.bitbucket;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BitbucketUrl {
  private final static Pattern URL_PATTERN = Pattern.compile("(https://[^@]+@bitbucket\\.org/|ssh://hg@bitbucket\\.org/|git@bitbucket\\.org:)([^:/]+)/(.+)");
  private final String myOwner;
  private final String myRepositoryName;

  public BitbucketUrl(@NotNull String githubUrl) {
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
  public static BitbucketUrl parse(@NotNull String url) {
    BitbucketUrl parsed = new BitbucketUrl(url);
    if (parsed.myOwner != null && parsed.myRepositoryName != null) return parsed;
    return null;
  }
}
