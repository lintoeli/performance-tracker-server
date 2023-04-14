package com.diversolab.servicies;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.diversolab.entities.github.GithubIssueEvent;
import com.diversolab.entities.github.GithubIssueResult;
import com.diversolab.entities.github.GithubRelease;

public interface GitHubRestService {

	@GET
	@Path("/repos/{owner}/{repo}/releases")
	@Produces(MediaType.APPLICATION_JSON)
	List<GithubRelease> getGithubReleases(@PathParam("owner") String owner, @PathParam("repo") String repo,
								@QueryParam("page") Integer page, @QueryParam("per_page") Integer per_page);

	@GET
	@Path("/search/issues")
	@Produces(MediaType.APPLICATION_JSON)
	GithubIssueResult getGithubIssues(@QueryParam("q") String query, @QueryParam("page") Integer page,
							   @QueryParam("per_page") Integer per_page);

	@GET
	@Path("/repos/{owner}/{repo}/issues/{number}/events")
	@Produces(MediaType.APPLICATION_JSON)
	List<GithubIssueEvent> getGithubIssuesEvents(@PathParam("owner") String owner, @PathParam("repo") String repo,
										@PathParam("number") Integer number);

}
