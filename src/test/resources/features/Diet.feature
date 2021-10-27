Feature: Register a Diet

  Scenario Outline: As a nutritionist i want to register a new diet.

    Given I register a new diet as a nutritionist
    And I sending diet form to be created with name <name> and description <description>
    Then I should be able to see my newly created diet

    Examples:
      | name      | description       |
      | "Dieta 1" | "Comer verduras"  |

  Scenario Outline: As a nutritionist i want to register a new diet without description.

    Given I register a new diet as a nutritionist
    And I sending diet form to be created without description <description>
    Then I should be able to see an advice <advice>

    Examples:
      | description       | advice                     |
      | ""                | "Ingrese una descripcion"  |