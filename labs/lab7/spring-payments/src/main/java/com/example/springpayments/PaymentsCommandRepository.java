
package com.example.springpayments;

import org.springframework.data.jpa.repository.JpaRepository;

interface PaymentsCommandRepository extends JpaRepository<PaymentsCommand, Long> {

}


