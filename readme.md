# Custom AI Agent Helpers

This project provides customizable AI agent helpers, starting with a Jira integration. It is designed to connect to Jira, retrieve tickets based on configurable criteria, and generate concise summaries using the DIAL AI provider. The architecture is extensible for future integrations with other systems.

## Features
- Jira tickets' review agent. The agent connects to Jira, retrieves tickets for specified project and searchText:
  - for each found ticket:
    - summarizes the ticket using external AI provider (DIAL API)
    - reviews the ticket description using DIAL and generates readiness summary with questions
- Built with Java 21, Spring Boot, and Gradle

## Getting Started
1. Add your Jira and DIAL API keys to `application.yaml`.
2. Run `./gradlew bootRun` to start the service.
3. Access ticket summaries at: [http://localhost:8080/tickets/summaries](http://localhost:8080/tickets/summaries).

## Notes
- Linter/package errors are expected until you run `gradle build` and let the IDE index dependencies. All package and import statements are correct for a standard Spring Boot/Feign project.

## Contributing & Extending
If you need:
- Unit tests
- Dockerfile
- Further integrations (other systems)
- Additional features

Feel free to open an issue or submit a pull request!