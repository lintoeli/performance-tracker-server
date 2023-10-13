package com.diversolab.servicies;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import com.diversolab.entities.github.GithubIssueEvent;
import com.diversolab.entities.github.GithubIssueResult;
import com.diversolab.entities.github.GithubRelease;

// @Service
// @RegisterRestClient
// @RegisterClientHeaders(GitHubRestServiceHeaderFactory.class)
public interface GitHubRestService {

	// @GET
	// @Path("repos/{owner}/{repo}/releases")
	// @Produces(MediaType.APPLICATION_JSON)
	// List<GithubRelease> getGithubReleases(@PathParam("owner") String owner, @PathParam("repo") String repo,
	// 							@QueryParam("page") Integer page, @QueryParam("per_page") Integer per_page);

	@GetExchange("/repos/{owner}/{repo}/releases")
	List<GithubRelease> getGithubReleases(@PathVariable("owner") String owner, @PathVariable("repo") String repo,
								@RequestParam("page") Integer page, @RequestParam("per_page") Integer per_page);

	// @GET
	// @Path("search/issues")
	// @Produces(MediaType.APPLICATION_JSON)
	// GithubIssueResult getGithubIssues(@QueryParam("q") String query, @QueryParam("page") Integer page,
	// 						   @QueryParam("per_page") Integer per_page);

	@GetExchange("/search/issues")
	GithubIssueResult getGithubIssues(@RequestParam("q") String query, @RequestParam("page") Integer page,
	 						   @RequestParam("per_page") Integer per_page);

	// @GET
	// @Path("repos/{owner}/{repo}/issues/{number}/events")
	// @Produces(MediaType.APPLICATION_JSON)
	// List<GithubIssueEvent> getGithubIssuesEvents(@PathParam("owner") String owner, @PathParam("repo") String repo,
	// 									@PathParam("number") Integer number);

	@GetExchange("/repos/{owner}/{repo}/issues/{number}/events")
	List<GithubIssueEvent> getGithubIssuesEvents(@PathVariable("owner") String owner, @PathVariable("repo") String repo,
										@PathVariable("number") Integer number);

}
