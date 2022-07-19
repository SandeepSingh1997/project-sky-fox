package com.booking.customer;

import com.booking.config.featureTogglz.FeatureAssociation;
import com.booking.config.featureTogglz.FeatureOptions;
import com.booking.exceptions.UsernameAlreadyExistsException;
import com.booking.handlers.models.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Customers")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @FeatureAssociation(value = FeatureOptions.CUSTOMER_SIGNUP_FEATURE)
    @PostMapping()
    @ApiOperation(value = "signup a customer")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Successfully Signed up"), @ApiResponse(code = 400, message = "Server cannot process request due to client error", response = ErrorResponse.class), @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)})
    public void signup(@Valid @RequestBody CustomerSignupRequest customerSignupRequest) throws UsernameAlreadyExistsException {
        Customer customer = customerSignupRequest.getCustomer();
        customerService.signup(customer);
    }
}
