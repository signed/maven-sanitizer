Feature: As a build engineer
  I want MavenSanitizer to drop references to dependencies I configured
  because they only are required to compile or execute tests and we do not ship test sources to our customer.

  Background:
    Given the multi module sample in src/test/resources
    When I configure to drop dependencies in test scope
    And sanitize the maven project file

  Scenario: Remove references to the dependency section in each pom
    Then there are no dependencies in scope test remaining in the dependency section in the entire build

  Scenario: Remove references to the dependency management section in each pom
    Then there are no dependencies in scope test remaining in the dependency management  section in the entire build
