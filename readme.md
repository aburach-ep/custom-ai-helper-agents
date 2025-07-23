# Custom AI Agent Helpers

This project provides customizable AI agent helpers, starting with a Jira integration. It is designed to connect to Jira, retrieve tickets based on configurable criteria, and generate concise summaries using the DIAL AI provider. The architecture is extensible for future integrations with other systems.

## Features
- Connects to Jira using FeignClient
- Retrieves tickets for a configurable project and subject
- Summarizes each ticket (3-5 sentences) using DIAL AI
- All credentials and parameters are configurable via `application.yaml`
- Built with Java 21, Spring Boot, and Gradle

## Project Structure
- **Spring Boot main class:** `CustomAiJiraAgentApplication.java`
- **Feign configuration:** `config/FeignConfig.java`
- **Feign clients:** `client/JiraClient.java`, `client/DialClient.java`
- **Models:** `model/JiraIssue.java`, `model/DialRequest.java`, `model/DialResponse.java`
- **Service:** `service/TicketSummaryService.java`
- **REST Controller:** `controller/TicketController.java`
- **Gradle build and settings:** `build.gradle`, `settings.gradle`
- **Config:** `src/main/resources/application.yaml`

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