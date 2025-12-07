package com.gsb.accounts.service.impl;

import com.gsb.accounts.dto.AccountsDto;
import com.gsb.accounts.dto.CardsDto;
import com.gsb.accounts.dto.CustomerDetailsDto;
import com.gsb.accounts.dto.LoansDto;
import com.gsb.accounts.entity.Accounts;
import com.gsb.accounts.entity.Customer;
import com.gsb.accounts.exception.ResourceNotFoundException;
import com.gsb.accounts.mapper.AccountsMapper;
import com.gsb.accounts.mapper.CustomerMapper;
import com.gsb.accounts.repository.AccountsRepository;
import com.gsb.accounts.repository.CustomerRepository;
import com.gsb.accounts.service.ICustomersService;
import com.gsb.accounts.service.client.CardsFeignClient;
import com.gsb.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}
