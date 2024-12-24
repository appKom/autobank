package com.example.autobank.service

import org.springframework.stereotype.Service
import com.example.autobank.repository.receipt.ReceiptRepository
import org.springframework.beans.factory.annotation.Autowired
import com.example.autobank.repository.receipt.ReceiptInfoViewRepository
import com.example.autobank.data.receipt.*
import com.example.autobank.data.ReceiptReviewRequestBody
import com.example.autobank.repository.receipt.specification.ReceiptInfoSpecification
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort


@Service
class ReceiptAdminService {

    @Autowired
    lateinit var receiptRepository: ReceiptRepository

    @Autowired
    lateinit var receiptInfoViewRepository: ReceiptInfoViewRepository

    @Autowired
    lateinit var receiptService: ReceiptService

    fun getAll(from: Int, count: Int, status: String?, committeeName: String?, search: String?, sortField: String?, sortOrder: String?): ReceiptListResponseBody? {


        val sort = if (!sortField.isNullOrEmpty()) {
            Sort.by(Sort.Direction.fromString(sortOrder ?: "ASC"), sortField)
        } else {
            Sort.by(Sort.Direction.DESC, "receiptCreatedAt")
        }

        val pageable = PageRequest.of(from, count, sort)

        val specification = ReceiptInfoSpecification(status, committeeName, search)

        val receipts: List<ReceiptInfo> = receiptInfoViewRepository.findAll(specification, pageable).toList()

        val total: Long = receiptInfoViewRepository.count(specification)

        return ReceiptListResponseBody(receipts.toTypedArray(), total)


    }

    fun getReceipt(id: Int): CompleteReceipt? {
        val receipt = receiptInfoViewRepository.findByReceiptId(id)

        return receiptService.getCompleteReceipt(receipt)

    }


}