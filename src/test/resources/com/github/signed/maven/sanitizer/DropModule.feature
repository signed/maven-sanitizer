Feature: As a build engineer
  I want MavenSanitizer to drop maven modules I configured
  because we have modules that only contain test support code we do not want to ship to our customer.



  Background:
    Given the multi module sample in src/test/resources
    When I configure the module artifact-test-support to be dropped
    And sanitize the maven project file

  Scenario: Erase the module marked to be dropped in the reactor pom
    Then the reactor pom does not contain the module any more

  Scenario: Do not copy the excluded module to the destination directory
    Then the dropped module does not exist in the destination directory