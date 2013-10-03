Feature: As a build engineer
  I want MavenSanitizer to drop references to dependencies I configured
  because they only are required to compile or execute tests and we do not ship test sources to our customer.

  Background:
    Given the multi module sample in src/test/resources
    When I configure to drop dependencies in test scope

  Scenario: Remove references to the dependency in each pom
    And sanitize the maven project file
    Then there are no dependencies in scope test remaining in the entire build
