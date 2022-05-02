package com.example.springreqresclient;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
class ConsoleCommand {

    private String action ;
    private String message ;
    
}

