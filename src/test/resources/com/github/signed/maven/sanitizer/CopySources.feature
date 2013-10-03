Feature: As a build engineer
  I want MavenSanitizer to copy all sources in production scope
  because our client needs them to build and debug our product at his site.

  Background:
    Given the multi module sample in src/test/resources
    When sanitize the maven project files

  Scenario: Copy each configured source root
    Then the source root of the artifact module is copied to the destination directory

  Scenario: Copy each configured resource root
    Then the resource root of the artifact module is copied to the destination directory

  Scenario: Copy the warSourceDirectory for war artifacts
    Then the configured warSourceDirectory is copied to the destination directory