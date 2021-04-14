package com.example.springpayments;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
class PaymentsCommand {

    private String action ;
    private String firstname ;
    private String lastname ;
    
}
