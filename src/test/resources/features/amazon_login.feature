Feature: Amazon Login

  Scenario Outline: Login to Amazon with valid credentials
    Given I launch the "<browser>" browser
    And I navigate to the Amazon login page
    When I enter the username from the Excel file
    And I enter the password from the Excel file
    And I click the Sign-In button
    Then I should see the user account name on the homepage

    Examples:
      | browser |
      | chrome  |
      | firefox |
      | edge    |
