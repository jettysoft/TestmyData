package com.testmydata.dao;

import java.net.URI;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class JIRAAccess {
	private static final String JIRA_URL = "http://jira-dev:8080";
	private static final String JIRA_ADMIN_USERNAME = "admin";
	private static final String JIRA_ADMIN_PASSWORD = "admin";

	public static void main(String[] args) throws Exception {
		// Construct the JRJC client
		final URI jiraServerUri = new URI("https://jira-domain");
		final JiraRestClient restClient = new AsynchronousJiraRestClientFactory()
				.createWithBasicHttpAuthentication(jiraServerUri, "user@domain.com", "password");
		// Promise issuePromise =
		// restClient.getIssueClient().getIssue(issueKey);

	}
}
