package com.example.autobank.controller;

import com.example.autobank.data.receipt.CompleteReceipt
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.example.autobank.service.ReceiptAdminService;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import com.example.autobank.data.receipt.ReceiptInfo
import com.example.autobank.service.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import com.example.autobank.data.ReceiptReviewRequestBody
import com.example.autobank.data.receipt.ReceiptListResponseBody
import com.example.autobank.data.receipt.ReceiptReview
import com.example.autobank.service.ReceiptReviewService
import org.springframework.data.repository.query.Param


@RestController
@RequestMapping("/api/admin/receipt")
class AdminReceiptController {

    @Autowired
    lateinit var receiptAdminService: ReceiptAdminService

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var receiptReviewService: ReceiptReviewService


    @GetMapping("/all")
    fun getAllReceipts(@Param("page") from: Int, @Param("count") count: Int, @Param("status") status: String?, @Param("committee") committee: String?, @Param("search") search: String?, @Param("sortOrder") sortOrder: String?, @Param("sortField") sortField: String?): ResponseEntity<ReceiptListResponseBody> {
        if (!authenticationService.checkBankomMembership()) {
            return ResponseEntity.status(403).build()
        }

        return ResponseEntity.ok(receiptAdminService.getAll(from, count, status, committee, search, sortField, sortOrder))
    }

    @GetMapping("/get/{id}")
    fun getReceipt(@PathVariable id: Int): ResponseEntity<CompleteReceipt> {
        if (!authenticationService.checkBankomMembership()) {
            return ResponseEntity.status(403).build()
        }

        return ResponseEntity.ok(receiptAdminService.getReceipt(id))
    }

    @PostMapping("/review")
    fun reviewReceipt(@RequestBody reviewBody: ReceiptReviewRequestBody): ResponseEntity<ReceiptReview> {
        if (!authenticationService.checkBankomMembership()) {
            return ResponseEntity.status(403).build()
        }
        try {
            return ResponseEntity.ok(receiptReviewService.createReceiptReview(reviewBody));
        } catch (e: Exception) {
            println(e)
            return ResponseEntity.badRequest().build()
        }

    }


}
