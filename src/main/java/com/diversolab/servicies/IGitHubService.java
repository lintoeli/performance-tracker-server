package com.diversolab.servicies;

import java.util.List;
import com.diversolab.entities.github.GithubRelease;

public interface IGitHubService {

	List<GithubRelease> getGithubReleases();

	List<Long> getGithubDeploymentFrequency();

	List<Long> getGithubLeadTimeForChanges();

	List<Long> getGithubTimeToRestoreService();

	Double getGithubChangeFailureRate();


}
