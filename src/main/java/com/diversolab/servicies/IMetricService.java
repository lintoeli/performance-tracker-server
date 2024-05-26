package com.diversolab.servicies;

import reactor.util.function.Tuple2;

public interface IMetricService {

	Integer calculateGithubNumberOfReleases(String owner, String repository, String startPeriod, String endPeriod);

	Tuple2<Double,Double> calculateGithubDeploymentFrequency(String owner, String repository,  String startPeriod, String endPeriod);

	Tuple2<Double,Double> calculateGithubLeadTimeForChanges(String owner, String repository,  String startPeriod, String endPeriod);

	Tuple2<Double,Double> calculateGithubTimeToRestoreService(String owner, String repository,  String startPeriod, String endPeriod);

	Double calculateGithubChangeFailureRate(String owner, String repository,  String startPeriod, String endPeriod);

}
