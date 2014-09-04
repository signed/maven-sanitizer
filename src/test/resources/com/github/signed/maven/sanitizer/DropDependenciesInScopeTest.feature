Feature: As a build engineer
  I want MavenSanitizer to drop all dependencies in scope test
  because we do not ship the tests to our customer.

  @implementing
  Scenario: Inherit version from dependency management
    Given a build that manages hamcrest in test scope in version 1.3
    But in one of its modules includes hamcrest in compile scope
    And I configure to drop dependencies in test scope
    When sanitize the maven project files
    Then the managed dependencies do not include hamcrest
    And the hamcrest dependency in compile scope has version 1.3