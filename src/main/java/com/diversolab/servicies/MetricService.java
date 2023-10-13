package com.diversolab.servicies;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@AllArgsConstructor
@Service
public class MetricService implements IMetricService {

	private final IGitHubService githubService;

	/**
	 * Returns mean and variance of the elements of a list
	 * 
	 * @return mean and variance of the elements of a list
	 */
	public Tuple2<Double,Double> calculateMeanAndVariance(List<Long> list) {

		//We divide the elements of the list by a number to transform the results to days
		double divider = 86400000.0;

		Double mean = 0.0;
		for(int i = 0; i < list.size(); i++){
			mean = mean + list.get(i)/divider;
		}
		mean = mean/list.size();

		Double variance = 0.0;
		for (int i = 0; i < list.size(); i++) {
			variance += Math.pow(list.get(i)/divider - mean, 2);
		}
		variance = variance/list.size();
		
		Double standardDeviation = Math.sqrt(variance);
		return Tuples.of(mean,standardDeviation);
	}

	/**
	 * Returns GitHub number of releases
	 * 
	 * @return GitHub number of releases
	 */
	public Integer calculateGithubNumberOfReleases(String owner, String repository) {

		var deployments = this.githubService.getGithubReleases(owner, repository).size();

		return deployments;
	}

	/**
	 * Returns GitHub Deployment Frequency
	 * 
	 * @return GitHub Deployment Frequency
	 */
	public Tuple2<Double,Double> calculateGithubDeploymentFrequency(String owner, String repository) {

		var deployments = this.githubService.getGithubDeploymentFrequency(owner, repository);

		return this.calculateMeanAndVariance(deployments);
	}

	/**
	 * Returns GitHub Lead Time For Changes
	 * 
	 * @return GitHub Lead Time For Changes
	 */
	public Tuple2<Double,Double> calculateGithubLeadTimeForChanges(String owner, String repository) {

		var issues = this.githubService.getGithubLeadTimeForChanges(owner, repository);

		return this.calculateMeanAndVariance(issues);		
	}

	/**
	 * Returns GitHub Time To Restore Service
	 * 
	 * @return GitHub Time To Restore Service
	 */
	public Tuple2<Double,Double> calculateGithubTimeToRestoreService(String owner, String repository) {

		var incidents = this.githubService.getGithubTimeToRestoreService(owner, repository);

		return this.calculateMeanAndVariance(incidents);
	}

	/**
	 * Returns GitHub Change Failure Rate
	 * 
	 * @return GitHub Change Failure Rate
	 */
	public Double calculateGithubChangeFailureRate(String owner, String repository) {

		return this.githubService.getGithubChangeFailureRate(owner, repository) * 100;
	}

}
