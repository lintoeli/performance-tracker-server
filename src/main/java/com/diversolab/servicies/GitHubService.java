package com.diversolab.servicies;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.diversolab.entities.github.GithubIssue;
import com.diversolab.entities.github.GithubIssueEvent;
import com.diversolab.entities.github.GithubIssueResult;
import com.diversolab.entities.github.GithubRelease;
import one.util.streamex.StreamEx;

@Service
public class GitHubService implements IGitHubService {
	
	private String sinceDay;
	private String untilDay;
	private String bugLabel;
	private String bugTitle;
	private String owner;
	private String repository;
	private GitHubRestService githubRestService;


	/**
	 * Returns a date given a string
	 * 
	 * @return a date given a string
	 */
	private Date getFormattedDate(String date){

		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'");

		var calendar = Calendar.getInstance();
		try {
			calendar.setTime(dateFormatter.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Date formattedDate = calendar.getTime();

		return formattedDate;
	}

	/**
	 * Returns a number used to divide issues lists given the strings of the first and last day of the period
	 * 
	 * @return a number used to divide issues lists given the strings of the first and last day of the period
	 */
	private Integer getDivider(String sinceString, String untilString){

		int page = 1;
		int per_page = 100;

		String queryIncidents = "repo:"+owner+"/"+repository+" is:issue created:<"+untilString+" updated:>"+sinceString+" sort:created-asc";
		if(!bugLabel.equals("\"\"")){
			queryIncidents = "label:"+bugLabel+" "+queryIncidents;
		}
		if(!bugTitle.equals("\"\"")){
			queryIncidents = bugTitle+" in:title "+queryIncidents;
		}

		GithubIssueResult resultIncidents = this.githubRestService.getGithubIssues(queryIncidents, page, per_page);
		Long numberOfIncidents = resultIncidents.getTotalCount();

		String queryIssues = "repo:"+owner+"/"+repository+" is:issue created:<"+untilString+" updated:>"+sinceString+" sort:created-asc";
		GithubIssueResult resultIssues = this.githubRestService.getGithubIssues(queryIssues, page, per_page);
		Long numberOfIssues = resultIssues.getTotalCount();

		Long totalIncidentsAndIssues = numberOfIncidents + numberOfIssues;

		//To avoid having trouble with high amount of issues (because the GitHub API only allows 5000 queries per hour), we find if there are more than 5000 results, in which case we pick a percentage of the total results in each query (this is because we have to make a query for each result)
		Integer divider = 1;
		//We sum the total number of issues plus the total number divided by 100 (because each query returns a page with 100 results, so we will need to make that many queries)
		Long approxNumberOfQueries = totalIncidentsAndIssues + (totalIncidentsAndIssues/100);
		if(approxNumberOfQueries > 5000){
			approxNumberOfQueries = approxNumberOfQueries/5000;
			//We add one to get 1 more if the approxNumberOfQueries is not an exact number
			divider = Integer.valueOf(approxNumberOfQueries.intValue() + 1);
		}

		return divider;
	}

	/**
	 * Returns GitHub releases published in given period
	 * 
	 * @return GitHub releases published in given period
	 */
	public List<GithubRelease> getGithubReleases() {

		int page = 1;
		int per_page = 100;
		List<GithubRelease> releases = new ArrayList<>();
		List<GithubRelease> releasesInPeriod = new ArrayList<>();
		List<GithubRelease> resp = new ArrayList<>();

		Date since = this.getFormattedDate(sinceDay);
		Date until = this.getFormattedDate(untilDay);
		
		boolean notLastRelease = true;
		while(notLastRelease){

			// Deployments in page
			releases = this.githubRestService.getGithubReleases(owner, repository, page, per_page);

			// Deployments of the page inside the period
			releasesInPeriod = releases.stream().filter(release -> release.getPublishedAt().before(until) && release.getPublishedAt().after(since) && !(release.getPrerelease())).collect(Collectors.toList());
			resp.addAll(releasesInPeriod);

			// Is there any deployment before the period? (Which means we have finished)
			releasesInPeriod = releases.stream().filter(deployment -> deployment.getPublishedAt().before(since)).collect(Collectors.toList());
			if(releasesInPeriod.size() > 0){
				notLastRelease = false;
				break;
			}

			page++;
		}

		return resp;

	}

	/**
	 * Returns GitHub releases created in given period
	 * 
	 * @return GitHub releases created in given period
	 */
	public List<GithubRelease> getGithubReleasesCreated(String sinceRelease, String untilRelease) {

		int page = 1;
		int per_page = 100;
		List<GithubRelease> releases = new ArrayList<>();
		List<GithubRelease> releasesInPeriod = new ArrayList<>();
		List<GithubRelease> resp = new ArrayList<>();

		Date since = this.getFormattedDate(sinceRelease);
		Date until = this.getFormattedDate(untilRelease);
		
		boolean notLastRelease = true;
		while(notLastRelease){

			// Deployments in page
			releases = this.githubRestService.getGithubReleases(owner, repository, page, per_page);

			// Deployments of the page inside the period
			releasesInPeriod = releases.stream().filter(release -> (release.getCreatedAt().getTime() <= until.getTime()) && (release.getCreatedAt().getTime() >= since.getTime()) && (!(release.getPrerelease()))).collect(Collectors.toList());
			
			resp.addAll(releasesInPeriod);

			// Is there any deployment before the period? (Which means we have finished)
			releasesInPeriod = releases.stream().filter(deployment -> deployment.getCreatedAt().before(since)).collect(Collectors.toList());
			if(releasesInPeriod.size() > 0){
				notLastRelease = false;
				break;
			}

			page++;
		}

		return resp;

	}

	/**
	 * Returns GitHub deployment frequency
	 * 
	 * @return GitHub deployment frequency
	 */
	public List<Long> getGithubDeploymentFrequency() {

		List<GithubRelease> releases = this.getGithubReleases();

		releases = releases.stream().sorted(Comparator.comparing(GithubRelease::getPublishedAt)).toList();

		List<Long> lista = StreamEx.of(
				// Get ordered builds of date completed
				releases.stream().sorted(Comparator.comparing(GithubRelease::getPublishedAt))
						// Map to date completed
						.map(GithubRelease::getPublishedAt)
						// Map to milliseconds
						.map(Date::getTime))
				// Pairwise differences
				.pairMap(((p1, p2) -> p2 - p1)).toList();

		
		return lista;

	}

	/**
	 * Returns GitHub issues which last commit was made between two given dates (that usually correspond to two releases)
	 * 
	 * @return GitHub issues which last commit was made between two given dates (that usually correspond to two releases)
	 */
	private List<GithubIssue> getGithubIssuesCommitedInPeriod(Date sinceRelease, Date untilRelease) {
		int page = 1;
		int per_page = 100;

		String pattern = "yyyy-MM-dd'T'HH:mm:ss'+00:00'";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	
		String sinceString = "";
		sinceString = simpleDateFormat.format(sinceRelease);
		String untilString = "";
		untilString = simpleDateFormat.format(untilRelease);
		
		String query = "repo:"+owner+"/"+repository+" is:issue created:<"+untilString+" updated:>"+sinceString+" sort:created-asc";
		
		System.out.println("query: "+query);

		GithubIssueResult result = this.githubRestService.getGithubIssues(query, page, per_page);
		List<GithubIssue> issues = new ArrayList<>();
		List<GithubIssueEvent> events = new ArrayList<>();
		Date eventCreatedAt = new Date();

		String createdSince = "";

		Integer divider = this.getDivider(sinceString, untilString);


		// We do not update per_page but the query. That means that the result will be different in each iteration and, eventually the condition will be met
		while(result.getTotalCount() >= per_page){

			System.out.println("result: "+result.getTotalCount());

			result = this.githubRestService.getGithubIssues(query, page, per_page);
			List<GithubIssue> filteredIssues = new ArrayList<>();
			if(divider > 1){
				for(int i = 0; i < result.getItems().size(); i++){
					if(i % divider == 0){
						filteredIssues.add(result.getItems().get(i));
					}
				}
			}else{
				filteredIssues = result.getItems();
			}

			for(GithubIssue issue:filteredIssues){
				events = this.githubRestService.getGithubIssuesEvents(owner, repository, issue.getNumber());
				events = events.stream().filter(e -> e.getEvent().equals("referenced") && ((e.getCreatedAt().getTime() < untilRelease.getTime()) && (e.getCreatedAt().getTime() > sinceRelease.getTime()))).sorted(Comparator.comparing(GithubIssueEvent::getCreatedAt)).collect(Collectors.toList());
				if(events.size() > 0){
					eventCreatedAt = events.get(events.size() - 1).getCreatedAt();
					issue.setLastCommitDate(eventCreatedAt);
					issues.add(issue);
				}
			}

			//We get the creation date of the last retrieved issue (we get the list of non filtered results)
			createdSince = simpleDateFormat.format(result.getItems().get(result.getItems().size() - 1).getCreatedAt());
			query = "repo:"+owner+"/"+repository+" is:issue created:"+createdSince+".."+untilString+" updated:>"+sinceString+" sort:created-asc";
			
		}
			
		return issues;

	}

	/**
	 * Returns GitHub lead time for changes (there should be at least one release created before the first created one of the releases included in the period)
	 * 
	 * @return GitHub lead time for changes (there should be at least one release created before the first created one of the releases included in the period)
	 */
	public List<Long> getGithubLeadTimeForChanges() { 

		System.out.println("Hol1");

		int page = 1;
		int per_page = 100;

		List<GithubRelease> releases = this.getGithubReleases();
		releases = releases.stream().sorted(Comparator.comparing(GithubRelease::getCreatedAt).reversed()).collect(Collectors.toList());
		GithubRelease lastRelease = releases.get(releases.size() - 1);
		GithubRelease firstRelease = releases.get(0);

		// We add releases created between the first and last created releases in the period
		releases.clear();
		Timestamp sinceRelease = new Timestamp(lastRelease.getCreatedAt().getTime());
		Timestamp untilRelease = new Timestamp(firstRelease.getCreatedAt().getTime());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'");
		releases.addAll(this.getGithubReleasesCreated(format.format(sinceRelease), format.format(untilRelease)));
		releases = releases.stream().sorted(Comparator.comparing(GithubRelease::getCreatedAt).reversed()).collect(Collectors.toList());


		System.out.println("Hol2");

		// We include the first release created before the first one created of the ones included in the period (we include a release not published in the period, but created before the first created one in the period, to know since when commited issues have to be included in the first release (last one as the list starts with the most recently created releases))
		List<GithubRelease> releasesList = this.githubRestService.getGithubReleases(owner, repository, page, per_page);
		releasesList = releasesList.stream().filter(release -> release.getCreatedAt().getTime() < lastRelease.getCreatedAt().getTime() && (!(release.getPrerelease()))).collect(Collectors.toList());
		while(releasesList.size() == 0){
			page++;
			releasesList = this.githubRestService.getGithubReleases(owner, repository, page, per_page);
			releasesList = releasesList.stream().filter(release -> release.getCreatedAt().getTime() < lastRelease.getCreatedAt().getTime() && (!(release.getPrerelease()))).collect(Collectors.toList());
		}
		
		releasesList = releasesList.stream().filter(release -> release.getCreatedAt().getTime() < lastRelease.getCreatedAt().getTime() && (!(release.getPrerelease()))).collect(Collectors.toList());
		releasesList = releasesList.stream().sorted(Comparator.comparing(GithubRelease::getCreatedAt).reversed()).collect(Collectors.toList());
		releases.add(releasesList.get(0));

		System.out.println("Hol3");


		List<Long> leadTimeForChanges = new ArrayList<>();;
		List<GithubIssue> issuesInPeriod = new ArrayList<>();
		issuesInPeriod = this.getGithubIssuesCommitedInPeriod(releases.get(releases.size() - 1).getCreatedAt(), releases.get(0).getCreatedAt());
		releases.sort(Comparator.comparing(GithubRelease::getCreatedAt));
		issuesInPeriod.sort(Comparator.comparing(GithubIssue::getLastCommitDate));
		int index = 1;
		for(GithubIssue issue:issuesInPeriod){
			while(issue.getLastCommitDate().getTime() > releases.get(index).getCreatedAt().getTime()){
				index++;
			}
			leadTimeForChanges.add(releases.get(index).getPublishedAt().getTime() - issue.getLastCommitDate().getTime());
		}

		return leadTimeForChanges;
	}


	

	/**
	 * Returns GitHub incidents which last commit was made between two given dates (that usually correspond to two consecutive releases) (maximun of 1000 issues)
	 * 
	 * @return GitHub incidents which last commit was made between two given dates (that usually correspond to two consecutive releases) (maximun of 1000 issues)
	 */
	private List<GithubIssue> getGithubIncidentsCommitedInPeriod(Date sinceRelease, Date untilRelease) {
		
		int page = 1;
		int per_page = 100;

		String pattern = "yyyy-MM-dd'T'HH:mm:ss'+00:00'";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	
		String sinceString = "";
		sinceString = simpleDateFormat.format(sinceRelease);
		String untilString = "";
		untilString = simpleDateFormat.format(untilRelease);
		
		String query = "repo:"+owner+"/"+repository+" is:issue created:<"+untilString+" updated:>"+sinceString+" sort:created-asc";
		if(!bugLabel.equals("\"\"")){
			query = "label:"+bugLabel+" "+query;
		}
		if(!bugTitle.equals("\"\"")){
			query = bugTitle+" in:title "+query;
		}

		GithubIssueResult result = this.githubRestService.getGithubIssues(query, page, per_page);

		List<GithubIssue> issues = new ArrayList<>();
		List<GithubIssueEvent> events = new ArrayList<>();
		Date eventCreatedAt = new Date();
		String createdSince = "";

		Integer divider = this.getDivider(sinceString, untilString);

		while(result.getTotalCount() != 0){

			result = this.githubRestService.getGithubIssues(query, page, per_page);
			List<GithubIssue> filteredIssues = new ArrayList<>();
			if(divider > 1){
				for(int i = 0; i < result.getItems().size(); i++){
					if(i % divider == 0){
						filteredIssues.add(result.getItems().get(i));
					}
				}
			}else{
				filteredIssues = result.getItems();
			}

			//SI EL DE LAS ISSUES FUNCIONA, CORREGIR ESTE Y PONERLO IGUAL //////////////////////
			for(GithubIssue issue:filteredIssues){

				events = this.githubRestService.getGithubIssuesEvents(owner, repository, issue.getNumber());
				events = events.stream().filter(e -> e.getEvent().equals("referenced") && ((e.getCreatedAt().getTime() < untilRelease.getTime()) && (e.getCreatedAt().getTime() > sinceRelease.getTime()))).sorted(Comparator.comparing(GithubIssueEvent::getCreatedAt)).collect(Collectors.toList());
				if(events.size() > 0){
					eventCreatedAt = events.get(events.size() - 1).getCreatedAt();
					issue.setLastCommitDate(eventCreatedAt);
					issues.add(issue);
				}
			}

			createdSince = simpleDateFormat.format(result.getItems().get(result.getItems().size() - 1).getCreatedAt());
			query = "repo:"+owner+"/"+repository+" is:issue created:"+createdSince+".."+untilString+" updated:>"+sinceString+" sort:created-asc";
			if(!bugLabel.equals("\"\"")){
				query = "label:"+bugLabel+" "+query;
			}
			if(!bugTitle.equals("\"\"")){
				query = bugTitle+" in:title "+query;
			}

			result = this.githubRestService.getGithubIssues(query, page, per_page);

		}

		return issues;

	}

	/**
	 * Returns GitHub time to restore service (there should be at least one release created before the first created one of the releases included in the period)
	 * 
	 * @return GitHub time to restore service (there should be at least one release created before the first created one of the releases included in the period)
	 */
	public List<Long> getGithubTimeToRestoreService() { 
		int page = 1;
		int per_page = 100;

		List<GithubRelease> releases = this.getGithubReleases();
		releases = releases.stream().sorted(Comparator.comparing(GithubRelease::getCreatedAt).reversed()).collect(Collectors.toList());
		GithubRelease lastRelease = releases.get(releases.size() - 1);
		GithubRelease firstRelease = releases.get(0);

		// We add releases created between the first and last created releases in the period
		releases.clear();
		Timestamp sinceRelease = new Timestamp(lastRelease.getCreatedAt().getTime());
		Timestamp untilRelease = new Timestamp(firstRelease.getCreatedAt().getTime());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'");
		releases.addAll(this.getGithubReleasesCreated(format.format(sinceRelease), format.format(untilRelease)));
		releases = releases.stream().sorted(Comparator.comparing(GithubRelease::getCreatedAt).reversed()).collect(Collectors.toList());

		// We include the first release created before the first one created of the ones included in the period (we include a release not published in the period, but created before the first created one in the period, to know since when commited issues have to be included in the first release (last one as the list starts with the most recently created releases))
		List<GithubRelease> releasesList = this.githubRestService.getGithubReleases(owner, repository, page, per_page);
		releasesList = releasesList.stream().filter(release -> release.getCreatedAt().getTime() < lastRelease.getCreatedAt().getTime() && (!(release.getPrerelease()))).collect(Collectors.toList());
		while(releasesList.size() == 0){
			page++;
			releasesList = this.githubRestService.getGithubReleases(owner, repository, page, per_page);
			releasesList = releasesList.stream().filter(release -> release.getCreatedAt().getTime() < lastRelease.getCreatedAt().getTime() && (!(release.getPrerelease()))).collect(Collectors.toList());
		}

		releasesList = releasesList.stream().filter(release -> release.getCreatedAt().getTime() < lastRelease.getCreatedAt().getTime() && (!(release.getPrerelease()))).collect(Collectors.toList());
		releasesList = releasesList.stream().sorted(Comparator.comparing(GithubRelease::getCreatedAt).reversed()).collect(Collectors.toList());
		releases.add(releasesList.get(0));

		List<Long> timeToRestoreService = new ArrayList<>();;
		List<GithubIssue> issuesInPeriod = new ArrayList<>();
		issuesInPeriod = this.getGithubIncidentsCommitedInPeriod(releases.get(releases.size() - 1).getCreatedAt(), releases.get(0).getCreatedAt());
		releases.sort(Comparator.comparing(GithubRelease::getCreatedAt));
		issuesInPeriod.sort(Comparator.comparing(GithubIssue::getLastCommitDate));

		int index = 1;
		for(GithubIssue issue:issuesInPeriod){
			while(issue.getLastCommitDate().getTime() > releases.get(index).getCreatedAt().getTime()){
				index++;
			}
			timeToRestoreService.add(releases.get(index).getPublishedAt().getTime() - issue.getCreatedAt().getTime());
		}

		return timeToRestoreService;
	}

	/**
	 * Returns GitHub change failure rate
	 * 
	 * @return GitHub change failure rate
	 */
	public Double getGithubChangeFailureRate() {
		int page = 1;
		int per_page = 100;

		String queryIncidents = "repo:"+owner+"/"+repository+" is:issue created:"+sinceDay+".."+untilDay+" sort:created-asc";
		if(!bugLabel.equals("\"\"")){
			queryIncidents = "label:"+bugLabel+" "+queryIncidents;
		}
		if(!bugTitle.equals("\"\"")){
			queryIncidents = bugTitle+" in:title "+queryIncidents;
		}

		GithubIssueResult resultIncidents = this.githubRestService.getGithubIssues(queryIncidents, page, per_page);
		Double numberOfIncidents = Double.valueOf(resultIncidents.getTotalCount());

		String queryIssues = "repo:"+owner+"/"+repository+" is:issue created:"+sinceDay+".."+untilDay+" sort:created-asc";
		GithubIssueResult resultIssues = this.githubRestService.getGithubIssues(queryIssues, page, per_page);
		Double numberOfIssues = Double.valueOf(resultIssues.getTotalCount());

		return numberOfIncidents/numberOfIssues;
	}
}
