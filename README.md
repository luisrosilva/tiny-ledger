# Basic implementation for a ledger.

## Overview
 - Springboot project with OpenAPI specification in api.yml and code/endpoints generated from spec.
   - Create/get/list accounts.
   - Create/list money movements.
 - External domain defined in openapi and internal domain for the logic. Mapping is done via mapstruct.

## Considerations:
 - Accounts are stored in memory, in a Map (restarts lose all data).
 - Accounts should be threadsafe.
 - Only deposit/withdrawals are implemented.
 - No unit tests were implemented.
 - Simple integration tests provide very limited coverage (happy path for account creation, making some movements and verify the balance and movement list).
 - I started with the idea of building a double entry ledger, but backed down due to time constraints. There might be some leftover ideas.

## Usage
 - `mvn clean verify` builds the project and runs the tests.
 - `mvn springBoot:start` should launch the project running on port 8080.
 - `curl -XPOST http://127.0.0.1:8080/account` creates an account (save the account id).
 - `curl -XGET "http://127.0.0.1:8080/account/{id}"` should get the balance.
 - `curl -XGET "http://127.0.0.1:8080/account/{id}/movement"` should get the movement list.
 - `curl -XPOST "http://127.0.0.1:8080/account/{id}/movement?amount=300&type=DEPOSIT"` should make a deposit.
 - `curl -XPOST "http://127.0.0.1:8080/account/{id}/movement?amount=300&type=WITHDRAWAL"` should make a withdrawal.
 - `curl -XGET "http://127.0.0.1:8080/account/{id}/movement"` should list the money movements.
 - `mvn springBoot:stop` should stop the project