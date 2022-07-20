package com.booking.revenue.view;

import com.booking.handlers.models.ErrorResponse;
import com.booking.revenue.RevenueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Date;

@Api(tags = "Revenue")
@RestController
@RequestMapping("/revenue")
public class RevenueController {

    private final RevenueService revenueService;

    @Autowired
    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping
    @ApiOperation(value = "Fetch revenue")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched revenue successfully"),
            @ApiResponse(code = 400, message = "Server cannot process request due to client error", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public BigDecimal getRevenue(Principal principal, @Valid @RequestParam(name = "date") Date date) {
        return revenueService.revenueByDate(date);
    }
}
