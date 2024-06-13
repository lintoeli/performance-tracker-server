package com.diversolab.entities.github;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "github_releases")
public class GithubRelease {

	@Id
	@NonNull
	@JsonProperty("id")
	Long id;

	@NonNull
	@JsonProperty("prerelease")
	Boolean prerelease;

	@JsonProperty("name")
	String name;

	@NonNull
	@JsonProperty("created_at")
	Date createdAt;

	@NonNull
	@JsonProperty("published_at")
	Date publishedAt;

}
