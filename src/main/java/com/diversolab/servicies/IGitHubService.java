package com.diversolab.servicies;

import java.util.List;
import com.diversolab.entities.github.GithubRelease;

public interface IGitHubService {

	List<GithubRelease> getGithubReleases(String owner, String repository,  String startPeriod, String endPeriod);

	List<Long> getGithubDeploymentFrequency(String owner, String repository, String startPeriod, String endPeriod);

	List<Long> getGithubLeadTimeForChanges(String owner, String repository, String startPeriod, String endPeriod);

	List<Long> getGithubTimeToRestoreService(String owner, String repository, String startPeriod, String endPeriod);

	Double getGithubChangeFailureRate(String owner, String repository, String startPeriod, String endPeriod);


}
