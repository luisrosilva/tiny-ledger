package org.rosilva.tinyledger;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.rosilva.tinyledger.api.model.Account;
import org.rosilva.tinyledger.api.model.AccountReference;
import org.rosilva.tinyledger.api.model.MoneyMovement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAccount() {
        ResponseEntity<AccountReference> result = restTemplate.postForEntity("http://localhost:" + port + "/account", null, AccountReference.class);

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void testGetAccount() {
        // Account creation
        ResponseEntity<AccountReference> resultAccountCreate = restTemplate.postForEntity("http://localhost:" + port + "/account", null, AccountReference.class);

        // Get account
        ResponseEntity<AccountReference> result = restTemplate.getForEntity("http://localhost:" + port + "/account/" + resultAccountCreate.getBody().getId(), null, AccountReference.class);

        // Verify account exists (get account returns 200)
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void testAccountMovements() {
        // Account creation
        AccountReference accountReference = restTemplate.postForEntity("http://localhost:" + port + "/account", null, AccountReference.class).getBody();

        // Make some movements, get account and movement list
        MoneyMovement depositResponse = restTemplate.postForEntity("http://localhost:" + port + "/account/" + accountReference.getId() + "/movement?amount=300&type=DEPOSIT", null, MoneyMovement.class).getBody();
        MoneyMovement withdrawalResponse = restTemplate.postForEntity("http://localhost:" + port + "/account/" + accountReference.getId() + "/movement?amount=200&type=WITHDRAWAL", null, MoneyMovement.class).getBody();
        Account account = restTemplate.getForEntity("http://localhost:" + port + "/account/" + accountReference.getId(), Account.class).getBody();
        List<Map<String, ?>> moneyMovementList = restTemplate.getForEntity("http://localhost:" + port + "/account/" + accountReference.getId() + "/movement", List.class).getBody();

        assertThat(account.getBalance()).isEqualTo(100);
        assertThat(moneyMovementList)
                .extracting(item -> item.get("amount"), item -> item.get("type"))
                .containsExactly(
                        Tuple.tuple(300, MoneyMovement.TypeEnum.DEPOSIT.toString()),
                        Tuple.tuple(200, MoneyMovement.TypeEnum.WITHDRAWAL.toString()));
    }
}
