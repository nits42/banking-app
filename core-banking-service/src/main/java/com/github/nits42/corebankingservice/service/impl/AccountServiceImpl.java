package com.github.nits42.corebankingservice.service.impl;

import com.github.nits42.corebankingservice.clients.UserServiceFeignClient;
import com.github.nits42.corebankingservice.dto.AccountDTO;
import com.github.nits42.corebankingservice.dto.TransactionDTO;
import com.github.nits42.corebankingservice.entities.*;
import com.github.nits42.corebankingservice.enums.*;
import com.github.nits42.corebankingservice.exceptions.AccountNotFoundException;
import com.github.nits42.corebankingservice.exceptions.BankingAppCoreServiceApiException;
import com.github.nits42.corebankingservice.exceptions.CardNotFoundException;
import com.github.nits42.corebankingservice.exceptions.TransactionNotFoundException;
import com.github.nits42.corebankingservice.repository.*;
import com.github.nits42.corebankingservice.request.*;
import com.github.nits42.corebankingservice.service.AccountService;
import com.github.nits42.corebankingservice.util.BranchUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("accountService")
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final BranchRepository branchRepository;
    private final TransactionRepository transactionRepository;
    private final UserServiceFeignClient userService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String createAccount(AccountRequest request) {
        log.info("User's account creation process is started");
        User user = getUserByUsername(request.getUsername());
        if (user == null)
            throw BankingAppCoreServiceApiException.builder()
                    .message("User not found with username: " + request.getUsername())
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        Account account = Account.builder()
                .user(user)
                .accountNumber(System.currentTimeMillis())
                .accountType(AccountType.valueOf(request.getAccountType().toUpperCase()))
                .branch(BranchUtils.getBranchCode(
                        branchRepository.findAllByStatusAndPostalCode(Status.ACTIVE,
                                user.getUserDetails().getAddresses()
                                        .stream()
                                        .map(Address::getPostalCode)
                                        .toList()
                                        .stream()
                                        .findAny()
                                        .orElseThrow(() ->
                                                BankingAppCoreServiceApiException.builder()
                                                        .message("Address not found for: " + user.getUsername())
                                                        .httpStatus(HttpStatus.NOT_FOUND)
                                                        .build()
                                        )
                        )
                ))
                .balance(request.getBalance())
                .status(Status.ACTIVE)
                .build();

        account = accountRepository.save(account);

        if (account.getId() == null)
            throw BankingAppCoreServiceApiException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("User's account is not created")
                    .build();
        log.info("User's account creation process is completed");

        return "Account is created successfully";
    }

    private User getUserByUsername(String userName) {
        return modelMapper.map(userService
                .getUserByUsername(userName)
                .getBody(), User.class);
    }

    private Long generateAccountNumber(int prefix) {
        Random rand = new Random();
        long x = (long) (rand.nextDouble() * 100000000000000L);
        String s = prefix + String.format("%014d", x);
        return Long.valueOf(s);
    }


    @Override
    @Transactional
    public String deposit(AccountUpdateRequest request) {
        deposit(request.getAccountNo(), request.getAmount());
        log.info("Amount deposited");
        return "Amount deposited successfully";
    }

    @Override
    @Transactional
    public String withdraw(AccountUpdateRequest request) {
        withdraw(request.getAccountNo(), request.getAmount());
        log.info("Amount withdrew");
        return "Amount withdrawal successfully completed";
    }


    private void recordTransaction(Account account, double amount, String transactionType) {
        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(amount)
                .transactionType(TransactionType.valueOf(transactionType.toUpperCase()))
                .build();

        transactionRepository.save(transaction);
    }

    private void deposit(Long accountNo, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        Account account = accountRepository.findByAccountNumber(accountNo)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        if (account != null) {
            account.deposit(amount);
            accountRepository.save(account);

            // Record the deposit transaction
            recordTransaction(account, amount, "deposit");
        } else {
            throw new AccountNotFoundException("Account not found");
        }
    }

    private void withdraw(Long accountNo, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        Account account = accountRepository.findByAccountNumber(accountNo)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        if (account != null) {
            account.withdraw(amount);
            accountRepository.save(account);

            // Record the withdrawal transaction
            recordTransaction(account, amount, "withdrawal");
        } else {
            throw new AccountNotFoundException("Account not found");
        }
    }

    @Override
    public Set<AccountDTO> getAllAccountByUsername(String username) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new BankingAppCoreServiceApiException("User not found", HttpStatus.NOT_FOUND);
        }
        Set<AccountDTO> accounts = user.getUserDetails().getAccounts()
                .stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toSet());

        if (accounts.isEmpty())
            throw new AccountNotFoundException("Account not found");
        return accounts;
    }


    @Override
    public Set<AccountDTO> getAllAccountByUserId(String id) {
        User user = modelMapper.map(
                userService.getUserById(UUID.fromString(id)).getBody(), User.class
        );
        if (user == null) {
            throw new BankingAppCoreServiceApiException("User not found", HttpStatus.NOT_FOUND);
        }
        Set<AccountDTO> accounts = user.getUserDetails().getAccounts()
                .stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toSet());

        if (accounts.isEmpty())
            throw new AccountNotFoundException("Account not found");
        return accounts;
    }


    @Override
    public String deleteAccount(Long accountNo) {
        log.info("Deleting account: {}", accountNo);
        Account account = accountRepository.findByAccountNumber(accountNo)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));
        account.setStatus(Status.CLOSED);
        accountRepository.save(account);

        log.info("Account deletion is completed");
        return "Account deleted successfully";
    }


    @Override
    public AccountDTO getAccountByAccountNo(Long accountNo) {
        Account account = accountRepository.findByAccountNumber(accountNo)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));
        return modelMapper.map(account, AccountDTO.class);
    }


    @Override
    public Long getCountAllAccount() {
        return (long) accountRepository.findAll().size();
    }


    @Override
    public Long getCountAllActiveAccount() {
        return (long) accountRepository.findAllByStatus(Status.ACTIVE).size();
    }


    @Override
    public Long getCountAllInactiveAccount() {
        return (long) accountRepository.findAllByStatus(Status.INACTIVE).size();
    }

    @Override
    @Transactional
    public String fundTransfer(FundTransferRequest request) {
        withdraw(request.getFromAccount(), request.getAmount());
        deposit(request.getToAccount(), request.getAmount());
        return "Fund transferred successfully";
    }

    @Override
    @Transactional
    public String openAccount(NewAccountRequest request) {
        UserRequest userRequest = request.getUser();
        NewAddressRequest addressRequest = request.getAddress();
        AccounttRequest accounttRequest = request.getAccount();

        log.info("Account creation process is started");
        Optional<User> userOpt = userRepository.findByUsername(userRequest.getUsername());

        if (userOpt.isPresent())
            throw BankingAppCoreServiceApiException.builder()
                    .message(userRequest.getUsername() + " is already registered")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
//        FIXME - Check customer phone number is already registered or not in DB and throw appropriate exception if present
/*
        Optional<User> userPh = userRepository.findByPhoneNumber(userRequest.getPhoneNumber());
        if(userPh.isPresent())
            throw BankingAppUserServiceApiException.builder()
                    .message(userRequest.getPhoneNumber()+" is already registered")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
*/
        Optional<User> userEmail = userRepository.findByEmail(userRequest.getEmail());
        if (userEmail.isPresent())
            throw BankingAppCoreServiceApiException.builder()
                    .message(userRequest.getEmail() + " is already registered")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();

        User user = User.builder()
                .userDetails(UserDetails.builder()
                        .aboutMe(userRequest.getAboutMe())
                        .firstName(userRequest.getFirstName())
                        .lastName(userRequest.getLastName())
                        .dateOfBirth(LocalDate.parse(userRequest.getDateOfBirth()))
                        .phoneNumber(userRequest.getPhoneNumber())
                        .build()
                )
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode("test@123"))
                .status(Status.ACTIVE)
                .build();
        user = userRepository.save(user);

        Address address = Address.builder()
                .addressType(AddressType.valueOf(addressRequest.getAddressType().toUpperCase()))
                .houseNumber(addressRequest.getFlatNo())
                .streetName(addressRequest.getLine1())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .country(addressRequest.getCountry())
                .postalCode(addressRequest.getPostalCode())
                .landmark(addressRequest.getLandmark())
                .status(Status.ACTIVE)
                .user(user)
                .build();

        addressRepository.save(address);

        Account account = Account.builder()
                .user(user)
                .accountNumber(System.currentTimeMillis())
                .branch(BranchUtils.getBranchCode(
                                branchRepository.findAllByStatusAndPostalCode(Status.ACTIVE, addressRequest.getPostalCode())
                        )
                )
                .accountType(AccountType.valueOf(accounttRequest.getAccountType().toUpperCase()))
                .balance(accounttRequest.getOpeningBalance())
                .status(Status.ACTIVE)
                .build();

        account = accountRepository.save(account);

        // Record the deposit transaction
        recordTransaction(account, accounttRequest.getOpeningBalance(), "deposit");

        Card card = Card.builder()
                .account(account)
                .cardHolderName(userRequest.getFirstName().toUpperCase().concat(" ").concat(userRequest.getLastName().toUpperCase()))
                .cardType(CardType.DEBIT)
                .status(Status.ACTIVE)
                .build();

        card = cardRepository.save(card);

        if (account.getId() == null || card.getId() == null)
            throw BankingAppCoreServiceApiException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("User's account is not created")
                    .build();
        log.info("Account creation process is completed");

        return "Account opened successfully";

    }

    @Override
    public Double checkingBalance(Long accountNo) {
        Account account = accountRepository.findByAccountNumber(accountNo)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        return account.getBalance();
    }

    @Override
    public List<TransactionDTO> miniStatement(Long accountNo) {
        Account account = accountRepository.findByAccountNumber(accountNo)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        List<Transaction> transactions = transactionRepository
                .findFirst10ByAccountOrderByTransactionDateDesc(account);

        if (transactions.isEmpty())
            throw new TransactionNotFoundException("No transactions are found for provided account: " + accountNo);

        return getTransactionDTOList(transactions);
    }

    @Override
    public List<TransactionDTO> detailedStatement(Long accountNo, String startDate, String endDate) {
        Account account = accountRepository.findByAccountNumber(accountNo)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate sDate = LocalDate.parse(startDate, dateFormatter);
        LocalDate eDate = LocalDate.parse(endDate, dateFormatter);
        LocalDate today = LocalDate.now();

        if (!today.isAfter(sDate)) {
            throw BankingAppCoreServiceApiException.builder()
                    .message("Start date should not be a future date")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        if (!(today.isEqual(eDate) || today.isAfter(eDate))) {
            throw BankingAppCoreServiceApiException.builder()
                    .message("End date should not be a future date")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }


        LocalDateTime start = LocalDateTime.of(LocalDate.parse(startDate, dateFormatter), LocalDateTime.now().toLocalTime());
        LocalDateTime end = LocalDateTime.of(LocalDate.parse(endDate, dateFormatter), LocalDateTime.now().toLocalTime());

        List<Transaction> transactions = transactionRepository
                .findByAccountAndTransactionDateBetweenOrderByTransactionDateDesc(account, start, end);

        if (transactions.isEmpty())
            throw new TransactionNotFoundException("No transactions are found for provided account: " + accountNo);

        return getTransactionDTOList(transactions);
    }

    private List<TransactionDTO> getTransactionDTOList(List<Transaction> transactions) {
        return transactions.stream()
                .map(transaction ->
                        TransactionDTO.builder()
                                .amount(transaction.getAmount())
                                .transactionId(transaction.getId())
                                .transactionType(transaction.getTransactionType() != null ? transaction.getTransactionType().name() : null)
                                .transactionDate(transaction.getTransactionDate())
                                .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public String pinChange(CardPinChangeRequest request) {
        log.info("Card pin change process is started");
        Card card = cardRepository.findByCardNumberAndSecurityPin(request.getCardNo(), request.getPin())
                .orElseThrow(() ->
                        new CardNotFoundException("Card details is not found"));

        card.setSecurityPin(request.getNewPin());
        cardRepository.save(card);

        log.info("Card pin change process is completed");
        return "Pin is changed successfully";
    }

    @Override
    public String addNewDebitCard(NewDebitCardRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found")
                );
        Card card = Card.builder()
                .account(account)
                .cardHolderName(request.getCardHolderName() != null ?
                        request.getCardHolderName().toUpperCase() : account.getUser().getUserDetails().getFirstName().toUpperCase()
                        .concat(" ").concat(account.getUser().getUserDetails().getLastName().toUpperCase()))
                .cardType(CardType.DEBIT)
                .status(Status.ACTIVE)
                .build();

        cardRepository.save(card);
        return "Debit Card is added to account " + request.getAccountNumber() + " successfully";
    }

    @Override
    public String blockDebitOrCreditCard(BlockDebitOrCreditCardRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found")
                );

        Set<Card> cards = account.getCards();

        if (cards.isEmpty()) {
            throw new CardNotFoundException("Card not found");
        }
        Card cardToUpdate = cards.stream()
                .filter(card -> card.getCardNumber().equals(request.getCardNumber()))
                .toList().stream().findFirst().get();

        cardToUpdate.setStatus(Status.BLOCKED);
        cardRepository.save(cardToUpdate);
        return "Debit/Credit card is blocked successfully";
    }

}
