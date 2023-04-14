package com.diversolab.servicies;

import reactor.util.function.Tuple2;

public interface IMetricService {

	Integer calculateGithubNumberOfReleases();

	Tuple2<Double,Double> calculateGithubDeploymentFrequency();

	Tuple2<Double,Double> calculateGithubLeadTimeForChanges();

	Tuple2<Double,Double> calculateGithubTimeToRestoreService();

	Double calculateGithubChangeFailureRate();

}
