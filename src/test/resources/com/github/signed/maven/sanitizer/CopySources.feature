Feature: As a build engineer
  I want MavenSanitizer to copy all sources in production scope
  because our client needs them to build and debug our product at his site.

  Background:
    Given the multi module sample in src/test/resources

  Scenario: Copy each configured source root
    When sanitize the maven project files
    Then the source root of the artifact module is copied to the destination directory

  Scenario: Copy each configured resource root
    When sanitize the maven project files
    Then the resource root of the artifact module is copied to the destination directory

  Scenario: Copy the warSourceDirectory for war artifacts
    When sanitize the maven project files
    Then the configured warSourceDirectory is copied to the destination directory

  Scenario: Copy assembly descriptors that are configured for the maven assembly plugin
    When sanitize the maven project files
    Then the configured assembly descriptor is copied is copied to the destination directory

  Scenario: Copy directories I explicitly configure to be copied
    And I configure to copy the subdirectory important in the module org.example parent
    When sanitize the maven project files
    Then the directory important exists below the module root of org.example parent in the destination directory