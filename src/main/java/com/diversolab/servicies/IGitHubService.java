package com.diversolab.servicies;

import java.util.List;
import com.diversolab.entities.github.GithubRelease;

public interface IGitHubService {

	List<GithubRelease> getGithubReleases(String owner, String repository);

	List<Long> getGithubDeploymentFrequency(String owner, String repository);

	List<Long> getGithubLeadTimeForChanges(String owner, String repository);

	List<Long> getGithubTimeToRestoreService(String owner, String repository);

	Double getGithubChangeFailureRate(String owner, String repository);


}
