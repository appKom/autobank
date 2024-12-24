package com.example.autobank.repository.receipt.specification

import com.example.autobank.data.receipt.ReceiptInfo
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.Predicate

class ReceiptInfoSpecification(
    private val status: String?,
    private val committeeName: String?,
    private val search: String?
) : Specification<ReceiptInfo> {
    override fun toPredicate(
        root: jakarta.persistence.criteria.Root<ReceiptInfo>,
        query: jakarta.persistence.criteria.CriteriaQuery<*>,
        criteriaBuilder: jakarta.persistence.criteria.CriteriaBuilder
    ): Predicate? {
        val predicates = mutableListOf<Predicate>()


        if (status == "NONE") {
            predicates.add(criteriaBuilder.isNull(root.get<String>("latestReviewStatus")))
        } else if (status == "DONE") {
            predicates.add(criteriaBuilder.isNotNull(root.get<String>("latestReviewStatus")))
        }else if (status != null) {
            predicates.add(criteriaBuilder.equal(root.get<String>("latestReviewStatus"), status))
        }

        if (committeeName != null) {
            val committeeNames = committeeName.split(",").map { it.trim() }
            predicates.add(root.get<String>("committeeName").`in`(committeeNames))
        }

        if (search != null) {
            predicates.add(criteriaBuilder.like(root.get<String>("receiptName"), "%$search%"))
        }


        return criteriaBuilder.and(*predicates.toTypedArray())
    }
}
